package Items;

import Graphics.Assets;
import PaooGame.RefLinks;
import Tiles.Tile;
import FactoryMethod.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Boss static – „Taliban-Şef”.
 *    • stă pe loc (xMove = 0)
 *    • cade/gravitaţie până stă pe sol
 *    • trage săgeţi orizontal spre erou la un interval fix
 */
public class TalibanSef extends Character implements Enemy{
    /* === Constante mişcare === */
    private static final float GRAVITY        = 0.5f;
    private static final float MAX_FALL_SPEED = 10f;

    /* === Constante atac === */
    private static final long  SHOOT_COOLDOWN = 1200; // ms între focuri
    private static final int   SHOOT_TOL_Y    = 32;   // px toleranţă verticală

    /* === Sprite-uri === */
    private BufferedImage imgLeft  = Assets.talibanSefLeft;
    private BufferedImage imgRight = Assets.talibanSefRight;

    /* === Stare === */
    private float yVel = 0;
    private long lastShotTime = 0;

    private final Hero hero;
    private final List<Arrow> arrowPool;

    public TalibanSef(RefLinks refLink,
                      int spawnX,
                      int spawnY,
                      Hero hero,
                      List<Arrow> arrowPool) {

        // folosim fix aceleaşi dimensiuni ca Taliban (29×34) – dacă diferă, ajustează
        super(refLink, spawnX, spawnY, 29, 34);

        this.hero      = hero;
        this.arrowPool = arrowPool;

        // bounding box pe tot sprite-ul
        normalBounds.setBounds(0, 0, width, height);
        attackBounds.setBounds(normalBounds);

    }

    /* ======================================================================= */
    /*                         UPDATE & DRAW                                   */
    /* ======================================================================= */
    @Override
    public void Update() {
        applyGravity();  // mişcare verticală
        checkShoot();    // trage dacă poate
    }

    @Override
    public void Draw(Graphics g) {
        BufferedImage img = (hero.GetX() < x) ? imgLeft : imgRight;
        g.drawImage(
                img,
                (int) (x - refLink.GetGameCamera().getXOffset()),
                (int) (y - refLink.GetGameCamera().getYOffset()),
                width, height, null
        );

        /* // DEBUG bounding-box
        g.setColor(Color.RED);
        g.drawRect((int)(x - refLink.GetGameCamera().getXOffset()),
                   (int)(y - refLink.GetGameCamera().getYOffset()),
                   width, height);
        */
    }

    /* ======================================================================= */
    /*                         LOGICĂ INTERNĂ                                  */
    /* ======================================================================= */

    /** Gravitaţie + coliziune simplificată cu solul (similar cu Taliban). */
    private void applyGravity() {
        // nu ne deplasăm pe X
        yVel = Math.min(yVel + GRAVITY, MAX_FALL_SPEED);
        yMove = yVel;
        MoveY(); // foloseşte metoda din Character

        // dacă avem tile solid imediat sub picioare, ne „lipim”
        int tileBelowY  = (int) ((y + height) / Tile.TILE_HEIGHT);
        int leftTileX   = (int) ((x + bounds.x) / Tile.TILE_WIDTH);
        int rightTileX  = (int) ((x + bounds.x + bounds.width) / Tile.TILE_WIDTH);
        boolean solidBelow = false;
        for (int tx = leftTileX; tx <= rightTileX; tx++) {
            if (refLink.GetMap().GetTile(tx, tileBelowY).IsSolid()) {
                solidBelow = true;
                break;
            }
        }
        if (solidBelow) {
            y = tileBelowY * Tile.TILE_HEIGHT - height;
            yVel = 0;
        }
    }



    /** Dacă targetul este pe linie (± SHOOT_TOL_Y) şi cooldown-ul a expirat, adaugă o săgeată în listă. */
    private void checkShoot() {
        if (System.currentTimeMillis() - lastShotTime < SHOOT_COOLDOWN) return;

        int centerHeroY = (int)(hero.GetY()) + (int)(hero.GetHeight() / 2);
        int centerBossY = (int) (y + height / 2);
        if (Math.abs(centerHeroY - centerBossY) > SHOOT_TOL_Y) return;

        int dir   = (hero.GetX() < x) ? Arrow.LEFT : Arrow.RIGHT;
        float sx  = (dir == Arrow.LEFT) ? x - 6 : x + width;
        float sy  = y + height / 2 - 2;

        arrowPool.add(new Arrow(refLink, sx, sy, dir));
        lastShotTime = System.currentTimeMillis();
    }
}