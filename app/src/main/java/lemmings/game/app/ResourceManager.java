package lemmings.game.app;

//import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gère les ressources partagées (briques) entre les lemmings.
 */
public class ResourceManager {
    private static ResourceManager instance;

    private  AtomicInteger totalBricks = new AtomicInteger();
    //private final int MAXBRICK = 10;
    private LockFreeQueue<Lemming> waitingBuilders = new LockFreeQueue<>();
    private boolean running = true;

    /**
     * Initialise la gestion des ressources en ajoutant périodiquement de nouvelles briques.
     */
    private ResourceManager() {
        startResourceManagement();
    }


    // INFO
    // Cette implémentation lock-free à été abandonnée au profit de l'implémentation d'une waiting queue.
    // Nous avons fait un compteur jouet inspiré de cette pour montrer une implémentation lock-free dans ce projet

    // /**
    //  * Ajoute de nouvelles briques aux briques disponibles de manière atomique
    //  * Le nombre de brique est cappé à MAXBRICK
    //  */
    // public void addBrick(int number){
    //     int lastValue; int newValue;
    //     do{
    //         lastValue = totalBricks.get();
    //         newValue = (lastValue + number > this.MAXBRICK) ? this.MAXBRICK : lastValue + number;            
    //     }while(!totalBricks.compareAndSet(lastValue, newValue));
    // }

    // /**
    //  * Demande une brique pour la construction.
    //  * Décrémente le total de brique de 1 de manière atomique
    //  *
    //  * @return {@code true} Si une brique peu être untilisées, sinon {@code false}.
    //  */
    // public boolean pickBrick() {
    //     int value;
    //     do{
    //         value = totalBricks.get();
    //         System.out.println(value);
    //         if(value==0){return false;}     
    //     }while(!totalBricks.compareAndSet(value, value-1)); //On ne fait pas ici de décrementAndGet car on veux faire un test avant
    //     System.out.println(totalBricks);
    //     return true;
    // }

    public int getTotalBrick(){
        return totalBricks.get();
    }
    
    /**
     * Obtient une instance unique de la classe ResourceManager (Singleton).
     *
     * @return L'instance de ResourceManager.
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }    

    /**
     * Démarre un thread pour ajouter 3 nouvelles briques toutes les 10 secondes.
     */
    public void startResourceManagement() {
        totalBricks.set(10);
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(10000); // Attendre 10 secondes
                    addBricks(5); // Ajouter 5 nouvelles briques
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Arrête la gestion des ressources.
     */
    public void stopResourceManagement() {
        running = false;
    }

    /**
     * Ajoute un nombre spécifié de briques au total, et notifie car de nouvelles briques sont disponible.
     *
     * @param amount Le nombre de briques à ajouter.
     */
    public void addBricks(int amount) {
        synchronized(this){
            if(totalBricks.get()<30){
                totalBricks.addAndGet(amount);
            }
        }
    }

    /**
     * Demande une brique pour la construction.
     * Si aucune brique n'est disponible, le lemming est ajouté à la file d'attente.
     *
     * @param lemming Le lemming qui demande une brique.
     * @return {@code true} si la brique est attribuée, sinon {@code false}.
     */
    public boolean requestBuilding(Lemming lemming) {
        synchronized (this) {
            //Si pas dans la file d'attente : on le rajoute
            if(!waitingBuilders.contains(lemming)){
                waitingBuilders.offer(lemming);
            }
            if (totalBricks.get() > 0) {
                
                if(lemming == waitingBuilders.peek()){
                    //On l'enleve de la file et on supprime le premier (lui) de la file
                    totalBricks.decrementAndGet();
                    waitingBuilders.poll();
                    return true;
                }
            } 
            return false;
        }
    }
}
