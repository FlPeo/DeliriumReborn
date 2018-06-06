package model;

import com.sun.javafx.geom.Vec2d;

public interface Fallable {
    boolean isFalling();
    void stopFalling();
    void fallTo(int x, int y);
}
