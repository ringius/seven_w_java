package sevenWonders;

/**
 * Created by Jan on 2017-01-08.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Player {

    private GameUI UI;
    private HashMap<PlayCard.CardType, Integer> victoryPoints;
    private Player eastPlayer;
    private Player westPlayer;

    //tradeListMap contains information about which goods that are cheaper to buy from the east and west players
    private HashMap<Player, ResourceBank> tradeListMap;
    private MilitaryHistory milHistory = new MilitaryHistory();
    private University kth = new University();


    private String name;
    public Wonder wonder;
    public int balance;
    private CardList cards;
    private PlayCard pendingCard;               //Storage for card bought in current turn, and thus not available for other players yet.
    private ResourceBank rawMaterialsPublic;    // Raw materials from brown and grey cards.
    private ResourceBank rawMaterialsPrivate;   //pretty much only raw materials from the Wonder.
    //private ConditionalResourcesBank conditionalPublic; //Cards where player and neighbors can choose one of several raw materials (brown cards).
    //private ConditionalResourcesBank conditionalPrivate; //Cards where player only can choose one of several raw materials (yellow cards and wonders).
    private CardDeck currentCards;

    private ArrayList<Action> deferredActions;
    private GameController controller;
    private Transaction pendingTransaction;

    public Player(String name,  Wonder wonder) {
        this.name = name;
        this.wonder = wonder;
        this.tradeListMap = new HashMap<>();
        this.balance = 0;
        this.victoryPoints = new HashMap<>();
        cards = new CardList();
        currentCards = null;
        rawMaterialsPublic = new ResourceBank();
        rawMaterialsPrivate = new ResourceBank();
        //conditionalPublic = new ConditionalResourcesBank();
        //conditionalPrivate = new ConditionalResourcesBank();
        eastPlayer = null;
        westPlayer = null;
    }

    public Player(String name) {
        this(name, null);
    }

    public boolean initialize() {
        this.balance = 3;
        victoryPoints = new HashMap<>();
        pendingCard = null;
        pendingTransaction = null;
        cards.clear();
        rawMaterialsPublic.clear();
        rawMaterialsPrivate.clear();
        //conditionalPublic.clear();
        //conditionalPrivate.clear();
        milHistory.clear();
        deferredActions = new ArrayList<>();
        resetTradeListMap();
        if (wonder != null) {
            pendingCard = wonder.getStartCard();
            pendingTransaction =  new Transaction(Transaction.tType.INIT);
            try {
                executeCard();
            } catch (Exception e)  {
                //under control...
            }
        }
        return (wonder != null && eastPlayer != null && westPlayer != null);
    }

    public Player getEastPlayer() {
        return eastPlayer;
    }

    public Player getWestPlayer() {
        return westPlayer;
    }

    private void resetTradeListMap() {
        tradeListMap.clear();
        tradeListMap.put(eastPlayer, new ResourceBank());
        tradeListMap.put(westPlayer, new ResourceBank());
    }

    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
        pendingCard = wonder.getStartCard();
        pendingTransaction = new Transaction(Transaction.tType.INIT);
    }

    public void addMilitaryStrength(int strength) {
        milHistory.addStrength(strength);
    }

    //public int getMilitaryStrength() {
    //    return milHistory.getMilitaryStrength();
    //}

    public University getUniversity() {
        return kth;
    }

    public void handleDelayedActions() throws Exception {
        if (pendingCard != null && pendingCard.isDelayed()) {
            pendingCard.updatePlayer(this);
            cards.add(pendingCard);
            pendingCard = null;
        }
    }

    public void executeCard() throws Exception {
        if (pendingCard != null && !pendingCard.isDelayed()) {
            if (pendingTransaction != null)
                performTransaction(pendingCard, pendingTransaction);
            //pendingCard.updatePlayer(this);
            pendingCard = null;
            pendingTransaction = null;
        }
    }

    public void setNeighbours(Player eastPlayer, Player westPlayer) {
        this.eastPlayer = eastPlayer;
        this.westPlayer = westPlayer;

        tradeListMap.put(eastPlayer, new ResourceBank());
        tradeListMap.put(westPlayer, new ResourceBank());
    }

    public String getName() {
        return name;
    }

    public void addDeferredAction(Action a) {
        deferredActions.add(a);
    }


    public void deposit(int amount) {
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public ResourceBank getTradeList(Player p) {
        return tradeListMap.get(p);
    }

    public int getImportPrice(Player p, RawMaterial.type t) {
        if (p == eastPlayer || p == westPlayer) {
            ResourceBank b = tradeListMap.get(p);
            if (b.getAmountOfMaterial(t) > 0) {
                return 1;
            }
        }
        return 2;
    }

    public void setCardDeck(CardDeck deck) {
        currentCards = deck;
    }


    /**
     * Calculate the cost of a card and updates the solution object with cost information. Takes
     * trade discounts into consideration.
     * @param solutions The solutions that are to have the cost calculated. If the player cannot buy the card,
     *                  no cost will be calculated.
     */
    public void calculateCost(PlayCard card, Solutions solutions) {
        for (Solution s :solutions.solutions) {
            calculateSolutionPrices(card, s);
        }
    }

    private void calculateSolutionPrices(PlayCard card, Solution s) {
        int ePrice = 0;
        int wPrice = 0;
        for (ResourcePayment p : s.getPayment()) {
            boolean eastPlayer = (p.getCard().getOwner()==getEastPlayer());
            RawMaterial.type type = p.getMaterialType();
            Player owner = p.getCard().getOwner();
            if (owner != this) {
                int importPrice = getImportPrice(p.getCard().getOwner(), type);
                if (eastPlayer) {
                    ePrice += p.getAmount() * importPrice;
                } else {
                    wPrice += p.getAmount() * importPrice;
                }
            }
        }
        s.setPriceEast(ePrice);
        s.setPriceWest(wPrice);
        s.setPriceBank(s.getTotalCost()-wPrice - ePrice);
        int totalCost = ePrice + wPrice + card.getCoinCost();
        s.setCanAffordPrice(getBalance() >= totalCost);
        s.setPriceIsCalculated(true);
    }

    /**
     *
     * @param card  The card the players wishes to buy.
     * @param solutions OUT: List of all posssible ways to pay all or parts of the cost using own resources.
     * @return true if the card can be bought by own means, otherwise false. Outparams contains detailed information.
     */
    public boolean canBuy(PlayCard card, Solutions solutions) {
        boolean canAffordCoinCost = true;
        boolean hasCoinCost = card.hasCoinCost();
        boolean needsResources = card.hasResourceCost();

        if (!hasCoinCost && !needsResources) { //Card is free!
            solutions.addSolution(new Solution(0, true));
            return true;
        }

        if (hasCoinCost) {
            int price = card.getCoinCost();
            if (price > balance) {
                canAffordCoinCost = false;
            }

            if (canAffordCoinCost && !needsResources) {
                Solution s = new Solution(card.getCoinCost(), true);
                s.setPriceBank(card.getCoinCost());
                solutions.addSolution(s);
                solutions.removeCopies();
                return true;
            } else {
                solutions.removeCopies();
                return false;
            }
        }

        if (canAffordCoinCost && needsResources) { //Resources only will get this card
            ResourceBank myBanks = ResourceBank.merge(getRawMaterialsPrivate(), getRawMaterialsPublic());
            myBanks.getAllSolutions(card, solutions);
/*
            if (conditionalPublic != null)
                conditionalPublic.getAllSolutions(card, solutions);
            if (conditionalPrivate != null)
                conditionalPrivate.getAllSolutions(card, solutions);
*/
            if (!solutions.empty() && solutions.hasAffordableSolution()) {
                solutions.removeCopies();
                System.out.println("" + getName() + " can afford " + card.getName() + ".Solutions:\n" + solutions);
                return true;
            } else {
                //Let's see if it is possible to buy with own conditional cards here instead.
                System.out.println("canBuy(): Check conditional cards");
                //System.out.println("Error, not even empty solution found.");
            }
        }
        solutions.removeCopies();
        return false;
    }

    public boolean canBuySelf(PlayCard card, Solutions solutions) {
        if (card.hasCoinCost() && !card.hasResourceCost()) {
            boolean canAfford = card.getCoinCost() <= this.getBalance();
            solutions.addSolution(new Solution(card.getCoinCost(), canAfford));
            System.out.println("Card has cost. Can buy the card = " + canAfford);
            return canAfford;
        }

        //Remove cost related to unconditional cards, should be fairly simple...
        System.out.println("Public = " + rawMaterialsPublic);
        System.out.println("Private = " + rawMaterialsPrivate);
        ResourceBank bank = ResourceBank.merge(rawMaterialsPublic, rawMaterialsPrivate);
        System.out.println("total = " + bank);
        ResourceList remaining = new ResourceList();
        for (RawMaterial.type t: card.getResourceCost().getResources().keySet()) {
            int cost = card.getResourceCost().getResourceAmount(t);
            int inBank = bank.getAmountOfMaterial(t);
            if (cost > inBank) {
                remaining.addResource(t, cost - inBank);
            }
        }

        if (remaining.isZero()) {
            System.out.println("Can afford the card");
            solutions.addSolution(new Solution(new ResourceList(), new ResourcePayments(), true));
            return true;
        }

        System.out.println("Remaining cost = " + remaining);
        solutions.addSolution(new Solution(remaining, new ResourcePayments(), false));
        //Now it is time to look into the conditional cards...
        checkConditionals(bank, solutions);
        return false;
    }

    public void checkConditionals(ResourceBank bank, Solutions sol) {
        if (bank.getNumberOfConditionals() == 0) {
            return;
        }
        for (Solution s : sol.solutions) {
            if (!s.isAffordable()) {
                Solutions solutionsToAdd = new Solutions();
                ResourceList cost = s.getRemainingCost();
                for (RawMaterial.type t : cost.resources.keySet()) {
                    int partCost = cost.getResourceAmount(t);
                    ArrayList<PlayCard> al = bank.getConditionalBank().get(t);
                    if (al != null) {
                        for (PlayCard p : al) {
                            //hitta alla varianter av kort som är (del-)mängd av lösningen.
                            int amount = p.getAmountOfMaterial(t);
                            partCost = partCost - amount;
                            ResourcePayment newPay = new ResourcePayment(t, amount, p);
                            ResourcePayments rp = new ResourcePayments(s.getPayment());
                            rp.add(newPay);
                            ResourceList newCost = cost.copyMinus(t, amount);
                            solutionsToAdd.addSolution(new Solution(newCost, rp, newCost.isZero()));
                            ResourceBank newBank = bank.copyMinusConditional(p);
                            checkConditionals(newBank, solutionsToAdd);

                        }
                    }
                }
                if (!solutionsToAdd.empty()) {
                    s.setCascadedSolutions(solutionsToAdd);
                    sol.addSolution(s);
                }
            } else { //Affordable!

            }
        }
        sol.flatten();
    }

    /*
    private boolean checkConditionalCards(ResourceList remaining, Solutions solutions) {
        HashMap<RawMaterial.type, ArrayList<PlayCard>> allCards = new HashMap<RawMaterial.type, ArrayList<PlayCard>>();
        allCards.putAll(getPrivateConditionalCards());
        HashMap<RawMaterial.type, ArrayList<PlayCard>> c = getPublicConditionalCards();
        ArrayList<PlayCard> a = null;
        for (RawMaterial.type t : c.keySet()) {
            if (allCards.get(t) == null) {
                a = new ArrayList<>();
                ArrayList<PlayCard> tList = c.get(t);
                for (PlayCard card : tList)
                    a.add(card);
                allCards.put(t, a);
            } else {
                a = allCards.get(t);
                for (PlayCard card : c.get(t))
                    a.add(card);
            }
        }

        System.out.println("All conditional cards = " + allCards.values());
        System.out.println("Size = " + allCards.size());
        ResourcePayments paidSoFar = new ResourcePayments();
        ResourceList notFound = new ResourceList();
        checkConditionalCardsInternal(remaining, notFound, paidSoFar, allCards, solutions);
        if (paidSoFar.isEmpty())
            return true;
        else
            return false;
    }

    private boolean checkConditionalCardsInternal(ResourceList remaining,
                                                  ResourceList notSatisfiable,
                                                  ResourcePayments paidSoFar,
                                                  HashMap<RawMaterial.type, ArrayList<PlayCard>> allCards,
                                                  Solutions solutions) {
        for (RawMaterial.type t : remaining.getResources().keySet()) {




            int amount = remaining.getResourceAmount(t);
            if (allCards.get(t) != null) {
                HashMap<RawMaterial.type, ArrayList<PlayCard>> almostAllCards = new HashMap(allCards);
                ArrayList<PlayCard> l = almostAllCards.get(t);

                for (PlayCard payment : l) {
                    //PlayCard payment = l.get(0);
                    paidSoFar.add(new ResourcePayment(t, 1, payment));
                    removeCard(almostAllCards, payment);
                    checkConditionalCardsInternal(remaining.copyMinus(t, amount), notSatisfiable, paidSoFar, almostAllCards, solutions);
                }
            } else {
                notSatisfiable.addResource(t, amount);
                checkConditionalCardsInternal(remaining.copyMinus(t, amount), notSatisfiable, paidSoFar, allCards, solutions);
            }
        }
        return false;
        //Find all cards that satisfy first remaining cost. If none, move card to "not possible list" and continue.
    }

    private void removeCard(HashMap<RawMaterial.type, ArrayList<PlayCard>> almostAllCards, PlayCard playCard) {
        for (RawMaterial.type t : almostAllCards.keySet()) {
            ArrayList<PlayCard> l = almostAllCards.get(t);
            l.remove(playCard);
            if (l.isEmpty()) {
                almostAllCards.remove(t);
            }
        }
    }
*/
    /**
     * Find all possible solutions for buying a card, using own cards, wonders and neighbouring players public ca
rds.
     * If a solution using only the player's resources exist, neighbours are not checked.
     * @param card to buy.
     * @param s Collection of all solutions that enables the player to buy the card. Empty if no solutions exist.
     * @return true if an affordable solution exist.
     */

    public boolean solveFinance(PlayCard card, Solutions s) {
        boolean needExternalResources = true;
        canBuySelf(card, s);
        if (canBuy(card, s)) {
            //System.out.println("Solved with no import needs. :\n" + s);
            //needExternalResources = false;
            //calculateCost(card, s);
            s.cleanup();
            return true;
        } else {
            if (balance <= card.getCoinCost()) {
                //System.out.println("Not enough money to buy the card");
                s.cleanup();
                return false;
            }
        }

        //Could not find a resource solution with own resources only, needs to check neighbours...
        if (needExternalResources || !needExternalResources) {
            if (eastPlayer != null) {
                eastPlayer.getRawMaterialsPublic().getAllSolutions(card, s);
                //TODO: This needs to be handled!
                //eastPlayer.getPublicConditionalResourceBank().getAllSolutions(card, s);
                //System.out.println("Added solutions from " + eastPlayer.getName() + ":\n");
            }


            if (westPlayer != null) {
                westPlayer.getRawMaterialsPublic().getAllSolutions(card, s);
                //TODO: This needs to be handled!
                //westPlayer.getPublicConditionalResourceBank().getAllSolutions(card, s);
                //System.out.println("Added solutions from " + westPlayer.getName() + ":\n");
            }
        }

        if (!s.empty()) {
            s.cleanup();
        }

        if (!s.empty() && s.hasAffordableSolution()) {
            s.cleanup();
            calculateCost(card, s);
            //s.removeUnaffordableSolutions();
            //printCosts(s);
            return true;
        } else {
            s.cleanup();
            return false;
        }
    }

    private void printCosts(Solutions sols) {
        for (Solution s:sols.solutions) {
            String out = new String();
            out += s + " cost: From " + eastPlayer.getName() + " " + s.getPriceEast() + ". From " + westPlayer.getName()
                    + " " + s.getPriceWest() + " Card cost = " + s.getPriceBank() + " Total Cost = " + (s.getPriceEast() + s.getPriceWest()) + ". Can Afford: " + s.canAffordPrice();
            System.out.println(out);
        }
    }

    public ResourceBank getRawMaterialsPublic() {
        return rawMaterialsPublic;
    }

    public ResourceBank getRawMaterialsPrivate() {
        return rawMaterialsPrivate;
    }

    /*
    public ConditionalResourcesBank getPublicConditionalResourceBank() {
        return conditionalPublic;
    }

    public ConditionalResourcesBank getPrivateConditionalResourceBank() {
        return conditionalPrivate;
    }
    */

    // Returns a collection of the materials that this player can export
    // Note, conditional materials are not included.
    public ResourceBank getExportableGoods() {
        return rawMaterialsPublic;
    }

    private void addPendingCard(PlayCard card) {
        pendingCard = card;
        //potentially resolve any immediate actions caused by the card (if any).
    }

    public boolean buyWithCoins(PlayCard card) {
        if (card.hasResourceCost())
            return false;
        int price = card.getCoinCost();
        if (balance >= price) {
            withdraw(price);
            addPendingSolution(card, new Transaction(Transaction.tType.BUY));
            //addPendingCard(card);
            return true;
        }
        return false;
    }

    //for now, just buy the first available card, or sell the first one if no card is affordable.
    public boolean ai_evaluate(CardDeck currentCards) {
        if (currentCards == null) {
            return false;
        }

        //for (PlayCard c: currentCards.getCardsAsList())
        CardList l = currentCards.getCardsAsList();

        for (int i=0;i<l.size();i++) {
            Solutions s = new Solutions();
            if (solveFinance(l.get(i), s)) {
                s.cleanup();
                buyCard(l.get(i), s.getFirstAffordableSolution());
                controller.buyCard(this, l.get(i), s.getFirstAffordableSolution());
                return true;
            }
        }
        sellCard(currentCards.getCard(0));
        return true;
    }

    public boolean sellCard(PlayCard card) {
        if (getUI() != null)
            return (getUI().getServer().sellCard(this, card));
        else
            return controller.sellCard(this, card);
    }

    public void buyCard(PlayCard card, Solution s) {
        addPendingCard(card);
        addPendingSolution(card, new Transaction(Transaction.tType.BUY, s));
    }

    private void addPendingSolution(PlayCard card, Transaction transaction) {
        pendingTransaction = transaction;
    }

    public boolean performTransaction(PlayCard card, Transaction t) throws Exception { //Solution s) {
        if (t == null) {
            System.out.println("Should not end up here.");
            Exception e = new Exception();
            e.printStackTrace();
            sellCard(card);
            return false;
        }

        if (t.getType() == Transaction.tType.INIT) {
            card.updatePlayer(this);
            moveCardToPlayer(card);
            return true;
        }

        if (t.getType() == Transaction.tType.SELL) {
            sellCard(card);
            return true;
        }

        if (t.getType() == Transaction.tType.BUY) {

            Solution s = t.getSolution();

            if (s == null) {
                System.out.println("Should not be able to get here");
                Exception e = new Exception("poor programming");
                e.printStackTrace();
            }

            if (!s.isPriceCalculated()) {
                calculateSolutionPrices(card, s);
            }

            if (!s.canAffordPrice() || !s.isAffordable()) {
                System.out.println("Can't buy this card");
                //Here the card should be sold to the bank for 3 dollars.
                sellCard(card);
                return false;
            } else {
                withdraw(s.getTotalCost());
                eastPlayer.deposit(s.getPriceEast());
                westPlayer.deposit(s.getPriceWest());
                try {
                    card.updatePlayer(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                moveCardToPlayer(card);
                //getUI().getServer().removeCardFromDeck(card);
                return true;
            }
        }
        return false;
    }

    private void moveCardToPlayer(PlayCard card) {
        cards.add(card);
        controller.removeCardFromDeck(card);
    }

    public String toString() {
        String res = "Player: " + name + "\n";
        res += "Wonder: " + wonder + "\n";
        res += "Cards: " + cards + "\n";
        res += "Public Materials: " + rawMaterialsPublic.toString() + '\n';
        res += "Private Materials: " + rawMaterialsPrivate.toString() + '\n';
        //res += "Public Optional Materials: " + conditionalPublic.toString() + '\n';
        //res += "Private Optional Materials: " + conditionalPrivate.toString() + '\n';
        res += "Banking Account: " + balance + '\n';
        res += "Military Strength: " + milHistory.getMilitaryStrength() + "\n";
        res += "Victory Points: " + victoryPoints + "\n";

        if (eastPlayer != null) {
            res += "East player: " + eastPlayer.getName() + ". ";
        }

        if (westPlayer != null) {
            res += "West Player : " + westPlayer.getName() + "\n";
        }

        return res;
    }

    private String cardsToString() {
        String res = "";
        Iterator<PlayCard> iter = cards.iterator();
        while (iter.hasNext()) {
            res += iter.next().toString() + '\n';
        }
        return res;
    }

    public void addVictoryPoints(PlayCard.CardType type, int amount) {
        Integer soFarInt = victoryPoints.get(type);
        int soFar = 0;
        if (soFarInt != null)
            soFar = victoryPoints.get(type);;
        soFar += amount;
        victoryPoints.replace(type, soFar);
    }

    public int getNumberOfCards(PlayCard.CardType type) {
        int a=0;
        for (PlayCard c: cards) {
            if (c.getType() == type) {
                a++;
            }
        }
        return a;
    }

    public void play(CardDeck cardDeck) {
        PlayCard card = cardDeck.pickCard(cardDeck.getCard(0));
    }

    public void handleEndOfAgeCards(int age) {
    }

    public void resolveCombat(int age) {
        int wp = westPlayer.getMilitaryStrength();
        int ep = eastPlayer.getMilitaryStrength();
        milHistory.resolveCombat(age, wp, ep);
    }

    private int getMilitaryStrength() {
        return milHistory.getMilitaryStrength();
    }

    public void executeDeferredActions() {
        for (Action a: deferredActions) {
            try {

            } catch (Exception e) {
                System.out.println("Failed to execute deferred action: e = " + e);
            }
        }
    }

    public ScoreBoard calculateVictoryPoints() {
        ScoreBoard res = new ScoreBoard();
        //Military
        res.addMilitaryScore(milHistory.getTotalScore());
        //funds
        res.addMonetaryScore((int)(balance/3));
        //Wonder
        res.addWonderScore(victoryPoints.get(PlayCard.CardType.Wonder));
        //Civilian structures
        res.addCivialianScore(victoryPoints.get(PlayCard.CardType.CivilianStructure));
        //Scientific structures
        res.addScienceScore(kth.getMaxVictoryPoints());
        //Commercial structures
        res.addCommercialScore(victoryPoints.get(PlayCard.CardType.CommercialStructure));
        //Guilds (not handled before).
        res.addGuildScore(victoryPoints.get(PlayCard.CardType.Guild));
        return res;
    }

    public void setUI(GameUI UI) {
        this.UI = UI;
    }

    public GameUI getUI() {
        return UI;
    }

    public void addController(GameController gameController) {
        this.controller = gameController;
    }

    public CardList getCards() {
        return cards;
    }

    public Wonder getWonder() {
        return wonder;
    }

    public HashMap<RawMaterial.type, ArrayList<PlayCard>> getPublicConditionalCards() {
        return rawMaterialsPublic.getConditionalBank();
    }

    public HashMap<RawMaterial.type, ArrayList<PlayCard>> getPrivateConditionalCards() {
        return rawMaterialsPrivate.getConditionalBank();
    }

}
