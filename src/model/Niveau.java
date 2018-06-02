package model;

import com.sun.javafx.geom.Vec2d;

import java.util.ArrayList;
import java.util.List;

public class Niveau {

    private static final byte[][][] niveaux = {
            LevelBuilder.niveau1,
            LevelBuilder.niveau2,
            LevelBuilder.niveau3,
            LevelBuilder.niveau4
    };
    public static byte[][] currentLvl;
    public final int VIDE = 0;
    public final int MUR = 1;
    public final int CLAY = 2;
    public final int DIAMAND = 3;
    public final int PIERRE = 4;
    public final int MONSTRE_BLEU = 5;
    public final int MONSTRE_ROUGE = 6;
    public final int MINEUR = 7;
    private int nbDimandToWin;
    private List<Monstre> monstreList;
    private List<Bloc> blocList;
    private Mineur mineur;

    /**
     * Initialise un niveau
     * TODO pour l'instant le nombre de vie est à 3 pour tous les niveaux et le nombre de diamand à collecter = à 10
     * TODO il faudra sans doute niveller celà
     *
     * @param niveau (niveau à initialiser (/!\ on commence à 0)
     */
    public Niveau(int niveau) {
        nbDimandToWin = 10;
        currentLvl = niveaux[niveau];
        blocList = new ArrayList<>();
        monstreList = new ArrayList<>();
        for( int i = 0; i < currentLvl.length; i++ ) {
            for( int j = 0; j < currentLvl[0].length; j++ ) {
                switch( currentLvl[i][j] ) {
                    case MUR:
                        blocList.add(new Mur(i, j));
                        break;
                    case CLAY:
                        blocList.add(new Clay(i, j));
                        break;
                    case DIAMAND:
                        blocList.add(new Diamand(i, j));
                        break;
                    case PIERRE:
                        blocList.add(new Pierre(i, j));
                        break;
                    case MINEUR:
                        mineur = new Mineur(i, j);
                        break;
                    case MONSTRE_BLEU:
                        monstreList.add(new MonstreBleu(i, j));
                        break;
                    case MONSTRE_ROUGE:
                        monstreList.add(new MonstreRouge(i, j));
                        break;
                }
            }
        }
    }

    /**
     * retrait d'un bloc par un joueur
     *
     * @param position : position du bloc à retirer
     * @return (true si la partie est finie (c'est à dire si le nombre de Diamand tombe à O), false sinon)
     */
    public boolean removeBloc(Vec2d position) {
        for( int i = 0; i < blocList.size(); i++ ) {
            Bloc blocTemp = blocList.get(i);
            if( !(blocTemp.isFriable()) &&
                    blocTemp.getPosition().x == position.x
                    && blocTemp.getPosition().y == position.y ) {
                if( blocTemp instanceof Diamand ) {
                    nbDimandToWin--;
                }
                if( nbDimandToWin == 0 ) {
                    return true;
                }
                blocList.remove(blocTemp);
                break;
            }
        }
        return false;
    }

    /**
     * retrait d'un monstre du jeu quand il est mort
     * TODO il faudra gérer si le monstre est bleu ou rouge mais je sais pas si on le fait ici ou pas pour l'instant
     *
     * @param position (position du monstre à retirer)
     */
    public void removeMonstre(Vec2d position) {
        for( int i = 0; i < monstreList.size(); i++ ) {
            Monstre monstreTemp = monstreList.get(i);
            if( monstreTemp.getPosition().x == position.x && monstreTemp.getPosition().y == position.y ) {
                monstreList.remove(monstreTemp);
                break;
            }
        }
    }

