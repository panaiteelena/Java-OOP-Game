package Items;

import java.awt.*;
import java.awt.image.BufferedImage;
import Graphics.Assets;
import PaooGame.RefLinks;

/**
 * Clasa Artefact reprezintă un obiect static colectabil din joc (ex: o relicvă sau obiect special).
 */
public class Artefact {
    // Coordonatele poziției artefactului pe hartă
    private int x, y;

    // Dimensiunile artefactului (standard 32x32 pixeli)
    private final int width = 32;
    private final int height = 32;

    // Imaginea grafică a artefactului
    private BufferedImage image;

    /**
     * Constructor care inițializează poziția și imaginea artefactului.
     *
     * @param x poziția X pe hartă
     * @param y poziția Y pe hartă
     */
    public Artefact(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = Assets.artefact; // Imaginea este preluată din clasa Assets
    }

    /**
     * Desenează artefactul pe ecran ținând cont de poziția camerei.
     *
     * @param g obiectul Graphics folosit pentru desenare
     * @param refLink referință la contextul general al jocului (inclusiv camera)
     */
    public void Draw(Graphics g, RefLinks refLink) {
        g.drawImage(image,
                x - (int)refLink.GetGameCamera().getXOffset(), // Ajustează X în funcție de cameră
                y - (int)refLink.GetGameCamera().getYOffset(), // Ajustează Y în funcție de cameră
                width, height, null); // Desenează imaginea la dimensiunea definită
    }

    /**
     * Returnează dreptunghiul de coliziune al artefactului (util pentru detectarea colectării).
     *
     * @return un obiect Rectangle ce reprezintă zona ocupată de artefact
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getter pentru poziția X
    public int getX() { return x; }

    // Getter pentru poziția Y
    public int getY() { return y; }
}
