package lemmings.game.app;

/** 
 * Enum Level 
 * Cet  enum représente un niveau du jeu. 
 * Chaque niveau est caractérisé par son numéro, les coordonnées de la porte de départ et d'arrivée, et le nombre de chaque compétences disponible pour les lemmings. 
 */
public enum Level {
    level1(1, 420, 650, 1072, 341,15, new int[]{4,6,15,0,6,6,8, 8}),
    
    level2(2, 200, 740, 1110, 277,25, new int[]{4,10,10,0,3,8,6, 5}),

    level3(3, 200, 540, 1310, 475,25, new int[]{4,10,10,0,3,8,6, 5});

          // 
    /** 
     * Numéro du niveau. 
     */ 
    private final int levelNumber;

    /**Tout ce qui correspond à la map */
    private Map map;

    /**Coordonnée X du point en bas à gauche de la porte de départ du niveau */
    private final int beginDoorsX;
    /**Coordonnée Y du point en bas à gauche de la porte de départ du niveau */
    private final int beginDoorsY;
    /**Coordonnée X du point en bas à gauche de la porte d'arrivée du niveau */
    private final int endDoorsX;
    /**Coordonnée Y du point en bas à gauche de la porte d'arrivée du niveau */
    private final int endDoorsY;

    private int nbLemmings;
    /**
     * Case 1: nombre de compétences du bouton 1 disponible , case 2 ...
     * Diminue au fur et à mesure de l'utilisation de compétences dans le niveau
     */
    private int[] skillsAvailable;



    /** 
     * Constructeur de la classe Level
     * @param levelNumber Le numéro du niveau
     * @param map La carte du niveau
     * @param beginDoorsX La coordonnée X de la porte de départ
     * @param beginDoorsY La coordonnée Y de la porte de départ
     * @param endDoorsX La coordonnée X de la porte d'arrivée
     * @param endDoorsY La coordonnée Y de la porte d'arrivée
     * @param nbLemmings nombre de lemmings dans ce level
     * @param skillsAvailable Le nomnbre de compétences disponibles pour les lemmings
     */ 
    private Level(int levelNumber, int beginDoorsX, int beginDoorsY, int endDoorsX, int endDoorsY, int nbLemmings,
            int[] skillsAvailable) {
        this.levelNumber = levelNumber;
        this.beginDoorsX = beginDoorsX;
        this.beginDoorsY = beginDoorsY;
        this.endDoorsX = endDoorsX;
        this.endDoorsY = endDoorsY;
        this.nbLemmings = nbLemmings;
        this.skillsAvailable = skillsAvailable;
    
    }

    

    public int getLevelNumber() {
        return levelNumber;
    }

    public Map getMap() {
        return map;
    }

    public int getBeginDoorsX() {
        return beginDoorsX;
    }

    public int getBeginDoorsY() {
        return beginDoorsY;
    }

    public int getEndingDoorsX() {
        return endDoorsX;
    }

    public int getEndingDoorsY() {
        return endDoorsY;
    }

    public int getNbLemmings(){
        return nbLemmings;
    }

    public int[] getSkillsAvailable() {
        return skillsAvailable;
    }
    


    
}
