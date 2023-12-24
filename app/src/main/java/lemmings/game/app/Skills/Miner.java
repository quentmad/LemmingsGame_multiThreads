package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.LevelManager;

import com.badlogic.gdx.graphics.Color;

import lemmings.game.app.Animation;

/**
 * Mineur est une compétence qui peut être donnée à un lemming. <p>
 * Un mineur {@code creuse un tunnel diagonal vers le bas}. Il s'arrêtera s'il manque de terrain ou si on lui dit de faire autre chose. <p> 
 */
public class Miner extends Skill{

    private Animation animationMiner;
    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Miner(Lemming lemmingg) {
       super(lemmingg);
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT)
        animationMiner = new Animation(235, 258, 259, 282, true, 16, 20, 0);
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationMiner);
        lemming.setCurrentFrameToFirst();
    }
    @Override
    public void act() {
        lemming.updateAnimationFrame();
        if(lemming.isCurrentFrameThe(5)){
            lemming.moveInDirection(4,-4);

            LevelManager.getInstance().getMap().getPixColision().setColor(Color.BLACK);
            if(lemming.getDirection()==0){//Creuse vers la droite
                LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()+8, 830 - lemming.getPosHitboxY()-13 ,30, 13 ); 


            }else{
                LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()+lemming.getHitboxWidth()-28, 830 - lemming.getPosHitboxY()-13 ,30, 13 ); 

            }

        }
        if(lemming.isCurrentFrameThe(23)){
            //Si il n'y a pas de colission a droite alors qu'il doit creuser vers la droite ou idem pour la gauche --> REDEVIENT UN WALKER (son boulot est fini)
            if(!lemming.isCollidingDown()){
                lemming.setSkill(new Walker(lemming));
            }
        }
    }

}
