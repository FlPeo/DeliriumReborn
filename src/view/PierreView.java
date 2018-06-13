package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import tools.Path;

public class PierreView extends ImageView {
    private Timeline rotateAnim;
    private static final float speed = 0.8f;

    public PierreView() {
        super(new Image(Path.stone));
        rotateAnim = new Timeline(new KeyFrame(
                Duration.millis(16),
                ae -> rotate())
        );
        rotateAnim.setCycleCount(Animation.INDEFINITE);
    }

    private void rotate() {
        setRotate(getRotate()+speed);
    }

    public void setRotating(boolean rotating) {
        if(rotating) rotateAnim.play();
        else rotateAnim.pause();
    }
}
