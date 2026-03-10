package States;

// Importuri necesare pentru funcționare
import PaooGame.RefLinks;
import PaooGame.Game;
import Input.MouseManager;
import Graphics.WoodButton;
import java.awt.image.BufferedImage;
import Graphics.Assets;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Clasa MenuState – reprezintă meniul principal al jocului.
 * Afișează opțiunile: START GAME, LOAD și EXIT, fiecare cu funcționalitate proprie.
 */
public class MenuState extends State {

    // Etichetele butoanelor din meniu
    private static final String[] ITEMS = {
            "START GAME", "LOAD", "EXIT"
    };

    // Zonele clicabile pentru fiecare buton
    private final Rectangle2D[] bounds = new Rectangle2D[ITEMS.length];
    private boolean boundsReady = false; // pentru a evita regenerarea zonelor

    // Fundalul grafic
    private BufferedImage menuBackground;

    // Fontul folosit pentru etichetele butoanelor
    private final Font uiFont = new Font("Press Start 2P", Font.PLAIN, 16);

    /**
     * Constructorul – setează referința jocului.
     * @param refLink contextul general al jocului
     */
    public MenuState(RefLinks refLink) {
        super(refLink);
    }

    /**
     * Actualizează logica meniului – verifică dacă s-a făcut click pe vreun buton.
     */
    @Override
    public void Update() {
        MouseManager mm = refLink.GetMouseManager();

        if (mm.isLeftClicked()) {
            for (int i = 0; i < bounds.length; i++)
                if (bounds[i] != null && bounds[i].contains(mm.getX(), mm.getY()))
                    execute(i); // execută acțiunea pentru butonul apăsat
        }

        mm.resetClick(); // evită dublu-click-uri
    }

    /**
     * Execută acțiunea asociată fiecărui buton din meniu.
     * @param idx indexul butonului apăsat
     */
    private void execute(int idx) {
        switch (idx) {
            case 0 -> { // START GAME
                Game game = refLink.GetGame();
                State.SetState(new PlayState(refLink, true)); // pornește jocul de la zero
                game.getCanvas().requestFocus(); // redă focusul la tastatură
            }
            case 1 -> { // LOAD GAME
                Game game = refLink.GetGame();
                PlayState loaded = new PlayState(refLink, false); // creează un joc fără resetare
                loaded.loadGame(); // încarcă starea salvată
                State.SetState(loaded); // setează noua stare
                game.getCanvas().requestFocus();
            }
            case 2 -> System.exit(0); // EXIT
        }
    }

    /**
     * Desenează fundalul, titlul și butoanele meniului.
     * @param g contextul grafic
     */
    @Override
    public void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        drawBackground(g2);  // desenează imaginea de fundal
        drawTitle(g2);       // desenează titlul de sus

        int centerX = refLink.GetWidth() / 2; // centru orizontal
        int startY  = 250;                    // poziția butonului de sus
        int gap     = 30;                     // spațiul între butoane

        MouseManager mm = refLink.GetMouseManager();

        // Desenează fiecare buton și setează zona de click
        for (int i = 0; i < ITEMS.length; i++) {
            int y = startY + i * (WoodButton.HEIGHT + gap);

            boolean hover = new Rectangle(centerX - WoodButton.WIDTH / 2, y,
                    WoodButton.WIDTH, WoodButton.HEIGHT)
                    .contains(mm.getX(), mm.getY());

            Rectangle r = WoodButton.draw(g, centerX, y, ITEMS[i], hover, uiFont);
            if (!boundsReady) bounds[i] = r;
        }

        boundsReady = true; // setăm o singură dată zonele de click
    }

    /**
     * Desenează titlul jocului în stil pixel-art.
     * @param g2 contextul grafic 2D
     */
    private void drawTitle(Graphics2D g2) {
        final String txt = "The race of the fearless adventurer";
        final String txt1 = "~Welcome~";

        Font font = new Font("Monospaced", Font.BOLD, 45); // font pixel-art
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int x = (refLink.GetWidth() - fm.stringWidth(txt1)) / 2;
        int y = 150;
        int w = (refLink.GetWidth() - fm.stringWidth(txt)) / 2;
        int z = 200;

        // contur vectorial pentru text
        Shape shape1 = font.createGlyphVector(g2.getFontRenderContext(), txt1).getOutline(x, y);
        Shape shape  = font.createGlyphVector(g2.getFontRenderContext(), txt).getOutline(w, z);

        // gradient galben-portocaliu
        g2.setPaint(new GradientPaint(x, y - 32, new Color(244, 199, 71),
                x, y,       new Color(210, 128, 28)));
        g2.fill(shape1);

        g2.setPaint(new GradientPaint(w, z - 32, new Color(244, 199, 71),
                w, z,       new Color(210, 128, 28)));
        g2.fill(shape);

        // contur negru gros
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        g2.draw(shape1);
        g2.draw(shape);
    }

    /**
     * Desenează fundalul meniului.
     * @param g contextul grafic
     */
    private void drawBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // fundal negru de bază
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, refLink.GetWidth(), refLink.GetHeight());

        // imaginea de fundal definită în Assets
        BufferedImage bg = Assets.menuBg;
        g2.drawImage(bg, 0, 0, refLink.GetWidth(), refLink.GetHeight(), null);
    }
}
