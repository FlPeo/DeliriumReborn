package timeline;

import ia.IAComputeAction;
import javafx.animation.AnimationTimer;

public class IATimeline extends AnimationTimer {

    private long lastUpdate;
    private IAComputeAction ia;

    public IATimeline(IAComputeAction ia) {
        this.ia = ia;
    }

    @Override
    //TODO
    public void handle(long now) {
        if( now > lastUpdate ) {
            if( now - lastUpdate >= 20_000_000 ) {
                //ia.defineActionMineur();
                lastUpdate = now;
            }
        }
    }
}
