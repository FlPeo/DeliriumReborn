package model;

public class Partie {

    private Niveau niveau;

    public Partie(int niveau) {
        this.niveau = new Niveau(niveau);
    }

    public Niveau getNiveau() { return niveau; }
}
