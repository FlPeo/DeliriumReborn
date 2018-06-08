package model;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;
import view.ClayView;

public class Clay extends Bloc {
    public ClayView vue;

    public Clay(int i, int j) {
        position = new Vec2d(i, j);
        vue = new ClayView();
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
