package ia;

import model.Niveau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class IAComputeAction {

    //Ne pas appeler cette fonction si tous les diamants ont été trouvés
    List<Etat> defineActionMineur(Etat etatActuel) {
        int[] coordonneesObjectif;
        List<Etat> closedList=null;

        if(!etatActuel.finCollecteDiamants()) {
            List<int[]> coordonneesInterdites = new ArrayList<>();
            boolean rechercheDeDiamantTerminee = false;
            while(!rechercheDeDiamantTerminee){   //On cherche le diamant accessible le plus proche
                coordonneesObjectif = getCoordonnesDuDiamantLePlusProche(etatActuel, coordonneesInterdites);
                if(Arrays.equals(coordonneesObjectif, new int[]{-1,-1})){
                    return null;  //L'IA n'a aucun objectif qu'elle sait atteindre
                }

                etatActuel.defineNewObjectif(coordonneesObjectif);
                closedList = AEtoile(etatActuel);

                if(closedList != null) rechercheDeDiamantTerminee = true;    //Si l'IA a su atteindre le diamant le plus proche
                else coordonneesInterdites.add(coordonneesObjectif);         //Sinon
            }
        }
        else{
            coordonneesObjectif = getCoordonnesPorte(etatActuel);
            etatActuel.defineNewObjectif(coordonneesObjectif);
            closedList = AEtoile(etatActuel);
        }

        List<Etat> pathList = new ArrayList<>();
        pathList.add(closedList.get(closedList.size()-1));
        Etat parent;

        while((parent = pathList.get(0).getEtatParent()) != null){
            pathList.add(0, parent);
        }

        return pathList;
    }

    private List<Etat> AEtoile(Etat etatActuel){
        List<Etat> openList = new ArrayList<>();
        List<Etat> closedList = new ArrayList<>();
        openList.add(etatActuel);
        boolean objectifTrouve = false;

        while(!openList.isEmpty() && !objectifTrouve){
            Etat e = openList.get(0);
            openList.remove(0);
            closedList.add(e);

            if(e.objectifEstAtteint()){
                objectifTrouve = true;
            }
            else{
                for(Etat etat : e.getSuivants()) {
                    if(etatPeutAllerDansListeOpen(etat, openList, closedList)) openList.add(etat);
                }

                Collections.sort(openList);         //Tri selon la valeur f(etat)
            }
        }

        if(!objectifTrouve) return null;
        return closedList;
    }

    private boolean etatPeutAllerDansListeOpen(Etat etat, List<Etat> openList, List<Etat> closedList){
        for(Etat etat2 : openList){
            if(etat.equals(etat2) && etat.getGValue() >= etat2.getGValue()) return false;
        }

        for(Etat etat2 : closedList){
            if(etat.equals(etat2) && etat.getGValue() >= etat2.getGValue() ){
               return false;
            }
        }

        return true;
    }

    private int[] getCoordonnesDuDiamantLePlusProche(Etat etatActuel, List<int[]> coordonneesInterdites){
        byte[][] currentState = etatActuel.getCurrentState();
        int[] coordonneesDuDiamantLePlusProche = new int[]{-1, -1};
        double meilleureDistance = -1;

        for (int ligne = 0; ligne < currentState.length; ligne++) {
            for (int colonne = 0; colonne < currentState[0].length; colonne++) {
                double distance = Math.sqrt(Math.pow(ligne - etatActuel.getLigneMineur(),2) + Math.pow(colonne-etatActuel.getColonneMineur(), 2));
                if(currentState[ligne][colonne] == Niveau.DIAMAND && !arrayIsInList(new int[]{ligne, colonne}, coordonneesInterdites) && (meilleureDistance == -1 || distance<meilleureDistance)){
                    meilleureDistance = distance;
                    coordonneesDuDiamantLePlusProche[0]= ligne;
                    coordonneesDuDiamantLePlusProche[1] = colonne;
                }
            }
        }
        return coordonneesDuDiamantLePlusProche;
    }

    private int[] getCoordonnesPorte(Etat etatActuel){
        byte[][] currentState = etatActuel.getCurrentState();

        for (int ligne = 0; ligne < currentState.length; ligne++) {
            for (int colonne = 0; colonne < currentState[0].length; colonne++) {
                if (currentState[ligne][colonne] == Niveau.PORTE) {
                    return new int[]{ligne, colonne};
                }
            }
        }
        return null;  //jamais censé arriver
    }

    private boolean arrayIsInList(int[] array, List<int[]> list){
        for(int[] array2 : list){
            if(Arrays.equals(array, array2)) return true;
        }
        return false;
    }
}
