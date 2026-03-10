package Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Clasa abstractă Tile definește comportamentul de bază pentru o dală (element de teren).
 * Este implementată ca Flyweight – fiecare tip de dală are o singură instanță reutilizată.
 */
public abstract class Tile {

    /* ------------------------ 1. Flyweight: Caching ---------------------------- */

    /**
     * Cache-ul static reține o singură instanță per tip de Tile (în funcție de ID).
     * Astfel, dacă cerem același ID de mai multe ori, vom primi aceeași instanță.
     */
    private static final Map<Integer, Tile> cache = new HashMap<>();

    /**
     * Metodă statică de acces – obține Tile-ul asociat unui ID.
     * Dacă nu există încă, îl creează folosind TileFactory.
     *
     * @param id identificatorul unic al dalei
     * @return instanța corespunzătoare tipului de Tile
     */
    public static Tile of(int id) {
        // dacă ID-ul nu există în cache, creează-l cu TileFactory și salvează-l
        return cache.computeIfAbsent(id, TileFactory::create);
    }

    /* ------------------------ 2. Date comune tuturor tipurilor de dale ---------------------------- */

    // Dimensiunea standard a unei dale (în pixeli)
    public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

    // Imaginea grafică a dalei
    protected final BufferedImage img;

    // ID-ul unic asociat acestei dale
    protected final int id;

    /**
     * Constructorul protejat, apelat doar de subclase (ex: GrassTile, StoneTile).
     *
     * @param img imaginea asociată dalei
     * @param id identificatorul acestei dale
     */
    protected Tile(BufferedImage img, int id) {
        this.img = img;
        this.id = id;
    }

    /**
     * Desenează dala pe ecran la poziția (x, y) specificată.
     *
     * @param g contextul grafic
     * @param x poziția X în pixeli
     * @param y poziția Y în pixeli
     */
    public void Draw(Graphics g, int x, int y) {
        g.drawImage(img, x, y, TILE_WIDTH, TILE_HEIGHT, null);
    }

    /**
     * Indică dacă dala este solidă (adică jucătorul sau inamicii nu pot trece prin ea).
     * Implicit returnează false – poate fi suprascrisă în subclase.
     *
     * @return true dacă dala este solidă (ex: piatră), false dacă este traversabilă (ex: iarbă)
     */
    public boolean IsSolid() {
        return false;
    }

    /**
     * Returnează ID-ul unic al dalei.
     */
    public int getId() {
        return id;
    }

    /**
     * Șterge toate instanțele din cache – util la schimbarea hărții sau în testare.
     */
    public static void clearCache() {
        cache.clear();
    }
}
