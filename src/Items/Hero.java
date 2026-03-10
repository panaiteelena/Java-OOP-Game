package Items;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import Graphics.Assets;
import PaooGame.RefLinks;
import Tiles.Tile;
import States.PlayState;

/*! \class public class Hero extends Character
    \brief Implementeaza notiunea de erou/player (caracterul controlat de jucator).

    Elementele suplimentare pe care le aduce fata de clasa de baza sunt:
        imaginea (acest atribut poate fi ridicat si in clasa de baza)
        deplasarea
        atacul (nu este implementat momentan)
        dreptunghiul de coliziune
 */
public class Hero extends Character {
    private BufferedImage image;

    private float yVelocity = 0;          // viteză verticală
    private final float GRAVITY = 0.5f;   // gravitație (poți ajusta)
    private final float JUMP_STRENGTH = -8f; // forța de săritură
    private boolean onGround = false;
    private int height = 32;
    private int dir = 1;

    private final List<Bullet> bullets;
    private final long SHOOT_COOLDOWN = 120; // ms intre focuri
    private long lastShotTime = 0;
    private boolean shoot = false;
    // Exemplu de înălțime, modifică în funcție de eroul tău
// e pe sol?
    /*!< Referinta catre imaginea curenta a eroului.*/

    /*! \fn public Hero(RefLinks refLink, float x, float y)
        \brief Constructorul de initializare al clasei Hero.

        \param refLink Referinta catre obiectul shortcut (obiect ce retine o serie de referinte din program).
        \param x Pozitia initiala pe axa X a eroului.
        \param y Pozitia initiala pe axa Y a eroului.
     */

    private static Hero instance;

    public Hero(RefLinks refLink, float x, float y, List<Bullet> bullets) {

        ///Apel al constructorului clasei de baza
        super(refLink, x, y, Character.DEFAULT_CREATURE_WIDTH, Character.DEFAULT_CREATURE_HEIGHT);
        this.bullets = bullets;
        ///Seteaza imaginea de start a eroului
        image = Assets.heroRight;
        ///Stabilieste pozitia relativa si dimensiunea dreptunghiului de coliziune, starea implicita(normala)
        normalBounds.x = 16;
        normalBounds.y = 16;
        normalBounds.width = 16;
        normalBounds.height = 32;

        ///Stabilieste pozitia relativa si dimensiunea dreptunghiului de coliziune, starea de atac
        attackBounds.x = 10;
        attackBounds.y = 10;
        attackBounds.width = 38;
        attackBounds.height = 38;

    }

    /*! \fn public void Update()
        \brief Actualizeaza pozitia si imaginea eroului.
     */

    public static Hero getInstance(RefLinks refLink, float x, float y, List <Bullet> bullets){
        if (instance == null ){
            instance = new Hero (refLink, x, y, bullets);
        }
        return instance;
    }

    public static Hero getInstance() {
        if (instance == null)
            throw new IllegalStateException("Hero nu a fost încă iniţializat!");
        return instance;
    }

    public static void resetSingleton() {
        instance = null;
    }

    public int getTileX() {
        return (int) (x / Tile.TILE_WIDTH);
    }

    public int getHeight() {
        return height;
    }


    public int getY() {
        return (int) this.y;
    }

    public int getX() {
        return (int) this.x;
    }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void alignToGround() {
        int tileX = (int) (x / Tile.TILE_WIDTH);
        int tileY = (int) (y / Tile.TILE_HEIGHT);

        while (!refLink.GetMap().GetTile(tileX, tileY + 1).IsSolid() && tileY < refLink.GetMap().GetHeight()) {
            tileY++;
        }

        y = tileY * Tile.TILE_HEIGHT - height;
        yVelocity = 0;
        onGround = true;
    }

