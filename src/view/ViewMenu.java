package view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import tools.Path;

public class ViewMenu {
    // TODO VUE A TROIS BOUTONS : NOUVELLE PARTIE // CHARGER NIVEAU // QUITTER
    // TODO Il faudra trouver une solution pour que dans nouvelle partie comme dans charger niveau on puisse choisir si
    // TODO le joueur est l'humain ou l'IA

    private Group root;
    private ImageView imgBg;

    ViewMenu(Group root)
    {
        this.root = root;
        initBackground();
        this.root.getChildren().addAll(imgBg);
    }

    /**
     * Mise en place de l'image de fond en fonction de la taille de l'écran de l'utilisateur
     */
    private void initBackground() {
        imgBg = new ImageView(Path.backgroundMenu);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds(); // Récupération de la taille de l'écran
        imgBg.setFitHeight((int) primaryScreenBounds.getHeight());
        imgBg.setFitWidth((int) primaryScreenBounds.getWidth());
    }
}
