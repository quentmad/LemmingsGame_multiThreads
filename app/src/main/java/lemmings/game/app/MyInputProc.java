package lemmings.game.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class MyInputProc implements InputProcessor {

    private int selectedSkill;
    private LemmingsManager lManager;

    public MyInputProc() {
        Gdx.input.setInputProcessor(this);//gestionnaire d'entrée actif
        this.lManager = LemmingsManager.getInstance();
        this.selectedSkill = -1;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button==0 && screenY > 690 && screenX < 957){//set action si clique gauche
                this.selectedSkill = screenX / 94;//Taille d'un button
                //Acceleration / freinage de la vitesse de jeu
                if (selectedSkill == 0 & screenY > 775 ){
                    Game.removeOneToUpdateInterval();
                }else if (selectedSkill == 0 & screenY > 590 ){
                    Game.addOneToUpdateInterval();
                }
         
            //System.out.println("action select : "+ selectedSkill);
        }else if(button==0 && selectedSkill !=-1 && screenY < 590){
            lManager.setBehaviorToLemmingInZone(screenX, screenY, selectedSkill);
            //System.out.println("action set du lemming : "+ selectedSkill);
        }
        return true;
    }




    //Inutile ici:
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
    
}
