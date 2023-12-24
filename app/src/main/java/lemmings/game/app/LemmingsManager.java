package lemmings.game.app;

import java.util.concurrent.ConcurrentLinkedQueue;

import lemmings.game.app.Skills.*;


/**Gère les differents lemmings et leur thread */
public class LemmingsManager {
    private LevelManager levelManager;
    private static LemmingsManager instance;

    /**Liste des lemmings qui attendent de rentrer dans la map */
    public ConcurrentLinkedQueue<Lemming> waitingToEnter;
    /**Liste des lemmings dans la map (déjà rentré) */
    public ConcurrentLinkedQueue<Lemming> lemQueue;
    /**Compteur de lemming vivant */
    private Compteur lemmingCompteur;

    private LemmingsManager(){
        this.waitingToEnter= new ConcurrentLinkedQueue<Lemming>();//Rempli lors du nextLevel()
        this.lemQueue = new ConcurrentLinkedQueue<Lemming>();

        this.levelManager = LevelManager.getInstance();

        lemmingCompteur = new Compteur();

    }


    /**
     * Obtient une instance unique de la classe LemmingManager (Singleton).
     *
     * @return L'instance de LemmingManager.
     */
    public static LemmingsManager getInstance() {
        if (instance == null) {
            instance = new LemmingsManager();
        }
        return instance;
    }

    /**
     * Ajoute @param nbLemmings Lemmings dans la liste d'attente pour entrer dans la carte lors du début du niveau.
     * Creer également un thread qui lui sera dédié.
     * @param nbLemmings Le nombre de Lemmings à ajouter dans la liste d'attente.
     */
    public void ajouterLemming(Lemming nouveauLemming){
        synchronized(lemQueue)
        {
            if(lemQueue.contains(nouveauLemming)){
                System.err.println("Le lemming actuel est déjà dans la map");
            }else{
                lemQueue.add(nouveauLemming);
                Thread th = new Thread(() -> foreverUpdate(nouveauLemming));
                th.start();
            }
        }
    }

    /**
     * Ajoute @param nbLemmings Lemmings dans la liste d'attente pour entrer dans la carte lors du début du niveau.
     *
     * @param nbLemmings Le nombre de Lemmings à ajouter dans la liste d'attente.
     */    
    public void addInWaitingToEnterList(int nbLemmings){
        for(int i =0; i< nbLemmings; i++){
            waitingToEnter.add(new Lemming(levelManager.getBeginDoorsX()+10, levelManager.getBeginDoorsY()+15));
        }
    }
    