    /**
     * déplace le mineur en fonction de la direction souhaitée seulement si la case est vide
     * TODO je ne sais pas si on doit attaquer une case ou non (clay + diamand) S'il suffit de " marcher dessus"
     * TODO on le fait ici, sinon faut faire une méthode à part
     * /!\ JE PARS DU PRINCIPE QUE LE POINT ORIGINE EST EN HAUT A GAUCHE !
     * @param direction (direction ciblée : b = bas, g = gauche, d = droite, h = haut)
     */
    public void deplacerMineur(char direction) {
        Vec2d positionCible = null;
        if( direction == 'g' ) {
            positionCible = new Vec2d(mineur.getPosition().x - 1, mineur.getPosition().y);
        } else if( direction == 'd' ) {
            positionCible = new Vec2d(mineur.getPosition().x + 1, mineur.getPosition().y);
        } else if( direction == 'h' ) {
            positionCible = new Vec2d(mineur.getPosition().x, mineur.getPosition().y-1);
        } else if( direction == 'b' ) {
            positionCible = new Vec2d(mineur.getPosition().x, mineur.getPosition().y+1);
        }
        if (positionCible!= null && currentLvl[(int)positionCible.x][(int)positionCible.y] == CLAY) {
            currentLvl[(int)positionCible.x][(int)positionCible.y] = 0;
            for( int i = 0; i < blocList.size(); i++ ) {
                if (blocList.get(i).position.x == positionCible.x && blocList.get(i).position.y == positionCible.y){
                    blocList.remove(blocList.get(i));
                }
            }
        } else if (positionCible!= null && currentLvl[(int)positionCible.x][(int)positionCible.y] == DIAMAND) {
            currentLvl[(int)positionCible.x][(int)positionCible.y] = 0;
            for( int i = 0; i < blocList.size(); i++ ) {
                if (blocList.get(i).position.x == positionCible.x && blocList.get(i).position.y == positionCible.y){
                    blocList.remove(blocList.get(i));
                }
            }
            nbDimandToWin--;
            if (nbDimandToWin == 0) {
                victory();
            }
        } else if (positionCible!= null && currentLvl[(int)positionCible.x][(int)positionCible.y] == MUR) {
            return;
        }  else if (positionCible!= null && currentLvl[(int)positionCible.x][(int)positionCible.y] == PIERRE) {
            Vec2d caseDerrierePierre = new Vec2d(
                    positionCible.x+(positionCible.x-mineur.getPosition().x),
                    positionCible.y+(positionCible.y-mineur.getPosition().y));
            if (currentLvl[(int)caseDerrierePierre.x][(int)caseDerrierePierre.y] == VIDE) {
                currentLvl[(int)caseDerrierePierre.x][(int)caseDerrierePierre.y] = PIERRE;
                for( int i = 0; i < blocList.size(); i++ ) {
                    if (blocList.get(i).position.x == caseDerrierePierre.x && blocList.get(i).position.y == positionCible.y){
                        blocList.get(i).position.x = caseDerrierePierre.x;
                        blocList.get(i).position.y = caseDerrierePierre.y;
                    }
                }
            } else {
                return;
            }
        } else if (positionCible!= null && (currentLvl[(int)positionCible.x][(int)positionCible.y] == MONSTRE_ROUGE
                || currentLvl[(int)positionCible.x][(int)positionCible.y] == MONSTRE_BLEU)) {
            gameover();
        }
        mineur.deplacement(positionCible);
    }

    private void victory() {

    }

    private void gameover() {

    }

    /**
     * fait tomber un bloc vers la zone donnée.
     * TODO /!\ il faudra controler DANS LE CONTROLLER que le bloc est bien un bloc qui peut tomber avant
     * TODO /!\ d'appeler cette méthode.
     * @param blocToMove (bloc à faire tomber
     * @param position (position vers laquelle il faut faire tomber le bloc)
     */
    public void moveBloc(Bloc blocToMove, Vec2d position) {
        blocToMove.setPosition(position);
    }

    /**
     * regarde si la case à la position donnée est vide ou non
     * @param position (position à contrôler)
     * @return (true si la case est vide, false sinon)
     */
    private boolean isEmptyCase(Vec2d position) {
        for( Monstre monstreTemp : monstreList ) {
            if( monstreTemp.getPosition().x == position.x
                    && monstreTemp.getPosition().y == position.y ) {
                return false;
            }
        }
        for( Bloc blocTemp : blocList ) {
            // Pour pouvoir tester on fais comme si clay on pouvait marcher dessus
            if(blocTemp instanceof Clay)
                return true;
            if( blocTemp.getPosition().x == position.x
                    && blocTemp.getPosition().y == position.y ) {
                return false;
            }
        }
        return true;
    }

    public List<Monstre> getMonstreList() { return monstreList; }
    public List<Bloc> getBlocList() { return blocList; }
    public Mineur getMineur() { return mineur; }
}