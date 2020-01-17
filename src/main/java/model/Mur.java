package model;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import view.MurView;

public class Mur extends Bloc {
    public MurView vue;
    Mur(int i, int j) {
        position = new Point2D(i, j);
        vue = new MurView();
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
