package model;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;
import view.PierreView;

public class Pierre extends Bloc implements Fallable{
    public PierreView vue;
    private boolean falling;

    public Pierre(int i, int j) {
        position = new Vec2d(i, j);
        falling = false;
        vue = new PierreView();
    }

    @Override
    public boolean isFalling() {
        return falling;
    }

    @Override
    public void stopFalling() {
        falling = false;
        vue.setRotating(false);
    }

    @Override
    public void fallTo(int x, int y) {
        position = new Vec2d(x,y);
        falling = true;
        vue.setRotating(true);
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
