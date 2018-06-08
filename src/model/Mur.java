package model;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;
import view.MurView;

public class Mur extends Bloc {
    public MurView vue;
    public Mur(int i, int j) {
        position = new Vec2d(i, j);
        vue = new MurView();
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
