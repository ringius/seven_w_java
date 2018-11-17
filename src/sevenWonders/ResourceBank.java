package sevenWonders;

import java.util.*;

/**
 * Created by Jan on 2017-01-10.
 */
public class ResourceBank {
    protected HashMap<RawMaterial.type, Integer> bank;
    private HashMap<RawMaterial.type, ArrayList<PlayCard>> cardList;
    protected HashMap<RawMaterial.type, ArrayList<PlayCard>> conditionalCards;

    public ResourceBank() {
        bank = new HashMap<RawMaterial.type, Integer>();
        cardList = new HashMap<>();
        conditionalCards = new HashMap<>();
    }

    public ResourceBank(ResourceBank orig) {
        this();
        addBank(orig);
        cardList.putAll(orig.cardList);
        conditionalCards.putAll(orig.conditionalCards);
    }

    public void clear() {
        bank.clear();
        cardList.clear();
        conditionalCards.clear();
    }

    protected HashMap<RawMaterial.type, Integer> getBank() {
        return bank;
    }


    public static ResourceBank merge(ResourceBank b1, ResourceBank b2) {
        ResourceBank res = new ResourceBank(b1);

        res.addBank(b2);
        return res;
    }

    //TODO: Fix (conditionalCards)
    public void addBank(ResourceBank bank2) {
        for (RawMaterial.type t : bank2.bank.keySet()) {
            int a2 = bank2.getAmountOfMaterial(t);
            int a1 = getAmountOfMaterial(t);
            int tot = a1 + a2;
            if (a1 != 0)
                bank.replace(t, tot);
            else
                bank.put(t, tot);
        }
        cardList.putAll(bank2.cardList);
        for (RawMaterial.type t : bank2.conditionalCards.keySet()) {
            ArrayList l = conditionalCards.get(t);
            if (l == null) {
                l = bank2.conditionalCards.get(t);
            } else {
                l.addAll(bank2.conditionalCards.get(t));
            }
            conditionalCards.put(t, l);
        }
    }

    public void addCard(PlayCard card) {
        if(card.getResourceProduction().getConditionalSelection()) {
            addConditionalCard(card);
        } else {
            addUnconditionalCard(card);
        }
    }

    private void addUnconditionalCard(PlayCard card) {
        ResourceList l = card.getResourceProduction();
        for (RawMaterial.type t : l.getResources().keySet()) {
            ArrayList<PlayCard> list = cardList.get(t);
            if(list == null) {
                list = new ArrayList<PlayCard>();
            }
            list.add(card);
            cardList.put(t, list);
            int amount = l.getResourceAmount(t);
            addUnconditionalMaterial(t, amount);
        }
    }

    public void addUnconditionalMaterial(RawMaterial.type m, int amount) {
        if (!bank.containsKey(m)) {
            bank.put(m, amount);
        } else {
            int a = bank.get(m);
            bank.replace(m, a+amount);
        }
    }

    public void addConditionalCard(PlayCard card) {
        ResourceList c = card.getResourceProduction();
        for  (RawMaterial.type t : c.getResources().keySet()) {
            ArrayList<PlayCard> l = conditionalCards.get(t);
            if (l == null)
                l = new ArrayList<>();
            l.add(card);
            conditionalCards.put(t, l);
        }
    }

    //Todo: Fix conditional cards!!
    public void add(ResourceBank stuffToAdd) {
        for (RawMaterial.type t : stuffToAdd.getBank().keySet()) {
            addUnconditionalMaterial(t, stuffToAdd.getAmountOfMaterial(t));
        }
        conditionalCards.putAll(stuffToAdd.conditionalCards);
    }

    public boolean getAllSolutions(PlayCard card, Solutions s) {
        boolean ret = false;
        if (s.solutions.isEmpty())
            s.addSolution(new Solution(card.getResourceCost(), new ResourcePayments(), false));

        for (Solution solution : s.solutions) {
            Solutions newS = new Solutions();
            if (getSolutions(solution.getRemainingCost(), this, solution.getPayment(), newS))
                ret = true;
            solution.setCascadedSolutions(newS);
        }
        s.flatten();
        return ret;
    }

    //This assumes that there is at least one solution in 's'.
    //Method is used to continue searching for solutions in neighbouring players banks...
    //Argument PlayCard only to keep the card in the call chain...

    /**
     *
     * @param card: Card being investigated
     * @param bank Bank to use for finding payment.
     * @param s     Solutions that are potential partial solutions to the payment problem. Note
     *              that s *must* be flattened; cascaded solutions are assumed to be empty.
     * @return      true if at least one affordable solution exist, false otherwise.
     */
    public boolean getMoreSolutions(PlayCard card, ResourceBank bank, Solutions s) {
        boolean ret = false;
        for (Solution solution : s.solutions) {
            Solutions newS = new Solutions();
            if (getSolutions(solution.getRemainingCost(), bank, solution.getPayment(), newS))
                ret = true;
            solution.setCascadedSolutions(newS);
        }
        return ret;
    }


