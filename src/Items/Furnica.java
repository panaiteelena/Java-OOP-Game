package Items;

import FactoryMethod.Enemy;
import Graphics.Assets;
import PaooGame.RefLinks;
import Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clasa Furnica reprezintă un inamic static (nu se deplasează orizontal),
 * dar afectat de gravitație – cade până atinge o platformă solidă.
 * Poate interacționa cu eroul dacă dorim (ex: coliziune).
 */
public class Furnica extends Character implements Enemy {

    // === Constante pentru gravitație ===
    private static final float GRAVITY = 0.5f;         // accelerația gravitațională
    private static final float MAX_FALL_SPEED = 10f;   // viteză maximă la cădere

    // === Variabile de stare verticală ===
    private float yVelocity = 0;       // viteza curentă pe axa Y
    private boolean onGround = false;  // dacă furnica se află pe sol

    // === Sprite pentru desenare ===
    private BufferedImage image;

    // === Referință către erou ===
    private final Hero hero; // folosită opțional pentru coliziune/damage

    /**
     * Constructor care setează poziția inițială, imaginea și hitbox-ul furnicii.
     *
     * @param refLink contextul general al jocului
     * @param spawnX poziția X de apariție (pixeli)
     * @param spawnY poziția Y de apariție (pixeli)
     * @param hero referință către erou (pentru coliziune, dacă dorim)
     */
    public Furnica(RefLinks refLink, int spawnX, int spawnY, Hero hero) {
        super(refLink, spawnX, spawnY, Character.DEFAULT_CREATURE_WIDTH, Character.DEFAULT_CREATURE_HEIGHT);
        this.hero = hero;

        image = Assets.furnica; // sprite-ul furnicii

        // Setăm un bounding box mai mic decât sprite-ul pentru coliziune realistă
        normalBounds = new Rectangle(8, 16, 16, 16);
        bounds = normalBounds; // legăm bounds de normalBounds

        // Copiem hitbox-ul și în attackBounds (dacă e nevoie în viitor)
        attackBounds.setBounds(normalBounds);

        // Alternativ, putem folosi un hitbox care acoperă tot sprite-ul:
        /*
        normalBounds.x = 0;
        normalBounds.y = 0;
        normalBounds.width = width;
        normalBounds.height = height;
        */
    }

    /**
     * Actualizează logica furnicii:
     * - Nu se mișcă orizontal
     * - Este afectată de gravitație
     * - Se oprește când atinge o platformă solidă
     * - Verifică coliziunea cu eroul (dacă vrem)
     */
    @Override
    public void Update() {
        // === Mișcare orizontală inexistentă ===
        xMove = 0;
        MoveX(); // Apelăm totuși pentru a evita ieșirea din hartă

        // === Aplicăm gravitația ===
        yVelocity = Math.min(yVelocity + GRAVITY, MAX_FALL_SPEED);
        yMove = yVelocity;
        MoveY();

        // === Verificăm dacă există tile solid sub furnică ===
        int tileBelowY = (int) ((y + height) / Tile.TILE_HEIGHT);
        int leftTileX = (int) ((x + bounds.x) / Tile.TILE_WIDTH);
        int rightTileX = (int) ((x + bounds.x + bounds.width - 1) / Tile.TILE_WIDTH);

        boolean solidBelow = false;
        for (int tx = leftTileX; tx <= rightTileX; tx++) {
            if (refLink.GetMap().GetTile(tx, tileBelowY).IsSolid()) {
                solidBelow = true;
                break;
            }
        }

        // === Dacă există sol, ne oprim pe el ===
        if (solidBelow) {
            y = tileBelowY * Tile.TILE_HEIGHT - height;
            yVelocity = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        // === Interacțiune cu eroul (comentată) ===
        /*
        if (hero != null && GetBounds().intersects(hero.GetBounds())) {
            hero.SetPosition(0, 100); // exemplu: relochează eroul sau aplică damage
        }
        */
    }

    /**
     * Desenează furnica pe ecran în funcție de camera jocului.
     * @param g contextul grafic în care se desenează
     */
    @Override
    public void Draw(Graphics g) {
        g.drawImage(image,
                (int) (x - refLink.GetGameCamera().getXOffset()),
                (int) (y - refLink.GetGameCamera().getYOffset()),
                width, height, null);


    }
}
