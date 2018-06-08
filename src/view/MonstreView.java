package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tools.Path;

public class MonstreView extends ImageView {
    private Image[] etats;

    public MonstreView(char color) {
        super(new Image(color=='r'?Path.redMonster:Path.blueMonster));
        etats = new Image[2];
        etats[0] = getImage();
        etats[1] = new Image(color=='r'?Path.redMonsterAttacking:Path.blueMonsterAttacking);
    }

    public void enTrainDAttaquer(boolean b) {
        setImage(etats[b?1:0]);
    }
}
