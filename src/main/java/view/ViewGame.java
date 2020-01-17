package view;

import controller.ControllerGame;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.*;
import tools.Path;

import java.util.ArrayList;

public class ViewGame {
    //GERE L'AFFICHAGE DU JEU

    public final static int TAILLE_IMAGES = 64;
    private Group root;
    private Partie partie;

    private StackPane plateau;

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

    private void initPlateau() {
        root.getChildren().clear();
        plateau = new StackPane();

        ArrayList<ImageView> images = new ArrayList<>();

        // On met le fond partout
        for (int i = 0; i < Niveau.currentLvl.length; i++) {
            for (int j = 0; j < Niveau.currentLvl[i].length; j++) {
                images.add(new ImageView(Path.background));
                images.get(images.size() - 1).setTranslateY(i * TAILLE_IMAGES);
                images.get(images.size() - 1).setTranslateX(j * TAILLE_IMAGES);
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

        plateau.getChildren().addAll(images);
        root.getChildren().clear();
        root.getChildren().add(plateau);
    }

    public void rafraichirVue() {

        // Modfification des coordonnées de l'image en fonction des coordonnées de l'objet
        // On multiplie par la taille des images pour ne pas qu'elles se chevauches
        for (Bloc bloc : partie.getNiveau().getBlocList()) {
            bloc.getView().setTranslateY(bloc.getPosition().getX() * TAILLE_IMAGES);
            bloc.getView().setTranslateX(bloc.getPosition().getY() * TAILLE_IMAGES);
        }
        for (Monstre monstre : partie.getNiveau().getMonstreList()) {
            monstre.vue.setTranslateY(monstre.getPosition().getX() * TAILLE_IMAGES);
            monstre.vue.setTranslateX(monstre.getPosition().getY() * TAILLE_IMAGES);
        }
        partie.getNiveau().getMineur().vue.setTranslateY(partie.getNiveau().getMineur().getPosition().getX() * TAILLE_IMAGES);
        partie.getNiveau().getMineur().vue.setTranslateX(partie.getNiveau().getMineur().getPosition().getY() * TAILLE_IMAGES);

        placerLaCamera();
    }

    private void placerLaCamera() {
        Point2D windowSize = new Point2D(root.getScene().getWidth(), root.getScene().getHeight()),
        plateauSize = new Point2D(Niveau.currentLvl[0].length * TAILLE_IMAGES, Niveau.currentLvl.length * TAILLE_IMAGES);

        double finalTranslateX,finalTranslateY;


        if(windowSize.getX() < plateauSize.getX()) {
            finalTranslateX = -partie.getNiveau().getMineur().getPosition().getY() * TAILLE_IMAGES + windowSize.getX()/2;
            if(finalTranslateX>0) finalTranslateX = 0;
            if(finalTranslateX<windowSize.getX()-plateauSize.getX()) finalTranslateX = windowSize.getX()-plateauSize.getX();
        } else {
            finalTranslateX = (windowSize.getX()-plateauSize.getX())/2;
        }

        if(windowSize.getY() < plateauSize.getY()) {
            finalTranslateY = -partie.getNiveau().getMineur().getPosition().getX() * TAILLE_IMAGES + windowSize.getY()/2;
            if(finalTranslateY>0) finalTranslateY = 0;
            if(finalTranslateY<windowSize.getY()-plateauSize.getY()) finalTranslateY = windowSize.getY()-plateauSize.getY();
        } else {
            finalTranslateY = (windowSize.getY()-plateauSize.getY())/2;
        }

        plateau.setTranslateX(finalTranslateX);
        plateau.setTranslateY(finalTranslateY);
    }

    /**
     * active l'écoute du clavier
     * @param kc (Controlleur du clavier)
     */
    void setEvents(ControllerGame kc) {
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
