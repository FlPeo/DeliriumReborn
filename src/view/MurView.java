package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tools.Path;

public class MurView extends ImageView {
    public MurView() {
        super(new Image(Path.wall));
    }
}
