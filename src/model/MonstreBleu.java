package model;

import com.sun.javafx.geom.Vec2d;

public class MonstreBleu extends Monstre {
    public MonstreBleu(int i, int j) {
        position = new Vec2d(i, j);
    }
    // monstre bleu = quand mort créé 9 diamands
}
