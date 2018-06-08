package view;

import ia.Etat;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private Image imagePorte;
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
        imagePorte = new Image(Path.lockedDoor);
        canvas = new Canvas();
        root.getChildren().add(canvas);

        canvas.setHeight(root.getScene().getHeight());
        canvas.setWidth(root.getScene().getWidth());
    }

    public void updateView(byte[][] currentState){
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
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        gc.drawImage(imageMineur, j*tailleImages, i*tailleImages);
                        break;
                    case Etat.PORTE:
                        gc.drawImage(imageVide, j*tailleImages, i*tailleImages);
                        gc.drawImage(imagePorte, j*tailleImages, i*tailleImages);
                        break;
                }
            }
        }
    }
}
