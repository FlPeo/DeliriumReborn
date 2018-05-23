package model;

import com.sun.javafx.geom.Vec2d;

public abstract class Joueur {
    private Mineur mineur;

    /**
     * Action principale du joueur : déplace le mineur de sa position vers la position définie par x et y
     * @param x (abcisse)
     * @param y (ordonnée)
     */
    protected void deplacerMineur (int x, int y) {
        if ( (x-mineur.position.x == 0 ^ y-mineur.position.y == 0) &&
                (Math.abs(mineur.position.x - x) == 1 ^ Math.abs(mineur.position.y - y) == 1)) {
            mineur.deplacement(new Vec2d(x, y));
        }
    }
}
