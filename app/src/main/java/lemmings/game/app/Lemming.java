package lemmings.game.app;
import com.badlogic.gdx.math.Rectangle;

import lemmings.game.app.Skills.*;

/**Un lemming est géré par un thread qui lui est propre. <p>
 * Il a une position, une direction, une animation, une hitBox (pour les colissions) ainsi qu'un comportement.
 */
public class Lemming {

    /**Position X du lemming*/
    private int posX;
    /**Position Y du lemming*/
    private int posY;


    /**Dimension de la hitbox en largeur. Plus petit que la taille de la frame, depend de l'animation actuelle */
    private int currentHitboxWidth;
    /**Dimension de la hitbox en hauteur. Plus petit que la taille de la frame, depend de l'animation actuelle */
    private int currentHitboxHeight;
    /**Différent de 0 dans le cas où l'hitbox ne commence pas tout en bas de la frame, depend de l'animation actuelle */
    private int currentOffsetY;
    /**Position du debut de l'hitbox du lemming (point en bas à gauche) */
    private int posHitboxX;
    /**Position du debut de l'hitbox du lemming (point en bas à gauche) */
    private int posHitboxY;
    /**Zone de colission du lemming */
    private Rectangle hitBox;

    /**True si il y a une colission par rapport à la gauche en cours, sinon false*/
    private boolean isCollidingLeft;

    /**True si il y a une colission par rapport à la droite en cours, sinon false*/
    private boolean isCollidingRight;
    
    /**True si il y a une colission par rapport au bas en cours, sinon false*/
    private boolean isCollidingDown;
    /**True si il y a une colission par rapport au haut en cours, sinon false*/
    private boolean isCollidingAbove;
    
    private int pixelsToClimb;
    /**
     * Direction actuelle du lemming: <p>
     * {@code 0} pour 'vers la droite' <p>
     * {@code 1} pour 'vers la gauche'
    */
    private int direction;

    /**SpriteSheet pour les animations */
    private SpriteSheetInfo spriteSheetInfo;

    private Skill skill;
    /**Index du sprite actuel:
     * On récupérera ensuite le sprite correspondant dans le spriteSheet (par exemple 5 correspond à la 2e colonne et  2e ligne d'un tableau 3 par 3)
     */
    private int currentFrameSpriteIndex;

    /**Une animation est une liste de sprites (appartenant à la spriteSheet). <p> On stocke ici les coordonnées sous forme d'une seule valeur
     * (on retrouve ensuite l'équivalent dans le tab 2D) du sprite de début de l'animation et de celui de fin (la lecture se faisant de gauche à droite et ligne par ligne<p>
     * Défini selon le comportement<p>
     * RIGHT movements and  LEFT movements*/
    private Animation currentAnimation;

    private boolean isAlive;
    private boolean haveFinishLevel;


    public Lemming(int posX,int posY){
        //Position initiale : la porte d'
        this.posX = posX;//80;
        this.posY = posY;//650; //Attention : Y = 0 correspond au bas
        this.direction = 0;
        //Width : 1280 / 20 : 64
        //Height : 704 / 22 : 32
        this.spriteSheetInfo = new SpriteSheetInfo(64,32, "lemmingSpriteSheet - 2");
        this.skill = new Walker(this);
        //Hitbox:
        this.updateInfoHitbox(); //en fonction de l'animation actuelle
        this.hitBox = new Rectangle(posHitboxX, posHitboxY, currentHitboxWidth, currentHitboxHeight);
        this.isCollidingLeft = false;//Aucune colission au départ
        this.isCollidingRight = false;//Aucune colission au départ
        this.isCollidingDown = false;//Aucune colission au départ
        this.isCollidingAbove = false;//Aucune colission au départ
        this.isAlive = true;
        this.haveFinishLevel = false;

    }


    public Animation currentAnimation() {
        return currentAnimation;
    }

    /**
     * Direction actuelle du lemming: <p>
     * {@code 0} pour 'vers la droite' <p>
     * {@code 1} pour 'vers la gauche'
    */
    public int getDirection(){
        return direction;
    }
    
    public SpriteSheetInfo getSpriteSheetInfo() {
        return spriteSheetInfo;
    }

    /**Position du lemming */
    public int getPosX() {
        return posX;
    }

    /**Position du lemming */
    public int getPosY() {
        return posY;
    }

    /**Position du debut de l'hitbox du lemming (point en bas à gauche) */
    public int getPosHitboxX() {
        return posHitboxX;
    }

    /**Position du debut de l'hitbox du lemming (point en bas à gauche) */
    public int getPosHitboxY() {
        return posHitboxY;
    }

    /**Taille de la hitbox */
    public int getHitboxWidth() {
        return currentHitboxWidth;
    }

     /**Taille de la hitbox */
    public int getHitboxHeight() {
        return currentHitboxHeight;
    }

    public Skill getSkill() {
        return skill;
    }

    public boolean isCollidingLeft() {
        return isCollidingLeft;
    }


    public void setCollidingLeft(boolean isCollisionLeft) {
        this.isCollidingLeft = isCollisionLeft;
    }


    public boolean isCollidingRight() {
        return isCollidingRight;
    }


    public void setCollidingRight(boolean isCollisionRight) {
        this.isCollidingRight = isCollisionRight;
    }


    public boolean isCollidingDown() {
        return isCollidingDown;
    }


    public void setCollidingDown(boolean isCollisionDown) {
        this.isCollidingDown = isCollisionDown;
    }

