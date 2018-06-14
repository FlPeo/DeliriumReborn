package ia;

import model.Niveau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class IAComputeAction {

    //Cette fonction prend en paramètre l'état actuel du jeu
    //Elle peut retourner :
    //  - La liste des états successifs permettant d'atteindre le diamant accessible le plus proche
    //  - La liste des états successifs permettant d'atteindre la porte (si suffisamment de diamants ont été récoltés)
    //  - "null" si l'IA ne sait atteindre aucun diamant (si qu'il faut encore récolter des diamants)
    List<Etat> defineActionMineur(Etat etatActuel) {
        int[] coordonneesObjectif;
        List<Etat> closedList=null;

        if(!etatActuel.finCollecteDiamants()) {  //Si on doit encore chercher des diamants
            List<int[]> coordonneesInterdites = new ArrayList<>();
            boolean rechercheDeDiamantTerminee = false;
            while(!rechercheDeDiamantTerminee){
                coordonneesObjectif = getCoordonnesDuDiamantLePlusProche(etatActuel, coordonneesInterdites);   //On cherche le diamant le plus proche
                if(Arrays.equals(coordonneesObjectif, new int[]{-1,-1})){    //Si l'IA ne sait pas comment atteindre un diamant
                    return null;
                }

                etatActuel.defineNewObjectif(coordonneesObjectif);
                closedList = AEtoile(etatActuel);     //On récupère la closedList de A*

                if(closedList != null) rechercheDeDiamantTerminee = true;    //Si l'IA a su atteindre le diamant le plus proche
                else coordonneesInterdites.add(coordonneesObjectif);         //Sinon on oublie le diamant le plus proche, et on essaye d'atteindre le second plus proche
            }
        }
        else{    //Si la porte est ouverte (suffisamment de diamants ont été récoltés)
            coordonneesObjectif = getCoordonnesPorte(etatActuel);  //On récupère les coordonnées de la porte
            etatActuel.defineNewObjectif(coordonneesObjectif);
            closedList = AEtoile(etatActuel);            // On récupère la closedList de A*
        }

        List<Etat> pathList = new ArrayList<>();             //Liste des états à parcourir pour atteindre l'objectif
        pathList.add(closedList.get(closedList.size()-1));   //Le dernier état de la closedList est l'état où on a atteint l'objectif
        Etat parent;

        while((parent = pathList.get(0).getEtatParent()) != null){   //On rajoute tous les états parents de l'état "objectif" pour savoir comment y aller
            pathList.add(0, parent);
        }

        return pathList;
    }

    //Algorithme A*
    //Retour : la closedList (ou null si l'IA n'a pas trouvé comment atteindre l'objectif)
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

                Collections.sort(openList);         //Tri selon la valeur f(etat), grâce à la méthode compareTo de Etat
            }
        }

        if(!objectifTrouve) return null;  //Si openList est vide, et donc qu'on a pas trouvé l'objectif
        return closedList;
    }

    //On vérifie s'il faut insérer ou non l'état dans openList
    private boolean etatPeutAllerDansListeOpen(Etat etat, List<Etat> openList, List<Etat> closedList){
        //Utilisation de la méthode equals de Etat pour savoir si l'état correspond à une case déjà à parcourir
        for(Etat etat2 : openList){
            if(etat.equals(etat2) && etat.getGValue() >= etat2.getGValue()) return false;
        }

        //Utilisation de la méthode equals de Etat pour savoir si l'état correspond à une case déjà parcourue
        for(Etat etat2 : closedList){
            if(etat.equals(etat2) && etat.getGValue() >= etat2.getGValue() ){
               return false;
            }
        }

        return true;
    }

    //Récupération des coordonnées du diamant le plus proche à vol d'oiseau (et qui n'a pas déjà été repéré comme étant inaccessible)
    private int[] getCoordonnesDuDiamantLePlusProche(Etat etatActuel, List<int[]> coordonneesInterdites){
        byte[][] currentState = etatActuel.getCurrentState();   //Tableau des cases de la map
        int[] coordonneesDuDiamantLePlusProche = new int[]{-1, -1};   //On retourne {-1, -1} si on ne trouve pas de diamant
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

    //Récupération des coordonnées de la porte
    private int[] getCoordonnesPorte(Etat etatActuel){
        byte[][] currentState = etatActuel.getCurrentState();   //Tableau des cases de la map

        for (int ligne = 0; ligne < currentState.length; ligne++) {
            for (int colonne = 0; colonne < currentState[0].length; colonne++) {
                if (currentState[ligne][colonne] == Niveau.PORTE) {
                    return new int[]{ligne, colonne};
                }
            }
        }
        return null;  //Jamais censé arriver
    }

    //Méthode permettant de vérifier si un tableau est présent ou non dans une liste de tableaux
    private boolean arrayIsInList(int[] array, List<int[]> list){
        for(int[] array2 : list){
            if(Arrays.equals(array, array2)) return true;
        }
        return false;
    }
}
