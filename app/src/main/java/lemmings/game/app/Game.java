package lemmings.game.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;


/**
 * Classe principale de l'application libGDX. Gère le cycle de vie du jeu, la mise à jour et le rendu graphique.
 */
public class Game extends ApplicationAdapter {
    /**Police pour afficher du texte */
    /**lot de sprites pour les graphiques 2D */
    private SpriteBatch batch;
    private Sprite allButons; 

    private LevelManager levelManager;
    /**Gestionnaire d'entree (clavier,souris...) */
    private MyInputProc inputProc;
    private LemmingsManager lemmingsManager;

    private NumberRenderer numberRenderer;

    private float elapsedTime = 0; // Initialise à zéro au début
    private static float updateInterval; // Défini l'intervalle de mise à jour (8 frames par seconde)
    private static int rangeSpeed;//Vitesse du jeu entre 1 et 99
    Random random = new Random();


    @Override
    public void create () {
        levelManager = LevelManager.getInstance();
        System.out.println("refresh frequence is"+ (1/updateInterval));
        this.lemmingsManager = LemmingsManager.getInstance();
        this.numberRenderer = new NumberRenderer();
        updateInterval = 0.1f;
        rangeSpeed = mapSpeedToRange(updateInterval);

        this.allButons = new Sprite(new Texture(Gdx.files.internal("src/main/resources/allbuttons_big.png")));
        this.allButons.setPosition(0, 0);

        //Initialisation des éléments pour le rendu
        this.batch = new SpriteBatch(); 
        this.inputProc = new MyInputProc();
        this.levelManager.nextLevel();//Niveau 1;

    }
    
    /**
     * Méthode de rendu de l'écran de jeu.
     * Gère le rafraîchissement visuel des éléments du jeu tels que la carte, les portes, les nombres représentant la vitesse, le nombre de Lemmings vivants et les briques disponibles.
     * Met à jour les éléments du jeu en fonction du temps écoulé.
     */
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        allButons.draw(batch);
        levelManager.getMap().getPixColision().setColor(Color.RED);

        batch.draw(levelManager.getMap().getMapTexture(), 0, 239);
        levelManager.getMap().getMapTexture().draw(levelManager.getMap().getPixColision(), 0, 0);
        numberRenderer.drawTwoDigitNumber(batch, rangeSpeed, 1);//Vitesse du jeu
        numberRenderer.drawTwoDigitNumber(batch, lemmingsManager.getLemmingAlive(), 10);//V
        numberRenderer.drawTwoDigitNumber(batch, ResourceManager.getInstance().getTotalBrick(), 5);//V
        numberRenderer.drawAllTwoDigitNumber(batch, levelManager.getSkillsAvailable());
    
        //Met à jour le temps écoulé
        elapsedTime += Gdx.graphics.getDeltaTime();
    
        //Vérifie si le temps écoulé dépasse l'intervalle de mise à jour
        if (elapsedTime >= updateInterval) {
            lemmingsManager.update();
            elapsedTime = 0; //Réinitialise le temps écoulé
            levelManager.updateAnimationDoors();
        }
    
        batch.draw(levelManager.getBeginDoors().spriteSheetRegions()[levelManager.getCurrentBeginDoorsIndex()][0], levelManager.getBeginDoorsX(), levelManager.getBeginDoorsY());
        batch.draw(levelManager.getEndingDoors().spriteSheetRegions()[0][levelManager.getCurrentEndingDoorsIndex()], levelManager.getEndingDoorsX(), levelManager.getEndingDoorsY());

        //afficher tt les lemmings

        synchronized(lemmingsManager.lemQueue){
            for(Lemming lem : lemmingsManager.lemQueue){
                batch.draw(lem.getSpriteSheetInfo().spriteSheetRegions()[lem.currentSpriteYOf(lem.currentFrameSpriteIndex())][lem.currentSpriteXOf(lem.currentFrameSpriteIndex())], lem.getPosX(), lem.getPosY()-lem.getCurrentOffsetY() );
            }
        }
        batch.end();
    }
    


    /**Accelere le jeu */
    public static void addOneToUpdateInterval(){
        if((updateInterval / 1.2f) >= 0.0010482089){
            updateInterval = updateInterval / 1.2f;
            rangeSpeed = mapSpeedToRange(updateInterval);
        }
    }

    /**Ralenti le jeu */
    public static void removeOneToUpdateInterval(){
        if((updateInterval / 0.8f) <= 0.25f){
            updateInterval = updateInterval / 0.8f;
            rangeSpeed = mapSpeedToRange(updateInterval);
        }

    }

    /**
     * Permet d'afficher la vitesse du jeu.
     * Mappe la valeur de vitesse sur une échelle logarithmique de 99 à 1.
     * @param speed La valeur de vitesse à mapper.
     * @return La valeur de vitesse mappée sur une échelle de 99 à 1, où 99 représente la vitesse la plus lente et 1 la vitesse la plus rapide.
     */
    public static int mapSpeedToRange(float speed) {
        // Limiter la plage de valeurs entre 0.0011212266 et 0.25
        float clampedSpeed = Math.max(Math.min(speed, 0.25f), 0.0011212266f);

        // Appliquer un mapping logarithmique inversé
        float logMapped = (float) (Math.log(0.25 / clampedSpeed) / Math.log(0.25 / 0.0011212266));

        // Convertir la valeur log à la plage 1-99
        int result = (int) (1 + logMapped * 98);

        return result;
    }


    public void dispose(){
       
    }


}

