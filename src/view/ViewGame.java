package view;

import controller.ControllerGame;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import model.Niveau;
import model.Partie;
import tools.Path;

import java.util.ArrayList;
import java.util.List;

public class ViewGame {
    //TODO GERE L'AFFICHAGE DU JEU

    private Group root;
    private Partie partie;

    private List<ImageView> images;

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
        //Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds(); // Récupération de la taille de l'écran

        int hauteurPlateau = Niveau.currentLvl.length;
        int largeurPlateau = Niveau.currentLvl[0].length;
        int tailleImages = 64;/*
        root.setScaleX(2);
        root.setScaleY(2);*/

        images = new ArrayList<>();

        for (int i = 0; i < hauteurPlateau; i++) {
            for (int j = 0; j < largeurPlateau; j++) {
                ImageView vide = new ImageView(Path.background);
                vide.setTranslateX(i*tailleImages);
                vide.setTranslateY(j*tailleImages);
                images.add(vide);
                switch (Niveau.currentLvl[i][j])
                {
                    case 1:
                        ImageView mur = new ImageView(Path.wall);
                        mur.setTranslateX(i*tailleImages);
                        mur.setTranslateY(j*tailleImages);
                        images.add(mur);
                        break;
                    case 2:
                        ImageView terre = new ImageView(Path.clay);
                        terre.setTranslateX(i*tailleImages);
                        terre.setTranslateY(j*tailleImages);
                        images.add(terre);
                        break;
                    case 3:
                        ImageView diamond = new ImageView(Path.diamond);
                        diamond.setTranslateX(i*tailleImages);
                        diamond.setTranslateY(j*tailleImages);
                        images.add(diamond);
                        break;
                    case 4:
                        ImageView pierre = new ImageView(Path.stone);
                        pierre.setTranslateX(i*tailleImages);
                        pierre.setTranslateY(j*tailleImages);
                        images.add(pierre);
                        break;
                    case 5:
                        ImageView monstreBleu = new ImageView(Path.blueMonster);
                        monstreBleu.setTranslateX(i*tailleImages);
                        monstreBleu.setTranslateY(j*tailleImages);
                        images.add(monstreBleu);
                        break;
                    case 6:
                        ImageView monstreRouge = new ImageView(Path.redMonster);
                        monstreRouge.setTranslateX(i*tailleImages);
                        monstreRouge.setTranslateY(j*tailleImages);
                        images.add(monstreRouge);
                        break;
                    case 7:
                        ImageView mineur = new ImageView(Path.minerDown);
                        mineur.setTranslateX(i*tailleImages);
                        mineur.setTranslateY(j*tailleImages);
                        images.add(mineur);
                        break;
                }
            }
        }
    }

    private void vueJeuComplet() {
        root.getChildren().clear();
        root.getChildren().addAll(images);
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
}
