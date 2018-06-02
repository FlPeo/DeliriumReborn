package view;

import controller.ControllerGame;
import controller.ControllerMenu;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import model.MenuGame;
import model.Partie;

public class ViewHandler extends Application {
    private Stage primaryStage;
    private ViewMenu menu;
    private ViewGame game;
    private ViewLoadGame loadGame;
    private MenuGame menuGame;
    private Partie partie;
    private ControllerGame controllerGame;
    private ControllerMenu controller;
    private Group root;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        root = new Group();
        Scene scene = new Scene(root);

        menuGame = new MenuGame();

        menu = new ViewMenu(root);
        loadGame = new ViewLoadGame();

        controller = new ControllerMenu(this);

        primaryStage.setTitle("Délirium");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * On attribut un détecteur d'événement pour chaque élément cliquable
     * @param cm ()
     */
    public void setEventHandlerMenu(ControllerMenu cm) {
        menu.setEvents(cm);
    }

    public void demarrerPartie(int niveau, int typeJoueur)
    {
        this.partie = new Partie(niveau, typeJoueur);
        controllerGame = new ControllerGame(this, partie);
        game = new ViewGame(root, partie);
        game.setEvents(controllerGame);
    }

    public ViewMenu getMenu() { return menu; }
    public Stage getPrimaryStage() { return primaryStage; }
    public ViewGame getGame() { return game; }
}