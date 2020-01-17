package timeline;

import ia.NiveauIA;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import model.Niveau;
import view.ViewGameIA;
import view.ViewHandler;

public class IATimeline extends AnimationTimer {

    private NiveauIA niveauIA;       //Modèle (pour récupérer les états à afficher)
    private ViewGameIA viewGameIA;   //Vue
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
                if(!niveauIA.updateEtat()){    //On calcule l'état suivant
                    Platform.runLater(() -> {   //Si l'IA n'a pas réussi à trouver un état suivant pour finir le niveau
                        viewGameIA.affichageBlocageIA();    //Affichage d'une popup prévenant de la défaite de l'IA
                        launcher.getMenu().vueMenuComplete();  //Retour au menu
                    });

                    this.stop();   //Fin de la boucle de jeu
                }
                else {  //Si un état suivant a été trouvé par l'Ia
                    viewGameIA.updateView(niveauIA.getCurrentState(), niveauIA.finCollecteDiamants());   //On met la vue à jour

                    if (niveauIA.victory()) {   //Si l'IA a gagné
                        Platform.runLater(() -> {
                            viewGameIA.affichageVictoire();  //On affiche une popup prévenant de la victoire
                            if (niveauIA.numLevel + 1 < Niveau.niveaux.length)   //Si il y a un niveau suivant
                                launcher.demarrerPartieIA(niveauIA.numLevel + 1);   //On commence le niveau suivant
                            else
                                launcher.getMenu().vueMenuComplete();  //Sinon, on revient au menu
                        });

                        this.stop();   //Fin de la boucle de jeu
                    }
                }

                lastUpdate = now;
            }
        }
    }
}
