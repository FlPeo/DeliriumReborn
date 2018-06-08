package timeline;

import ia.NiveauIA;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import model.Niveau;
import view.ViewGameIA;
import view.ViewHandler;

public class IATimeline extends AnimationTimer {

    private NiveauIA niveauIA;
    private ViewGameIA viewGameIA;
    private long lastUpdate;
    private ViewHandler launcher;

    public IATimeline(ViewHandler launcher, NiveauIA niveauIA, ViewGameIA viewGameIA) {
        this.lastUpdate = 0;
        this.niveauIA = niveauIA;
        this.viewGameIA = viewGameIA;
        this.launcher = launcher;
    }

    @Override
    public void handle(long now) {
        if( now > lastUpdate ) {
            if( now - lastUpdate >= 500_000_000 ) {   //0.5s
                niveauIA.updateEtat();
                viewGameIA.updateView(niveauIA.getCurrentState(), niveauIA.finCollecteDiamants());

                if(niveauIA.victory()){
                    Platform.runLater(() -> {
                        viewGameIA.affichageVictoire();
                        if(niveauIA.numLevel+1 < Niveau.niveaux.length)
                            launcher.demarrerPartieIA(niveauIA.numLevel+1);
                        else
                            launcher.getMenu().vueMenuComplete();
                    });

                    this.stop();
                }

                lastUpdate = now;
            }
        }
    }
}
