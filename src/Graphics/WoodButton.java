package Graphics;

import java.awt.*;

/**
 * Clasa WoodButton – reprezintă un buton grafic în stil pixel-art, de tip lemn.
 * Oferă o metodă statică pentru desenarea unui buton și întoarcerea zonei clicabile (Rectangle).
 */
public class WoodButton {

    // Dimensiunile standard ale butonului (în pixeli)
    public static final int WIDTH  = 220;
    public static final int HEIGHT = 48;

    /**
     * Desenează butonul pe ecran și returnează zona de coliziune (dreptunghiul clicabil).
     *
     * @param g       contextul grafic pe care se desenează
     * @param centerX poziția X a centrului butonului
     * @param y       poziția Y de sus a butonului
     * @param text    textul afișat pe buton
     * @param hover   dacă este mouse-ul deasupra butonului (pentru efect vizual)
     * @param font    fontul folosit pentru text
     * @return        dreptunghiul (Rectangle) care acoperă zona clicabilă a butonului
     */
    public static Rectangle draw(Graphics g, int centerX, int y,
                                 String text, boolean hover, Font font) {

        // Culoarea de umplere – imită o textură de lemn
        Color fill   = new Color(219, 173, 103);      // culoarea de bază (lemn)
        Color border = new Color( 99,  50,   6);      // culoarea conturului (mai închis)
        Color fillHover = fill.brighter();            // versiune mai deschisă când mouse-ul este deasupra

        // Calculează colțul stânga-sus al butonului pe baza centrului
        int x = centerX - WIDTH / 2;

        // Desenează umplerea butonului (hover sau normal)
        g.setColor(hover ? fillHover : fill);
        g.fillRect(x, y, WIDTH, HEIGHT);

        // Desenează contur gros de 3 pixeli
        g.setColor(border);
        for(int i = 0; i < 3; i++)
            g.drawRect(x + i, y + i, WIDTH - 1 - 2*i, HEIGHT - 1 - 2*i);

        // Setează fontul și culoarea textului
        g.setFont(font);
        g.setColor(Color.BLACK);

        // Calculează poziția textului pentru a fi centrat în interiorul butonului
        FontMetrics fm = g.getFontMetrics();
        int tx = centerX - fm.stringWidth(text) / 2;                       // text centrat pe orizontală
        int ty = y + (HEIGHT + fm.getAscent() - fm.getDescent()) / 2;     // text centrat pe verticală

        // Desenează textul
        g.drawString(text, tx, ty);

        // Returnează dreptunghiul butonului pentru verificarea coliziunii cu mouse-ul
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
