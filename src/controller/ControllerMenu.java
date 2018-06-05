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
        if(event.getSource().equals(launcher.getMenu().getNouvellePartieJoueur())
                && event.getEventType().equals(MouseEvent.MOUSE_CLICKED) ) {
            launcher.demarrerPartie(Integer.parseInt(launcher.getMenu().getChoixNiveau().getSelectedToggle().getUserData().toString()),2);
        } else if(event.getSource().equals(launcher.getMenu().getQuitter())
                && event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
            launcher.getPrimaryStage().close();
        }
    }
}
