package controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.JoueurHuman;
import model.LevelBuilder;
import model.Niveau;
import model.Partie;
import timeline.JeuTimeline;
import view.ViewHandler;


public class ControllerGame implements EventHandler<KeyEvent> {
    private boolean upPressed;
    private boolean rightPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean escapePressed;
    private ViewHandler launcher;
    private Partie partie;
    private JeuTimeline jeuTimeLine;

    public ControllerGame(ViewHandler launcher, Partie partie) {
        upPressed = false;
        rightPressed = false;
        downPressed = false;
        leftPressed = false;
        escapePressed = false;
        this.launcher = launcher;
        this.partie = partie;
        jeuTimeLine = new JeuTimeline(this);
        jeuTimeLine.start();
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if( keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED) ) {
            if( upPressed = keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.KP_UP || keyEvent.getCode() == KeyCode.Z ) {
                upPressed = true;
                rightPressed = false;
                downPressed = false;
                leftPressed = false;
            } else if( keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.KP_RIGHT || keyEvent.getCode() == KeyCode.D ) {
                rightPressed = true;
                upPressed = false;
                downPressed = false;
                leftPressed = false;
            } else if( keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.KP_DOWN || keyEvent.getCode() == KeyCode.S ) {
                downPressed = true;
                upPressed = false;
                rightPressed = false;
                leftPressed = false;
            } else if( keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.KP_LEFT || keyEvent.getCode() == KeyCode.Q ) {
                leftPressed = true;
                upPressed = false;
                rightPressed = false;
                downPressed = false;
            } else if( keyEvent.getCode() == KeyCode.ESCAPE ) {
                escapePressed = true;
            }
        }
    }

    /**
     * IDEE DE L'ALGO
     * TODO en gros si on appuie sur gauche droite haut bas il faut controler la case voisine est libre ou contient
     * TODO un cristal. Si elle est vide on déplace le personnage, sinon on ne fait rien.
     * <p>
     * TODO il faut également prendre en compte la barre d'espace. Si on regarde dans une direction et qu'on appuie sur
     * TODO la barre d'espace, si la case vers "là ou on regarde" est un "clay" alors on détruit le bloc.
     * <p>
     * Mais est-ce qu'on détruirait pas le bloc quand on veux se déplacer tout simplement vers un truc de clay ?
     */
    public synchronized void computeAction() {
        if(partie.getNiveau().isVictoire()){
            jeuTimeLine.stop();
            finPartieVictoire();
            return;
        }
        if( !leftPressed && !rightPressed && !upPressed && !downPressed ) return;
        if( leftPressed ) {
            partie.getNiveau().deplacerMineur('g');
        } else if( rightPressed ) {
            partie.getNiveau().deplacerMineur('d');
        } else if( upPressed ) {
            partie.getNiveau().deplacerMineur('h');
        } else if( downPressed ) {
            partie.getNiveau().deplacerMineur('b');
        }
        upPressed = downPressed = leftPressed = rightPressed = false;
        launcher.getGame().initPlateau();
        launcher.getGame().vueJeuComplet();
    }

    public void finPartieVictoire()
    {
        launcher.getGame().affichageVictoire();
        if(partie.getNiveau().numLevel+1 < Niveau.niveaux.length)
            launcher.demarrerPartie(partie.getNiveau().numLevel+1,partie.getJoueur() instanceof JoueurHuman ? partie.JOUEUR_HUMAN:partie.JOUEUR_IA);
        else
            launcher.getMenu().vueMenuComplete();
    }
}