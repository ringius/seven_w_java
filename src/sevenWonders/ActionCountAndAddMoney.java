package sevenWonders;

/**
 * Created by Jan on 2017-02-05.
 */
public class ActionCountAndAddMoney extends Action {
    private boolean self;
    private boolean neighbours;
    private int multiplier;
    private PlayCard.CardType type;

    public void setSelf(boolean self) {
        this.self = self;
    }

    public boolean getSelf() {
        return self;
    }

    public void setNeighbours(boolean neighbours) {
        this.neighbours = neighbours;
    }

    public boolean getNeighbours() {
        return neighbours;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setType(PlayCard.CardType type) {
        this.type = type;
    }

    public ActionCountAndAddMoney() {

    }

    public ActionCountAndAddMoney(boolean self, boolean neighbours, int muliplier, PlayCard.CardType type) {
        setSelf(self);
        setNeighbours(neighbours);
        setMultiplier(muliplier);
        setType(type);
    }

    public boolean perform(Player p) {
        int no=0;
        if (self){
            no+=p.getNumberOfCards(type);
        }
        if (neighbours) {
            no+=p.getEastPlayer().getNumberOfCards(type);
            no+=p.getWestPlayer().getNumberOfCards(type);
        }
        p.deposit(this.multiplier * no);
        return true;
    }
}
