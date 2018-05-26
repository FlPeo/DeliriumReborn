package view;

import controller.ControllerMenu;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.stage.StageStyle;

public class ViewHandler extends Application {
    private Stage primaryStage;
    private ViewMenu menu;
    private ViewGame game;
    private ViewLoadGame loadGame;
    private ControllerMenu controller;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Group root = new Group();
        Scene scene = new Scene(root);

        menu = new ViewMenu(root);
        game = new ViewGame();
        loadGame = new ViewLoadGame();
        controller = new ControllerMenu(this);

        primaryStage.setTitle("Délirium");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    /**
     * On attribut un détecteur d'événement pour chaque élément cliquable
     *
     * @param cm ()
     */
    public void setEventHandlerMenu(ControllerMenu cm) {
        menu.setEvents(cm);
    }

    public ViewMenu getMenu() { return menu; }
    public Stage getPrimaryStage() { return primaryStage; }
}
