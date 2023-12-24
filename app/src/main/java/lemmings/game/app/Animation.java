package lemmings.game.app;

/**Permet de stocker le premier et dernier sprite/frame d'une animation (pour la droite et pour la gauche), et si elle doit boucler ou non */
public class Animation {
    /**Premiere frame de l'animation dans le cas "RIGHT" */
    private int firstFrameRight;
    /**Derniere frame de l'animation dans le cas "RIGHT" */
    private int lastFrameRight;
    /**Premiere frame de l'animation dans le cas "LEFT" */
    private int firstFrameLeft;
    /**Derniere frame de l'animation dans le cas "LEFT" */
    private int lastFrameLeft;
    /**@return {@code true} si l'animation est en boucle et {@code false} si elle ne s'effectue qu'une seule fois */
    private boolean looping;
    /**Dimension de la hitbox en largeur. Plus petit que la taille de la frame, permet de définir le {@code currentHitboxWidth} du lemming*/
    private int hitboxWidth;
    /**Dimension de la hitbox en hauteur. Plus petit que la taille de la frame, permet de définir le {@code currentHitboxHeight} du lemming*/
    private int hitboxHeight;
    /**Différent de 0 dans le cas où l'hitbox ne commence pas tout en bas de la frame, permet le décalage de {@code currentOffsetY} du lemming*/
    private int offsetY;

    
    

    public Animation(int firstFrameRight, int lastFrameRight, int firstFrameLeft, int lastFrameLeft, boolean looping, int hitboxWidth, int hitboxHeight, int offsetY) {
        this.firstFrameRight = firstFrameRight;
        this.lastFrameRight = lastFrameRight;
        this.firstFrameLeft = firstFrameLeft;
        this.lastFrameLeft = lastFrameLeft;
        this.looping = looping;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.offsetY = offsetY;

    }
    

    /**Constructeur quand l'animation pour la droite et pour la gauche sont les memes */
    public Animation(int firstFrame, int lastFrame, boolean looping, int hitboxWidth, int hitboxHeight, int offsetY) {
        this.firstFrameRight = firstFrame;
        this.lastFrameRight = lastFrame;
        this.firstFrameLeft = firstFrame;
        this.lastFrameLeft = lastFrame;
        this.looping = looping;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.offsetY = offsetY;
    }

    /**Index du premier sprite/frame de l'animation 
     * @param direction 0 (right) ou 1 (left)
    */
    public int first(int direction) {
        if(direction == 0){
            return firstFrameRight;
        }else if(direction == 1){
            return firstFrameLeft;
        }
        throw new IllegalArgumentException("direction must be 0 or 1, not " + direction);
    }

    /**Index du dernier sprite/frame de l'animation 
     * @param direction 0 (right) ou 1 (left)
    */
    public int last(int direction) {
        if(direction == 0){
            return lastFrameRight;
        }else if(direction == 1){
            return lastFrameLeft;
        }
        throw new IllegalArgumentException("direction must be 0 or 1, not " + direction);
    }


    /**@return {@code true} si l'animation est en boucle et {@code false} si elle ne s'effectue qu'une seule fois */
    public boolean isLooping() {
        return looping;
    }


    /**Dimension de la hitbox en largeur. Plus petit que la taille de la frame, permet de définir le {@code currentHitboxWidth} du lemming*/
    public int getHitboxWidth() {
        return hitboxWidth;
    }


    /**Dimension de la hitbox en hauteur. Plus petit que la taille de la frame, permet de définir le {@code currentHitboxHeight} du lemming*/
    public int getHitboxHeight() {
        return hitboxHeight;
    }

    /**Différent de 0 dans le cas où l'hitbox ne commence pas tout en bas de la frame, permet le décalage de {@code currentOffsetY} du lemming*/
    public int getOffsetY() {
        return offsetY;
    }

    
    
}
