package timeline;

import ia.IAComputeAction;
import javafx.animation.AnimationTimer;

public class IAThread extends AnimationTimer {

    private long lastUpdate;
    private IAComputeAction ia;

    public IAThread(IAComputeAction ia) {
        this.ia = ia;
    }

    @Override
    public void handle(long now) {
        if( now > lastUpdate ) {
            if( now - lastUpdate >= 20_000_000 ) {
                ia.defineActionMineur();
                lastUpdate = now;
            }
        }
    }
}
