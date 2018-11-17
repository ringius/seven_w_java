package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public class ActionAddVictoryPoints extends Action {
    private int amount;

    public ActionAddVictoryPoints() {
        amount = -1;
    }

    public ActionAddVictoryPoints(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean perform(Player player) {
        player.addVictoryPoints(getOwner().getType(), amount);
        return true;
    }
}
