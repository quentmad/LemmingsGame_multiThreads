package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.Animation;

/**
 * Flotteur est une compétence qui peut être donnée à un lemming. <p>
 * Un flotteur a la capacité de {@code survivre à une chute} de n’importe quelle hauteur en utilisant un parapluie. <p>
 * Le flotteur est une {@code compétence permanente} : une fois assigné à un lemming, il possède cette compétence pour le reste du niveau. 
 */
public class Floater extends Walker {

    /**Cet attribut permet de n'afficher l'animation complète (avec le "début de saut") que au début */
    private boolean firstTourOfFall;

    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Floater(Lemming lemmingg) {//Redéfini le walker en changeant la chute
        super(lemmingg);
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT)
        this.animationFall = new Animation(56, 65, 66, 75, true, 16, 20, 0);//!!!debut pas en boucle

    }

    //! Floater utilise le act() de Walker, la seule différence est que lors de fall(), on appel le fall() ici
    
    /**Appelé par act() lorsque currentActionWalker est "fall", la premiere animation est complete, les suivantes n'affichent pas les premieres frames */
    public void fall(){
        lemming.move(0, -2); 
        if(firstTourOfFall){//début du saut
            //Alors on affiche tout
            if(lemming.currentFrameSpriteIndex() == lemming.currentAnimation().last(lemming.getDirection())){//A la fin de la premiere animation
                firstTourOfFall= false;
            }
        }else{
            //On passe la partie de l'animation correspondant au début du saut
            if(lemming.isCurrentFrameThe(1)){
                lemming.setCurrentFrame(lemming.getFrame(5));
            }
        }
    }


    /**Défini l'animation actuelle du lemming au animationFall, remet à 0 le spriteCurrent, et défini currentWalkerAction 
     * Ici on utilise firstTourCompleteFall permet de n'afficher l'animation complète (avec le "début de saut") que au début 
     */
    public void startFallingWithAnimation(){
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationFall);
        lemming.setCurrentFrameToFirst(); 
        this.firstTourOfFall = true;
    }
}
