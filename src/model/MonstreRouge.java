package model;

import com.sun.javafx.geom.Vec2d;
import view.MonstreView;

public class MonstreRouge extends Monstre {
    public MonstreRouge(int i, int j) {
        position = new Vec2d(i, j);
        vue = new MonstreView('r');
        dir = 'g';
    }
    // monstre rouge = explosion quand il meurt (AOE) puis vide (sauf mur indestructible)
}

