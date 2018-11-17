package sevenWonders;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Jan on 2017-02-24.
 */
public class WonderDisplay {
    private final Wonder wonder;
    private ImageView image;

    public WonderDisplay(Wonder wonder) {
        this.wonder = wonder;
        image = new ImageView(new Image("file:rc/"+wonder.getFileName(),
                                        400,
                                        200,
                                        true,
                                        true));
    }
}
