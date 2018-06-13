package model;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;

public abstract class Bloc {
    Vec2d position;

    public Vec2d getPosition() {
        return position;
    }

    public void setPosition(Vec2d position) {
        this.position = position;
    }

    public abstract ImageView getView();
}
