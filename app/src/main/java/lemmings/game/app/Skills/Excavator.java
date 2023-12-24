package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.LevelManager;

import com.badlogic.gdx.graphics.Color;

import lemmings.game.app.Animation;

/**
 * Excavateur est une compétence qui peut être donnée à un lemming.<p>
 * Les creuseurs {@code créent un tunnel vertical vers le bas}, ne s'arrêtant que lorsqu'ils manquent de terre à creuser. <p> 
 * Vous pouvez également arrêter un creuseur en lui confiant une autre tâche, comme celle d'un constructeur. Lorsqu'il a fini de creuser, il redevient un {@code Walker}.
 */
public class Excavator extends Skill{

    private Animation animationExcavator;
    
    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Excavator(Lemming lemmingg) {
        super(lemmingg);     
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT): Meme animation dans les 2 cas
        animationExcavator = new Animation(283, 298, true, 20, 18, 2);
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationExcavator);
        lemming.setCurrentFrameToFirst();
        if(!lemming.isCollidingDown()){
            lemming.setSkill(new Walker(lemming));  
        }
        
    }


    @Override
    public void act() {
        lemming.updateAnimationFrame();
        if(lemming.isCurrentFrameThe(5)||lemming.isCurrentFrameThe(13)){
            lemming.move(0,-2);
            if(!lemming.isCollidingDown()){
                lemming.setSkill(new Walker(lemming));
            }
            //Modification de la map.
            LevelManager.getInstance().getMap().getPixColision().setColor(Color.BLACK);
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX(), 830 - lemming.getPosHitboxY()-6 ,lemming.getHitboxWidth(), 2);
        }



    }


}
