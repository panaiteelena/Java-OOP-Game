package Items;

import Graphics.Assets;
import PaooGame.RefLinks;
import Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clasa Bullet reprezintă un proiectil tras (de exemplu, de erou sau inamic).
 * Se deplasează orizontal, dispare când lovește un tile solid sau iese din ecran.
 */
public class Bullet {

    // === Direcții posibile ===
    public static final int LEFT = -1, RIGHT = 1;

    // === Constante de mișcare și dimensiune ===
    private static final float SPEED = 8f;    // viteză constantă a glonțului
    private static final int SIZE  = 32;      // dimensiunea sprite-ului (sprite-ul e 32x32 px)
    private static final int HIT_W  = 10;     // lățimea hitbox-ului (pentru coliziune)
    private static final int HIT_H  = 6;      // înălțimea hitbox-ului

    // Dreptunghi pentru coliziuni
    private final Rectangle bounds = new Rectangle(0, 0, HIT_W, HIT_H);

    // Poziția și direcția glonțului
    private float x, y;
    private final int dir;

    // Dacă glonțul mai este activ (altfel se elimină din joc)
    private boolean active = true;

    // Referință la contextul general al jocului
    private final RefLinks refLink;

    // Sprite-urile pentru fiecare direcție
    private final BufferedImage imgLeft = Assets.bulletLeft;
    private final BufferedImage imgRight = Assets.bulletRight;

    /**
     * Constructorul inițializează poziția și direcția glonțului.
     *
     * @param ref contextul jocului
     * @param spawnX poziția X de lansare
     * @param spawnY poziția Y de lansare
     * @param dir direcția de mișcare (LEFT sau RIGHT)
     */
    public Bullet(RefLinks ref, float spawnX, float spawnY, int dir) {
        if (dir != LEFT  && dir != RIGHT ){
            throw new IllegalArgumentException("Directie invalida pentru glont.");
        }
        this.refLink = ref;
        this.x   = spawnX;
        this.y   = spawnY;
        this.dir = dir;
    }

    /**
     * Actualizează poziția glonțului și verifică dacă trebuie dezactivat.
     */
    public void Update() {
        if (!active) return;

        // Mișcă glonțul în direcția lui
        x += SPEED * dir;

        // Verifică coliziunea cu un tile solid
        if (collisionWithMap()){
            active = false;
            return;
        }

        // Verifică dacă iese din ecran
        if (x + SIZE < 0 || x > refLink.GetGame().GetWidth()) {
            active = false;
        }
    }

    /**
     * Desenează glonțul pe ecran, dacă este activ.
     */
    public void Draw(Graphics g) {
        if (!active) return;

        // Alege sprite-ul în funcție de direcție
        BufferedImage img = (dir == LEFT ) ? imgLeft : imgRight;

        // Ajustează coordonatele în funcție de cameră
        float drawX = x - refLink.GetGameCamera().getXOffset();
        float drawY = y - refLink.GetGameCamera().getYOffset();

        // Desenează sprite-ul la dimensiunea definită
        g.drawImage(img, (int) drawX, (int) drawY, SIZE, SIZE, null);
    }

    /**
     * Verifică dacă glonțul a lovit un tile solid.
     *
     * @return true dacă este coliziune, altfel false
     */
    private boolean collisionWithMap() {
        // Punctul din față al hit-box-ului, în funcție de direcție
        int frontX  = (int) (x + (dir == RIGHT ? HIT_W - 1 : 0));
        int centreY = (int) (y + SIZE / 2); // punctul central pe verticală

        // Tile-ul în care se află acel punct
        int tileX = frontX  / Tile.TILE_WIDTH;
        int tileY = centreY / Tile.TILE_HEIGHT;

        // Verifică dacă acel tile este solid
        return refLink.GetMap().GetTile(tileX, tileY).IsSolid();
    }

    /**
     * Returnează hitbox-ul actualizat al glonțului.
     */
    public Rectangle getBounds() {
        // Pentru RIGHT, hitbox-ul începe la x;
        // Pentru LEFT, îl mutăm la x + (SIZE - HIT_W)
        bounds.x = (int) (x + (dir == RIGHT ? 0 : SIZE - HIT_W));

        // Centrare verticală a hitbox-ului în sprite
        bounds.y = (int) (y + (SIZE - HIT_H) / 2);

        return bounds;
    }

    /**
     * Returnează dacă glonțul este activ (vizibil și periculos).
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Dezactivează glonțul (util când lovește ceva).
     */
    public void deactivate() {
        active = false;
    }
}
