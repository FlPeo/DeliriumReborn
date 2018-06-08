package model;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;
import view.DiamandView;

public class Diamand extends Bloc implements Fallable {
    public DiamandView vue;
    private boolean falling;

    public Diamand(int i, int j) {
        position = new Vec2d(i, j);
        falling = false;
        vue = new DiamandView();
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

    @Override
    public ImageView getView() {
        return vue;
    }
}
