package timeline;

import ia.NiveauIA;
import javafx.animation.AnimationTimer;
import view.ViewGameIA;

public class IATimeline extends AnimationTimer {

    private NiveauIA niveauIA;
    private ViewGameIA viewGameIA;
    private long lastUpdate;

    public IATimeline(NiveauIA niveauIA, ViewGameIA viewGameIA) {
        this.lastUpdate = 0;
        this.niveauIA = niveauIA;
        this.viewGameIA = viewGameIA;
    }

    @Override
    public void handle(long now) {
        if( now > lastUpdate ) {
            if( now - lastUpdate >= 500_000_000 ) {   //0.5s
                niveauIA.updateEtat();
                viewGameIA.updateView(niveauIA.getCurrentState());
                lastUpdate = now;
            }
        }
    }
}
