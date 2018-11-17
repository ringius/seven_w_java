package sevenWonders;

import javafx.scene.control.Label;

/**
 * Created by Jan on 2017-02-24.
 */
public class MoneyDisplay extends Label {
    private int amount;
    public MoneyDisplay() {
        super();
    }

    public void setAmount(int amount) {
        this.amount = amount;
        setText("Balance: " + amount);
    }
}
