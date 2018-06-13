package view;

import controller.ControllerGame;
import controller.ControllerMenu;
import ia.NiveauIA;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.stage.StageStyle;
import model.MenuGame;
import model.Partie;
import timeline.IATimeline;

public class ViewHandler extends Application {
    private Stage primaryStage;
    private ViewMenu menu;
    private ViewGame game;
    private Group root;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);

        MenuGame menuGame = new MenuGame();

        menu = new ViewMenu(root, menuGame);
        new ControllerMenu(this);

        primaryStage.setTitle("Délirium");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
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

    public void demarrerPartie(int niveau)
    {
        Partie partie = new Partie(niveau);
        ControllerGame controllerGame = new ControllerGame(this, partie);
        game = new ViewGame(root, partie);
        game.setEvents(controllerGame);
    }
    public void demarrerPartieIA(int niveau)
    {
        NiveauIA niveauIA = new NiveauIA(niveau);
        ViewGameIA viewGameIA = new ViewGameIA(root);
        viewGameIA.updateView(niveauIA.getCurrentState(), niveauIA.finCollecteDiamants());
        IATimeline timeline = new IATimeline(this, niveauIA, viewGameIA);
        timeline.start();
    }

    public ViewMenu getMenu() { return menu; }
    public Stage getPrimaryStage() { return primaryStage; }
    public ViewGame getGame() { return game; }
}