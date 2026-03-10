package Items;

// Importă imaginea monedei
import Graphics.Assets;
// Referință către contextul jocului (camera, dimensiuni, etc.)
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clasa Coin – reprezintă o monedă colectabilă din joc.
 * Are poziție (x, y), dimensiune fixă și poate fi desenată și colizionată.
 */
public class Coin {

    // Coordonatele monedei în lume (nu pe ecran)
    private float x, y;

    // Dimensiunea fixă a monedei
    private final int width = 25, height = 25;

    /**
     * Constructor – setează poziția inițială a monedei.
     * @param x poziția X în lume
     * @param y poziția Y în lume
     */
    public Coin(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Desenează moneda pe ecran, ținând cont de offsetul camerei.
     * @param g contextul grafic
     * @param refLink referință către camera jocului pentru a ajusta poziția
     */
    public void Draw(Graphics g, RefLinks refLink) {
        Graphics2D g2 = (Graphics2D) g;

        // Calcul offset pentru poziționarea corectă pe ecran
        float xOffset = refLink.GetGameCamera().getXOffset();
        float yOffset = refLink.GetGameCamera().getYOffset();

        // Desenează imaginea monedei la poziția ajustată pe ecran
        g2.drawImage(Assets.coin,
                (int)(x - xOffset), (int)(y - yOffset),  // poziție ajustată
                width, height,                          // dimensiune
                null);
    }

    /**
     * Returnează un dreptunghi (Rectangle) pentru coliziune cu jucătorul.
     * @return zona de coliziune a monedei
     */
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public void collectIfTouched(Hero hero, java.util.List<Coin> coins) {
        Rectangle heroBounds = hero.GetBounds();
        Rectangle coinBounds = getBounds();

        if (heroBounds.intersects(coinBounds)) {
            // 1. Elimină din lista de bănuți
            coins.remove(this);

            // 2. Salvează în baza de date că a fost colectat
            try (java.sql.Connection conn = DataBase.DatabaseManager.getConnection()) {
                java.sql.PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO CollectedCoins(x, y) VALUES (?, ?)"
                );
                ps.setInt(1, (int) this.getX());
                ps.setInt(2, (int) this.getY());
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // (opțional) sunet sau efect
            System.out.println("Monedă colectată la (" + (int)x + "," + (int)y + ")");
        }
    }


    /** @return poziția X a monedei în lume */
    public float getX() { return x; }

    /** @return poziția Y a monedei în lume */
    public float getY() { return y; }
}
