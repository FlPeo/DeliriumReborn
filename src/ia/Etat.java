package ia;

import model.Niveau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Etat implements Comparable<Etat>{
    public static final byte OBJET_TOMBE = 0;
    public static final byte OBJET_TOMBE_PAS = 1;

    public static final byte ENNEMI_VA_EN_HAUT = 0;
    public static final byte ENNEMI_VA_A_GAUCHE = 1;
    public static final byte ENNEMI_VA_A_DROITE = 2;
    public static final byte ENNEMI_VA_EN_BAS = 3;


    private byte[][] currentState;
    private byte[][] currentInfos;
    private int ligneMineur;
    private int colonneMineur;
    private double g_value;
    private double f_value;
    private int[] coordonneesObjectif;
    private Etat etatParent;

    public Etat(int ligneMineur, int colonneMineur, byte[][] currentState, byte[][] currentInfos, int[] coordonneesObjectif){
        this.currentState = currentState;
        this.currentState = currentInfos;

        this.ligneMineur = ligneMineur;
        this.colonneMineur = colonneMineur;

        this.etatParent = null;
        this.coordonneesObjectif = coordonneesObjectif;

        this.g_value = 0;
        this.f_value = Math.sqrt(Math.pow(ligneMineur - coordonneesObjectif[0], 2) + Math.pow(colonneMineur - coordonneesObjectif[1], 2));
    }

    private Etat(int ligneMineur, int colonneMineur, byte[][] currentState, byte[][] currentInfos, int[] coordonneesObjectif, Etat parent){
        this.currentState = currentState;
        this.currentState = currentInfos;

        this.ligneMineur = ligneMineur;
        this.colonneMineur = colonneMineur;

        this.etatParent = parent;
        this.coordonneesObjectif = coordonneesObjectif;

        this.g_value = parent.g_value + 1;
        this.f_value = this.g_value + Math.sqrt(Math.pow(ligneMineur - coordonneesObjectif[0], 2) + Math.pow(colonneMineur - coordonneesObjectif[1], 2));
    }

    public List<Etat> getSuivants(){
        List<Etat> etats = new ArrayList<>();
        Etat etat;

        if((etat=getSuivantHaut()) != null) etats.add(etat);
        if((etat=getSuivantGauche()) != null) etats.add(etat);
        if((etat=getSuivantDroite()) != null) etats.add(etat);
        if((etat=getSuivantBas()) != null) etats.add(etat);

        return etats;
    }

    private Etat getSuivantHaut(){
        //Si on ne peut pas se déplacer en haut
        if(ligneMineur == 0 || currentState[ligneMineur -1][colonneMineur] == Niveau.MUR || currentState[ligneMineur-1][colonneMineur] == Niveau.PIERRE ||
                (currentState[ligneMineur-1][colonneMineur] == Niveau.DIAMAND && currentInfos[ligneMineur-1][colonneMineur] == OBJET_TOMBE) ||
                currentState[ligneMineur-1][colonneMineur] == Niveau.MONSTRE_BLEU || currentState[ligneMineur-1][colonneMineur] == Niveau.MONSTRE_ROUGE) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        newState[ligneMineur-1][colonneMineur] = Niveau.MINEUR;
        newState[ligneMineur][colonneMineur] = Niveau.VIDE;
        int newLigneMineur = ligneMineur-1;

        gestionDeplacementsNonIA(newState, newInfos, newLigneMineur, colonneMineur); //Changements effectués dans newState et newInfos

        if((newLigneMineur != 0 && (newState[newLigneMineur-1][colonneMineur] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur-1][colonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (newLigneMineur != newState.length-1 && (newState[newLigneMineur+1][colonneMineur] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur+1][colonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (colonneMineur != 0 && (newState[newLigneMineur][colonneMineur-1] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur-1] == Niveau.MONSTRE_BLEU)) ||
                (colonneMineur != newState[0].length-1 && (newState[newLigneMineur][colonneMineur+1] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur+1] == Niveau.MONSTRE_BLEU)) ||
                (newLigneMineur !=0 && (newState[newLigneMineur-1][colonneMineur] == Niveau.PIERRE || newState[newLigneMineur-1][colonneMineur] == Niveau.DIAMAND) && newInfos[newLigneMineur-1][colonneMineur] == OBJET_TOMBE)){
            return null;    //Si un monstre finit dans une case à coté du mineur, cet état n'est pas bon
        }

        return new Etat(newLigneMineur, colonneMineur, newState, newInfos, coordonneesObjectif, this);
    }

    private Etat getSuivantGauche(){
        //Si on ne peut pas se déplacer à droite
        if(colonneMineur == 0 || currentState[ligneMineur][colonneMineur-1] == Niveau.MUR ||
                (currentState[ligneMineur][colonneMineur-1] == Niveau.PIERRE && currentInfos[ligneMineur][colonneMineur-1] == OBJET_TOMBE) ||
                (currentState[ligneMineur][colonneMineur-1] == Niveau.PIERRE && colonneMineur-1 == 0) ||
                (currentState[ligneMineur][colonneMineur-1] == Niveau.PIERRE && currentState[ligneMineur][colonneMineur-2] != Niveau.VIDE) ||
                currentState[ligneMineur][colonneMineur-1] == Niveau.MONSTRE_BLEU || currentState[ligneMineur][colonneMineur-1] == Niveau.MONSTRE_ROUGE) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        if(newState[ligneMineur][colonneMineur-1] == Niveau.PIERRE){
            newState[ligneMineur][colonneMineur-2] = Niveau.PIERRE;
            newInfos[ligneMineur][colonneMineur-2] = OBJET_TOMBE_PAS;
        }

        newState[ligneMineur][colonneMineur-1] = Niveau.MINEUR;
        newState[ligneMineur][colonneMineur] = Niveau.VIDE;
        int newColonneMineur = colonneMineur-1;

        gestionDeplacementsNonIA(newState, newInfos, ligneMineur, newColonneMineur); //Changements effectués dans newState et newInfos

        if((ligneMineur != 0 && (newState[ligneMineur-1][newColonneMineur] == Niveau.MONSTRE_ROUGE || newState[ligneMineur-1][newColonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (ligneMineur != newState.length-1 && (newState[ligneMineur+1][newColonneMineur] == Niveau.MONSTRE_ROUGE || newState[ligneMineur+1][newColonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (newColonneMineur != 0 && (newState[ligneMineur][newColonneMineur-1] == Niveau.MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur-1] == Niveau.MONSTRE_BLEU)) ||
                (newColonneMineur != newState[0].length-1 && (newState[ligneMineur][newColonneMineur+1] == Niveau.MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur+1] == Niveau.MONSTRE_BLEU)) ||
                (ligneMineur !=0 && (newState[ligneMineur-1][newColonneMineur] == Niveau.PIERRE || newState[ligneMineur-1][newColonneMineur] == Niveau.DIAMAND) && newInfos[ligneMineur-1][newColonneMineur] == OBJET_TOMBE)){
            return null;    //Si un monstre finit dans une case à coté du mineur, ou qu'on ce prend quelque chose sur la tête, cet état n'est pas bon
        }

        return new Etat(ligneMineur, newColonneMineur, newState, newInfos, coordonneesObjectif, this);
    }

    private Etat getSuivantDroite(){
        //Si on ne peut pas se déplacer à droite
        if(colonneMineur == currentState[0].length-1 || currentState[ligneMineur][colonneMineur+1] == Niveau.MUR ||
                (currentState[ligneMineur][colonneMineur+1] == Niveau.PIERRE && currentInfos[ligneMineur][colonneMineur+1] == OBJET_TOMBE) ||
                (currentState[ligneMineur][colonneMineur+1] == Niveau.PIERRE && colonneMineur+1 == currentState[0].length-1) ||
                (currentState[ligneMineur][colonneMineur+1] == Niveau.PIERRE && currentState[ligneMineur][colonneMineur+2] != Niveau.VIDE) ||
                currentState[ligneMineur][colonneMineur+1] == Niveau.MONSTRE_BLEU || currentState[ligneMineur][colonneMineur+1] == Niveau.MONSTRE_ROUGE) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        if(newState[ligneMineur][colonneMineur+1] == Niveau.PIERRE){
            newState[ligneMineur][colonneMineur+2] = Niveau.PIERRE;
            newInfos[ligneMineur][colonneMineur+2] = OBJET_TOMBE_PAS;
        }

        newState[ligneMineur][colonneMineur+1] = Niveau.MINEUR;
        newState[ligneMineur][colonneMineur] = Niveau.VIDE;
        int newColonneMineur = colonneMineur+1;

        gestionDeplacementsNonIA(newState, newInfos, ligneMineur, newColonneMineur); //Changements effectués dans newState et newInfos

        if((ligneMineur != 0 && (newState[ligneMineur-1][newColonneMineur] == Niveau.MONSTRE_ROUGE || newState[ligneMineur-1][newColonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (ligneMineur != newState.length-1 && (newState[ligneMineur+1][newColonneMineur] == Niveau.MONSTRE_ROUGE || newState[ligneMineur+1][newColonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (newColonneMineur != 0 && (newState[ligneMineur][newColonneMineur-1] == Niveau.MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur-1] == Niveau.MONSTRE_BLEU)) ||
                (newColonneMineur != newState[0].length-1 && (newState[ligneMineur][newColonneMineur+1] == Niveau.MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur+1] == Niveau.MONSTRE_BLEU)) ||
                (ligneMineur !=0 && (newState[ligneMineur-1][newColonneMineur] == Niveau.PIERRE || newState[ligneMineur-1][newColonneMineur] == Niveau.DIAMAND) && newInfos[ligneMineur-1][newColonneMineur] == OBJET_TOMBE)){
            return null;    //Si un monstre finit dans une case à coté du mineur, ou qu'on ce prend quelque chose sur la tête, cet état n'est pas bon
        }

        return new Etat(ligneMineur, newColonneMineur, newState, newInfos, coordonneesObjectif, this);
    }

    private Etat getSuivantBas(){
        //Si on ne peut pas se déplacer en bas
        if(ligneMineur == currentState.length-1 || currentState[ligneMineur +1][colonneMineur] == Niveau.MUR || currentState[ligneMineur+1][colonneMineur] == Niveau.PIERRE ||
                currentState[ligneMineur+1][colonneMineur] == Niveau.MONSTRE_BLEU || currentState[ligneMineur+1][colonneMineur] == Niveau.MONSTRE_ROUGE) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        newState[ligneMineur+1][colonneMineur] = Niveau.MINEUR;
        newState[ligneMineur][colonneMineur] = Niveau.VIDE;
        int newLigneMineur = ligneMineur+1;

        gestionDeplacementsNonIA(newState, newInfos, newLigneMineur, colonneMineur); //Changements effectués dans newState et newInfos

        if((newLigneMineur != 0 && (newState[newLigneMineur-1][colonneMineur] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur-1][colonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (newLigneMineur != newState.length-1 && (newState[newLigneMineur+1][colonneMineur] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur+1][colonneMineur] == Niveau.MONSTRE_BLEU)) ||
                (colonneMineur != 0 && (newState[newLigneMineur][colonneMineur-1] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur-1] == Niveau.MONSTRE_BLEU)) ||
                (colonneMineur != newState[0].length-1 && (newState[newLigneMineur][colonneMineur+1] == Niveau.MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur+1] == Niveau.MONSTRE_BLEU)) ||
                (newLigneMineur !=0 && (newState[newLigneMineur-1][colonneMineur] == Niveau.PIERRE || newState[newLigneMineur-1][colonneMineur] == Niveau.DIAMAND) && newInfos[newLigneMineur-1][colonneMineur] == OBJET_TOMBE)){
            return null;    //Si un monstre finit dans une case à coté du mineur, ou qu'on ce prend quelque chose sur la tête, cet état n'est pas bon
        }

        return new Etat(newLigneMineur, colonneMineur, newState, newInfos, coordonneesObjectif, this);
    }

    private void gestionDeplacementsNonIA(byte[][] state, byte[][] infos, int ligneMineur, int colonneMineur){
        for(int ligne=0 ; ligne<state.length ; ligne++){
            for(int colonne = 0 ; colonne<state[0].length ; colonne++){
                if(state[ligne][colonne] == Niveau.DIAMAND || state[ligne][colonne] == Niveau.PIERRE) {

                    if(state[ligne+1][colonne] == Niveau.VIDE){
                        state[ligne+1][colonne] = state[ligne][colonne];
                        infos[ligne+1][colonne] = OBJET_TOMBE;
                        state[ligne][colonne] = Niveau.VIDE;
                    }
                    else if(state[ligne+1][colonne] == Niveau.DIAMAND || state[ligne+1][colonne] == Niveau.PIERRE) {
                        if(colonne>0 && state[ligne+1][colonne-1] == Niveau.VIDE && state[ligne][colonne-1] == Niveau.VIDE &&
                                (ligneMineur!=ligne+1 || colonneMineur!=colonne-1)) {

                            state[ligne+1][colonne-1] = state[ligne][colonne];
                            state[ligne][colonne] = Niveau.VIDE;
                            infos[ligne+1][colonne-1] = OBJET_TOMBE;
                        } else if(colonne<state[0].length-1 && state[ligne+1][colonne+1] == Niveau.VIDE && state[ligne][colonne+1] == Niveau.VIDE &&
                                (ligneMineur!=ligne+1 || colonneMineur!=colonne+1)) {
                            state[ligne+1][colonne+1] = state[ligne][colonne];
                            state[ligne][colonne] = Niveau.VIDE;
                            infos[ligne+1][colonne+1] = OBJET_TOMBE;
                        }
                    }
                    else infos[ligne][colonne] = OBJET_TOMBE_PAS;
                }

                else if(state[ligne][colonne] == Niveau.MONSTRE_BLEU || state[ligne][colonne] == Niveau.MONSTRE_ROUGE) {
                    byte directionAvant = infos[ligne][colonne];
                    byte directionGauche = getGauche(directionAvant);
                    byte directionArriere = getGauche(directionGauche);
                    byte directionDroite = getGauche(directionArriere);

                    int[] deplacementAvant = getDeplacementFromDirection(directionAvant);
                    int[] deplacementGauche = getDeplacementFromDirection(directionGauche);
                    int[] deplacementArriere = getDeplacementFromDirection(directionArriere);
                    int[] deplacementDroite = getDeplacementFromDirection(directionDroite);

                    if(state[ligne+deplacementGauche[0]][colonne+deplacementGauche[1]] == Niveau.VIDE){
                        state[ligne+deplacementGauche[0]][colonne+deplacementGauche[1]] = state[ligne][colonne];
                        state[ligne][colonne] = Niveau.VIDE;
                        infos[ligne+deplacementGauche[0]][colonne+deplacementGauche[1]] = directionGauche;
                    }
                    else if(state[ligne+deplacementAvant[0]][colonne+deplacementAvant[1]] == Niveau.VIDE){
                        state[ligne+deplacementAvant[0]][colonne+deplacementAvant[1]] = state[ligne][colonne];
                        state[ligne][colonne] = Niveau.VIDE;
                        infos[ligne+deplacementAvant[0]][colonne+deplacementAvant[1]] = directionAvant;
                    }
                    else if(state[ligne+deplacementDroite[0]][colonne+deplacementDroite[1]] == Niveau.VIDE){
                        state[ligne+deplacementDroite[0]][colonne+deplacementDroite[1]] = state[ligne][colonne];
                        state[ligne][colonne] = Niveau.VIDE;
                        infos[ligne+deplacementDroite[0]][colonne+deplacementDroite[1]] = directionDroite;
                    }
                    else if(state[ligne+deplacementArriere[0]][colonne+deplacementArriere[1]] == Niveau.VIDE){
                        state[ligne+deplacementArriere[0]][colonne+deplacementArriere[1]] = state[ligne][colonne];
                        state[ligne][colonne] = Niveau.VIDE;
                        infos[ligne+deplacementArriere[0]][colonne+deplacementArriere[1]] = directionArriere;
                    }
                    //sinon il reste immobile
                }
            }

        }
    }

    private byte getGauche(byte direction){    //direction est l'une des quatre constantes de direction
        if(direction<3) return (byte)(direction+1);
        else return ENNEMI_VA_EN_HAUT;
    }
    private int[] getDeplacementFromDirection(byte direction){    //direction est l'une des quatre constantes de direction
        int ligne;
        int colonne;

        if(direction == ENNEMI_VA_A_GAUCHE){
            ligne = 0;
            colonne = -1;
        }
        else if(direction == ENNEMI_VA_A_DROITE){
            ligne = 0;
            colonne = 1;
        }
        else if(direction == ENNEMI_VA_EN_HAUT){
            ligne = -1;
            colonne = 0;
        }
        else{
            ligne = 1;
            colonne = 0;
        }

        return new int[]{ligne, colonne};
    }

    public boolean objectifEstAtteint(int[] objectif){
        return ligneMineur == objectif[0] && colonneMineur == objectif[1];
    }

    @Override
    public int compareTo(Etat o) {
        if(f_value > o.f_value) return 1;
        else if(f_value == o.f_value) return 0;
        else return -1;
    }

    public Etat getEtatParent() {
        return etatParent;
    }
}
