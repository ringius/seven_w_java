package sevenWonders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Jan on 2017-01-09.
 */
public class PlayCard {


    public enum CardType {RawMaterial, Goods, CivilianStructure, ScienceStructure, CommercialStructure, Wonder, MilitaryStructure, Guild};
    private int id;

    private HashSet<Integer>  successors;
    private HashSet<Integer>  predecessors;
    private HashSet<PlayCard> predReferences;

    private boolean delayed;
    private String name;
    private int age;
    private CardType type;
    private Player owner;
    private ResourceList resourceCost;
    //private boolean conditionalSelect;
    private ResourceList resourceProduction;
    private HashSet<Action> actions;
    private int coinCost;
    private int minNoOfPlayers;

    private Image image;

    public PlayCard() {
        //serializable
        actions = new HashSet<>();
        minNoOfPlayers = 0;
        resourceCost = new ResourceList();
        resourceProduction = new ResourceList();
        predecessors = new HashSet<>();
        successors = new HashSet<>();
        predReferences = new HashSet<>();
        //transient
        this.owner = null;
        this.delayed = false;
    }
    public PlayCard(String name, PlayCard.CardType type, int age) {
        this(name, type, age, 0);
    }

    public PlayCard(String name, PlayCard.CardType type, int age, int cCost /*, boolean conditionalSelect*/) {
        this();
        this.type =type;
        this.age = age;
        this.coinCost = cCost;
        this.name = name;
        //this.conditionalSelect = conditionalSelect;
     }

     @JsonIgnore
    public boolean isDelayed() {
        return delayed;
    }

    @JsonIgnore
    public void setImage(Image i) {
        image = i;
    }

    public Image getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMinNoOfPlayers(int minNoOfPlayers) {
        this.minNoOfPlayers = minNoOfPlayers;
    }

    public int getMinNoOfPlayers() {
        return minNoOfPlayers;
    }

    public HashSet<Integer> getSuccessors() {
        return successors;
    }

    public HashSet<Integer> getPredecessors() {
        return predecessors;
    }

    public void setSuccessors(HashSet<Integer>successors) {
        this.successors = successors;
    }

    public void setPredecessors(HashSet<Integer>preds) {
        this.predecessors = preds;
    }

    public void setActions(HashSet<Action> actions) {
        this.actions = actions;
        this.delayed = false;
        for (Action a: this.actions) {
            if (a instanceof ActionCountAndAddMoney && ((ActionCountAndAddMoney) a).getNeighbours()) {
                delayed = true;
            }
            a.setOwner(this);
        }
    }

    public HashSet<Action> getActions() {
        return this.actions;
    }

