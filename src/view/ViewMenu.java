package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import tools.Path;

import java.util.ArrayList;
import java.util.List;

public class ViewMenu {
    // TODO VUE A TROIS BOUTONS : NOUVELLE PARTIE // CHARGER NIVEAU // QUITTER
    // TODO Il faudra trouver une solution pour que dans nouvelle partie comme dans charger niveau on puisse choisir si
    // TODO le joueur est l'humain ou l'IA

    private Group root;
    private ImageView imgBg;
    private Text titre;
    private VBox menu;
    private List<Button> listesBoutons;
    private Button nouvellePartie, chargerNiveau, quitter;

    ViewMenu(Group root)
    {
        this.root = root;
        initBackground();
        initMenu();
        vueMenuComplete();
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

    /**
     * Instanciation du titre du jeu
     */
    private void initTitle() {
        titre = new Text("Delirium");
        titre.setStyle("-fx-padding: 100px;" +
                "-fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, #ff9f00 0%, #ecf5ac 82%);" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 10, 0.7, 0, 0);");

        Font policeTitre = Font.loadFont(getClass().getResourceAsStream(Path.fontHeadCase), 110);
        titre.setFont(policeTitre);
    }

    private void initBoutons() {
        listesBoutons = new ArrayList<>();
        listesBoutons.add(nouvellePartie = new Button("Nouvelle partie"));
        listesBoutons.add(chargerNiveau = new Button("Charger un niveau"));
        listesBoutons.add(quitter = new Button("Quitter"));

        Font policeBoutons = Font.loadFont(getClass().getResourceAsStream(Path.fontRemachineScript), 60);
        listesBoutons.forEach(button -> button.setFont(policeBoutons));
        listesBoutons.forEach(button -> button.setTextFill(Color.DARKRED));
        listesBoutons.forEach(button -> button.setPickOnBounds(false));
        listesBoutons.forEach(button -> button.setBackground(null));

        VBox.setMargin(nouvellePartie, new Insets((imgBg.getFitHeight() / 15), 0, 0,0));
        /*VBox.setMargin(chargerNiveau, new Insets(0, 0,0,imgBg.getFitWidth()/2.-imgBg.getFitWidth()/6.));
        VBox.setMargin(quitter, new Insets(0, 0,0,imgBg.getFitWidth()/2.-imgBg.getFitWidth()/9.));
    */}

    private void initMenu()
    {
        menu = new VBox(25);
        menu.setAlignment(Pos.CENTER); // Les boutons sont centrés les uns par rapport aux autres

        initTitle();
        menu.getChildren().addAll(titre);

        initBoutons();
        menu.getChildren().addAll(listesBoutons);
        menu.setPadding(new Insets(0,0,0,imgBg.getFitWidth()/2.-imgBg.getFitWidth()/5.));
        menu.setLayoutX(50);
        menu.setLayoutY(50);
    }

    private void vueMenuComplete()
    {
        root.getChildren().addAll(imgBg);
        root.getChildren().addAll(menu);
    }
}
