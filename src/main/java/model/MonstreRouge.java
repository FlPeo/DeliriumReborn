package model;

import javafx.geometry.Point2D;
import view.MonstreView;

class MonstreRouge extends Monstre {
    MonstreRouge(int i, int j) {
        position = new Point2D(i, j);
        vue = new MonstreView('r');
        dir = 'a'; // a comme 'aucune'
    }
    // monstre rouge = explosion quand il meurt (AOE) puis vide (sauf mur indestructible)
}