    @JsonIgnore
    public Player getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public int getAmountOfMaterial(RawMaterial.type t) {
        return resourceProduction.getResourceAmount(t);
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceList getResourceProduction() {
        return resourceProduction;
    }

    public ResourceList getResourceCost() {
        return resourceCost;
    }

    public int getCoinCost() {
        return coinCost;
    }

    public CardType getType() {
        return type;
    }

    public int getAge() {
        return age;
    }

    public void setResourceCost(ResourceList rList) {
        resourceCost = rList;
        rList.setCard(this);
    }

    public void setResourceProduction(ResourceList pList) {
        resourceProduction = pList;
        pList.setCard(this);
    }

    public void loadImage() {
        int width = 120;
        int height = 190;
        String nameOnFile = new String("file:rc/"+name+".png").replace(' ', '_');
        nameOnFile.replace(' ', '_');
        try {
            this.image = new Image(nameOnFile, width, height, false, true);
            if (image.isError()) {
                this.image = new Image("file:rc/undefined.png", width, height, false, true);
            }
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            this.image = new Image("file:rc/undefined.png", width, height, false, true);
        }
    }


    public boolean hasCoinCost() {
        return coinCost > 0;
    }

    public boolean hasResourceCost() {
        return !resourceCost.isZero();
    }

    public void addAction(Action a) {
        actions.add(a);
    }

    public void performActions(Player player) throws Exception {
        for (Action a:actions) {
            a.perform(player);
        }
    }

    public void initPredRefs(HashMap<Integer, PlayCard> map) {
        for (Integer i : predecessors) {
            predReferences.add(map.get(i));
        }
    }

    public boolean buyCard(Player buyer, ResourcePayments payments) throws Exception{
        boolean payOk = false;
        boolean resourcesOk = true;

        //Check any cost in money for this card.
        if (hasCoinCost()) {
            int pFunds = buyer.getBalance();
            if (pFunds >= coinCost) {
                payOk = true;
                buyer.withdraw(coinCost);
            }
        } else {
            payOk = true;
        }

        if (payOk && hasResourceCost()) {
            resourcesOk = true;
            for (int i=0;i<payments.size();i++) {
                ResourcePayment rp = payments.get(i);
                Player seller = rp.getCard().getOwner();
                if (buyer != seller) {
                    int specialPriceForYou = buyer.getImportPrice(seller, rp.getMaterialType()) * rp.getAmount();
                    if (buyer.getBalance() < specialPriceForYou) {
                        resourcesOk = false;
                    }
                    buyer.withdraw(specialPriceForYou);
                    seller.deposit(specialPriceForYou);
                }
            }
        }

        if (payOk && resourcesOk) {
            updatePlayer(buyer);
            return true;
        } else {
            return false;
        }
    }

    //TODO: Move this to sub-classes instead. This will become messy when a lot of init is being done.
    public void updatePlayer(Player player) throws Exception {
        owner = player;
        if (!resourceProduction.isZero()) {
            //Normal production card:
            if (!resourceProduction.isConditional()&& type != CardType.Wonder) {
                updatePublicResourceProduction(player);
            }

            if (!resourceProduction.isConditional() && type == CardType.Wonder) {
                updatePrivateResourceProduction(player);
            }

            //conditional brown card
            if (resourceProduction.isConditional() && (type == CardType.RawMaterial)) {
                updateOptionalPublicProduction(player);
            }

            if (resourceProduction.isConditional() && (type == CardType.CommercialStructure || type == CardType.Wonder)) {
                updateOptionalPrivateProduction(player);
                //test only
                //updateOptionalPublicProduction(player);

            }
        }

        if (!actions.isEmpty()) {
            performActions(player);
        }
    }

    private void updateOptionalPublicProduction(Player player) {
        //resourceProduction.updateOptionalProduction(player.getPublicConditionalResourceBank());
        player.getRawMaterialsPublic().addConditionalCard(this);
        //resourceProduction.updateOptionalProduction(player.getRawMaterialsPublic());

    }

    private void updateOptionalPrivateProduction(Player player){
        //resourceProduction.updateOptionalProduction(player.getPrivateConditionalResourceBank());
        player.getRawMaterialsPrivate().addConditionalCard(this);
    }

    private void updatePrivateResourceProduction(Player player) {
        resourceProduction.updateBank(player.getRawMaterialsPrivate(), this);
    }

    private void updatePublicResourceProduction(Player player) {
        resourceProduction.updateBank(player.getRawMaterialsPublic(), this);

    }

    public String toString() {
        return this.name; // + ", Type = " + type + " ";
    }
/*
    public static PlayCard statue() {
        PlayCard p = new PlayCard("Statue", CardType.CivilianStructure, 2, 0, false);
        p.setResourceCost(ResourceList.statue());
        p.addAction(new ActionAddVictoryPoints(2));
        p.addAction(new ActionAddMilitaryStrength(3));
        p.addAction(new ActionAddMoney(12));
        return p;
    }

    public static PlayCard test() {
        PlayCard p = new PlayCard("test", CardType.RawMaterial, 1, 0, false);
        p.addAction(new ActionAddMoney(10));
        p.addAction(new ActionAddMilitaryStrength(5));
        p.addAction(new ActionAddVictoryPoints(3));
        return p;
    }
*/
}
