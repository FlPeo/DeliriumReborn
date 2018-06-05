package view;

import controller.ControllerMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import model.MenuGame;
import tools.Path;

import java.util.ArrayList;
import java.util.List;

public class ViewMenu {
    // TODO Il faudra trouver une solution pour que dans nouvelle partie comme dans charger niveau on puisse choisir si
    // TODO le joueur est l'humain ou l'IA

    private Group root;
    private ImageView imgBg;
    private Text titre, titreNiveau;
    private VBox menu, niveauxMenu, menuComplet;
    private HBox boutonsEtNiveaux;
    private List<Button> listesBoutons;
    private List<RadioButton> listeBoutonsRadio;
    private Button nouvellePartieJoueur, quitter, nouvellePartieIA;
    private ToggleGroup choixNiveau;
    private MenuGame model;

    /**
     * Constructeur de la vue du menu
     * @param root : élément de base dans le seront ajouter tous les éléments de la vue
     */
    ViewMenu(Group root, MenuGame model)
    {
        this.root = root;
        this.model = model;
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

    /**
     * initialise la liste des boutons du menu :
     *  nouvelle partie (charge une nouvelle partie)
     *  nouvelle partie IA (charge une nouvelle partie mais c'est l'IA qui joue)
     *  charger niveau (permet au joueur de choisir le niveau)
     *  quitter (bah... ca quitte !)
     */
    private void initBoutons() {
        listesBoutons = new ArrayList<>();
        listesBoutons.add(nouvellePartieJoueur = new Button("Nouvelle partie"));
        listesBoutons.add(nouvellePartieIA = new Button("Nouvelle partie avec l'IA"));
        listesBoutons.add(quitter = new Button("Quitter"));
        listesBoutons.forEach(button -> button.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 2, 0.7, 0, 0);"));

        Font policeBoutons = Font.loadFont(getClass().getResourceAsStream(Path.fontRemachineScript), 60);
        listesBoutons.forEach(button -> button.setFont(policeBoutons));
        listesBoutons.forEach(button -> button.setTextFill(Color.DARKRED));
        listesBoutons.forEach(button -> button.setPickOnBounds(false));
        listesBoutons.forEach(button -> button.setBackground(null));

        VBox.setMargin(nouvellePartieJoueur, new Insets((imgBg.getFitHeight() / 15), 0, 0,0));
    }

    /**
     * Permet d'initialiser autant de boutons radios que de niveaux disponibles
     */
    private void initRadioButton()
    {
        titreNiveau = new Text("Niveau :");
        titreNiveau.setFill(Color.WHITE);
        choixNiveau = new ToggleGroup();
        int nbNiveaux = model.getNbNiveaux();
        listeBoutonsRadio = new ArrayList<>();
        RadioButton boutonRadio;
        for (int i = 0; i < nbNiveaux; i++) {
            boutonRadio = new RadioButton("N. " + (i+1));
            boutonRadio.setTextFill(Color.WHITE);
            boutonRadio.setUserData(i);
            boutonRadio.setToggleGroup(choixNiveau);
            listeBoutonsRadio.add(boutonRadio);
        }
        listeBoutonsRadio.get(0).setSelected(true);
    }

    /**
     * gère l'affichage du menu
     */
    private void initMenu()
    {
        menu = new VBox(8);
        menu.setAlignment(Pos.CENTER); // Les boutons sont centrés les uns par rapport aux autres

        niveauxMenu = new VBox(40);
        niveauxMenu.setAlignment(Pos.CENTER);

        boutonsEtNiveaux = new HBox(25);
        boutonsEtNiveaux.setAlignment(Pos.CENTER);

        menuComplet = new VBox(12);
        menuComplet.setAlignment(Pos.CENTER);

        initTitle();
        initBoutons();
        initRadioButton();
        menu.getChildren().addAll(listesBoutons);
        niveauxMenu.getChildren().addAll(titreNiveau);
        niveauxMenu.getChildren().addAll(listeBoutonsRadio);
        boutonsEtNiveaux.getChildren().addAll(menu);
        boutonsEtNiveaux.getChildren().addAll(niveauxMenu);

        menuComplet.getChildren().addAll(titre);
        menuComplet.getChildren().addAll(boutonsEtNiveaux);

        menuComplet.setPadding(new Insets(0,0,0,imgBg.getFitWidth()/2.-imgBg.getFitWidth()/5.));
        menuComplet.setLayoutX(50);
        menuComplet.setLayoutY(50);
    }

    /**
     * Rattachement d'un controleur aux boutons
     *
     * @param mc (Controleur correspondant au menu)
     */
    void setEvents(ControllerMenu mc) {
        listesBoutons.forEach(button -> button.setOnMouseClicked(mc));
    }

    public void vueMenuComplete()
    {
        root.getChildren().clear();
        root.getChildren().addAll(imgBg);
        root.getChildren().addAll(menuComplet);
    }

    // GETTERS
    public Button getNouvellePartieJoueur() { return nouvellePartieJoueur; }
    public Button getNouvellePartieIA() { return nouvellePartieIA; }
    public Button getQuitter() { return quitter; }
    public ToggleGroup getChoixNiveau() { return choixNiveau; }
}
