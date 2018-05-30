package view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import model.Niveau;
import model.Partie;

public class ViewGame {
    //TODO GERE L'AFFICHAGE DU JEU

    private Group root;
    private Partie partie;
    private GridPane grille;

    /**
     * Constructeur de la vue du jeu
     * @param root : élément de base dans lequel seront ajoutés tous les autres éléments de la vue
     */
    ViewGame(Group root, Partie partie){
        this.root = root;
        this.partie = partie;
        initPlateau();
        vueJeuComplet();
    }

    private void initPlateau(){
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds(); // Récupération de la taille de l'écran

        int hauteurPlateau = Niveau.currentLvl.length;
        int largeurPlateau = Niveau.currentLvl[0].length;

        grille = new GridPane();
        grille.setGridLinesVisible(true);
        grille.setMinSize(primaryScreenBounds.getWidth()/largeurPlateau,
                primaryScreenBounds.getHeight()/hauteurPlateau);

        grille.add(new Button("Coucou !"),0,0);


    }

    private void vueJeuComplet()
    {
        root.getChildren().clear();
        root.getChildren().addAll(grille);
    }


}
