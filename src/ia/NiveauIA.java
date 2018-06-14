package ia;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class NiveauIA {

    private static final byte[][][] niveaux = {
            LevelBuilder.niveau1,
            LevelBuilder.niveau2
    };


    private List<Etat> etatsCalcules;   //Liste des états encore à parcourir pour atteindre l'objectif courant
    private Etat etatActuel;            //L'état actuellement affiché par le jeu
    private IAComputeAction iaAction;   //Objet qui s'occupe de calculer le chemin à parcourir avec A*


    public int numLevel;
    private static final byte NB_DIAMAND_TO_WIN = 10;

    /**
     * Initialise un niveau
     *
     * @param niveau (niveau à initialiser (/!\ on commence à 0))
     */
    public NiveauIA(int niveau) {
        numLevel = niveau;

        byte[][] currentLvl = new byte[niveaux[niveau].length][niveaux[niveau][0].length];   //Répertorie ce qui ce trouve dans les cases
        byte[][] currentInfos = new byte[niveaux[niveau].length][niveaux[niveau][0].length]; //Répertorie des informations pour chaque case
        int iMineur = -1;
        int jMineur = -1;
        for (int i = 0; i < niveaux[niveau].length; i++){
            System.arraycopy(niveaux[niveau][i], 0, currentLvl[i], 0, niveaux[niveau][i].length);
            for(int j=0 ; j<currentLvl[i].length; j++)
                if(currentLvl[i][j] == Etat.MINEUR){
                    iMineur = i;
                    jMineur = j;
                }
        }

        iaAction = new IAComputeAction();
        etatActuel = new Etat(iMineur, jMineur, currentLvl, currentInfos, NB_DIAMAND_TO_WIN);  //Etat initial
        etatsCalcules = new ArrayList<>();
    }

    //Fonction qui permet de passer à l'état suivant (à afficher)
    //Renvoie true il y a un état suivant calculé par l'IA
    public boolean updateEtat(){
        if(etatsCalcules.isEmpty()){   //Si il n'y a plus d'états à parcourir pour atteindre l'objectif courant, on calcule le chemin vers un autre objectif
            etatsCalcules = iaAction.defineActionMineur(etatActuel);
            if(etatsCalcules == null) return false;     //Si aucun des objectifs possibles n'est accessible
        }

        etatActuel = etatsCalcules.get(0);   //On sort l'état actuel de la liste
        etatsCalcules.remove(0);
        return true;
    }


    public boolean finCollecteDiamants() {
        return etatActuel.finCollecteDiamants();
    }
    public boolean victory(){ return etatActuel.victory();}
    public byte[][] getCurrentState(){return etatActuel.getCurrentState();}
}