package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tools.Path;

public class PorteView extends ImageView {
    private Image[] etats;

    public PorteView() {
        super(new Image(Path.lockedDoor));
        etats = new Image[2];
        etats[0] = getImage();
        etats[1] = new Image(Path.unlockedDoor);
    }

    public void ouvrirPorte() {
        setImage(etats[1]);
    }
}
