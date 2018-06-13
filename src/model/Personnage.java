package model;

import com.sun.javafx.geom.Vec2d;

public abstract class Personnage {

    protected Vec2d position;

    public Vec2d getPosition() {
        return position;
    }

    void deplacement(Vec2d position) {
        this.position = position;
    }
}



