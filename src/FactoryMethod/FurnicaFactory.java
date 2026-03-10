package FactoryMethod;

import Items.*;
import PaooGame.RefLinks;

/**
 * Creează un inamic de tip Furnica folosind Factory Method.
 * Clasa extinde EnemyFactory și implementează metoda pentru a crea obiecte de tip Furnica.
 */
public class FurnicaFactory extends EnemyFactory {
    // Referință la obiectul RefLinks care conține legături spre clasele esențiale ale jocului
    private final RefLinks refLink;

    // Coordonatele de poziționare inițială ale inamicului
    private final int x, y;

    // Referință către eroul principal, necesară pentru logica de interacțiune
    private final Hero hero;

    /**
     * Constructorul inițializează toate datele necesare pentru a crea o Furnica.
     *
     * @param refLink contextul general al jocului
     * @param x poziția X inițială
     * @param y poziția Y inițială
     * @param hero referință către erou, folosită în comportamentul inamicului
     */
    public FurnicaFactory(RefLinks refLink, int x, int y, Hero hero) {
        this.refLink = refLink;
        this.x = x;
        this.y = y;
        this.hero = hero;
    }

    /**
     * Creează și returnează o instanță de Furnica cu datele specificate.
     *
     * @return un obiect de tip Enemy (Furnica)
     */
    @Override
    public Enemy createEnemy() {
        return new Furnica(refLink, x, y, hero);
    }
}