   /**
     * Fait rentrer tous les Lemmings dans la carte avec un intervalle de temps entre chacun.
     *
     * @param nbLemmings Le nombre de Lemmings à faire entrer dans la carte.
     */    
    public void joinMap(int nbLemmings){
        Thread th = new Thread(() -> {
            Lemming l;
            for(int i =0; i< nbLemmings; i++){
                l = waitingToEnter.poll();
                ajouterLemming(l);
                if(l.getSkill() instanceof Walker) {((Walker)l.getSkill()).setImmortal(true);}
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
            th.start();
        
    }



    /**
     * Supprime un lemming existant de la simulation
     * Utiliser cette fonction pour la concurence
     * */
    public void supprimerLemming(Lemming lemming){
        synchronized(lemQueue)
        {
            synchronized(lemming){
                if(!lemQueue.remove(lemming)){
                   System.err.println("On ne peux retirer un lemming absent de la liste");
                }
            }
        }
    }

    /**
     * Met à jour la simulation des Lemmings.
     * Réveille tous les threads endormis des Lemmings pour chaque tick.
     */
    public void update(){
        synchronized(levelManager){
            //System.out.println(lemmingCompteur.getValeur()); //affichage du nombre de lemmings compté à chaque tick
            //On reset le compteur de lemming à chaque tick
            lemmingCompteur.reset();
            levelManager.notifyAll();

            //Si il n'y a plus de lemmings dans le niveau, On passe au niveau suivant
            if(lemQueue.size()==0){
                levelManager.nextLevel();
            }
        }
    }

    /**
     * Méthode exécutée par un thread pour gérer les actions du Lemming en continu.
     * Elle ne s'arrête que lorque le lemming est supprimé de la liste
     * Le while(true) permet de garder les threads en vie
     */
    private void foreverUpdate(Lemming lem){
        while(true){
            synchronized(levelManager){
                try { 
                    //arrête les thread(lemmings) qui arrivent à ce point. Ils seront réveilés lors du prochain tick
                    levelManager.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); 
                    System.err.println("Thread Interrupted");
                }
           }

           //On incrémente tout les lemmings qui se sont réveillés dans le compteur
           lemmingCompteur.ajouterUn();
           
           synchronized(lem)
           {
                //Enleve le lemming si il est a la porte
                levelManager.isLemmingAtDoors(lem);
                // Si le lemming à été retiré, arrête le thread
                if(!lemQueue.contains(lem)){
                    //On décrémente le compteur puisque notre lemming va disparaitre
                    lemmingCompteur.enleverUn();

                    return;
                }
                updateThread(lem);
           }
        }
    }

    /**
     * Met a jour l'act, les animations, mouvements, etc d'un lemming.
     * Elle est appellée par chaque thread à chaque tick
    */
    private void updateThread(Lemming lem){
        //Calcul des collisions
        int hitX = lem.getPosHitboxX();
        int hitY = lem.getPosHitboxY();
        int width = lem.getHitboxWidth();
        int height = lem.getHitboxHeight();

        lem.setCollidingDown(levelManager.getMap().isCollidingGround(hitX, hitY, width, height));
        lem.setCollisionAbove(levelManager.getMap().isCollidingAbove(hitX, hitY, width, height));
        if(lem.getDirection()==0){
            lem.setPixelsToClimb(levelManager.getMap().getPixelsToClimbRight(hitX, hitY, width, height));
            if(lem.getPixelsToClimb()!=0){lem.setCollidingRight(true);
            }else{lem.setCollidingRight(false);}
            lem.setCollidingLeft(false);
        } else{
            lem.setPixelsToClimb(levelManager.getMap().getPixelsToClimbLeft(hitX, hitY, width, height));
            if(lem.getPixelsToClimb()!=0){lem.setCollidingLeft(true);
            }else{lem.setCollidingLeft(false);}
            lem.setCollidingRight(false);
        }

        //Lancer les actions
        lem.getSkill().act();

    }

    /**
     * Recherche un Lemming à proximité des coordonnées spécifiées et lui attribue une compétence.
     *
     * @param xSouris Coordonnée x de la souris.
     * @param ySouris Coordonnée y de la souris.
     * @param skill    Compétence à attribuer au Lemming à proximité.
     */
    public void setBehaviorToLemmingInZone(int xSouris, int ySouris, int skill){
        int screenHeight = levelManager.getMap().getscreenHeight(); 
        synchronized(lemQueue){
            for(Lemming l : lemQueue){
                synchronized(l){
                    if(arePointsClose(l.getPosX() + l.getHitboxWidth()/2+30, l.getPosY()+l.getHitboxHeight()/2, xSouris,screenHeight - ySouris, 20)){
                            switch (skill){
                                case 1:
                                    if(levelManager.isSkillAvailable(1)){
                                        l.setSkill(new Climber(l));
                                        levelManager.decrementSkillAvailable(1);
                                    }
                                    break;
                                case 2:
                                    if(levelManager.isSkillAvailable(2)){
                                        l.setSkill(new Floater(l));
                                        levelManager.decrementSkillAvailable(2);

                                    }
                                    break;
                                case 3:
                                    if(levelManager.isSkillAvailable(3)){
                                        l.setSkill(new Bomber(l, false));
                                        levelManager.decrementSkillAvailable(3);

                                    }
                                    break;
                                case 5:
                                    if(levelManager.isSkillAvailable(5)){
                                        l.setSkill(new Builder(l));
                                        levelManager.decrementSkillAvailable(5);

                                    }
                                    break;
                                case 6:
                                    if(levelManager.isSkillAvailable(6)){
                                        l.setSkill(new Basher(l));
                                        levelManager.decrementSkillAvailable(6);

                                    }
                                    break;
                                case 7:
                                    if(levelManager.isSkillAvailable(7)){
                                        l.setSkill(new Miner(l));
                                        levelManager.decrementSkillAvailable(7);

                                    }
                                    break;
                                case 8:
                                    if(levelManager.isSkillAvailable(8)){
                                        l.setSkill(new Excavator(l));
                                        levelManager.decrementSkillAvailable(8);
                                    }
                                    break;
 
                            }
                            return;
                    }

                }
            }
           
        }

    }


/**
 * Vérifie si deux points sont à une distance inférieure à maxDistanceBetween.
 * @param x1 Coordonnée X du premier point
 * @param y1 Coordonnée Y du premier point
 * @param x2 Coordonnée X du deuxième point
 * @param y2 Coordonnée Y du deuxième point
 * @param maxDistanceBetween Distance maximale permise entre les points
 * @return {@code true} si les points sont à une distance inférieure à maxDistanceBetween, sinon {@code false}.
 */
public boolean arePointsClose(int x1, int y1, int x2, int y2, int maxDistanceBetween) {
    int deltaX = x2 - x1;
    int deltaY = y2 - y1;
    int distanceSquared = deltaX * deltaX + deltaY * deltaY;
    int maxDistanceSquared = maxDistanceBetween * maxDistanceBetween;
    //System.out.println("distance between" + distanceSquared);
    return distanceSquared <= maxDistanceSquared;
}
    

/**@return le nombre de lemmings vivant */
public int getLemmingAlive(){
    return lemmingCompteur.getValeur();
}
}
