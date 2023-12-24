package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;
import lemmings.game.app.LevelManager;
import lemmings.game.app.ResourceManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import lemmings.game.app.Animation;

/**
 * Constructeur est une compétence qui peut être donnée à un lemming. <p>
 * Les constructeurs {@code créent un escalier diagonal vers le haut}, composé de douze briques. Une fois qu'un constructeur n'a plus de briques, 
 * il hausse les épaules et continue de marcher en tant que {@code Walker}.<p> Vous pouvez le faire reconstruire en le sélectionnant avant qu'il ne quitte le pont.
 * Le lemming s’arrêtera et fera demi-tour s’il heurte un mur. Le constructeur est la seule compétence classique capable d'ajouter du terrain au niveau.
 * Les builders ont accès aux briques de manière partagé, ils doivent réaliser une demande au {@code ResourceManager} lorsqu'ils souhaite avoir une brique.
 */
public class Builder extends Skill{

    private Animation animationBuilder;
    private Rectangle brick;
    /**Nombre de brique que dois poser le lemming */
    private int counterPerso;
    
    /**La création du comportement initialise l'animation du lemming et défini son spriteCurrent au premier sprite de l'animation */
    public Builder(Lemming lemmingg) {
        super(lemmingg);
        this.counterPerso = 12;
        //Défini l'animation du lemming à celui de ce comportement (RIGHT, LEFT)
        animationBuilder = new Animation(126, 141,151, 166, true, 16, 20, 0);
        brick = new Rectangle(32,0,12,2);
        lemming.setCurrentAnimationAndUpdateCurrentHitboxInfo(animationBuilder);
        lemming.setCurrentFrameToFirst();
        // Le lemming ne peut construire que depuis le sol
        if(!lemming.isCollidingDown()){
            lemming.setSkill(new Walker(lemming));   
        }
    }

    @Override
    public void act() {
        boolean haveBrick = true;
        if(lemming.isCurrentFrameThe(1)){
            //Demande de brique au manager
            if(!ResourceManager.getInstance().requestBuilding(lemming)){

                haveBrick = false; 
            }
        }
        //Si il a une brique il continu
        if(haveBrick){

            
            lemming.updateAnimationFrame();
            if(lemming.isCurrentFrameThe(13)){
                lemming.moveInDirection(4,2);

                if(counterPerso == 0){//Pu de briques, on s'arrete
                lemming.setSkill(new Walker(lemming));
                }else if(lemming.getPixelsToClimb()==-1 & lemming.getDirection()==0){//demi tour mais on continu
                    this.lemming.setDirectionAndResetSprite(1);
                }else if(lemming.getPixelsToClimb()==-1 && lemming.isCollidingLeft() && lemming.getDirection()==1){
                    this.lemming.setDirectionAndResetSprite(0);
                }
            }
            if(lemming.isCurrentFrameThe(5)){
                LevelManager.getInstance().getMap().getPixColision().setColor(Color.GRAY);
                counterPerso --;
                
                if(lemming.getDirection()==0){//Si il va vers la droite
                    LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosX()+(int)brick.getX()+3, 830 - (int)lemming.getPosY()-(int)brick.getHeight(),(int)brick.getWidth(),(int)brick.getHeight()  );
                }else{//Si il va vers la gauche
                    LevelManager.getInstance().getMap().getPixColision().fillRectangle(lemming.getPosX()+ (int)brick.getX()-2 - (int)brick.getWidth(), 830 - (int)lemming.getPosY()-(int)brick.getHeight(),(int)brick.getWidth(),(int)brick.getHeight()  );

                }
            }
        }
    }

}
