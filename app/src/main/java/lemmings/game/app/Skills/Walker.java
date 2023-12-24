package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.Animation;
/**
 * Un marcheur est un lemming ordinaire sans compétences ni capacités, et il se jettera volontiers du haut d'une falaise 
 * si vous le laissez faire. S'il trouve le portail de sortie , il le franchira.  
 * Les skills extends de {@code Walker} sont des marcheurs avec certains comportement spéciaux. Ces lemmings ont un skill permanent.
 */
public class Walker extends Skill{
    
    protected Animation animationFall;
    protected Animation animationWalk;
    /*L'animation de chute ne commence qu'après quelques frame de chute*/
    protected int fallBegin; 
    /**Pour les skills avec plusieurs animation, on doit connaitre l'animation/action actuelle 
     * Ici soit walk soit fall*/
    private int fallDuration;
    protected String currentWalkerAction;
    /**Permet de ne pas mourir de chute en arrivant dans le level... */
    private boolean immortal;

    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Walker(Lemming lemmingg) {
        super(lemmingg);   
        this.animationFall = new Animation(16, 19, 20, 23, true, 12, 20, 0);
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT)
        this.animationWalk = new Animation(0, 7, 8, 15, true, 15, 20, 0);
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(this.animationWalk);
        lemming.setCurrentFrameToFirst();
        this.currentWalkerAction = "walk";
        this.fallBegin = 5;
        this.fallDuration =0;
        this.immortal =false;

    }

   /**
    * Cette méthode est appelée à chaque itération du jeu pour mettre à jour le comportement du lemming Walker (et ceux extends de Walker). 
    * Si le lemming est en train de marcher et qu'il n'y a pas de collision en dessous de lui, il commence à tomber. 
    * Si le lemming est en train de tomber et qu'il y a collision en dessous de lui, il arrête de tomber. 
    * Si le lemming a une collision en dessous de lui, il continue à marcher. 
    * Sinon, il continue à tomber. 
    * Enfin, la méthode met à jour le frame d'animation du lemming. 
    */ 
    @Override
    public void act() {
        lemming.updateAnimationFrame();
        //Si il marche alors qu'il y a du vide en dessous
        if(currentWalkerAction.equals("walk") & !lemming.isCollidingDown()){
            startFallingWithoutAnimation();//Debut de saut: sans animation
        //Si il chute alors qu'il y'a du sol en dessous
        }else if(currentWalkerAction.equals("fall") & lemming.isCollidingDown()){
            startWalking();
            immortal = false;
        }
        if(lemming.isCollidingDown()){
            walk();
        }else{
            fall();
        }
        if(this.fallBegin >0 && (currentWalkerAction.equals("fall"))){
        this.fallBegin--;
        }else if(this.fallBegin == 0 && (currentWalkerAction.equals("fall"))){
            this.fallBegin = -1;
            startFallingWithAnimation();//Ajout de l'animation
        }
    }

    /**Appelé par act() lorsque currentActionWalker est "walk" 
     * Avance et change de sens si besoin
     */
    public void walk(){
        //Colissions franchissables
        if(lemming.getPixelsToClimb() >0){
            //System.out.print(lemming.getPixelsToClimb());
            lemming.move(0,lemming.getPixelsToClimb());
            lemming.setPixelsToClimb(0);
        }
        //Gestion des colisions : changement de sens (demi tour)
        else if(lemming.getDirection() == 0 && lemming.isCollidingRight()){ //droite --> gauche
                this.lemming.setDirectionAndResetSprite(1);
        } else if(lemming.getDirection() == 1 && lemming.isCollidingLeft()){ //gauche --> droite
                this.lemming.setDirectionAndResetSprite(0);
        }

        //Gestion des déplacements : mouvement de 1 dans le sens 
        lemming.moveInDirection(2, 0);


    }

    /** Appelé par act() lorsque currentActionWalker est "fall" 
     * Bouge vers le bas 
     * Attention, les Skills herité de Walker peuvent redéfinir fall différemment
     */
    public void fall(){
       lemming.move(0, -2); 
       fallDuration+=2;
    }

    /**Permet de faire un début de saut sans l'animation*/
    public void startFallingWithoutAnimation(){
        this.currentWalkerAction = "fall";
    }

    /**Défini l'animation actuelle du lemming au animationFall, remet à 0 le spriteCurrent  */
    public void startFallingWithAnimation(){
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationFall);
        lemming.setCurrentFrameToFirst(); 
    }

    /**Défini l'animation actuelle du lemming au animationWalk, remet à 0 le spriteCurrent, et défini currentWalkerAction  */
    public void startWalking(){
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationWalk);
        lemming.setCurrentFrameToFirst(); 
        this.currentWalkerAction = "walk";
        this.fallBegin = 5;
        //Si la chute a ete trop longue il meurt...
        if(fallDuration > 150 && !immortal){
            lemming.setSkill(new Bomber(lemming, true) );
        }
        fallDuration =0;

    }

    public boolean isImmortal() {
        return immortal;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

    

}