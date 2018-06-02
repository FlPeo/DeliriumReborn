package model;

public class Partie {

    public final int JOUEUR_IA = 1;
    public final int JOUEUR_HUMAN = 2;
    private Joueur joueur;
    private Niveau niveau;

    public Partie(int niveau, int joueur) {
        this.niveau = new Niveau(niveau);
        if (joueur == JOUEUR_IA) {
            this.joueur = new JoueurIA();
        } else if (joueur == JOUEUR_HUMAN) {
            this.joueur = new JoueurHuman();
        }
    }

    public Niveau getNiveau() { return niveau; }
}
