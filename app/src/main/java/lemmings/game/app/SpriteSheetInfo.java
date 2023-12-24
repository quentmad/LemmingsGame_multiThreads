package lemmings.game.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Permet de stocker les élements concernant les spriteSheet...
 */
public class SpriteSheetInfo{
    /**Largeur d'un sprite dans le sprite sheet */
    private int SPRITE_WIDTH;
    /**Hauteur d'un sprite dans le sprite sheet */
    private int SPRITE_HEIGHT;
    /**Chargement de la sprite sheet Texture */
    private Texture texture;
    /**Division de l'image en sous-images correspondant à chaque sprite. <p> 
     * Dans le format {@code Y}  {@code X} */
    private TextureRegion[][] spriteSheetRegions;
    /**Nombre de sprite en X (colonnes)*/
    private int nbSpriteX;
    /**Nombre de sprite en Y (lignes) */
    private int nbSpriteY;


    /**Constructeur où spriteWidth et spriteHeight sont différents */
    public SpriteSheetInfo(int spWidth, int spHeight,String file){
        this.SPRITE_WIDTH = spWidth;
        this.SPRITE_HEIGHT = spHeight;
        this.texture = new Texture(Gdx.files.internal("src/main/resources/"+file+".png"));
        this.spriteSheetRegions  = TextureRegion.split(texture, SPRITE_WIDTH, SPRITE_HEIGHT);  
        this.nbSpriteX = texture.getWidth() /spWidth;
        this.nbSpriteY = texture.getHeight() /spHeight;
    }

    /**Constructeur où spriteWidth et spriteHeight sont égaux */
    public SpriteSheetInfo(int spSize,String file){
        this.SPRITE_WIDTH = spSize;
        this.SPRITE_HEIGHT = spSize;
        this.texture = new Texture(Gdx.files.internal("src/main/resources/"+file+".png"));
        this.spriteSheetRegions  = TextureRegion.split(texture, SPRITE_WIDTH, SPRITE_HEIGHT);  
    }


    /**Largeur d'un sprite dans le sprite sheet */
    public int SPRITE_WIDTH() {
        return SPRITE_WIDTH;
    }

    /**Hauteur d'un sprite dans le sprite sheet */
    public int SPRITE_HEIGHT() {
        return SPRITE_HEIGHT;
    }

    public Texture texture() {
        return texture;
    }

    public TextureRegion[][] spriteSheetRegions() {
        return spriteSheetRegions;
    }

    /**@return le nombre de colonnes du spriteSheet (nombre de sprites en X) */
    public int nbSpriteX(){//Attention: +1 par rapport à un tableau (20e case du tableau c'est 19)
        return nbSpriteX;
    }

    /**@return le nombre de lignes du spriteSheet (nombre de sprites en Y) */
    public int nbSpriteY(){//Attention: +1 par rapport à un tableau (20e case du tableau c'est 19)
        return nbSpriteY;
    }



    
}