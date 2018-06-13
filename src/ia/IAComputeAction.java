package ia;

import model.Niveau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IAComputeAction {

    //TODO : améliorer (prendre en compte le fait que le diamant objectif peut tomber)
    //Ne pas appeler cette fonction si tous les diamants ont été trouvés
    public List<Etat> defineActionMineur(Etat etatActuel) {
        int[] coordonneesObjectif = getCoordonnesObjectif(etatActuel);
        etatActuel.defineNewObjectif(coordonneesObjectif);

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

        List<Etat> pathList = new ArrayList<>();
        pathList.add(closedList.get(closedList.size()-1));
        Etat parent;

        while((parent = pathList.get(0).getEtatParent()) != null){
            pathList.add(0, parent);
        }

        return pathList;  //le dernier état de la liste contient le currentInfos qui sera utile au prochain appel de defineActionMineur
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

    //TODO : améliorer (ne pas viser le premier diamant donné)
    private int[] getCoordonnesObjectif(Etat etatActuel){
        int[] coordonnees = new int[]{-1, -1};
        byte[][] currentState = etatActuel.getCurrentState();

        if(!etatActuel.finCollecteDiamants()) {
            for (int ligne = 0; ligne < currentState.length; ligne++) {
                for (int colonne = 0; colonne < currentState[0].length; colonne++) {
                    if (currentState[ligne][colonne] == Niveau.DIAMAND) {
                        coordonnees[0] = ligne;
                        coordonnees[1] = colonne;
                        return coordonnees;
                    }
                }
            }
        }
        else{
            for (int ligne = 0; ligne < currentState.length; ligne++) {
                for (int colonne = 0; colonne < currentState[0].length; colonne++) {
                    if (currentState[ligne][colonne] == Niveau.PORTE) {
                        coordonnees[0] = ligne;
                        coordonnees[1] = colonne;
                        return coordonnees;
                    }
                }
            }
        }
        return coordonnees;
    }

    public void defineActionMonstreBleu(){

    }

    public void defineActionMonstreRouge(){

    }
}
