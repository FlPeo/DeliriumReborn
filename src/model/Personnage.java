package model;

import com.sun.javafx.geom.Vec2d;

public abstract class Personnage {

    private int pv;
    private int pvMax;
    private boolean estMort;
    protected Vec2d position;

    public Vec2d getPosition() {
        return position;
    }

    public void deplacement(Vec2d position) {
        this.position = position;
    }
}



