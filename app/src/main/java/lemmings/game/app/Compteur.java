package lemmings.game.app;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cette classe est un compteur lock-free
 */
class Compteur{

    private AtomicInteger compteur = new AtomicInteger();

    private int ancienneValeur = 0;

    /**
     * Incrémente le compteur de 1
     * On pourrait utiliser incrementAndGet pour cet exemple mais nous voulions montrer cette implémentation
     */
    public void ajouterUn(){
        int lastValue;
        do{
            lastValue = compteur.get();          
        }while(!compteur.compareAndSet(lastValue, lastValue+1)); 
    }

    /**
     * Décrémente le compteur de 1
     * On pourrait utiliser decrementAndGet pour cet exemple mais nous voulions montrer cette implémentation
     *
     * @return {@code true} Si une brique peu être untilisées, sinon {@code false}.
     */
    public boolean enleverUn() {
        int lastValue;
        do{
            lastValue = compteur.get();
            //System.out.println(lastValue);
            if(lastValue==0){return false;}     
        }while(!compteur.compareAndSet(lastValue, lastValue-1));
        //System.out.println(compteur);
        return true;
    }

    /**
     * Le compteur est reset à chaque tour de boucle pour s'actualiser
     */
    public void reset(){
        ancienneValeur = compteur.get();
        compteur.set(0);
    }

    public int getValeur(){
        return ancienneValeur;
    }
}