package FactoryMethod;

import Items.*;
import PaooGame.RefLinks;

/**
 * Creează un inamic de tip Albina folosind Factory Method.
 * Clasa implementează o fabrică concretă ce extinde EnemyFactory.
 */
public class AlbinaFactory extends EnemyFactory {
    // Referință la obiectul RefLinks care oferă acces la componentele jocului
    private final RefLinks refLink;

    // Coordonatele inițiale ale inamicului
    private final int x, y;

    // Limitele patrulării pentru inamic (de la stânga la dreapta)
    private final int patrolLeft, patrolRight;

    // Referință către eroul principal, pentru ca inamicul să-l poată detecta/ataca
    private final Hero hero;

    /**
     * Constructor ce inițializează toți parametrii necesari pentru crearea unei Albina.
     *
     * @param refLink Referință la contextul general al jocului
     * @param x Coordonata X a poziției inițiale
     * @param y Coordonata Y a poziției inițiale
     * @param patrolLeft Limita stângă a patrulării
     * @param patrolRight Limita dreaptă a patrulării
     * @param hero Referință la eroul principal
     */
    public AlbinaFactory(RefLinks refLink,
                         int x, int y,
                         int patrolLeft, int patrolRight,
                         Hero hero) {
        this.refLink = refLink;
        this.x = x;
        this.y = y;
        this.patrolLeft  = patrolLeft;
        this.patrolRight = patrolRight;
        this.hero = hero;
    }

    /**
     * Metodă suprascrisă din EnemyFactory.
     * Creează și returnează o instanță de Albina.
     */
    @Override
    public Enemy createEnemy() {
        return new Albina(refLink, x, y,
                patrolLeft, patrolRight, hero);
    }
}
