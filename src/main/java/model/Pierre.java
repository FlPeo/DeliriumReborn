package model;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import view.PierreView;

public class Pierre extends Bloc implements Fallable{
    public PierreView vue;
    private boolean falling;
    private int fallingCounter;

    Pierre(int i, int j) {
        position = new Point2D(i, j);
        falling = false;
        vue = new PierreView();
        fallingCounter = DELAY;
    }

    @Override
    public boolean isFalling() {
        return falling;
    }

    @Override
    public void stopFalling() {
        fallingCounter = DELAY;
        falling = false;
        vue.setRotating(false);
    }

    @Override
    public boolean fallTo(int x, int y) {
        fallingCounter--;
        if (fallingCounter>0) return false;
        position = new Point2D(x,y);
        falling = true;
        vue.setRotating(true);
        return true;
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
