package model;

import com.sun.javafx.geom.Vec2d;
import view.MonstreView;

public class MonstreBleu extends Monstre {
    public MonstreBleu(int i, int j) {
        position = new Vec2d(i, j);
        vue = new MonstreView('b');
        dir = 'g';
    }
    // monstre bleu = quand mort créé 9 diamands
}