    public boolean getSolutions(ResourceList cost,
                                ResourceBank bank,
                                ResourcePayments paidSoFar,
                                Solutions s) {
        s.addSolution(new Solution(cost, paidSoFar, cost.isZero()));

        if (cost.isZero()) {
            return true;
        }

        if (bank.isEmpty()) {
            return false;
        }

        Iterator<RawMaterial.type> iter = cost.iterator();
        boolean ret = false;
        while (iter.hasNext()) {
            RawMaterial.type t = iter.next();
            int mtrlCost = cost.getResourceAmount((t));
            int inBank = bank.getAmountOfMaterial(t);
            if (mtrlCost > 0 && inBank > 0) {
                for (int i = Math.min(mtrlCost, inBank); i > 0; i--) {
                    ResourcePayments pay = bank.getPayment(t, i);
                    if (getSolutions(cost.minus(t, i), bank.minus(pay), paidSoFar.plus(pay), s))
                        ret = true;
                }
            }
        }
        return ret;
    }

    private boolean isEmpty() {
        return bank.isEmpty();
    }

    public ResourcePayments getPayment(RawMaterial.type type, int amount) {
        ResourcePayments p = new ResourcePayments();
        ArrayList<PlayCard> cards = cardList.get(type);
        int pek = 0;
        while (amount > 0 && pek < cards.size()) {
            int contribution = cards.get(pek).getResourceProduction().getResourceAmount(type);
            if (contribution > 0) {
                ResourcePayment pay = new ResourcePayment(type, Math.min(amount, contribution), cards.get(pek));
                p.add(pay);
                amount -= contribution;
                pek++;
            }
        }
        return p;
    }

    //Clone bank, subtract and return...
    public ResourceBank minus(ResourcePayments toSubtract) {
        ResourceBank b = new ResourceBank(this);
        for (ResourcePayment p:toSubtract) {
            b.subtractPayment(p);
            b.removeCard(p.getCard());
        }
        return b;
    }

    private void subtractPayment(ResourcePayment pay) {
        //int maxAmountToSubtract = pay.getCard().getAmountOfMaterial(pay.getMaterialType());
        subtract(pay.getMaterialType(), pay.getAmount());
    }


    private void removeCard(PlayCard ctr) {
        cardList.remove(ctr);
    }

    /*
    private void removeCard(PlayCard payment) {
        ResourceList production = payment.getResourceProduction();

        for (RawMaterial.type t : production.resources.keySet()) {
            int no = payment.getAmountOfMaterial(t);
            if (no > 0) {
                subtract(t, no);
            } else {
                System.out.println("This card cannot be used as payment: " + payment);
            }
        }
        cardList.remove(payment);
    }
    */

    private void subtract(RawMaterial.type t, int no) {

        Integer A = bank.get(t);
        if (A == null) {
            System.out.println("bank does not have material");
            System.out.println("Type = " + t + " Amount = " + no);
            return;
        }
        int a = A.intValue();
        if ((a-no) <= 0) {
            bank.remove(t);
        } else {
            bank.put(t, Math.max(0, a - no));
        }
    }

    public int getAmountOfMaterial(RawMaterial.type materialType) {
        if (bank.containsKey(materialType)) {
            return bank.get(materialType);
        } else {
            return 0;
        }
    }

    public String toString() {
        String res = "";
        for (Map.Entry<RawMaterial.type, Integer> e : bank.entrySet()) {
            res +=  "[" + RawMaterial.string(e.getKey()) + ": " + e.getValue() + "] ";
        }
        return res;
    }

    public HashMap<RawMaterial.type,ArrayList<PlayCard>> getConditionalBank() {
        return conditionalCards;
    }

    public int getNumberOfConditionals() {
        return conditionalCards.size();
    }

    public ResourceBank copyMinusConditional(PlayCard p) {
            ResourceBank copy = new ResourceBank(this);
            copy.removeConditionalCard(p);
            return copy;
    }

    private void removeConditionalCard(PlayCard p) {
        HashSet<RawMaterial.type>typesToRemove = new HashSet();
        for (RawMaterial.type t : conditionalCards.keySet()) {
            ArrayList<PlayCard> cards = conditionalCards.get(t);
            cards.remove(p);
            if (cards.size() == 0) {
                typesToRemove.add(t);
            }
        }
        for  (RawMaterial.type t : typesToRemove) {
            conditionalCards.remove(t);
        }
    }
}
