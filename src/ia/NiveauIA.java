package ia;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class NiveauIA {

    private static final byte[][][] niveaux = {
            LevelBuilder.niveau1,
            LevelBuilder.niveau2
    };


    private List<Etat> etatsCalcules;
    private Etat etatActuel;
    private IAComputeAction iaAction;


    public int numLevel;
    private static final byte NB_DIAMAND_TO_WIN = 10;

    /**
     * Initialise un niveau
     *
     * @param niveau (niveau à initialiser (/!\ on commence à 0)
     */
    public NiveauIA(int niveau) {
        numLevel = niveau;

        byte[][] currentLvl = new byte[niveaux[niveau].length][niveaux[niveau][0].length];
        byte[][] currentInfos = new byte[niveaux[niveau].length][niveaux[niveau][0].length];
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
        etatActuel = new Etat(iMineur, jMineur, currentLvl, currentInfos, NB_DIAMAND_TO_WIN);
        etatsCalcules = new ArrayList<>();
    }

    public boolean updateEtat(){
        if(etatsCalcules.isEmpty()){
            etatsCalcules = iaAction.defineActionMineur(etatActuel);
            if(etatsCalcules == null) return false;
        }

        etatActuel = etatsCalcules.get(0);
        etatsCalcules.remove(0);
        return true;
    }


    public boolean finCollecteDiamants() {
        return etatActuel.finCollecteDiamants();
    }
    public boolean victory(){ return etatActuel.victory();}
    public byte[][] getCurrentState(){return etatActuel.getCurrentState();}
}