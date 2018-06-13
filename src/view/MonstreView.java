package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tools.Path;

public class MonstreView extends ImageView {
    public MonstreView(char color) {
        super(new Image(color=='r'?Path.redMonster:Path.blueMonster));
    }
}
