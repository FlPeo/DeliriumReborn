package view;

import controller.ControllerGame;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import model.*;
import tools.Path;

import java.util.ArrayList;
import java.util.List;

public class ViewGame {
    //TODO GERE L'AFFICHAGE DU JEU

    private Group root;
    private Partie partie;

    private List<ImageView> images;
    private final int tailleImages = 64;

    /**
     * Constructeur de la vue du jeu
     * @param root : élément de base dans lequel seront ajoutés tous les autres éléments de la vue
     */
    ViewGame(Group root, Partie partie){
        this.root = root;
        this.partie = partie;
        initPlateau();
        rafraichirVue();
    }

    public void initPlateau() {
        root.getChildren().clear();
        images = new ArrayList<>();

        // On met le fond partout
        for (int i = 0; i < Niveau.currentLvl.length; i++) {
            for (int j = 0; j < Niveau.currentLvl[i].length; j++) {
                images.add(new ImageView(Path.background));
                images.get(images.size() - 1).setTranslateY(i * tailleImages);
                images.get(images.size() - 1).setTranslateX(j * tailleImages);
            }
        }

        // Les blocs de matière
        for (Bloc bloc : partie.getNiveau().getBlocList())
                images.add(bloc.getView());


        // Les monstres
        for (Monstre monstre : partie.getNiveau().getMonstreList())
            images.add(monstre.vue);


        // Le mineur
        images.add(partie.getNiveau().getMineur().vue);

        root.getChildren().addAll(images);
    }

    public void rafraichirVue() {

        // Modfification des coordonnées de l'image en fonction des coordonnées de l'objet
        // On multiplie par la taille des images pour ne pas qu'elles se chevauches
        for (Bloc bloc : partie.getNiveau().getBlocList()) {
            bloc.getView().setTranslateY(bloc.getPosition().x * tailleImages);
            bloc.getView().setTranslateX(bloc.getPosition().y * tailleImages);
        }
        for (Monstre monstre : partie.getNiveau().getMonstreList()) {
            monstre.vue.setTranslateY(monstre.getPosition().x * tailleImages);
            monstre.vue.setTranslateX(monstre.getPosition().y * tailleImages);
        }
        partie.getNiveau().getMineur().vue.setTranslateY(partie.getNiveau().getMineur().getPosition().x * tailleImages);
        partie.getNiveau().getMineur().vue.setTranslateX(partie.getNiveau().getMineur().getPosition().y * tailleImages);
    }

    /**
     * active l'écoute du clavier
     * (TODO) penser à désactiver si on quitte la partie
     * @param kc (Controlleur du clavier)
     */
    public void setEvents( ControllerGame kc) {
        root.getScene().setOnKeyPressed(kc);
        root.getScene().setOnKeyReleased(kc);
    }

    /**
     * Affiche une popup informant le joueur qu'il à gagné
     */
    public void affichageVictoire()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin");
        alert.setHeaderText("Victoire !");
        alert.setContentText("Félicitation, vous avez gagnez !");
        alert.show();
    }

    public void affichageDefaite() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin");
        alert.setHeaderText("Défaite");
        alert.setContentText("Dommage... Vous avez perdu");
        alert.show();
    }
}
