package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import view.ViewHandler;

public class ControllerMenu implements EventHandler<MouseEvent> {

    private ViewHandler launcher;

    public ControllerMenu(ViewHandler vh){
         launcher = vh;
        this.launcher.setEventHandlerMenu(this);
    }

    @Override
    public void handle(MouseEvent event) {
        if(event.getSource().equals(launcher.getMenu().getNouvellePartie())
                && event.getEventType().equals(MouseEvent.MOUSE_CLICKED) ) {
            // TODO : Pop up avec choix IA ou pas
            launcher.demarrerPartie(1,2);
        } else if(event.getSource().equals(launcher.getMenu().getChargerNiveau())
                && event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
            // Pop up ou formulaire pour choisir le niveau
        } else if(event.getSource().equals(launcher.getMenu().getQuitter())
                && event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
            launcher.getPrimaryStage().close();
        }
    }
}
