package model;

import com.sun.javafx.geom.Vec2d;

public class Porte extends Bloc {
    private boolean locked;

    public Porte(int i, int j) {
        position = new Vec2d(i, j);
        locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public void unLock() {
        locked = false;
    }
}
