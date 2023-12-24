package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.LevelManager;

import com.badlogic.gdx.graphics.Color;

import lemmings.game.app.Animation;

/**
 * Basher est une compétence qui peut être donnée à un lemming.<p>
 * Un basher {@code creuse un tunnel horizontal} à travers n'importe quelle surface. 
 * Le basher redevient un {@code Walker} quand il n'a rien a creuser.
 */
public class Basher extends Skill {

    private Animation animationBasher;

    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Basher(Lemming lemmingg) {
        super(lemmingg);
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT)
        animationBasher = new Animation(175, 204, 205, 234, true, 18, 20, 0);
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationBasher);
        lemming.setCurrentFrameToFirst();
    }
    
    @Override
    public void act() {
        lemming.updateAnimationFrame();
        lemming.moveInDirection(1,0);
        //Modification de la map pour creuser
        LevelManager.getInstance().getMap().getPixColision().setColor(Color.BLACK);
        LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()+lemming.getHitboxWidth()/2, 830 - lemming.getPosHitboxY() - lemming.getHitboxHeight() ,6, lemming.getHitboxHeight() );
        
        //Si il n'y a pas de colission a droite alors qu'il doit creuser vers la droite ou idem pour la gauche --> REDEVIENT UN WALKER (son boulot est fini)
        if(!lemming.isCollidingRight()&lemming.getDirection()==0
        ||(!lemming.isCollidingLeft()&lemming.getDirection()==1 )){
            lemming.setSkill(new Walker(lemming));
        }
    }
}
