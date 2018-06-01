package controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Partie;
import timeline.JeuTimeline;
import view.ViewHandler;


public class ControllerGame implements EventHandler<KeyEvent> {
    private boolean upPressed;
    private boolean rightPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean spaceBarPressed;
    private boolean escapePressed;
    private ViewHandler launcher;
    private Partie partie;
    private JeuTimeline jeuTimeLine;

    public ControllerGame(ViewHandler launcher, Partie partie) {
        upPressed = false;
        rightPressed = false;
        downPressed = false;
        leftPressed = false;
        spaceBarPressed = false;
        escapePressed = false;
        this.launcher = launcher;
        this.partie = partie;
        jeuTimeLine = new JeuTimeline(this);
        jeuTimeLine.start();
    }


    boolean isUpPressed() {
        return upPressed;
    }

    boolean isRightPressed() {
        return rightPressed;
    }

    boolean isDownPressed() {
        return downPressed;
    }

    boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isSpaceBarPressed() {
        return spaceBarPressed;
    }

    public boolean isEscapePressed() {
        return escapePressed;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if( keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED) ) {
            if( keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.KP_UP || keyEvent.getCode() == KeyCode.Z ) {
                upPressed = true;
            }
            if( keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.KP_RIGHT || keyEvent.getCode() == KeyCode.D ) {
                rightPressed = true;
            }
            if( keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.KP_DOWN || keyEvent.getCode() == KeyCode.S ) {
                downPressed = true;
            }
            if( keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.KP_LEFT || keyEvent.getCode() == KeyCode.Q ) {
                leftPressed = true;
            }
            if( keyEvent.getCode() == KeyCode.SPACE ) {
                leftPressed = true;
            }
            if( keyEvent.getCode() == KeyCode.ESCAPE ) {
                escapePressed = true;
            }
        } else if( keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED) ) {
            if( keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.KP_UP || keyEvent.getCode() == KeyCode.Z ) {
                upPressed = false;
            }
            if( keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.KP_RIGHT || keyEvent.getCode() == KeyCode.D ) {
                rightPressed = false;
            }
            if( keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.KP_DOWN || keyEvent.getCode() == KeyCode.S ) {
                downPressed = false;
            }
            if( keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.KP_LEFT || keyEvent.getCode() == KeyCode.Q ) {
                leftPressed = false;
            }
            if( keyEvent.getCode() == KeyCode.SPACE ) {
                leftPressed = false;
            }
            if( keyEvent.getCode() == KeyCode.ESCAPE ) {
                escapePressed = false;
            }
        }
    }

    /**
     * IDEE DE L'ALGO
     * TODO en gros si on appuie sur gauche droite haut bas il faut controler la case voisine est libre ou contient
     * TODO un cristal. Si elle est vide on déplace le personnage, sinon on ne fait rien.
     *
     * TODO il faut également prendre en compte la barre d'espace. Si on regarde dans une direction et qu'on appuie sur
     * TODO la barre d'espace, si la case vers "là ou on regarde" est un "clay" alors on détruit le bloc.
     *
     */
    public synchronized void computeAction() {
        if (leftPressed) {
            System.out.println("left");
        } else if (rightPressed) {
            System.out.println("right");
        }  else if (upPressed) {
            System.out.println("up");
        } else if (downPressed) {
            System.out.println("down");
        }
    }
}