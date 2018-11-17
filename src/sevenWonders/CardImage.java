package sevenWonders;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Jan on 2017-02-13.
 */
public class CardImage extends ImageView {
    private PlayCard card;

    public CardImage() {
        super();
        card = null;
    }

    public CardImage(PlayCard card, Image image) {
        super(image);
        this.card = card;
    }

    public PlayCard getPlayCard() {
        return card;
    }

    public void setPlayCard(PlayCard card) {
        this.card = card;
    }
}
