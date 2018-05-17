package model;

import com.sun.javafx.geom.Vec2d;

public class Mineur extends Personnage {
    private int nbVie;
    // TODO gerer perte de vie

    public Mineur(int i, int j) {
        position = new Vec2d(i, j);
        nbVie = 3; // je pense qu'il faudra passer la vie d'un mineur d'un niveau à l'autre
    }
}
