package lemmings.game.app.Skills;

import lemmings.game.app.Lemming;

/**Les compétences sont les {@code actions qu'un lemming peut effectuer} pour atteindre son objectif dans les niveaux. <p>
 * La classe abstraite définit les méthodes des différentes classes de logique d'action des comportements des lemmings.
 * Le comportement permet également de savoir quelles sprites du spriteSheet sont utilisés pour l'animation de ce comportement
 */
public abstract class Skill {

    protected Lemming lemming;


    public Skill(Lemming lemmingg){
        this.lemming = lemmingg;
    }

    /** action du lemming, appelé à chaque update (agit selon son comportement)*/
    public abstract void act();

}
