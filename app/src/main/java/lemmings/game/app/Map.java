package lemmings.game.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Map {

    /**Map modifiable et sur laquelle on peut connaitre la couleur des pixels */
   private Pixmap pixColision; 

   /**Meme map que pixColission, mais affichable à l'écran */
   private Texture mapTexture;


   /**592 sur 1475 (car boutons en bas)*/
   private int screenHeight;//SOL A 119

   final int groundThreshold = 1;
   final int colorFond;

   public Map(int level){
        this.pixColision = new Pixmap(Gdx.files.internal("src/main/resources/map_official_"+level+".png")); //TEST AVEC MAP OFFICIEL
        this.screenHeight= pixColision.getHeight() + 238; //238 is the height of the bottom image
        mapTexture = new Texture(pixColision); // update Lors de modif
        this.colorFond = 255;


   }


    /**Map modifiable et sur laquelle on peut connaitre la couleur des pixels */
   public Pixmap getPixColision() {
        return pixColision;
    }

    

   public Texture getMapTexture() {
        return mapTexture;
    }


    /**
    * Détecte si le rectangle (hitbox) est en colision avec le plafond
    * On verifie le "cadre" de la hitbox
    * @param posX
    * @param posY
    * @param width pour calculer la colission selon le hitbox
    * @param height pour calculer la colission selon le hitbox
    * @return {@code true} si il y a collision, sinon {@code false}.
    */
   public boolean isCollidingAbove(int posX, int posY, int width, int height){       
       for (int x = posX+4; x < posX + width - 4; x+=1) {
           if ( pixColision.getPixel(x, screenHeight - (posY + height )) != colorFond) {
               return true;
           }
       }
       return false;
   }
   

    /**
    * Détecte si le rectangle (hitbox) est en colision avec le sol
    * On verifie le "cadre" de la hitbox
    * @param posX
    * @param posY
    * @param width pour calculer la colission selon le hitbox
    * @param height pour calculer la colission selon le hitbox
    * @return {@code true} si il y a collision, sinon {@code false}.
    */
   public boolean isCollidingGround(int posX, int posY, int width, int height){       
       for (int x = posX; x < posX + width; x+=2) {
           if ( pixColision.getPixel(x, screenHeight - (posY)) != colorFond) {
               return true;
           }
       }
       return false;
   }

/**
 * Détecte si le rectangle (hitbox) est en collision avec la droite et calcule la hauteur à grimper pour franchir l'obstacle.
 *
 * @param posX   position horizontale de la hitbox
 * @param posY   position verticale de la hitbox
 * @param width  largeur de la hitbox
 * @param height hauteur de la hitbox
 * @return la hauteur à grimper pour franchir le mur franchissable, -1 si il n'est pas franchissable ou 0 s'il n'y a pas de mur
 */
   public int getPixelsToClimbRight(int posX, int posY, int width, int height) {
    final int maxClimbHeight = 5; // Nombre maximum de pixels à grimper

    for (int y = posY + groundThreshold; y < posY + height; y += 2) {
        if (y <= posY + maxClimbHeight) {
            //Considére comme franchissable si la collision est à une hauteur tolérable
            if (pixColision.getPixel(posX + width, screenHeight - y) != colorFond) {
                //Itére vers le haut pour voir combien de pixels peuvent être grimpés
                int climb = 0;
                while (climb <= maxClimbHeight && pixColision.getPixel(posX + width, screenHeight - y - climb) != colorFond) {
                    climb++;
                }
                if(climb <= maxClimbHeight)return climb;
                return -1;
            }
        } else {
            //Traiter comme une collision infranchissable
            if (pixColision.getPixel(posX + width, screenHeight - y) != colorFond) {
                return -1; // Collision infranchissable directe
            }
        }
    }
    return 0; //Pas de collision
}


/**
 * Détecte si le rectangle (hitbox) est en collision avec la gauche et calcule la hauteur à grimper pour franchir l'obstacle.
 *
 * @param posX   position horizontale de la hitbox
 * @param posY   position verticale de la hitbox
 * @param width  largeur de la hitbox
 * @param height hauteur de la hitbox
 * @return la hauteur à grimper pour franchir le mur franchissable, -1 si il n'est pas franchissable ou 0 s'il n'y a pas de mur
 */
public int getPixelsToClimbLeft(int posX, int posY, int width, int height) {
    final int maxClimbHeight = 5; // Nombre maximum de pixels à grimper

    for (int y = posY + groundThreshold; y < posY + height; y += 2) {
        if (y <= posY + maxClimbHeight) {
            // Considérer comme franchissable si la collision est à une hauteur tolérable
            if (pixColision.getPixel(posX, screenHeight - y) != colorFond) {
                // Itérer vers le haut pour voir combien de pixels peuvent être grimpés
                int climb = 0;
                while (climb <= maxClimbHeight && pixColision.getPixel(posX, screenHeight - y - climb) != colorFond) {
                    climb++;
                }
                if(climb <= maxClimbHeight)return climb;
                return -1;            
            }
        } else {
            // Traiter comme une collision infranchissable
            if (pixColision.getPixel(posX, screenHeight - y) != colorFond) {
                return -1; // Collision infranchissable directe
            }
        }
    }
    return 0; // Pas de collision
}



public int getscreenHeight() {
    return screenHeight ;
}

}
