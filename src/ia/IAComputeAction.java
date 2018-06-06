package ia;

import model.Mineur;
import model.Niveau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IAComputeAction {

    //TODO : améliorer (prendre en compte le fait que le diamant objectif peut tomber)
    //Ne pas appeler cette fonction si tous les diamants ont été trouvés
    public List<Etat> defineActionMineur(Mineur mineur, byte[][] currentState, byte[][] currentInfos) {
        int[] coordonneesObjectif = getCoordonnesObjectif(currentState);

        List<Etat> openList = new ArrayList<>();
        List<Etat> closedList = new ArrayList<>();
        openList.add(new Etat((int)mineur.getPosition().x, (int)mineur.getPosition().y, currentState, currentInfos, coordonneesObjectif));
        boolean objectifTrouve = false;

        while(!openList.isEmpty() && !objectifTrouve){
            Etat e = openList.get(0);
            openList.remove(0);
            closedList.add(e);

            if(e.objectifEstAtteint(coordonneesObjectif)){
                objectifTrouve = true;
            }
            else{
                openList.addAll(e.getSuivants());   //TODO : vérifier si besoin ouu non du if
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

    //TODO : améliorer (ne pas viser le premier diamant donné)
    private int[] getCoordonnesObjectif(byte[][] currentState){
        int[] coordonnees = new int[]{-1, -1};

        for(int ligne = 0 ; ligne < currentState.length; ligne++){
            for(int colonne = 0 ; colonne < currentState[0].length ; colonne++){
                if(currentState[ligne][colonne] == Niveau.DIAMAND){
                    coordonnees[0] = ligne;
                    coordonnees[1] = colonne;
                    return coordonnees;
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
