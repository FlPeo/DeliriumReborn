package model;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Niveau {

    public static final byte[][][] niveaux = {
            LevelBuilder.niveau1,
            LevelBuilder.niveau2,
            /*LevelBuilder.niveau3,
            LevelBuilder.niveau4*/
    };
    public static byte[][] currentLvl;
    public static final int VIDE = 0;
    public static final int MUR = 1;
    public static final int CLAY = 2;
    public static final int DIAMAND = 3;
    public static final int PIERRE = 4;
    public static final int MONSTRE_BLEU = 5;
    public static final int MONSTRE_ROUGE = 6;
    public static final int MINEUR = 7;
    public static final int PORTE = 8;
    public int numLevel;
    private int nbDimandToWin;
    private List<Monstre> monstreList;
    private List<Bloc> blocList;
    private Mineur mineur;
    private Porte porte;
    private boolean victoire, defaite;

    /**
     * Initialise un niveau
     * TODO pour l'instant le nombre de vie est à 3 pour tous les niveaux et le nombre de diamand à collecter = à 10
     * TODO il faudra sans doute niveller celà
     *
     * @param niveau (niveau à initialiser (/!\ on commence à 0)
     */
    public Niveau(int niveau) {
        numLevel = niveau;
        defaite = false;
        nbDimandToWin = 10;
        victoire = false;
        currentLvl = new byte[niveaux[niveau].length][niveaux[niveau][0].length];
        for (int i = 0; i < niveaux[niveau].length; i++)
            System.arraycopy(niveaux[niveau][i], 0, currentLvl[i], 0, niveaux[niveau][i].length);
        blocList = new ArrayList<>();
        monstreList = new ArrayList<>();
        for (int i = 0; i < currentLvl.length; i++) {
            for (int j = 0; j < currentLvl[i].length; j++) {
                switch (currentLvl[i][j]) {
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
                    case PORTE:
                        blocList.add(porte = new Porte(i, j));
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
     * @return (true si la partie est finie ( c ' est à dire si le nombre de Diamand tombe à O), false sinon)
     */
    public boolean removeBloc(Vec2d position) {
        for (int i = 0; i < blocList.size(); i++) {
            Bloc blocTemp = blocList.get(i);
            if (!(blocTemp.isFriable()) &&
                    blocTemp.getPosition().x == position.x
                    && blocTemp.getPosition().y == position.y) {
                if (blocTemp instanceof Diamand) {
                    nbDimandToWin--;
                }
                if (nbDimandToWin == 0) {
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
        for (int i = 0; i < monstreList.size(); i++) {
            Monstre monstreTemp = monstreList.get(i);
            if (monstreTemp.getPosition().x == position.x && monstreTemp.getPosition().y == position.y) {
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
     *
     * @param direction (direction ciblée : b = bas, g = gauche, d = droite, h = haut)
     */
    public void deplacerMineur(char direction) {
        Vec2d positionCible = null;
        if (direction == 'h') {
            positionCible = new Vec2d(mineur.getPosition().x - 1, mineur.getPosition().y);
        } else if (direction == 'b') {
            positionCible = new Vec2d(mineur.getPosition().x + 1, mineur.getPosition().y);
        } else if (direction == 'g') {
            positionCible = new Vec2d(mineur.getPosition().x, mineur.getPosition().y - 1);
        } else if (direction == 'd') {
            positionCible = new Vec2d(mineur.getPosition().x, mineur.getPosition().y + 1);
        }

        if(positionCible==null) return;
        mineur.vue.changerDirection(direction);

        switch (currentLvl[(int) positionCible.x][(int) positionCible.y]) {
            case CLAY:
                removeBlocAt((int) positionCible.x, (int) positionCible.y);
                currentLvl[(int) positionCible.x][(int) positionCible.y] = VIDE;
                for (int i = 0; i < blocList.size(); i++) {
                    if (blocList.get(i).position.x == positionCible.x && blocList.get(i).position.y == positionCible.y) {
                        blocList.remove(blocList.get(i));
                    }
                }
                break;
            case DIAMAND:
                removeBlocAt((int) positionCible.x, (int) positionCible.y);
                currentLvl[(int) positionCible.x][(int) positionCible.y] = VIDE;
                for (int i = 0; i < blocList.size(); i++) {
                    if (blocList.get(i).position.x == positionCible.x && blocList.get(i).position.y == positionCible.y) {
                        blocList.remove(blocList.get(i));
                    }
                }
                nbDimandToWin--;
                if (nbDimandToWin <= 0) porte.unLock();
                break;
            case MUR:
                return;
            case PIERRE:
                Vec2d caseDerrierePierre = new Vec2d(
                        positionCible.x + (positionCible.x - mineur.getPosition().x),
                        positionCible.y + (positionCible.y - mineur.getPosition().y));
                if (currentLvl[(int) caseDerrierePierre.x][(int) caseDerrierePierre.y] != VIDE || direction == 'h')
                    return;
                for (Bloc pierre : blocList) {
                    if (pierre.position.x == positionCible.x && pierre.position.y == positionCible.y) {
                        pierre.position.x = caseDerrierePierre.x;
                        pierre.position.y = caseDerrierePierre.y;
                    }
                }
                currentLvl[(int) positionCible.x][(int) positionCible.y] = VIDE;
                currentLvl[(int) caseDerrierePierre.x][(int) caseDerrierePierre.y] = PIERRE;
                break;
            case PORTE:
                if(porte.isLocked()) return;
                victoire = true;
                break;
        }
        mineur.deplacement(positionCible);
        currentLvl[(int) mineur.position.x][(int) mineur.position.y] = VIDE;
    }

    /**
     * Retourne vrai si le mineur se trouve à côté d'un monstre. Cette fonction est aussi bien utilisée pour chaque
     * déplacement du mineur que pour ceux des monstres
     *
     * @return
     */
    private boolean contactMonstre() {
        for (Monstre monstre : monstreList)
            if (((mineur.getPosition().x == monstre.position.x)
                    && ((mineur.getPosition().y == monstre.position.y + 1) || mineur.getPosition().y == monstre.position.y - 1))
                    || ((mineur.getPosition().y == monstre.position.y)
                    && ((mineur.getPosition().x == monstre.position.x + 1) || mineur.getPosition().x == monstre.position.x - 1)))
                return true;
        return false;
    }

    // GETTERS
    public List<Monstre> getMonstreList() {
        return monstreList;
    }

    public List<Bloc> getBlocList() {
        return blocList;
    }

    public Mineur getMineur() {
        return mineur;
    }

    public boolean isVictoire() {
        return victoire;
    }

    public boolean isDefaite() {
        return defaite;
    }

    public void appliquerGravite() {
        for (Bloc bloc : blocList) if(bloc instanceof Fallable && bloc.position.y<currentLvl[0].length-1) {
            int x = (int) bloc.position.x,
                    y = (int) bloc.position.y;

            if(((Fallable) bloc).isFalling() && mineur.position.x==x+1 && mineur.position.y==y)
            {
                defaite = true;
                return;
            }

            if(currentLvl[x+1][y] == VIDE && (mineur.position.x!=x+1 || mineur.position.y!=y)) {
                if(((Fallable) bloc).fallTo(x+1,y)){
                    currentLvl[x+1][y] = currentLvl[x][y];
                    currentLvl[x][y] = VIDE;
                }
            } else if(currentLvl[x+1][y] == DIAMAND || currentLvl[x+1][y] == PIERRE) {
                if(y>0 && currentLvl[x+1][y-1] == VIDE && currentLvl[x][y-1] == VIDE &&
                        (mineur.position.x!=x+1 || mineur.position.y!=y-1)) {
                    if(((Fallable) bloc).fallTo(x+1,y-1)){
                        currentLvl[x+1][y-1] = currentLvl[x][y];
                        currentLvl[x][y] = VIDE;
                    }
                } else if(y<currentLvl[0].length-1 && currentLvl[x+1][y+1] == VIDE && currentLvl[x][y+1] == VIDE &&
                        (mineur.position.x!=x+1 || mineur.position.y!=y+1)) {
                    if(((Fallable) bloc).fallTo(x+1,y+1)){
                        currentLvl[x+1][y+1] = currentLvl[x][y];
                        currentLvl[x][y] = VIDE;
                    }
                } else ((Fallable) bloc).stopFalling();
            } else ((Fallable) bloc).stopFalling();
        }
    }


    public void removeBlocAt(int x, int y) {
        for (Bloc bloc : blocList) if(bloc.position.x==x && bloc.position.y==y) {
            StackPane st = (StackPane) bloc.getView().getParent();
            st.getChildren().remove(bloc.getView());
        }
    }

    public void deplacerMonstre(Monstre m) {
        int[] devant, droite, derriere, gauche;

        switch (m.dir) {
            case 'h':
                devant =   new int[]{(int) m.getPosition().x-1,(int) m.getPosition().y};
                droite =   new int[]{(int) m.getPosition().x,(int) m.getPosition().y+1};
                derriere = new int[]{(int) m.getPosition().x+1,(int) m.getPosition().y};
                gauche =   new int[]{(int) m.getPosition().x,(int) m.getPosition().y-1};
                break;
            case 'd':
                devant =   new int[]{(int) m.getPosition().x,(int) m.getPosition().y+1};
                droite =   new int[]{(int) m.getPosition().x+1,(int) m.getPosition().y};
                derriere = new int[]{(int) m.getPosition().x,(int) m.getPosition().y-1};
                gauche =   new int[]{(int) m.getPosition().x-1,(int) m.getPosition().y};
                break;
            case 'b':
                devant =   new int[]{(int) m.getPosition().x+1,(int) m.getPosition().y};
                droite =   new int[]{(int) m.getPosition().x,(int) m.getPosition().y-1};
                derriere = new int[]{(int) m.getPosition().x-1,(int) m.getPosition().y};
                gauche =   new int[]{(int) m.getPosition().x,(int) m.getPosition().y+1};
                break;
            default://case 'g':
                devant =   new int[]{(int) m.getPosition().x,(int) m.getPosition().y-1};
                droite =   new int[]{(int) m.getPosition().x-1,(int) m.getPosition().y};
                derriere = new int[]{(int) m.getPosition().x,(int) m.getPosition().y+1};
                gauche =   new int[]{(int) m.getPosition().x+1,(int) m.getPosition().y};
        }


        if(currentLvl[gauche[0]][gauche[1]]==VIDE) {
            currentLvl[(int) m.position.x][(int) m.position.y] = VIDE;
            m.position = new Vec2d(gauche[0],gauche[1]);
            currentLvl[gauche[0]][gauche[1]] = (byte) (m instanceof MonstreBleu?MONSTRE_BLEU:MONSTRE_ROUGE);
            switch (m.dir) {
                case 'h': m.dir = 'g'; break;
                case 'd': m.dir = 'h'; break;
                case 'b': m.dir = 'd'; break;
                default:  m.dir = 'b';
            }
        } else if(currentLvl[devant[0]][devant[1]]==VIDE) {
            currentLvl[(int) m.position.x][(int) m.position.y] = VIDE;
            m.position = new Vec2d(devant[0],devant[1]);
            currentLvl[devant[0]][devant[1]] = (byte) (m instanceof MonstreBleu?MONSTRE_BLEU:MONSTRE_ROUGE);
        } else if(currentLvl[droite[0]][droite[1]]==VIDE) {
            currentLvl[(int) m.position.x][(int) m.position.y] = VIDE;
            m.position = new Vec2d(droite[0],droite[1]);
            currentLvl[droite[0]][droite[1]] = (byte) (m instanceof MonstreBleu?MONSTRE_BLEU:MONSTRE_ROUGE);
            switch (m.dir) {
                case 'h': m.dir = 'd'; break;
                case 'd': m.dir = 'b'; break;
                case 'b': m.dir = 'g'; break;
                default:  m.dir = 'h';
            }
        } else if(currentLvl[derriere[0]][derriere[1]]==VIDE) {
            currentLvl[(int) m.position.x][(int) m.position.y] = VIDE;
            m.position = new Vec2d(derriere[0],derriere[1]);
            currentLvl[derriere[0]][derriere[1]] = (byte) (m instanceof MonstreBleu?MONSTRE_BLEU:MONSTRE_ROUGE);
            switch (m.dir) {
                case 'h': m.dir = 'b'; break;
                case 'd': m.dir = 'g'; break;
                case 'b': m.dir = 'h'; break;
                default:  m.dir = 'd';
            }
        }


        if (contactMonstre()) {
            defaite = true;
        }
    }
}