package view;

import controller.ControllerGame;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
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
        int tailleImages = 64;

        List<Bloc> blocs = partie.getNiveau().getBlocList();
        List<Monstre> monstres = partie.getNiveau().getMonstreList();
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
            else if (bloc instanceof Porte)
                images.add(new ImageView(((Porte) bloc).isLocked()?Path.lockedDoor:Path.unlockedDoor));
            // Modfification des coordonnées de l'image en fonction des coordonnées de l'objet
            // On multiplie par la taille des images pour ne pas qu'elles se chevauches
            images.get(images.size() - 1).setTranslateY(bloc.getPosition().x * tailleImages);
            images.get(images.size() - 1).setTranslateX(bloc.getPosition().y * tailleImages);
        }

        // Les monstres
        for(Monstre monstre : monstres)
        {
            if(monstre instanceof MonstreBleu)
                images.add(new ImageView(Path.blueMonster));
            else if(monstre instanceof MonstreRouge)
                images.add(new ImageView(Path.redMonster));

            images.get(images.size() - 1).setTranslateY(monstre.getPosition().x * tailleImages);
            images.get(images.size() - 1).setTranslateX(monstre.getPosition().y * tailleImages);
        }

        // Le mineur
        images.add(new ImageView(Path.minerDown));
        images.get(images.size() - 1).setTranslateY(partie.getNiveau().getMineur().getPosition().x * tailleImages);
        images.get(images.size() - 1).setTranslateX(partie.getNiveau().getMineur().getPosition().y * tailleImages);
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