    public boolean isCollidingAbove(){
        return this.isCollidingAbove;
    }
    public void setCollisionAbove(boolean collidingAbove) {
        this.isCollidingAbove = collidingAbove;
    }

    public int currentFrameSpriteIndex() {
        return currentFrameSpriteIndex;
    }

    /**Pixel a monter si il y un "petit mur" franchissable
     *  @return le nombre de pixel a franchir
     *  ou 0 si il ni a pas de colission
     *  ou -1 si il n'est pas franchissable
     */
    public int getPixelsToClimb(){
        return pixelsToClimb;
    }

        public void setPixelsToClimb(int px){
            pixelsToClimb = px;
    }

    /**Défini le currentSprite de l'animation au premier de l'animation, selon la direction */
    public void setCurrentFrameToFirst() {
        currentFrameSpriteIndex = currentAnimation().first(direction);
    }

    /**Défini le currentSprite de l'animation au frame*/
    public void setCurrentFrame(int frame) {
        currentFrameSpriteIndex = frame;
    }

    /**@return la case X du spriteSheet correspondant à l'index, selon la direction*/
    public int currentSpriteXOf(int index){
        return (currentFrameSpriteIndex % spriteSheetInfo.nbSpriteX());
    }

    /**@return la case Y du spriteSheet correspondant à l'index, selon la direction */
    public int currentSpriteYOf(int index){
        return (currentFrameSpriteIndex / spriteSheetInfo.nbSpriteX());
    }

    /**Défini l'animation actuelle
     * Défini le début et fin de l'animation (suffisant pour avoir l'animation complete) 
     * Met en suite à jour les infos des currentHitbox et offsetY via {@code updateInfoHitbox}*/
    public void setCurrentAnimationAndUpdateCurrentHitboxInfo(Animation animation) {
        this.currentAnimation = animation;
        this.updateInfoHitbox();
    }

    /**Met a jour la position du hitbox (rectangle) <p>, p
     * Par rapport à l'animation actuelle*/
    public void updateHitbox(){
        this.posHitboxX =this.posX + spriteSheetInfo.SPRITE_WIDTH()/2 - currentHitboxWidth/2;
        this.posHitboxY =this.posY;
        hitBox.setPosition(posHitboxX,posHitboxY);
    }

    /**Met à jour les current hitbox et offsetY
     * A appeler lorsqu'on change d'animation.
     * Met en suite à jour via {@code updateHitbox() }
    */
    public void updateInfoHitbox(){
        this.currentHitboxWidth = currentAnimation.getHitboxWidth();
        this.currentHitboxHeight = currentAnimation.getHitboxHeight();
        this.currentOffsetY = currentAnimation.getOffsetY();
        this.hitBox = new Rectangle(posHitboxX, posHitboxY, currentHitboxWidth, currentHitboxHeight);
        updateHitbox();

    }

    /**Permet d'animer le lemming en changeant de sprite/frame à chaque update (via act()) <p>.
     * Défini le {@code currentSprite} au prochain sprite de l'animation selon le comportement 
     * L'animation est en boucle entre le sprite first et last'*/
    public void updateAnimationFrame(){
        //Passe au prochain...
        if(currentAnimation().isLooping()){
            currentFrameSpriteIndex = (currentFrameSpriteIndex +1);
            if(currentFrameSpriteIndex > currentAnimation().last(direction)){
                currentFrameSpriteIndex = currentAnimation().first(direction);
            }
        }else{
            if (currentFrameSpriteIndex <= currentAnimation().last(direction)) {
                currentFrameSpriteIndex++; 
            }
        }
    }

    /**Bouge de x et de y.
     * Update L'hitbox.
    */
    public void move(int x, int y) {
        this.posX+=x;
        this.posY+=y;
        updateHitbox();
    }

    /**Bouge de dx dans la direction du lemming, et de y.
     * Update L'hitbox.
    */
    public void moveInDirection(int dx, int y) {
        if(direction ==0){
            this.posX+=dx;
        }else{
            this.posX -=dx;
        }
        this.posY+=y;
        updateHitbox();
    }

    public void setSkill(Skill newBehavior) {
        this.skill = newBehavior;
    }

    /**
     * Défini la direction du lemming: <p>
     * {@code 0} pour 'vers la droite' <p>
     * {@code 1} pour 'vers la gauche'
    */
    public void setDirection(int direct) {
        this.direction = direct;
    }

    public void setDirectionAndResetSprite(int direct){
        if(this.direction != direct){
            this.direction = direct;
        }
        this.setCurrentFrameToFirst();
    }



    /** Permet d'avoir la ie frame de l'animation, selon la direction
     * @param index: l'ie frame de l'animation
     * 1 pour la premiere,
     * 2 pour la 2e... */
    public int getFrame(int frameIndex){
        int indexAdd = frameIndex-1;//Si on veut la premiere, on ajoute 0...
        if(indexAdd >= 0 && currentAnimation().first(direction) + indexAdd <= currentAnimation().last(direction)){
            return (currentAnimation().first(direction)+indexAdd);

        }
        return -1000;
    }
    
    /**@return {@code true} si le frame current est le frameIndex ieme, sinon {@code false}. <p>
     * Prend en compte la direction actuelle
    */
    public boolean isCurrentFrameThe(int frameIndex){
        return (currentFrameSpriteIndex == getFrame(frameIndex));
    }


    public int getCurrentOffsetY() {
        return currentOffsetY;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void finishLevel() {
        this.haveFinishLevel = true;
        LemmingsManager.getInstance().supprimerLemming(this);
    }





}
