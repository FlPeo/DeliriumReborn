package model;

public class MenuGame {
    private int nbNiveaux;

    public MenuGame(){
        nbNiveaux = Niveau.niveaux.length;
    }

    // GETTERS
    public int getNbNiveaux() { return nbNiveaux; }
}
