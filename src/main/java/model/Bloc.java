package model;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public abstract class Bloc {
    Point2D position;

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public abstract ImageView getView();
}
