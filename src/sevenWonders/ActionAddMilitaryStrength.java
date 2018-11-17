package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public class ActionAddMilitaryStrength extends Action {
    private int amount;

    public ActionAddMilitaryStrength() {
        amount = 0;
    }

    public void setAmount(int a) {
        amount = a;
    }

    public int getAmount() {
        return amount;
    }

    public ActionAddMilitaryStrength(int amount) {
        this.amount = amount;
    }

    public boolean perform(Player player) {
        player.addMilitaryStrength(amount);
        return true;
    }
}
