package sevenWonders;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Created by Jan on 2017-02-24.
 */
public class PlayerDisplay extends GridPane {
    private final Player player;
    private boolean showName;
    //private GridPane panel;
    private MoneyDisplay bankDisplay;
    private ResourceDisplay resourceDisplay;
    private WonderDisplay wonderDisplay;

    public PlayerDisplay(Player player, boolean showName) {
        this.player = player;
        this.showName = showName;
        //wonderDisplay = new WonderDisplay(player.getWonder());
        //panel = new GridPane();
        bankDisplay = new MoneyDisplay();
        bankDisplay.setAmount(player.getBalance());
        resourceDisplay = new ResourceDisplay();

        int rowIndex = 0;
        if (showName)
            add(new Label(player.getName()), 0, rowIndex++);
        add(bankDisplay, 0, rowIndex++);
        add(resourceDisplay, 0, rowIndex++);
        this.getColumnConstraints().add(new ColumnConstraints(200));
        //this.getColumnConstraints().add(new ColumnConstraints(600));

    }

    public void update() {
        bankDisplay.setAmount(player.getBalance());
        resourceDisplay.update(player);
    }

    public Pane getLayout() {
        return this;
    }
}

