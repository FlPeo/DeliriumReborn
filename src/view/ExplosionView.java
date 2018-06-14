package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import tools.Path;

public class ExplosionView extends ImageView {
    private double duration = 1000;

    public ExplosionView(double y, double x) {
        super(new Image(Path.explosion));
        setVisible(false);
        setTranslateX(x);
        setTranslateY(y);
    }

    public ExplosionView start() {
        setVisible(true);
        Timeline t = new Timeline(new KeyFrame(
                Duration.millis(duration),
                ae -> ((StackPane)getParent()).getChildren().remove(this)
        ));
        t.play();
        return this;
    }
}
