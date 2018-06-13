package model;

import com.sun.javafx.geom.Vec2d;
import view.MineurView;

public class Mineur extends Personnage {
    public MineurView vue;
    private int nbVie;

    public Mineur(int i, int j) {
        position = new Vec2d(i, j);
        nbVie = 3; // je pense qu'il faudra passer la vie d'un mineur d'un niveau à l'autre
        vue = new MineurView();
    }
}