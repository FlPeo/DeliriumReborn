package model;

import com.sun.javafx.geom.Vec2d;

public class Pierre extends Bloc implements Fallable{
    private boolean falling;

    public Pierre(int i, int j) {
        position = new Vec2d(i, j);
        falling = false;
    }

    @Override
    public boolean isFalling() {
        return falling;
    }

    @Override
    public void stopFalling() {
        falling = false;
    }

    @Override
    public void fallTo(int x, int y) {
        position = new Vec2d(x,y);
        falling = true;
    }
}
