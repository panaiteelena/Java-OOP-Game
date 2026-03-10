package FactoryMethod;

import java.awt.*;

/**
 * Interfața Enemy definește comportamentul de bază
 * pentru toate tipurile de inamici din joc.
 * Toate clasele care reprezintă inamici trebuie să o implementeze.
 */
public interface Enemy {

    /**
     * Actualizează starea inamicului (poziție, logică, etc.).
     * Este apelată la fiecare ciclu de actualizare al jocului.
     */
    void Update();

    /**
     * Desenează inamicul pe ecran.
     *
     * @param g Obiectul Graphics folosit pentru desenare.
     */
    void Draw(Graphics g);

    /**
     * Returnează dreptunghiul de coliziune al inamicului.
     *
     * @return Rectangle care definește zona ocupată de inamic.
     */
    Rectangle GetBounds();

    /**
     * Returnează poziția verticală (Y) a inamicului.
     *
     * @return coordonata Y (float).
     */
    float GetY();

    /**
     * Returnează poziția orizontală (X) a inamicului.
     *
     * @return coordonata X (float).
     */
    float GetX();

    /**
     * Returnează numărul de vieți (sau puncte de viață) al inamicului.
     *
     * @return viața curentă a inamicului (int).
     */
    int GetLife();
}
