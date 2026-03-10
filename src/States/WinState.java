package States;

// Importă gestionarea mouse-ului și contextul jocului
import Input.MouseManager;
import PaooGame.RefLinks;

// Importă clase grafice
import java.awt.*;
import java.awt.image.BufferedImage;
import Graphics.ImageLoader;

/**
 * Clasa WinState – reprezintă ecranul de victorie afișat la finalul jocului.
 * Afișează un mesaj „YOU WIN!” și un buton pentru revenirea la meniul principal.
 */
public class WinState extends State {

    private Rectangle mainMenuButtonBounds; // zona clicabilă pentru butonul "Main Menu"
    private String winMessage = "YOU WIN!"; // mesajul afișat
    private String buttonText = "Main Menu"; // textul din buton
    private int windowWidth = 1440;  // lățimea ferestrei
    private int windowHeight = 800;  // înălțimea ferestrei
    private BufferedImage background;  // imaginea de fundal

    /**
     * Constructorul – setează starea și încarcă fundalul.
     * @param refLink referință către contextul jocului
     */
    public WinState(RefLinks refLink) {
        super(refLink);

        // Încarcă imaginea de fundal din resurse
        background = ImageLoader.LoadImage("/textures/menuFundal.png");

        // Setează poziția și dimensiunea butonului
        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (windowWidth - buttonWidth) / 2;
        int buttonY = 400;
        mainMenuButtonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
    }

    /**
     * Actualizează logica stării – verifică dacă s-a dat click pe butonul "Main Menu".
     */
    @Override
    public void Update() {
        MouseManager mouse = refLink.GetMouseManager();

        if (mouse.isLeftClicked()) {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();

            // Dacă s-a dat click în interiorul butonului, mergem la meniul principal
            if (mainMenuButtonBounds.contains(mouseX, mouseY)) {
                mouse.resetClick(); // resetăm click-ul pentru a nu repeta acțiunea
                State.SetState(new MenuState(refLink));
            } else {
                // Dacă s-a dat click pe altceva, tot îl resetăm
                mouse.resetClick();
            }
        }
    }

    /**
     * Desenează fundalul, mesajul de câștig și butonul pentru revenire la meniu.
     */
    @Override
    public void Draw(Graphics g) {
        // Desenează imaginea de fundal dacă există
        if (background != null) {
            g.drawImage(background, 0, 0, windowWidth, windowHeight, null);
        }

        // Creează un overlay semi-transparent peste fundal
        Graphics2D g2d = (Graphics2D) g;
        Color overlay = new Color(0, 0, 0, 150); // negru cu 60% opacitate
        g2d.setColor(overlay);
        g2d.fillRect(0, 0, windowWidth, windowHeight);

        // Desenează mesajul „YOU WIN!” centrat
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(winMessage);
        int textX = (windowWidth - textWidth) / 2;
        int textY = 300;
        g.drawString(winMessage, textX, textY);

        // Desenează butonul gri
        g.setColor(Color.GRAY);
        g.fillRect(mainMenuButtonBounds.x, mainMenuButtonBounds.y,
                mainMenuButtonBounds.width, mainMenuButtonBounds.height);

        // Desenează textul „Main Menu” centrat în buton
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics buttonFontMetrics = g.getFontMetrics();
        int buttonTextWidth = buttonFontMetrics.stringWidth(buttonText);
        int buttonTextX = mainMenuButtonBounds.x +
                (mainMenuButtonBounds.width - buttonTextWidth) / 2;
        int buttonTextY = mainMenuButtonBounds.y +
                (mainMenuButtonBounds.height + buttonFontMetrics.getAscent()) / 2 - 5;

        g.drawString(buttonText, buttonTextX, buttonTextY);
    }
}
