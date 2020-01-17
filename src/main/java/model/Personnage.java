package model;


import javafx.geometry.Point2D;

public abstract class Personnage {

    protected Point2D position;

    public Point2D getPosition() {
        return position;
    }

    void deplacement(Point2D position) {
        this.position = position;
    }
}



