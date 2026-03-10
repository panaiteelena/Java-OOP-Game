package FactoryMethod;

import Items.*;
import PaooGame.RefLinks;
import java.util.List;

/**
 * Creează un inamic de tip Taliban-Șef folosind pattern-ul Factory Method.
 * Această clasă extinde EnemyFactory și este responsabilă de instanțierea inamicului de tip TalibanSef.
 */
public class TalibanSefFactory extends EnemyFactory {
    // Referință la contextul general al jocului (pentru acces la componente precum nivel, cameră, etc.)
    private final RefLinks refLink;

    // Coordonatele inițiale ale inamicului
    private final int x, y;

    // Referință la eroul principal, folosită pentru logica de atac sau urmărire
    private final Hero hero;

    // Lista de săgeți reutilizabile (obiecte de tip Arrow), partajată din PlayState
    private final List<Arrow> arrowPool;

    /**
     * Constructor ce setează toate datele necesare pentru a crea un TalibanSef.
     *
     * @param refLink contextul jocului
     * @param x poziția X a inamicului
     * @param y poziția Y a inamicului
     * @param hero referință către eroul principal
     * @param arrowPool lista partajată de săgeți (pentru atac)
     */
    public TalibanSefFactory(RefLinks refLink,
                             int x, int y,
                             Hero hero,
                             List<Arrow> arrowPool) {
        this.refLink = refLink;
        this.x = x;
        this.y = y;
        this.hero = hero;
        this.arrowPool = arrowPool;
    }

    /**
     * Creează și returnează o instanță de TalibanSef.
     *
     * @return un obiect de tip Enemy (TalibanSef)
     */
    @Override
    public Enemy createEnemy() {
        return new TalibanSef(refLink, x, y, hero, arrowPool);
    }
}
