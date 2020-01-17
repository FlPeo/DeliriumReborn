package model;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import view.PorteView;

public class Porte extends Bloc {
    public PorteView vue;
    private boolean locked;

    Porte(int i, int j) {
        position = new Point2D(i, j);
        locked = true;
        vue = new PorteView();
    }

    boolean isLocked() {
        return locked;
    }

    void unLock() {
        locked = false;
        vue.ouvrirPorte();
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
