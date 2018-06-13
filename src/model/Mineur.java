package model;

import com.sun.javafx.geom.Vec2d;
import view.MineurView;

public class Mineur extends Personnage {
    public MineurView vue;

    Mineur(int i, int j) {
        position = new Vec2d(i, j);
        vue = new MineurView();
    }
}