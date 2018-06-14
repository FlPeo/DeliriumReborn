package model;

import com.sun.javafx.geom.Vec2d;
import view.MonstreView;

class MonstreRouge extends Monstre {
    MonstreRouge(int i, int j) {
        position = new Vec2d(i, j);
        vue = new MonstreView('r');
        dir = 'a'; // a comme 'aucune'
    }
    // monstre rouge = explosion quand il meurt (AOE) puis vide (sauf mur indestructible)
}

