package sevenWonders;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

/**
 * Created by Jan on 2017-02-08.
 */
public class CardDisplay extends Pane {
    private double width;
    private double height;
    private GameUI ui;
    private double cardWidth;
    private double cardHeight;
    private final int gap = 5;
    private CardInfoPane infoPane;

    public CardDisplay(GameUI ui) {
        super();
        this.ui = ui;
        width = 900;
        height = 220;
        cardWidth = (width - 8*gap)/7;
        cardHeight = 1.6 *  cardWidth;

        this.setStyle("-fx-background-color: yellow;");
        setMinSize(width, height);
        setMaxSize(width, height);
    }

    public void displayCards(CardList cards) {
        if (cards.isEmpty()) {
            return;
        }

        int noc = cards.size();
        double tW = noc*cardWidth+(noc-1)*gap;
        double startX = getWidth()/2-tW/2;

        int i=0;
        for (PlayCard c : cards) {
            double x = startX + (i * (cardWidth + gap));
            placeCard(c, x);
            i++;
        }
    }

    private void placeCard(PlayCard c, double xPos) {
        CardImage v = new CardImage();
        v.setPlayCard(c);
        v.setOnMouseEntered((e)->System.out.println("" + v.getPlayCard().getName() + "  Entered"));
        v.setOnMouseEntered((e)->displayCardInfo(v));
        v.setOnMouseExited(e->removeCardInfo());

        v.setOnMouseDragged(e->
        {
            removeCardInfo();
            v.setX(e.getX());
            v.setY(e.getY());
        });

        v.setOnMouseClicked(e-> {
            if(e.getClickCount() > 1) {
                selectCard(v.getPlayCard());
            }
        });

        v.setOnMouseReleased(e-> {
            checkHit(e);
        });

        v.setImage(c.getImage());
        v.setX(xPos);
        v.setY(10); //10
        getChildren().add(v);
    }

    private void selectCard(PlayCard playCard) {
        /* temporary, I think */
        Solutions s = new Solutions();

        ui.selectCard(playCard);
    }

    private void checkHit(MouseEvent e) {
        //Check if mouse hits some other object in the scene-graph...
        ObservableList<Node> list = this.getParent().getChildrenUnmodifiable();
    }

    /**
     * Display "hovering" box with info about the card as well as info about
     * if and how the card can be purchased.
     * @param v
     */
    private void displayCardInfo(CardImage v) {
        if (infoPane != null)
            return;
        infoPane = new CardInfoPane(v, ui.getPlayer());
        infoPane.add(this);
        TextArea text = new TextArea();
        Player p = ui.getPlayer();
        Solutions s = new Solutions();
        p.canBuySelf(v.getPlayCard(), new Solutions());
        p.solveFinance(v.getPlayCard(), s);
        String r = new String();
        r += v.getPlayCard().getResourceCost().toString() + "\n";
        r += s.textDescription();
        text.setText(r);
    }

    private void removeCardInfo() {
        if (infoPane != null)
            infoPane.remove();
        infoPane = null;
   }

    public void clear() {
        if (infoPane != null) {
            infoPane.removeParent(this);
            getChildren().clear();
        }
    }
}