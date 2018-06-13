package view;

import com.sun.javafx.geom.Vec2d;
import ia.Etat;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import model.Niveau;
import tools.Path;

public class ViewGameIA {
    private Canvas canvas;
    private Image imageVide;
    private Image imageMur;
    private Image imageTerre;
    private Image imageDiamant;
    private Image imagePierre;
    private Image imageMonstreBleu;
    private Image imageMonstreRouge;
    private Image imageMineur;
    private Image imagePorteFermee;
    private Image imagePorteOuverte;
    private static final int tailleImages = 64;

    public ViewGameIA(Group root){
        root.getChildren().clear();
        imageVide = new Image(Path.background);
        imageMur = new Image(Path.wall);
        imageTerre = new Image(Path.clay);
        imageDiamant = new Image(Path.diamond);
        imagePierre = new Image(Path.stone);
        imageMonstreBleu = new Image(Path.blueMonster);
        imageMonstreRouge = new Image(Path.redMonster);
        imageMineur = new Image(Path.minerDown);
        imagePorteFermee = new Image(Path.lockedDoor);
        imagePorteOuverte = new Image(Path.unlockedDoor);
        canvas = new Canvas();
        root.getChildren().add(canvas);
    }

    public void updateView(byte[][] currentState, boolean finCollecteDiamants){
        int ligneMineur=0;
        int colonneMineur=0;

        canvas.setHeight(currentState.length*tailleImages);
        canvas.setWidth(currentState[0].length*tailleImages);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(int i = 0 ; i<currentState.length; i++){
            for(int j = 0 ; j < currentState[0].length ; j++){
                switch (currentState[i][j]){
                    case Etat.VIDE:
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.MUR:
                        gc.drawImage(imageMur, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.CLAY:
                        gc.drawImage(imageTerre, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.DIAMAND:
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        gc.drawImage(imageDiamant, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.PIERRE:
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        gc.drawImage(imagePierre, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.MONSTRE_BLEU:
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        gc.drawImage(imageMonstreBleu, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.MONSTRE_ROUGE:
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        gc.drawImage(imageMonstreRouge, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.MINEUR:
                        ligneMineur =i;
                        colonneMineur=j;
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        gc.drawImage(imageMineur, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.PORTE:
                        if(!finCollecteDiamants) {
                            gc.drawImage(imageVide, j * tailleImages, i * tailleImages);
                            gc.drawImage(imagePorteFermee, j * tailleImages, i * tailleImages);
                        }
                        else {
                            gc.drawImage(imageVide, j * tailleImages, i * tailleImages);
                            gc.drawImage(imagePorteOuverte, j * tailleImages, i * tailleImages);
                        }
                        break;
                }
            }
        }
        placerLaCamera(ligneMineur, colonneMineur, currentState[0].length, currentState.length);
    }
    private void placerLaCamera(int ligneMineur, int colonneMineur, int widthPlateau, int heightPlateau) {
        Vec2d windowSize = new Vec2d(canvas.getScene().getWidth(), canvas.getScene().getHeight()),
                plateauSize = new Vec2d(widthPlateau * tailleImages, heightPlateau * tailleImages);

        double finalTranslateX,
                finalTranslateY;

        if(windowSize.x < plateauSize.x) {
            finalTranslateX =  -colonneMineur * tailleImages + windowSize.x/2;
            if(finalTranslateX>0) finalTranslateX = 0;
            if(finalTranslateX<windowSize.x-plateauSize.x) finalTranslateX = windowSize.x-plateauSize.x;
        } else {
            finalTranslateX = (windowSize.x-plateauSize.x)/2;
        }

        if(windowSize.y < plateauSize.y) {
            finalTranslateY = -ligneMineur * tailleImages + windowSize.y/2;
            if(finalTranslateY>0) finalTranslateY = 0;
            if(finalTranslateY<windowSize.y-plateauSize.y) finalTranslateY = windowSize.y-plateauSize.y;
        } else {
            finalTranslateY = (windowSize.y-plateauSize.y)/2;
        }


        canvas.setTranslateX(finalTranslateX);
        canvas.setTranslateY(finalTranslateY);
    }

    public void affichageVictoire()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin");
        alert.setHeaderText("Victoire !");
        alert.setContentText("L'IA a gagné !");
        alert.showAndWait();
    }

    public void affichageBlocageIA() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin");
        alert.setHeaderText("Défaite !");
        alert.setContentText("L'IA n'arrive pas à trouver comment finir le niveau");
        alert.showAndWait();
    }
}
