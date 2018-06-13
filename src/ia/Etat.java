package ia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Etat implements Comparable<Etat>{
    public static final byte OBJET_TOMBE_PAS = 0;
    public static final byte OBJET_TOMBE = 1;

    public static final byte ENNEMI_VA_EN_HAUT = 0;
    public static final byte ENNEMI_VA_A_GAUCHE = 1;
    public static final byte ENNEMI_VA_EN_BAS = 2;
    public static final byte ENNEMI_VA_A_DROITE = 3;

    public static final byte VIDE = 0;
    public static final byte MUR = 1;
    public static final byte CLAY = 2;
    public static final byte DIAMAND = 3;
    public static final byte PIERRE = 4;
    public static final byte MONSTRE_BLEU = 5;
    public static final byte MONSTRE_ROUGE = 6;
    public static final byte MINEUR = 7;
    public static final byte PORTE = 8;

    public static final byte MASQUE_NE_PAS_PARCOURIR = 16;
    public static final byte MASQUE_PARCOURIR = MASQUE_NE_PAS_PARCOURIR-1;


    private byte[][] currentState;
    private byte[][] currentInfos;
    private int ligneMineur;
    private int colonneMineur;
    private double g_value;
    private double f_value;
    private int[] coordonneesObjectif;
    private byte nbDiamantsEncoreAAttraper;
    private Etat etatParent;

    public Etat(int ligneMineur, int colonneMineur, byte[][] currentState, byte[][] currentInfos, byte nbDiamantsEncoreAAttraper){
        this.currentState = currentState;
        this.currentInfos = currentInfos;
        this.nbDiamantsEncoreAAttraper = nbDiamantsEncoreAAttraper;

        this.ligneMineur = ligneMineur;
        this.colonneMineur = colonneMineur;

        this.etatParent = null;
    }


    private Etat(int ligneMineur, int colonneMineur, byte[][] currentState, byte[][] currentInfos, byte nbDiamantsEncoreAAttraper, int[] coordonneesObjectif, Etat parent){
        this.currentState = currentState;
        this.currentInfos = currentInfos;
        this.nbDiamantsEncoreAAttraper = nbDiamantsEncoreAAttraper;

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
        if(ligneMineur == 0 || currentState[ligneMineur -1][colonneMineur] == MUR || currentState[ligneMineur-1][colonneMineur] == PIERRE ||
                (currentState[ligneMineur-1][colonneMineur] == DIAMAND && currentInfos[ligneMineur-1][colonneMineur] == OBJET_TOMBE) ||
                currentState[ligneMineur-1][colonneMineur] == MONSTRE_BLEU || currentState[ligneMineur-1][colonneMineur] == MONSTRE_ROUGE ||
                (!finCollecteDiamants() && currentState[ligneMineur-1][colonneMineur] == PORTE)) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        byte newNbDiamantsEncoreAAttraper = nbDiamantsEncoreAAttraper;
        if(newState[ligneMineur-1][colonneMineur] == DIAMAND) newNbDiamantsEncoreAAttraper--;
        if(newState[ligneMineur-1][colonneMineur] != PORTE) newState[ligneMineur-1][colonneMineur] = MINEUR;
        newState[ligneMineur][colonneMineur] = VIDE;
        int newLigneMineur = ligneMineur-1;

        int[] newCoordonneesObjectif = gestionDeplacementsNonIA(newState, newInfos, newLigneMineur, colonneMineur, coordonneesObjectif); //Changements effectués dans newState et newInfos

        if((newLigneMineur != 0 && (newState[newLigneMineur-1][colonneMineur] == MONSTRE_ROUGE || newState[newLigneMineur-1][colonneMineur] == MONSTRE_BLEU)) ||
                (newLigneMineur != newState.length-1 && (newState[newLigneMineur+1][colonneMineur] == MONSTRE_ROUGE || newState[newLigneMineur+1][colonneMineur] == MONSTRE_BLEU)) ||
                (colonneMineur != 0 && (newState[newLigneMineur][colonneMineur-1] == MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur-1] == MONSTRE_BLEU)) ||
                (colonneMineur != newState[0].length-1 && (newState[newLigneMineur][colonneMineur+1] == MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur+1] == MONSTRE_BLEU))){
            return null;    //Si un monstre finit dans une case à coté du mineur, cet état n'est pas bon
        }

        return new Etat(newLigneMineur, colonneMineur, newState, newInfos, newNbDiamantsEncoreAAttraper, newCoordonneesObjectif, this);
    }

    private Etat getSuivantGauche(){
        //Si on ne peut pas se déplacer à gauche
        if(colonneMineur == 0 || currentState[ligneMineur][colonneMineur-1] == MUR ||
                (currentState[ligneMineur][colonneMineur-1] == PIERRE && currentInfos[ligneMineur][colonneMineur-1] == OBJET_TOMBE) ||
                (currentState[ligneMineur][colonneMineur-1] == DIAMAND && currentInfos[ligneMineur][colonneMineur-1] == OBJET_TOMBE) ||
                (ligneMineur!=0 && currentState[ligneMineur-1][colonneMineur-1] == PIERRE && currentInfos[ligneMineur-1][colonneMineur-1] == OBJET_TOMBE) ||
                (ligneMineur!=0 && currentState[ligneMineur-1][colonneMineur-1] == DIAMAND && currentInfos[ligneMineur-1][colonneMineur-1] == OBJET_TOMBE) ||
                (currentState[ligneMineur][colonneMineur-1] == PIERRE && colonneMineur-1 == 0) ||
                (currentState[ligneMineur][colonneMineur-1] == PIERRE && currentState[ligneMineur][colonneMineur-2] != VIDE) ||
                currentState[ligneMineur][colonneMineur-1] == MONSTRE_BLEU || currentState[ligneMineur][colonneMineur-1] == MONSTRE_ROUGE ||
                (!finCollecteDiamants() && currentState[ligneMineur][colonneMineur-1] == PORTE)) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        if(newState[ligneMineur][colonneMineur-1] == PIERRE){
            newState[ligneMineur][colonneMineur-2] = PIERRE;
            newInfos[ligneMineur][colonneMineur-2] = OBJET_TOMBE_PAS;
        }

        byte newNbDiamantsEncoreAAttraper = nbDiamantsEncoreAAttraper;
        if(newState[ligneMineur][colonneMineur-1] == DIAMAND) newNbDiamantsEncoreAAttraper--;
        if(newState[ligneMineur][colonneMineur-1] != PORTE) newState[ligneMineur][colonneMineur-1] = MINEUR;
        newState[ligneMineur][colonneMineur] = VIDE;
        int newColonneMineur = colonneMineur-1;

        int[] newCoordonneesObjectif = gestionDeplacementsNonIA(newState, newInfos, ligneMineur, newColonneMineur, coordonneesObjectif); //Changements effectués dans newState et newInfos

        if((ligneMineur != 0 && (newState[ligneMineur-1][newColonneMineur] == MONSTRE_ROUGE || newState[ligneMineur-1][newColonneMineur] == MONSTRE_BLEU)) ||
                (ligneMineur != newState.length-1 && (newState[ligneMineur+1][newColonneMineur] == MONSTRE_ROUGE || newState[ligneMineur+1][newColonneMineur] == MONSTRE_BLEU)) ||
                (newColonneMineur != 0 && (newState[ligneMineur][newColonneMineur-1] == MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur-1] == MONSTRE_BLEU)) ||
                (newColonneMineur != newState[0].length-1 && (newState[ligneMineur][newColonneMineur+1] == MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur+1] == MONSTRE_BLEU))){
            return null;    //Si un monstre finit dans une case à coté du mineur, ou qu'on ce prend quelque chose sur la tête, cet état n'est pas bon
        }

        return new Etat(ligneMineur, newColonneMineur, newState, newInfos, newNbDiamantsEncoreAAttraper, newCoordonneesObjectif, this);
    }

    private Etat getSuivantDroite(){
        //Si on ne peut pas se déplacer à droite
        if(colonneMineur == currentState[0].length-1 || currentState[ligneMineur][colonneMineur+1] == MUR ||
                (currentState[ligneMineur][colonneMineur+1] == PIERRE && currentInfos[ligneMineur][colonneMineur+1] == OBJET_TOMBE) ||
                (currentState[ligneMineur][colonneMineur+1] == DIAMAND && currentInfos[ligneMineur][colonneMineur+1] == OBJET_TOMBE) ||
                (ligneMineur!=0 && currentState[ligneMineur-1][colonneMineur+1] == PIERRE && currentInfos[ligneMineur-1][colonneMineur+1] == OBJET_TOMBE) ||
                (ligneMineur!=0 && currentState[ligneMineur-1][colonneMineur+1] == DIAMAND && currentInfos[ligneMineur-1][colonneMineur+1] == OBJET_TOMBE) ||
                (currentState[ligneMineur][colonneMineur+1] == PIERRE && colonneMineur+1 == currentState[0].length-1) ||
                (currentState[ligneMineur][colonneMineur+1] == PIERRE && currentState[ligneMineur][colonneMineur+2] != VIDE) ||
                currentState[ligneMineur][colonneMineur+1] == MONSTRE_BLEU || currentState[ligneMineur][colonneMineur+1] == MONSTRE_ROUGE ||
                (!finCollecteDiamants() && currentState[ligneMineur][colonneMineur+1] == PORTE)) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        if(newState[ligneMineur][colonneMineur+1] == PIERRE){
            newState[ligneMineur][colonneMineur+2] = PIERRE;
            newInfos[ligneMineur][colonneMineur+2] = OBJET_TOMBE_PAS;
        }

        byte newNbDiamantsEncoreAAttraper = nbDiamantsEncoreAAttraper;
        if(newState[ligneMineur][colonneMineur+1] == DIAMAND) newNbDiamantsEncoreAAttraper--;
        if(newState[ligneMineur][colonneMineur+1] != PORTE) newState[ligneMineur][colonneMineur+1] = MINEUR;
        newState[ligneMineur][colonneMineur] = VIDE;
        int newColonneMineur = colonneMineur+1;

        int[] newCoordonneesObjectif = gestionDeplacementsNonIA(newState, newInfos, ligneMineur, newColonneMineur, coordonneesObjectif); //Changements effectués dans newState et newInfos

        if((ligneMineur != 0 && (newState[ligneMineur-1][newColonneMineur] == MONSTRE_ROUGE || newState[ligneMineur-1][newColonneMineur] == MONSTRE_BLEU)) ||
                (ligneMineur != newState.length-1 && (newState[ligneMineur+1][newColonneMineur] == MONSTRE_ROUGE || newState[ligneMineur+1][newColonneMineur] == MONSTRE_BLEU)) ||
                (newColonneMineur != 0 && (newState[ligneMineur][newColonneMineur-1] == MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur-1] == MONSTRE_BLEU)) ||
                (newColonneMineur != newState[0].length-1 && (newState[ligneMineur][newColonneMineur+1] == MONSTRE_ROUGE || newState[ligneMineur][newColonneMineur+1] == MONSTRE_BLEU))){
            return null;    //Si un monstre finit dans une case à coté du mineur, ou qu'on ce prend quelque chose sur la tête, cet état n'est pas bon
        }

        return new Etat(ligneMineur, newColonneMineur, newState, newInfos, newNbDiamantsEncoreAAttraper, newCoordonneesObjectif, this);
    }

    private Etat getSuivantBas(){
        //Si on ne peut pas se déplacer en bas
        if(ligneMineur == currentState.length-1 || currentState[ligneMineur +1][colonneMineur] == MUR || currentState[ligneMineur+1][colonneMineur] == PIERRE ||
                currentState[ligneMineur+1][colonneMineur] == MONSTRE_BLEU || currentState[ligneMineur+1][colonneMineur] == MONSTRE_ROUGE ||
                (!finCollecteDiamants() && currentState[ligneMineur+1][colonneMineur] == PORTE)) {
            return null;
        }

        byte[][] newState = Arrays.stream(currentState)
                .map(byte[]::clone)
                .toArray(byte[][]::new);
        byte[][] newInfos = Arrays.stream(currentInfos)
                .map(byte[]::clone)
                .toArray(byte[][]::new);

        byte newNbDiamantsEncoreAAttraper = nbDiamantsEncoreAAttraper;
        if(newState[ligneMineur+1][colonneMineur] == DIAMAND) newNbDiamantsEncoreAAttraper--;
        if(newState[ligneMineur+1][colonneMineur] != PORTE) newState[ligneMineur+1][colonneMineur] = MINEUR;
        newState[ligneMineur][colonneMineur] = VIDE;
        int newLigneMineur = ligneMineur+1;

        int[] newCoordonneesObjectif = gestionDeplacementsNonIA(newState, newInfos, newLigneMineur, colonneMineur, coordonneesObjectif); //Changements effectués dans newState et newInfos

        if((newLigneMineur != 0 && (newState[newLigneMineur-1][colonneMineur] == MONSTRE_ROUGE || newState[newLigneMineur-1][colonneMineur] == MONSTRE_BLEU)) ||
                (newLigneMineur != newState.length-1 && (newState[newLigneMineur+1][colonneMineur] == MONSTRE_ROUGE || newState[newLigneMineur+1][colonneMineur] == MONSTRE_BLEU)) ||
                (colonneMineur != 0 && (newState[newLigneMineur][colonneMineur-1] == MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur-1] == MONSTRE_BLEU)) ||
                (colonneMineur != newState[0].length-1 && (newState[newLigneMineur][colonneMineur+1] == MONSTRE_ROUGE || newState[newLigneMineur][colonneMineur+1] == MONSTRE_BLEU))){
            return null;    //Si un monstre finit dans une case à coté du mineur, ou qu'on ce prend quelque chose sur la tête, cet état n'est pas bon
        }

        return new Etat(newLigneMineur, colonneMineur, newState, newInfos, newNbDiamantsEncoreAAttraper, newCoordonneesObjectif, this);
    }

    private int[] gestionDeplacementsNonIA(byte[][] state, byte[][] infos, int ligneMineur, int colonneMineur, int[] coordonneesObjectif){
        int[] newCoordonnesObjectif = Arrays.copyOf(coordonneesObjectif, 2);

        for(int ligne=0 ; ligne<state.length ; ligne++){
            for(int colonne = 0 ; colonne<state[0].length ; colonne++) {
                if ((infos[ligne][colonne]&MASQUE_NE_PAS_PARCOURIR) == MASQUE_NE_PAS_PARCOURIR) {
                    infos[ligne][colonne]&=MASQUE_PARCOURIR;
                }
                else{
                    if (state[ligne][colonne] == DIAMAND || state[ligne][colonne] == PIERRE) {
                        if ((infos[ligne][colonne] == OBJET_TOMBE_PAS) && (state[ligne + 1][colonne] == VIDE)) {
                            infos[ligne][colonne] = OBJET_TOMBE;     //Possibilité que cela écrase un monstre
                        }
                        else if((infos[ligne][colonne] == OBJET_TOMBE_PAS) && (state[ligne + 1][colonne] == DIAMAND || state[ligne + 1][colonne] == PIERRE) &&
                                ((colonne > 0 && state[ligne + 1][colonne - 1] == VIDE && state[ligne][colonne - 1] == VIDE &&
                                        (ligneMineur != ligne + 1 || colonneMineur != colonne - 1)) ||
                                 (colonne < state[0].length - 1 && state[ligne + 1][colonne + 1] == VIDE && state[ligne][colonne + 1] == VIDE &&
                                         (ligneMineur != ligne + 1 || colonneMineur != colonne + 1)))){
                            infos[ligne][colonne] = OBJET_TOMBE;
                        }
                        else if(infos[ligne][colonne] == OBJET_TOMBE && (state[ligne + 1][colonne] == VIDE || state[ligne + 1][colonne] == MONSTRE_BLEU
                                || state[ligne + 1][colonne] == MONSTRE_ROUGE)){
                            state[ligne + 1][colonne] = state[ligne][colonne];
                            infos[ligne + 1][colonne] = OBJET_TOMBE|MASQUE_NE_PAS_PARCOURIR;
                            state[ligne][colonne] = VIDE;

                            if(ligne == newCoordonnesObjectif[0] && colonne == newCoordonnesObjectif[1]){
                                newCoordonnesObjectif[0] = ligne+1;
                            }
                        }
                        else if (infos[ligne][colonne] == OBJET_TOMBE && (state[ligne + 1][colonne] == DIAMAND || state[ligne + 1][colonne] == PIERRE)) {
                            if (colonne > 0 && (state[ligne + 1][colonne - 1] == VIDE || state[ligne + 1][colonne - 1] == MONSTRE_BLEU ||state[ligne + 1][colonne - 1] == MONSTRE_ROUGE ) &&
                                    (state[ligne][colonne - 1] == VIDE) &&
                                    (ligneMineur != ligne + 1 || colonneMineur != colonne - 1)) {

                                state[ligne + 1][colonne - 1] = state[ligne][colonne];   //Possibilité d'écraser un monstre
                                state[ligne][colonne] = VIDE;
                                infos[ligne + 1][colonne - 1] = OBJET_TOMBE|MASQUE_NE_PAS_PARCOURIR;
                                if(ligne == newCoordonnesObjectif[0] && colonne == newCoordonnesObjectif[1]){
                                    newCoordonnesObjectif[0] = ligne+1;
                                    newCoordonnesObjectif[1] = colonne-1;
                                }
                            } else if (colonne < state[0].length - 1 && (state[ligne + 1][colonne + 1] == VIDE || state[ligne + 1][colonne + 1] == MONSTRE_BLEU|| state[ligne + 1][colonne + 1] == MONSTRE_ROUGE)
                                    && state[ligne][colonne + 1] == VIDE &&
                                    (ligneMineur != ligne + 1 || colonneMineur != colonne + 1)) {
                                state[ligne + 1][colonne + 1] = state[ligne][colonne];   //Possibilité d'écraser un monstre
                                state[ligne][colonne] = VIDE;
                                infos[ligne + 1][colonne + 1] = OBJET_TOMBE|MASQUE_NE_PAS_PARCOURIR;
                                if(ligne == newCoordonnesObjectif[0] && colonne == newCoordonnesObjectif[1]){
                                    newCoordonnesObjectif[0] = ligne+1;
                                    newCoordonnesObjectif[1] = colonne+1;
                                }
                            }
                        } else infos[ligne][colonne] = OBJET_TOMBE_PAS;
                    } else if (state[ligne][colonne] == MONSTRE_BLEU || state[ligne][colonne] == MONSTRE_ROUGE) {
                        byte directionAvant = infos[ligne][colonne];
                        byte directionGauche = getGauche(directionAvant);
                        byte directionArriere = getGauche(directionGauche);
                        byte directionDroite = getGauche(directionArriere);

                        int[] deplacementAvant = getDeplacementFromDirection(directionAvant);
                        int[] deplacementGauche = getDeplacementFromDirection(directionGauche);
                        int[] deplacementArriere = getDeplacementFromDirection(directionArriere);
                        int[] deplacementDroite = getDeplacementFromDirection(directionDroite);

                        if (state[ligne + deplacementGauche[0]][colonne + deplacementGauche[1]] == VIDE) {
                            state[ligne + deplacementGauche[0]][colonne + deplacementGauche[1]] = state[ligne][colonne];
                            state[ligne][colonne] = VIDE;
                            infos[ligne + deplacementGauche[0]][colonne + deplacementGauche[1]] = directionGauche;
                            if(deplacementGauche[0]+deplacementGauche[1]==1) infos[ligne + deplacementGauche[0]][colonne + deplacementGauche[1]] |= MASQUE_NE_PAS_PARCOURIR;  //Si en bas ou à droite
                        } else if (state[ligne + deplacementAvant[0]][colonne + deplacementAvant[1]] == VIDE) {
                            state[ligne + deplacementAvant[0]][colonne + deplacementAvant[1]] = state[ligne][colonne];
                            state[ligne][colonne] = VIDE;
                            infos[ligne + deplacementAvant[0]][colonne + deplacementAvant[1]] = directionAvant;
                            if(deplacementAvant[0]+deplacementAvant[1]==1) infos[ligne + deplacementAvant[0]][colonne + deplacementAvant[1]] |= MASQUE_NE_PAS_PARCOURIR;  //Si en bas ou à droite
                        } else if (state[ligne + deplacementDroite[0]][colonne + deplacementDroite[1]] == VIDE) {
                            state[ligne + deplacementDroite[0]][colonne + deplacementDroite[1]] = state[ligne][colonne];
                            state[ligne][colonne] = VIDE;
                            infos[ligne + deplacementDroite[0]][colonne + deplacementDroite[1]] = directionDroite;
                            if(deplacementDroite[0]+deplacementDroite[1]==1) infos[ligne + deplacementDroite[0]][colonne + deplacementDroite[1]] |= MASQUE_NE_PAS_PARCOURIR;  //Si en bas ou à droite
                        } else if (state[ligne + deplacementArriere[0]][colonne + deplacementArriere[1]] == VIDE) {
                            state[ligne + deplacementArriere[0]][colonne + deplacementArriere[1]] = state[ligne][colonne];
                            state[ligne][colonne] = VIDE;
                            infos[ligne + deplacementArriere[0]][colonne + deplacementArriere[1]] = directionArriere;
                            if(deplacementArriere[0]+deplacementArriere[1]==1) infos[ligne + deplacementArriere[0]][colonne + deplacementArriere[1]] |= MASQUE_NE_PAS_PARCOURIR;  //Si en bas ou à droite
                        }
                        //sinon il reste immobile
                    }
                }
            }

        }
        return newCoordonnesObjectif;
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

    public boolean objectifEstAtteint(){
        return ligneMineur == coordonneesObjectif[0] && colonneMineur == coordonneesObjectif[1];
    }

    @Override
    public int compareTo(Etat o) {
        if(f_value > o.f_value) return 1;
        else if(f_value == o.f_value) return 0;
        else return -1;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Etat)) return false;
        Etat e = (Etat)o;
        return ligneMineur == e.ligneMineur && colonneMineur == e.colonneMineur;
    }

    public Etat getEtatParent() {
        return etatParent;
    }
    public byte[][] getCurrentState() {
        return currentState;
    }

    public void defineNewObjectif(int[] coordonneesObjectif){
        this.etatParent = null;
        this.coordonneesObjectif = coordonneesObjectif;
        this.g_value = 0;
        this.f_value = Math.sqrt(Math.pow(ligneMineur - coordonneesObjectif[0], 2) + Math.pow(colonneMineur - coordonneesObjectif[1], 2));
    }

    double getGValue(){
        return g_value;
    }

    public boolean finCollecteDiamants() {
        return nbDiamantsEncoreAAttraper <= 0;
    }
    public boolean victory(){ return finCollecteDiamants() && currentState[ligneMineur][colonneMineur] == PORTE;}
}
