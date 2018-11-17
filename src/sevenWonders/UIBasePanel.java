package sevenWonders;

import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

/**
 * Created by Jan on 2017-02-17.
 */
public class UIBasePanel extends GridPane{
    private PlayerDisplay wPlayerDisplay;
    private PlayerDisplay ePlayerDisplay;

    protected GameUI ui;
    public CardDisplay cardDisplay;
    private PlayerDisplay playerDisplay;

    public UIBasePanel(GameUI ui) {
        super();
        this.ui = ui;
        cardDisplay = new CardDisplay(ui);
        cardDisplay.setStyle("-fx-background-color: green");
        playerDisplay = new PlayerDisplay(ui.getPlayer(), true);
        playerDisplay.getLayout().setStyle("-fx-background-color: yellow");
        wPlayerDisplay = new PlayerDisplay(ui.getPlayer().getWestPlayer(), true);
        ePlayerDisplay = new PlayerDisplay(ui.getPlayer().getEastPlayer(), true);


        add(wPlayerDisplay.getLayout(), 0, 1);
        add(playerDisplay.getLayout(), 1, 1);
        add(ePlayerDisplay.getLayout(), 2, 1);
        add(cardDisplay, 0, 0, 3, 1);

        getColumnConstraints().add(new ColumnConstraints(150, 150, 150));
        getColumnConstraints().add(new ColumnConstraints(600));
        getColumnConstraints().add(new ColumnConstraints(150, 150, 150));
        //setGridLinesVisible(true);
    }

    public void setButton(Button btn) {
        add(btn, 0, 0);
    }

    public void updatePlayer() {
        getColumnConstraints().clear();
        getColumnConstraints().add(new ColumnConstraints(150, 150, 150));
        getColumnConstraints().add(new ColumnConstraints(600));
        getColumnConstraints().add(new ColumnConstraints(150, 150, 150));

        playerDisplay.update();
        wPlayerDisplay.update();
        ePlayerDisplay.update();
    }

    public void removeButton(Button btn) {
        getChildren().remove(btn);
    }
}
