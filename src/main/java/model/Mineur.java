package model;

import javafx.geometry.Point2D;
import view.MineurView;

public class Mineur extends Personnage {
    public MineurView vue;

    Mineur(int i, int j) {
        position = new Point2D(i, j);
        vue = new MineurView();
    }
}