    @Override
    public void Update() {
        ///Verifica daca a fost apasata o tasta
        // Verifica daca a fost apasata o tasta

        int tileBelowY = (int) ((y + height + 1) / Tile.TILE_HEIGHT);
        int tileX = (int) (x / Tile.TILE_WIDTH);

        Tile tileBelow = refLink.GetMap().GetTile(tileX, tileBelowY);
        GetInput();

        // Aplicăm mișcare pe axa X
        MoveX();
        checkShoot();

// Aplica gravitația
        yVelocity += GRAVITY;

// Dacă e pe sol, setăm poziția corectă


        if (tileBelow.IsSolid()) {
            y = tileBelowY * Tile.TILE_HEIGHT - height;
            yVelocity = 0;
            onGround = true;
        } else {
            onGround = false;
        }

// Apasă Space pentru săritură
        if (refLink.GetKeyManager().space && onGround) {
            yVelocity = JUMP_STRENGTH;
            onGround = false;
        }

        float nextY = y + yVelocity;
        boolean collision = false;

        if (yVelocity > 0) { // Cădere
            int startY = (int)((y + height) / Tile.TILE_HEIGHT);
            int endY = (int)((nextY + height) / Tile.TILE_HEIGHT);
            int leftTileX = (int)((x + bounds.x) / Tile.TILE_WIDTH);
            int rightTileX = (int)((x + bounds.x + bounds.width) / Tile.TILE_WIDTH);

            for (int ty = startY; ty <= endY; ty++) {
                for (int i = leftTileX; i <= rightTileX; i++) {
                    Tile tile = refLink.GetMap().GetTile(i, ty);
                    if (tile.IsSolid()) {
                        y = ty * Tile.TILE_HEIGHT - height;
                        yVelocity = 0;
                        onGround = true;
                        collision = true;
                        break;
                    }
                }
                if (collision) break;
            }

            if (!collision) onGround = false;

        } else if (yVelocity < 0) { // Săritură (în sus)
            int topTileY = (int) ((nextY) / Tile.TILE_HEIGHT);
            int leftTileX = (int) ((x + bounds.x) / Tile.TILE_WIDTH);
            int  rightTileX = (int) ((x + bounds.x + bounds.width) / Tile.TILE_WIDTH);

            for (int i = leftTileX; i <= rightTileX; i++) {
                Tile tile = refLink.GetMap().GetTile(i, topTileY);
                if (tile.IsSolid()) {
                    y = (topTileY + 1) * Tile.TILE_HEIGHT;
                    yVelocity = 0;
                    collision = true;
                    break;
                }
            }
        }

// Aplică doar dacă nu ai coliziune
        if (!collision) {
            y = nextY;
        }

// Aplică viteza verticală
        y += yVelocity;
        // Actualizează imaginea eroului în funcție de direcție
        if (!onGround) {
            image = Assets.heroJump; // <- ai nevoie să încarci această imagine în Assets.java
        } else if (refLink.GetKeyManager().right) {
            image = Assets.heroRight;
        } else if (refLink.GetKeyManager().left) {
            image = Assets.heroLeft;
        }
        else {
            image = Assets.heroIdle; // <- imaginea de repaus, trebuie adăugată în Assets
        }


    }

    /*! \fn private void GetInput()
        \brief Verifica daca a fost apasata o tasta din cele stabilite pentru controlul eroului.
     */
    private void GetInput()
    {
        ///Implicit eroul nu trebuie sa se deplaseze daca nu este apasata o tasta
        xMove = 0;
        yMove = 0;
        shoot = false;
        ///Verificare apasare tasta "sus"
        if(refLink.GetKeyManager().up)
        {
            yMove = -speed;
        }
        ///Verificare apasare tasta "jos"
        if(refLink.GetKeyManager().down)
        {
            yMove = speed;
        }
        ///Verificare apasare tasta "left"
        if(refLink.GetKeyManager().left)
        {
            xMove = -speed;
            dir = -1;
        }
        ///Verificare apasare tasta "dreapta"
        if(refLink.GetKeyManager().right)
        {
            xMove = speed;
            dir = 1;
        }

        if(refLink.GetKeyManager().k){
            shoot = true;
        }
    }

    public void SetPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /*! \fn public void Draw(Graphics g)
        \brief Randeaza/deseneaza eroul in noua pozitie.

        \brief g Contextul grafi in care trebuie efectuata desenarea eroului.
     */
    @Override
    public void Draw(Graphics g)
    {
        g.drawImage(image,
                (int)(GetX() - refLink.GetGameCamera().getXOffset()),
                (int)(GetY() - refLink.GetGameCamera().getYOffset()),
                GetWidth(), GetHeight(), null);
        ///doar pentru debug daca se doreste vizualizarea dreptunghiului de coliziune altfel se vor comenta urmatoarele doua linii
        //g.setColor(Color.blue);
        //g.fillRect((int)(x + bounds.x), (int)(y + bounds.y), bounds.width, bounds.height);
    }

    private void checkShoot (){
        if (System.currentTimeMillis() - lastShotTime < SHOOT_COOLDOWN){
            return ;
        }
        if(shoot) {
            float sx = (dir == Bullet.RIGHT) ? x + width  : x - 6;
            float sy = y + height / 2 - 10;          // mijlocul eroului
            bullets.add(new Bullet(refLink, sx, sy, dir));

            lastShotTime = System.currentTimeMillis();
        }
    }

}
