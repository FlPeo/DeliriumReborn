package timeline;

import controller.ControllerGame;
import javafx.animation.AnimationTimer;

public class JeuTimeline extends AnimationTimer {

    private ControllerGame controllerGame;
    private long lu150ms;

    public JeuTimeline(ControllerGame controllerGame) {
        this.controllerGame = controllerGame;
        lu150ms = 0;
    }

    @Override
    public void handle(long now) {
        if( now - lu150ms >= 150_000_000 ) {
            controllerGame.majPositions();
            lu150ms = now;
        }
    }
}