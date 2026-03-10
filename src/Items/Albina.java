package Items;

import Graphics.Assets;
import PaooGame.RefLinks;
import FactoryMethod.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Inamic zburător „Albina” – patrulează orizontal între două limite fixe\,
 * NU este afectat de gravitație.
 */
public class Albina extends Character implements Enemy {
    // === Sprite‑uri ===
    private BufferedImage image;

    // === Patrulare ===
    private final int patrolLeft;
    private final int patrolRight;
    private boolean movingRight = true;

    // === Referințe ===
    private final Hero hero;

    /**
     * @param refLink     Shortcut engine.
     * @param spawnX      Poziție inițială X (pixeli).
     * @param spawnY      Poziție inițială Y (pixeli) – va rămâne constantă.
     * @param patrolLeft  Limită stângă (pixeli).
     * @param patrolRight Limită dreaptă (pixeli).
     * @param hero        Referință la erou pentru verificarea coliziunii.
     */
    public Albina(RefLinks refLink, int spawnX, int spawnY, int patrolLeft, int patrolRight, Hero hero) {
        super(refLink, spawnX, spawnY, Character.DEFAULT_CREATURE_WIDTH, Character.DEFAULT_CREATURE_HEIGHT);
        this.patrolLeft = Math.min(patrolLeft, patrolRight);
        this.patrolRight = Math.max(patrolLeft, patrolRight);
        this.hero = hero;

        speed = 1.5f;
        image = Assets.albinaRight; // sprite inițial

        // Bounding box = tot sprite‑ul
        normalBounds.x = 0;
        normalBounds.y = 0;
        normalBounds.width = width;
        normalBounds.height = height;
        attackBounds.setBounds(normalBounds);
    }

    @Override
    public void Update() {
        // === Mișcare orizontală ===
        xMove = movingRight ? speed : -speed;
        MoveX();

        // întoarce la capetele patrulei
        if (x <= patrolLeft) {
            x = patrolLeft;
            movingRight = true;
        } else if (x + width >= patrolRight) {
            x = patrolRight - width;
            movingRight = false;
        }

        // Fără gravitație → nu modificăm yMove; rămâne constant
        yMove = 0;

        /*
        // === Coliziune cu eroul === - Inutil deoarece este implementata in PlayState
        if (hero != null && GetBounds().intersects(hero.GetBounds())) {
            hero.SetPosition(0, 100); // exemplu: resetează eroul sau scade viață
        }
         */

        // === Sprite ===
        image = movingRight ? Assets.albinaRight : Assets.albinaLeft;
    }

    @Override
    public void Draw(Graphics g) {
        g.drawImage(image,
                (int) (x - refLink.GetGameCamera().getXOffset()),
                (int) (y - refLink.GetGameCamera().getYOffset()),
                width, height, null);
    }
}