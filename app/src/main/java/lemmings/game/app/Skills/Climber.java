package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.Animation;

/**
 * Grimpeur est une compétence qui peut être donnée à un Lemming.<p>
 * Les grimpeurs {@code peuvent escalader des murs verticaux}, quelle que soit leur hauteur, mais retomberont s'ils heurtent un plafond. <p>
 * Le grimpeur est une {@code compétence permanente} : une fois assigné à un lemming, il possède cette compétence pour le reste du niveau. 
 */
public class Climber extends Walker{
    
    private Animation animationClimber;
    
    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Climber(Lemming lemmingg) {
        super(lemmingg);
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT)
        animationClimber = new Animation(24, 31, 32, 39, true, 10, 20, 0);
        System.out.println("A new Climber just arrived");
    }

    public void act() {
        lemming.updateAnimationFrame();

        //Si il marche alors qu'il y a du vide en dessous
        if(currentWalkerAction.equals("walk") & !lemming.isCollidingDown()){
            startFallingWithoutAnimation();//Debut de saut: sans animation
        //Si il chute alors qu'il y'a du sol en dessous
        }else if(currentWalkerAction.equals("fall") & lemming.isCollidingDown()){
            startWalking();
        }
        if(lemming.getPixelsToClimb()==-1){//Mur infranchissable : climbing
            climb();
        }
        else if(lemming.isCollidingDown()){
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
        if(currentWalkerAction.equals("climb")){
            if((lemming.getDirection() == 0 && !lemming.isCollidingRight())//Il ni y a plus rien a escalader
                ||(lemming.getDirection() == 1 && !lemming.isCollidingLeft())){//Il ni y a plus rien a escalader){
                    startWalking();
            }else if(lemming.isCollidingAbove()){//Plafond --> Chute
                startWalking();
                lemming.moveInDirection(-3,-1);
            }
        }
    }

    private void climb(){
        //Gestion des colisions
        if(currentWalkerAction.equals("walk")){//Debut
            startClimbing();
        }
        lemming.moveInDirection(0, 1);
        this.lemming.setPixelsToClimb(0);
    }

    public void startClimbing(){
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationClimber);
        lemming.setCurrentFrameToFirst(); 
        this.currentWalkerAction = "climb";
    }


}
