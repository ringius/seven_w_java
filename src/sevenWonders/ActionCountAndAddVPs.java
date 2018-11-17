package sevenWonders;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Jan on 2017-02-05.
 */
public class ActionCountAndAddVPs extends Action{
    private boolean triggered;
    private boolean self;
    private boolean neighbours;
    private int multiplier;
    private PlayCard.CardType cardType;

    public ActionCountAndAddVPs() {
        this.multiplier = 1;
    }

    public ActionCountAndAddVPs(boolean self, boolean neighbours, PlayCard.CardType type) {
        this.self = self;
        this.triggered = false;
        this.neighbours = neighbours;
        this.cardType = type;
    }

    @JsonIgnore
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public void setMultiplier(int m ){
        this.multiplier = m;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public void setNeighbours(boolean neighbours) {
        this.neighbours = neighbours;
    }

    public void setCardType(PlayCard.CardType cardType) {
        this.cardType = cardType;
    }

    public boolean perform (Player p) {
        if (!triggered) {
            triggered = true;
            p.addDeferredAction(this);
        } else {
            int no = 0;
            if (self) {
                no += p.getNumberOfCards(cardType);
            }
            if (neighbours) {
                no += p.getEastPlayer().getNumberOfCards(cardType);
                no += p.getWestPlayer().getNumberOfCards(cardType);
            }
            p.addVictoryPoints(getOwner().getType(), no);
        }
        return true;
    }
}
