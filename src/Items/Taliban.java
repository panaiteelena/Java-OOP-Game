package Items;

import Graphics.Assets;
import PaooGame.RefLinks;
import Tiles.Tile;
import FactoryMethod.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clasa Taliban definește un inamic care patrulează orizontal între două limite
 * și este afectat de gravitație. Se poate adăuga logică de coliziune cu eroul.
 */
public class Taliban extends Character implements Enemy {

    // === Constante pentru gravitație ===
    private static final float GRAVITY = 0.5f;          // accelerația gravitațională
    private static final float MAX_FALL_SPEED = 10f;    // limită superioară pentru cădere

    // === Sprite curent ===
    private BufferedImage image;

    // === Parametri patrulare orizontală ===
    private final int patrolLeft;     // limita stângă în pixeli
    private final int patrolRight;    // limita dreaptă în pixeli
    private boolean movingRight = true; // direcția de mers (true = dreapta, false = stânga)

    // === Mișcare verticală ===
    private float yVelocity = 0;
    private boolean onGround = false;

    // === Referință la erou pentru coliziune (dacă se dorește) ===
    private final Hero hero;

    /**
     * Constructor pentru un Taliban care se mișcă între două capete.
     *
     * @param refLink     contextul jocului (motorul)
     * @param spawnX      poziția de pornire X (în pixeli)
     * @param spawnY      poziția de pornire Y (în pixeli)
     * @param patrolLeft  limita stângă a patrulării
     * @param patrolRight limita dreaptă a patrulării
     * @param hero        referință la erou (opțional, pentru coliziune)
     */
    public Taliban(RefLinks refLink, int spawnX, int spawnY, int patrolLeft, int patrolRight, Hero hero) {
        super(refLink, spawnX, spawnY, 29, 34); // dimensiunile personalizate ale sprite-ului
        this.patrolLeft = Math.min(patrolLeft, patrolRight);   // asigură ordonarea
        this.patrolRight = Math.max(patrolLeft, patrolRight);
        this.hero = hero;

        speed = 2.0f; // viteză constantă de deplasare laterală
        image = Assets.talibanRight; // sprite inițial: privind spre dreapta

        // Setăm hitbox-ul să acopere sprite-ul complet
        normalBounds.x = 0;
        normalBounds.y = 0;
        normalBounds.width = width;
        normalBounds.height = height;

        // Copiem aceleași dimensiuni și în attackBounds (dacă avem atacuri mai târziu)
        attackBounds.setBounds(normalBounds);
    }

    /**
     * Actualizează logica inamicului:
     * - Se mișcă orizontal între două limite (patrol)
     * - Cade sub gravitație până atinge solul
     * - Schimbă sprite-ul în funcție de direcție
     */
    @Override
    public void Update() {
        // === Patrulare orizontală ===
        xMove = movingRight ? speed : -speed;
        MoveX();

        // Inversează direcția la capetele patrulării
        if (x <= patrolLeft) {
            x = patrolLeft;
            movingRight = true;
        } else if (x + width >= patrolRight) {
            x = patrolRight - width;
            movingRight = false;
        }

        // === Gravitație: actualizare viteză verticală ===
        yVelocity = Math.min(yVelocity + GRAVITY, MAX_FALL_SPEED);
        yMove = yVelocity;
        MoveY();

        // Verifică dacă există tile solid sub picioare
        int tileBelowY = (int) ((y + height) / Tile.TILE_HEIGHT);
        int leftTileX = (int) ((x + bounds.x) / Tile.TILE_WIDTH);
        int rightTileX = (int) ((x + bounds.x + bounds.width) / Tile.TILE_WIDTH);
        boolean solidBelow = false;

        for (int i = leftTileX; i <= rightTileX; i++) {
            if (refLink.GetMap().GetTile(i, tileBelowY).IsSolid()) {
                solidBelow = true;
                break;
            }
        }

        if (solidBelow) {
            // Aliniază exact la tile și oprește căderea
            y = tileBelowY * Tile.TILE_HEIGHT - height;
            yVelocity = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        // === Actualizează sprite-ul în funcție de direcție ===
        image = movingRight ? Assets.talibanRight : Assets.talibanLeft;

    }

    /**
     * Desenează inamicul pe ecran, ținând cont de poziția camerei.
     *
     * @param g contextul grafic (Graphics) oferit de motor
     */
    @Override
    public void Draw(Graphics g) {
        g.drawImage(image,
                (int) (x - refLink.GetGameCamera().getXOffset()),
                (int) (y - refLink.GetGameCamera().getYOffset()),
                width, height, null);

    }
}
