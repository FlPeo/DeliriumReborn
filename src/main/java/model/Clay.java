package model;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import view.ClayView;

public class Clay extends Bloc {
    public ClayView vue;

    Clay(int i, int j) {
        position = new Point2D(i, j);
        vue = new ClayView();
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
