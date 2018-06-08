package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tools.Path;

public class MineurView extends ImageView {
    private Image[] directions;

    public MineurView() {
        super(new Image(Path.minerDown));
        directions = new Image[4];
        directions[0] = new Image(Path.minerUp);
        directions[1] = new Image(Path.minerRight);
        directions[2] = getImage();
        directions[3] = new Image(Path.minerLeft);
    }

    public void changerDirection(char dir) {
        switch (dir) {
            case 'h':
                setImage(directions[0]);
                break;
            case 'd':
                setImage(directions[1]);
                break;
            case 'b':
                setImage(directions[2]);
                break;
            case 'g':
                setImage(directions[3]);
                break;
        }
    }
}
