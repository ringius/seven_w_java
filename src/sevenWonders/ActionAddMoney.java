package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public class ActionAddMoney extends Action {
    private int amount;

    public ActionAddMoney() {
        amount = 0;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public ActionAddMoney(int amount) {
        this.amount = amount;
    }
    public boolean perform(Player player) {
        player.deposit(amount);
        return true;
    }
}
