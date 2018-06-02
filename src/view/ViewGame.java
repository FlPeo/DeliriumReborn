package view;

import controller.ControllerGame;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import model.*;
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

    public void initPlateau(){
        //Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds(); // Récupération de la taille de l'écran
/*
        int hauteurPlateau = Niveau.currentLvl.length;
        int largeurPlateau = Niveau.currentLvl[0].length;*/
        int tailleImages = 64;

        List<Bloc> blocs = partie.getNiveau().getBlocList();
        List<Monstre> monstres = partie.getNiveau().getMonstreList();
        images = new ArrayList<>();

        // On met le fond partout
        for (int i = 0; i < Niveau.currentLvl.length; i++) {
            for (int j = 0; j < Niveau.currentLvl[i].length; j++) {
                images.add(new ImageView(Path.background));
                images.get(images.size() - 1).setTranslateX(i * tailleImages);
                images.get(images.size() - 1).setTranslateY(j * tailleImages);
            }
        }

        // Les blocs de matière
        for (Bloc bloc : blocs) {
            // Ajout de l'image selon le type de bloc
            if (bloc instanceof Clay)
                images.add(new ImageView(Path.clay));
            else if (bloc instanceof Diamand)
                images.add(new ImageView(Path.diamond));
            else if (bloc instanceof Mur)
                images.add(new ImageView(Path.wall));
            else if (bloc instanceof Pierre)
                images.add(new ImageView(Path.stone));
            // Modfification des coordonnées de l'image en fonction des coordonnées de l'objet
            // On multiplie par la taille des images pour ne pas qu'elles se chevauches
            images.get(images.size() - 1).setTranslateX(bloc.getPosition().x * tailleImages);
            images.get(images.size() - 1).setTranslateY(bloc.getPosition().y * tailleImages);
        }

        // Les monstres
        for(Monstre monstre : monstres)
        {
            if(monstre instanceof MonstreBleu)
                images.add(new ImageView(Path.blueMonster));
            else if(monstre instanceof MonstreRouge)
                images.add(new ImageView(Path.redMonster));

            images.get(images.size() - 1).setTranslateX(monstre.getPosition().x * tailleImages);
            images.get(images.size() - 1).setTranslateY(monstre.getPosition().y * tailleImages);
        }

        // Le mineur
        images.add(new ImageView(Path.minerDown));
        images.get(images.size() - 1).setTranslateX(partie.getNiveau().getMineur().getPosition().x * tailleImages);
        images.get(images.size() - 1).setTranslateY(partie.getNiveau().getMineur().getPosition().y * tailleImages);


        /*
        root.setScaleX(2);
        root.setScaleY(2);*/

        /*images = new ArrayList<>();

        for (int i = 0; i < hauteurPlateau; i++) {
            for (int j = 0; j < largeurPlateau; j++) {
                ImageView vide = new ImageView(Path.background);
                vide.setTranslateX(i*tailleImages);
                vide.setTranslateY(j*tailleImages);
                images.add(vide);
                switch (Niveau.currentLvl[i][j])
                {
                    case 1:
                        // Création de l'image
                        ImageView mur = new ImageView(Path.wall);
                        // Définition de l'endroit où dessiner l'image
                        mur.setTranslateX(i*tailleImages);
                        mur.setTranslateY(j*tailleImages);
                        // Ajoute de l'image dans la liste contenant les images
                        images.add(mur);

                        Mur murObj = new Mur(i*tailleImages,j*tailleImages);


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
        }*/
    }

    public void vueJeuComplet() {
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
