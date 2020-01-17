package timeline;

import controller.ControllerGame;
import javafx.animation.AnimationTimer;

public class JeuTimeline extends AnimationTimer {

    private ControllerGame controllerGame;
    private long lu15ms, lu85ms;

    public JeuTimeline(ControllerGame controllerGame) {
        this.controllerGame = controllerGame;
        lu15ms = 0;
        lu85ms = 0;
    }

    @Override
    public void handle(long now) {

        if( now - lu85ms >= 150_000_000 ) {
            controllerGame.partie.getNiveau().appliquerGravite();
            controllerGame.launcher.getGame().rafraichirVue();
            lu85ms = now;
        }

        if( now - lu15ms >= 220_000_000 ) {
            controllerGame.computeAction();
            controllerGame.launcher.getGame().rafraichirVue();
            lu15ms = now;
        }
    }
}