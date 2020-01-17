package model;

import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import view.ExplosionView;
import view.ViewGame;

import java.util.ArrayList;
import java.util.List;

public class Niveau {

    public static final byte[][][] niveaux = {
            LevelBuilder.niveau1,
            LevelBuilder.niveau2
    };
    public static byte[][] currentLvl;
    private static final int VIDE = 0;
    private static final int MUR = 1;
    private static final int CLAY = 2;
    public static final int DIAMAND = 3;
    private static final int PIERRE = 4;
    private static final int MONSTRE_BLEU = 5;
    private static final int MONSTRE_ROUGE = 6;
    private static final int MINEUR = 7;
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
     *
     * @param niveau (niveau à initialiser (/!\ on commence à 0)
     */
    Niveau(int niveau) {
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
     * déplace le mineur en fonction de la direction souhaitée seulement si la case est vide
     * /!\ JE PARS DU PRINCIPE QUE LE POINT ORIGINE EST EN HAUT A GAUCHE !
     *
     * @param direction (direction ciblée : b = bas, g = gauche, d = droite, h = haut)
     */
    public void deplacerMineur(char direction) {
        Point2D positionCible = null;
        if (direction == 'h') {
            positionCible = new Point2D(mineur.getPosition().getX() - 1, mineur.getPosition().getY());
        } else if (direction == 'b') {
            positionCible = new Point2D(mineur.getPosition().getX() + 1, mineur.getPosition().getY());
        } else if (direction == 'g') {
            positionCible = new Point2D(mineur.getPosition().getX(), mineur.getPosition().getY() - 1);
        } else if (direction == 'd') {
            positionCible = new Point2D(mineur.getPosition().getX(), mineur.getPosition().getY() + 1);
        }

        if (positionCible == null) return;
        mineur.vue.changerDirection(direction);

        switch (currentLvl[(int) positionCible.getX()][(int) positionCible.getY()]) {
            case CLAY:
                removeBlocAt((int) positionCible.getX(), (int) positionCible.getY());
                currentLvl[(int) positionCible.getX()][(int) positionCible.getY()] = VIDE;
                for (int i = 0; i < blocList.size(); i++) {
                    if (blocList.get(i).position.getX() == positionCible.getX() && blocList.get(i).position.getY() == positionCible.getY()) {
                        blocList.remove(blocList.get(i));
                    }
                }
                break;
            case DIAMAND:
                removeBlocAt((int) positionCible.getX(), (int) positionCible.getY());
                currentLvl[(int) positionCible.getX()][(int) positionCible.getY()] = VIDE;
                for (int i = 0; i < blocList.size(); i++) {
                    if (blocList.get(i).position.getX() == positionCible.getX() && blocList.get(i).position.getY() == positionCible.getY()) {
                        blocList.remove(blocList.get(i));
                    }
                }
                nbDimandToWin--;
                if (nbDimandToWin <= 0) porte.unLock();
                break;
            case MUR:
                return;
            case PIERRE:
                Point2D caseDerrierePierre = new Point2D(
                        positionCible.getX() + (positionCible.getX() - mineur.getPosition().getX()),
                        positionCible.getY() + (positionCible.getY() - mineur.getPosition().getY()));
                if (currentLvl[(int) caseDerrierePierre.getX()][(int) caseDerrierePierre.getY()] != VIDE || direction == 'h')
                    return;
                for (Bloc pierre : blocList) {
                    if (pierre.position.getX() == positionCible.getX() && pierre.position.getY() == positionCible.getY()) {
                        pierre.setPosition(new Point2D(caseDerrierePierre.getX(), caseDerrierePierre.getY()));
                    }
                }
                currentLvl[(int) positionCible.getX()][(int) positionCible.getY()] = VIDE;
                currentLvl[(int) caseDerrierePierre.getX()][(int) caseDerrierePierre.getY()] = PIERRE;
                break;
            case PORTE:
                if (porte.isLocked()) return;
                victoire = true;
                break;
        }
        mineur.deplacement(positionCible);
        currentLvl[(int) mineur.position.getX()][(int) mineur.position.getY()] = VIDE;
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
        for (Bloc bloc : blocList)
            if (bloc instanceof Fallable && bloc.position.getY() < currentLvl[0].length - 1) {
                int x = (int) bloc.position.getX(),
                        y = (int) bloc.position.getY();

                if (((Fallable) bloc).isFalling() && mineur.position.getX() == x + 1 && mineur.position.getY() == y) {
                    defaite = true;
                    return;
                }
                for (Monstre m : getMonstreList()) {
                    if (((Fallable) bloc).isFalling() &&
                            x + 1 == m.getPosition().getX() &&
                            y == m.getPosition().getY()) {
                        currentLvl[x][y] = VIDE;
                        blocList.remove(bloc);
                        ((StackPane)bloc.getView().getParent()).getChildren().remove(bloc.getView());
                        exploserMonstre(m.position, m instanceof MonstreBleu);
                        monstreList.remove(m);
                        ((StackPane)m.vue.getParent()).getChildren().remove(m.vue);
                        return;
                    }
                }

                if (currentLvl[x + 1][y] == VIDE && (mineur.position.getX() != x + 1 || mineur.position.getY() != y)) {
                    if (((Fallable) bloc).fallTo(x + 1, y)) {
                        currentLvl[x + 1][y] = currentLvl[x][y];
                        currentLvl[x][y] = VIDE;
                    }
                } else if (currentLvl[x + 1][y] == DIAMAND || currentLvl[x + 1][y] == PIERRE) {
                    if (y > 0 && currentLvl[x + 1][y - 1] == VIDE && currentLvl[x][y - 1] == VIDE &&
                            (mineur.position.getX() != x + 1 || mineur.position.getY() != y - 1)) {
                        if (((Fallable) bloc).fallTo(x + 1, y - 1)) {
                            currentLvl[x + 1][y - 1] = currentLvl[x][y];
                            currentLvl[x][y] = VIDE;
                        }
                    } else if (y < currentLvl[0].length - 1 && currentLvl[x + 1][y + 1] == VIDE && currentLvl[x][y + 1] == VIDE &&
                            (mineur.position.getX() != x + 1 || mineur.position.getY() != y + 1)) {
                        if (((Fallable) bloc).fallTo(x + 1, y + 1)) {
                            currentLvl[x + 1][y + 1] = currentLvl[x][y];
                            currentLvl[x][y] = VIDE;
                        }
                    } else ((Fallable) bloc).stopFalling();
                } else ((Fallable) bloc).stopFalling();
            }
    }


    private void removeBlocAt(int x, int y) {
        for (Bloc bloc : blocList)
            if (bloc.position.getX() == x && bloc.position.getY() == y) {
                StackPane st = (StackPane) bloc.getView().getParent();
                st.getChildren().remove(bloc.getView());
            }
    }

    public void deplacerMonstre(Monstre m) {
        int[] devant, droite, derriere, gauche;


        // 0. Vérifier les objets en contact avec le monstre (Mineur|Fallable)
        if (((mineur.getPosition().getX() == m.position.getX())
                && ((mineur.getPosition().getY() == m.position.getY() + 1) || mineur.getPosition().getY() == m.position.getY() - 1))
                || ((mineur.getPosition().getY() == m.position.getY())
                && ((mineur.getPosition().getX() == m.position.getX() + 1) || mineur.getPosition().getX() == m.position.getX() - 1)))
            defaite = true;
        for (Bloc bloc : getBlocList()) {
            if (bloc instanceof Fallable && ((Fallable) bloc).isFalling() &&
                    Math.abs((bloc.getPosition().getX() + 1)-m.getPosition().getX()) <= 1 &&
                    Math.abs(bloc.getPosition().getY()-m.getPosition().getY())<=1) {
                currentLvl[(int) bloc.position.getX()][(int) bloc.position.getY()] = VIDE;
                blocList.remove(bloc);
                ((StackPane)bloc.getView().getParent()).getChildren().remove(bloc.getView());
                exploserMonstre(m.position, m instanceof MonstreBleu);
                monstreList.remove(m);
                ((StackPane)m.vue.getParent()).getChildren().remove(m.vue);
                return;
            }
        }

        // 1. Déterminer les coordonnées de la case gauche/droite/devant/derrière du monstre en fonction de sa direction
        switch (m.dir) {
            case 'h': // le monstre va vers le haut
                devant = new int[]{(int) m.getPosition().getX() - 1, (int) m.getPosition().getY()};
                droite = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() + 1};
                derriere = new int[]{(int) m.getPosition().getX() + 1, (int) m.getPosition().getY()};
                gauche = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() - 1};
                break;
            case 'd': // le monstre va vers la droite
                devant = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() + 1};
                droite = new int[]{(int) m.getPosition().getX() + 1, (int) m.getPosition().getY()};
                derriere = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() - 1};
                gauche = new int[]{(int) m.getPosition().getX() - 1, (int) m.getPosition().getY()};
                break;
            case 'b': // le monstre va vers le bas
                devant = new int[]{(int) m.getPosition().getX() + 1, (int) m.getPosition().getY()};
                droite = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() - 1};
                derriere = new int[]{(int) m.getPosition().getX() - 1, (int) m.getPosition().getY()};
                gauche = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() + 1};
                break;
            case 'g': // le monstre va vers la gauche
                devant = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() - 1};
                droite = new int[]{(int) m.getPosition().getX() - 1, (int) m.getPosition().getY()};
                derriere = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() + 1};
                gauche = new int[]{(int) m.getPosition().getX() + 1, (int) m.getPosition().getY()};
                break;
            default: // le monstre n'a pas de direction (model: 'a')
                // Il s'agit d'un cas spécial où le monstre doit déterminer vers où il
                // doit aller afin de suivre en mur dans un sens horaire.

                // On considère la vue du plateau (vers le haut)
                devant = new int[]{(int) m.getPosition().getX() - 1, (int) m.getPosition().getY()};
                droite = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() + 1};
                derriere = new int[]{(int) m.getPosition().getX() + 1, (int) m.getPosition().getY()};
                gauche = new int[]{(int) m.getPosition().getX(), (int) m.getPosition().getY() - 1};
                // Si on est près d'un mur on détermine une direction (sens horaire)
                if (currentLvl[devant[0]][devant[1]] != VIDE) {
                    m.dir = 'd';
                    deplacerMonstre(m);
                } else if (currentLvl[droite[0]][droite[1]] != VIDE) {
                    m.dir = 'b';
                    deplacerMonstre(m);
                } else if (currentLvl[derriere[0]][derriere[1]] != VIDE) {
                    m.dir = 'g';
                    deplacerMonstre(m);
                } else if (currentLvl[gauche[0]][gauche[1]] != VIDE) {
                    m.dir = 'h';
                    deplacerMonstre(m);
                }
                // Sinon on se déplace vers un direction unique (gauche par exemple) jusqu'à trouver un mur,
                // SANS attribuer de direction au monstre
                else {
                    currentLvl[(int) m.position.getX()][(int) m.position.getY()] = VIDE;
                    m.position = new Point2D(gauche[0], gauche[1]);
                    currentLvl[gauche[0]][gauche[1]] = (byte) (m instanceof MonstreBleu ? MONSTRE_BLEU : MONSTRE_ROUGE);
                }
                return;
        }

        // 2. Appliquer l'algorithme de déplacement du prof.
        //      ordre: GAUCHE DEVANT DROITE DERRIERE
        if (currentLvl[gauche[0]][gauche[1]] == VIDE) {
            // Déplacer monstre
            currentLvl[(int) m.position.getX()][(int) m.position.getY()] = VIDE;
            m.position = new Point2D(gauche[0], gauche[1]);
            currentLvl[gauche[0]][gauche[1]] = (byte) (m instanceof MonstreBleu ? MONSTRE_BLEU : MONSTRE_ROUGE);
            // Modifier sa direction
            switch (m.dir) {
                case 'h':
                    m.dir = 'g';
                    break;
                case 'd':
                    m.dir = 'h';
                    break;
                case 'b':
                    m.dir = 'd';
                    break;
                default:
                    m.dir = 'b';
            }
        } else if (currentLvl[devant[0]][devant[1]] == VIDE) {
            // Déplacer monstre
            currentLvl[(int) m.position.getX()][(int) m.position.getY()] = VIDE;
            m.position = new Point2D(devant[0], devant[1]);
            currentLvl[devant[0]][devant[1]] = (byte) (m instanceof MonstreBleu ? MONSTRE_BLEU : MONSTRE_ROUGE);
        } else if (currentLvl[droite[0]][droite[1]] == VIDE) {
            // Déplacer monstre
            currentLvl[(int) m.position.getX()][(int) m.position.getY()] = VIDE;
            m.position = new Point2D(droite[0], droite[1]);
            currentLvl[droite[0]][droite[1]] = (byte) (m instanceof MonstreBleu ? MONSTRE_BLEU : MONSTRE_ROUGE);
            // Modifier sa direction
            switch (m.dir) {
                case 'h':
                    m.dir = 'd';
                    break;
                case 'd':
                    m.dir = 'b';
                    break;
                case 'b':
                    m.dir = 'g';
                    break;
                default:
                    m.dir = 'h';
            }
        } else if (currentLvl[derriere[0]][derriere[1]] == VIDE) {
            // Déplacer monstre
            currentLvl[(int) m.position.getX()][(int) m.position.getY()] = VIDE;
            m.position = new Point2D(derriere[0], derriere[1]);
            currentLvl[derriere[0]][derriere[1]] = (byte) (m instanceof MonstreBleu ? MONSTRE_BLEU : MONSTRE_ROUGE);
            // Modifier sa direction
            switch (m.dir) {
                case 'h':
                    m.dir = 'b';
                    break;
                case 'd':
                    m.dir = 'g';
                    break;
                case 'b':
                    m.dir = 'h';
                    break;
                default:
                    m.dir = 'd';
            }
        }
    }

    private void exploserMonstre(Point2D position, boolean genererDiamands) {
        int[] aoe = new int[]{-1,0,1};

        for(int i = blocList.size()-1; i>=0; i--) {
            if(Math.abs(blocList.get(i).position.getX()-position.getX())<=1 &&
                    Math.abs(blocList.get(i).position.getY()-position.getY())<=1 &&
                    !(blocList.get(i) instanceof Mur)){
                ((StackPane)blocList.get(i).getView().getParent()).getChildren().remove(blocList.get(i).getView());
                blocList.remove(i);
            }
        }
        for (int row : aoe)
            for (int col : aoe)
                if (currentLvl[(int) position.getX() + row][(int) position.getY() + col] != MUR) {
                    currentLvl[(int) position.getX() + row][(int) position.getY() + col] = (byte) (genererDiamands ? DIAMAND : VIDE);
                    if (genererDiamands) {
                        Diamand d = new Diamand((int) position.getX() + row, (int) position.getY() + col);
                        blocList.add(d);
                        ((StackPane) mineur.vue.getParent()).getChildren().add(d.vue);
                    }
                    ((StackPane) mineur.vue.getParent()).getChildren().add(new ExplosionView((position.getX() + row)*ViewGame.TAILLE_IMAGES,(position.getY() + col)*ViewGame.TAILLE_IMAGES).start());
                }
    }

}