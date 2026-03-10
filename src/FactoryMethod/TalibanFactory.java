package FactoryMethod;

import Items.*;
import PaooGame.RefLinks;

/**
 * Creează un inamic de tip Taliban folosind pattern-ul Factory Method.
 * Clasa extinde EnemyFactory și implementează metoda de creare a unui inamic Taliban.
 */
public class TalibanFactory extends EnemyFactory {
    // Referință la obiectul RefLinks care oferă acces la elementele esențiale ale jocului
    private final RefLinks refLink;

    // Coordonatele poziției inițiale ale inamicului
    private final int x, y;

    // Limitele patrulării orizontale pentru inamic (stânga și dreapta)
    private final int patrolLeft, patrolRight;

    // Referință către eroul principal, pentru a permite interacțiunea cu acesta
    private final Hero hero;

    /**
     * Constructorul inițializează toate datele necesare pentru a crea un Taliban.
     *
     * @param refLink contextul general al jocului
     * @param x poziția X inițială a inamicului
     * @param y poziția Y inițială a inamicului
     * @param patrolLeft limita stângă a zonei de patrulare
     * @param patrolRight limita dreaptă a zonei de patrulare
     * @param hero referință către eroul principal
     */
    public TalibanFactory(RefLinks refLink,
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
     * Metodă suprascrisă care creează și returnează o instanță de Taliban.
     *
     * @return un obiect de tip Enemy (Taliban)
     */
    @Override
    public Enemy createEnemy() {
        return new Taliban(refLink, x, y,
                patrolLeft, patrolRight, hero);
    }
}
