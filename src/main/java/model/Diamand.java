package model;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import view.DiamandView;

public class Diamand extends Bloc implements Fallable {
    public DiamandView vue;
    private boolean falling;
    private int fallingCounter;

    Diamand(int i, int j) {
        position = new Point2D(i, j);
        falling = false;
        vue = new DiamandView();
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
    }

    @Override
    public boolean fallTo(int x, int y) {
        fallingCounter--;
        if (fallingCounter>0) return false;
        position = new Point2D(x,y);
        falling = true;
        return true;
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
