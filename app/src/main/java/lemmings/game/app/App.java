
package lemmings.game.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;



public class App {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Lemmings";
        config.width = 1475;//Largeur _
        config.height = 830;//hauteur |
        new LwjglApplication(new Game(), config);
        
    }
}
