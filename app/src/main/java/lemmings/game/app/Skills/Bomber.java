package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.LemmingsManager;
import lemmings.game.app.LevelManager;

import com.badlogic.gdx.graphics.Color;

import lemmings.game.app.Animation;

/**
 * Bomber est une compétence qui peut être donnée à un lemming. <p>
 * Lorsque cette compétence est donnée à un lemming, il se met à {@code vibrer et d'exploser}, créant un trou dans le sol. <p>
 * C'est la seule compétence non permanente pouvant être attribuée à un lemming qui n'est pas au sol. 
 * Contrairement aux autres skills, l'animation ne se fait qu'une seule fois.
 */
public class Bomber extends Skill{

    private Animation animationBomber;

    /**True si vient d'une chute, false si vient d'une explosion */
    private boolean causeByFalling;

    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Bomber(Lemming lemmingg, boolean causeByFalling) {
        super(lemmingg);
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT): Meme animation dans les 2 cas
        this.causeByFalling = causeByFalling;
        animationBomber = new Animation(76, 106, false,16, 20, 0);
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationBomber);
        lemming.setDirection(0);
        lemming.setCurrentFrameToFirst();
        if(this.causeByFalling){lemming.setCurrentFrame(lemming.getFrame(16));}

    }
    @Override
    public void act() {
        lemming.updateAnimationFrame();
        if(lemming.isCurrentFrameThe(15) && !causeByFalling){
            lemming.setCurrentFrame(lemming.getFrame(22));
        }
        if(lemming.isCurrentFrameThe(23) && !causeByFalling){
            //Modification de la map car explosion
            LevelManager.getInstance().getMap().getPixColision().setColor(Color.BLACK);
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()-28, 830 - lemming.getPosHitboxY()-5 ,11,8  );
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()-17, 830 - lemming.getPosHitboxY()-14 ,17,19  );
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()-8, 830 - lemming.getPosHitboxY()-16 ,8,26  );
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX(), 830 - lemming.getPosHitboxY()-18 ,lemming.getHitboxWidth(),31  );
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()+lemming.getHitboxWidth(), 830 - lemming.getPosHitboxY()-16 ,9,26 );
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()+lemming.getHitboxWidth()+7, 830 - lemming.getPosHitboxY()-13 ,15,20  );
            LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosHitboxX()+lemming.getHitboxWidth()+18, 830 - lemming.getPosHitboxY()-6 ,12,8  );

        }
        //le supprime de nos lemmings (mort)
        if(lemming.isCurrentFrameThe(24)){
            LemmingsManager.getInstance().supprimerLemming(lemming);
        }
    }

}
