package model;

import javafx.geometry.Point2D;
import view.MonstreView;

class MonstreBleu extends Monstre {
    MonstreBleu(int i, int j) {
        position = new Point2D(i, j);
        vue = new MonstreView('b');
        dir = 'a'; // a comme 'aucune'
    }
    // monstre bleu = quand mort créé 9 diamands
}
