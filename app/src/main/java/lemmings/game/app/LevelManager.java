package lemmings.game.app;

public class LevelManager {
    private static LevelManager instance;    
    /**Tableau d'enum Level */
    private Level[] levels;
    /**Animation porte de départ*/
    private SpriteSheetInfo beginDoors;
    private int currentBeginDoorsIndex;

    /**Animation porte d'arrivée*/
    private SpriteSheetInfo endingDoors;
    private int currentEndingDoorsIndex;        
    
    /** 
     * Numéro du niveau. 
     */ 
    private int levelNumber;

    /**Tout ce qui correspond à la map */
    private Map map;

    /**Coordonnée X du point en bas à gauche de la porte de départ du niveau */
    private int beginDoorsX;
    /**Coordonnée Y du point en bas à gauche de la porte de départ du niveau */
    private int beginDoorsY;
    /**Coordonnée X du point en bas à gauche de la porte d'arrivée du niveau */
    private int endDoorsX;
    /**Coordonnée Y du point en bas à gauche de la porte d'arrivée du niveau */
    private int endDoorsY;
    /**Nombre de lemmings dans ce niveau */
    private int nbLemmings;

    /**
     * Case 1: nombre de compétences du bouton 1 disponible , case 2 ...
     * Diminue au fur et à mesure de l'utilisation de compétences dans le niveau
     */
    private int[] skillsAvailable;



    public LevelManager(){

        this.levels = new Level[]{Level.level1, Level.level2, Level.level3};
        this.beginDoors = new SpriteSheetInfo(86,64,"doorsX2");//todo mettre direct dans level manager ??
        this.endingDoors = new SpriteSheetInfo(70,52,"end_doorsX2");//todo mettre direct dans level manager ??
        this.currentBeginDoorsIndex =0;
        this.currentEndingDoorsIndex =0;    
        this.levelNumber = 0;


    }

    /**
     * Obtient une instance unique de la classe (Singleton).
     *
     * @return L'instance de LevelManager.
     */
    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    /**Passe au niveau suivant*/
    public void nextLevel(){
        levelNumber++;
        if(levelNumber <= levels.length){
            this.beginDoorsX = levels[levelNumber-1].getBeginDoorsX();
            this.beginDoorsY = levels[levelNumber-1].getBeginDoorsY();
            this.endDoorsX = levels[levelNumber-1].getEndingDoorsX();
            this.endDoorsY = levels[levelNumber-1].getEndingDoorsY();
            this.nbLemmings = levels[levelNumber-1].getNbLemmings();
            this.skillsAvailable = levels[levelNumber-1].getSkillsAvailable();
            this.map = new Map(levelNumber);
            this.currentBeginDoorsIndex =0;
            this.currentEndingDoorsIndex =0; 


            LemmingsManager.getInstance().addInWaitingToEnterList(nbLemmings);
            LemmingsManager.getInstance().joinMap(nbLemmings);
        }else{System.out.println("game is finished, good game");}

    }



    public SpriteSheetInfo getBeginDoors() {
        return beginDoors;
    }

    public int getCurrentBeginDoorsIndex() {
        return currentBeginDoorsIndex;
    }

    public SpriteSheetInfo getEndingDoors() {
        return endingDoors;
    }

    public int getCurrentEndingDoorsIndex() {
        return currentEndingDoorsIndex;
    }

    /**Passe au frame suivant des animations des portes */
    public void updateAnimationDoors(){
        if(this.currentBeginDoorsIndex < beginDoors.nbSpriteY()-1){//Ouverture des portes unique
            currentBeginDoorsIndex++;
        } 
        this.currentEndingDoorsIndex = (currentEndingDoorsIndex + 1) % endingDoors.nbSpriteX(); //En boucle
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

    public int[] getSkillsAvailable() {
        return skillsAvailable;
    }

    /**Décremente de 1 le compteur de ce skill disponible*/
    public void decrementSkillAvailable(int skill){
       skillsAvailable[skill-1]--; 

    }

    /**@return true si il y a au moins 1 element restant pour ce skill */
    public boolean isSkillAvailable(int skill){
        skill--;//Premier case du tableau pour 0;
        if(skill >=0 && skill <= skillsAvailable.length){
            return (skillsAvailable[skill] > 0);
        }
        return false;

    }
        
    /**
     * @return true si le lemming est au niveau des portes, ce qui signifie qu'il a fini le level
     * Le supprime ainsi de la liste des lemmings
     */
    public boolean isLemmingAtDoors(Lemming lemming){
        if (lemming.isAlive() && LemmingsManager.getInstance().arePointsClose(lemming.getPosX() + lemming.getHitboxWidth()/2, lemming.getPosY(), endDoorsX+15, endDoorsY, 5) ){
            lemming.finishLevel();
            return true;
        }
        return false;
    }
}
