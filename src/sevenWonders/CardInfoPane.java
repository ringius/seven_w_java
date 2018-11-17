package sevenWonders;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by Jan on 2017-02-13.
 */
public class CardInfoPane {
    //private final ImageView coinIcon;
    private CardImage cardImage;
    private BorderPane infoPane;
    private Pane pane;

    public CardInfoPane(CardImage v, Player p) {
        this.cardImage = v;
        infoPane = new BorderPane();
        infoPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 209, 104), CornerRadii.EMPTY, Insets.EMPTY)));
        infoPane.setMaxSize(500, 75);
        Label nameLabel = new Label(cardImage.getPlayCard().getName());
        nameLabel.setStyle("-fx-font-family: \"Roboto Light\";" + "-fx-font-weight: bold; -fx-font-size: 18;" + "-fx-text-fill: white;");
        HBox topPane = new HBox();
        topPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
        topPane.getChildren().add(nameLabel);


        infoPane.setTop(topPane);

        TextArea text = new TextArea();
        text.setBlendMode(BlendMode.MULTIPLY);
        text.setBackground(new Background(new BackgroundFill(Color.rgb(255, 209, 104), CornerRadii.EMPTY, Insets.EMPTY)));

        infoPane.setCenter(text);
        Solutions s = new Solutions();
        //p.solveFinance(cardImage.getPlayCard(), s);
        p.canBuySelf(cardImage.getPlayCard(), s);
        System.out.println("Solutions after can buy: " + s);
        //s.cleanup();
        String r = new String();

        ResourceList list = cardImage.getPlayCard().getResourceCost();
        if (list.isZero())
            if (cardImage.getPlayCard().getCoinCost() == 0)
                r += "Card is free\n";
            else
                r += "No resources needed for this card\n";
        else
            r += cardImage.getPlayCard().getResourceCost().toString() + "\n";

        r += s.textDescription();
        text.setText(r);
    }

    public void add(Pane pane) {
        this.pane = pane;
        pane.getChildren().add(infoPane);
        infoPane.setLayoutX(cardImage.getX()); // + cardImage.getImage().getWidth());
        infoPane.setLayoutY(cardImage.getY() + cardImage.getImage().getHeight()+5);
        infoPane.setMaxSize(500, cardImage.getImage().getHeight());
    }

    public void remove() {
        pane.getChildren().remove(infoPane);
    }

    public void removeParent(CardDisplay cardDisplay) {
        if (infoPane != null) {
            infoPane.setVisible(false);
            cardDisplay.getChildren().remove(infoPane);
            infoPane = null;
        }
    }
}
