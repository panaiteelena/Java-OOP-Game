package Items;

import Graphics.Assets;
import PaooGame.RefLinks;
import Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clasa Arrow reprezintă un proiectil simplu (săgeată) tras de un inamic.
 * Se mișcă orizontal și dispare când lovește un tile solid sau iese din cameră.
 */
public class Arrow {

    // === Direcţii posibile ale săgeții ===
    public static final int LEFT  = -1;
    public static final int RIGHT = 1;

    // === Constante pentru mișcare și dimensiuni ===
    private static final float SPEED   = 5f;   // viteză constantă
    private static final int   WIDTH   = 8;    // lățimea reală a săgeții
    private static final int   HEIGHT  = 8;    // înălțimea reală a săgeții

    // === Stare internă a săgeții ===
    private float x, y;        // coordonatele curente
    private final int dir;     // direcția în care se deplasează (LEFT/RIGHT)
    private boolean active = true;  // dacă săgeata este activă sau trebuie eliminată

    // Referință către contextul jocului
    private final RefLinks refLink;

    // Dreptunghi folosit pentru coliziune
    private final Rectangle bounds = new Rectangle(0, 0, WIDTH, HEIGHT);

    // Sprite-uri diferite pentru fiecare direcție
    private final BufferedImage imgLeft  = Assets.arrowLeft;
    private final BufferedImage imgRight = Assets.arrowRight;

    /**
     * Constructorul creează o nouă săgeată cu direcția și poziția specificată.
     *
     * @param refLink contextul jocului
     * @param spawnX poziția X de unde apare săgeata
     * @param spawnY poziția Y de unde apare săgeata
     * @param dir direcția de deplasare (LEFT sau RIGHT)
     */
    public Arrow(RefLinks refLink, float spawnX, float spawnY, int dir) {
        if (dir != LEFT && dir != RIGHT) {
            throw new IllegalArgumentException("Direcţie invalidă pentru săgeată");
        }
        this.refLink = refLink;
        this.x       = spawnX;
        this.y       = spawnY;
        this.dir     = dir;
    }

    /**
     * Actualizează poziția și verifică coliziunile.
     * Dacă săgeata lovește un obstacol sau iese din ecran, devine inactivă.
     */
    public void Update() {
        if (!active) return;

        // Deplasare în direcția aleasă
        x += SPEED * dir;

        // Verifică coliziunea cu harta (tile-uri solide)
        if (collisionWithMap()) {
            active = false;
            return;
        }

        // Dezactivează dacă iese complet din fereastra jocului
        if (x + WIDTH < 0 || x > refLink.GetGame().GetWidth()) {
            active = false;
        }
    }

    /**
     * Desenează săgeata pe ecran, ținând cont de poziția camerei.
     *
     * @param g obiectul Graphics folosit pentru desenare
     */
    public void Draw(Graphics g) {
        if (!active) return;

        // Alege sprite-ul corespunzător direcției
        BufferedImage img = (dir == LEFT) ? imgLeft : imgRight;

        // Convertire la coordonate de ecran (scăzând offset-ul camerei)
        float drawX = x - refLink.GetGameCamera().getXOffset();
        float drawY = y - refLink.GetGameCamera().getYOffset();

        // Desenează săgeata (opțional cu scalare la 32x32 pentru vizibilitate)
        g.drawImage(img, (int) drawX, (int) drawY, 32, 32, null);
    }

    /**
     * Verifică dacă săgeata a lovit un tile solid din hartă.
     *
     * @return true dacă există coliziune, false altfel
     */
    private boolean collisionWithMap() {
        // Calculează tile-ul în care intră partea din față a săgeții
        int tileX = (int) ((x + (dir == RIGHT ? WIDTH : 0)) / Tile.TILE_WIDTH);
        int tileY1 = (int) (y / Tile.TILE_HEIGHT);                        // partea superioară
        int tileY2 = (int) ((y + HEIGHT - 1) / Tile.TILE_HEIGHT);        // partea inferioară

        // Dacă oricare dintre cele două tile-uri este solid, returnează true
        return refLink.GetMap().GetTile(tileX, tileY1).IsSolid()
                || refLink.GetMap().GetTile(tileX, tileY2).IsSolid();
    }

    /**
     * Returnează dreptunghiul de coliziune actualizat al săgeții.
     */
    public Rectangle getBounds() {
        bounds.x = (int) x;
        bounds.y = (int) y;
        return bounds;
    }

    /**
     * Verifică dacă săgeata este activă (vizibilă / periculoasă).
     *
     * @return true dacă este activă, altfel false
     */
    public boolean isActive() {
        return active;
    }
}
