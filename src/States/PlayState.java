package States;

import DataBase.DatabaseManager;
import FactoryMethod.*;
import Items.*;
import Maps.Map;
import PaooGame.RefLinks;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Tiles.*;

import Graphics.Assets;

import javax.sound.sampled.Clip;

/*! \class public class PlayState extends State
    \brief Implementeaza/controleaza jocul.
 */
public class PlayState extends State {

    private BufferedImage backgroundImage;
    private int currentLevel = 0;
    private boolean gameOver = false;  // Stare pentru Game Over
    private int consecutiveDeaths = 0;
    private final int MAX_DEATHS = 3;
    private boolean lostLife = false;
    private boolean paused = false;
    private ArrayList<Coin> coins;
    private ArrayList<Artefact> artefacts;
    private int score = 0;
    private boolean levelCompleted = false;
    private long levelCompleteTime = 0;
    private final long DISPLAY_DURATION = 2000; // 2 secunde
    private int scoreBeforeLevel; // scorul salvat la începutul nivelului

    private Clip currentMusic;


    private ArrayList<Coin> getCoinsForLevel(int level) {
        ArrayList<Coin> coins = new ArrayList<>();
        return coins;
    }

    // La începutul clasei
    private boolean clickProcessed = false;
    int buttonWidth = 250;
    int buttonHeight = 50;
    int centerX = (refLink.GetWidth() - buttonWidth) / 2;
    int startY = 230;

    private Rectangle pauseButtonBounds = new Rectangle(730, 20, 32, 32); // Mărimea imaginii
    // private boolean paused = false;

    // Dimensiuni și poziții buton pauză (dreapta sus)
    // private Rectangle pauseButtonBounds = new Rectangle(730, 20, 40, 40);  // x, y, width, height

    // Pentru butoanele din meniu pauză
    private Rectangle resumeButton = new Rectangle(centerX, startY, buttonWidth, buttonHeight);
    private Rectangle menuButton = new Rectangle(centerX, startY + 70, buttonWidth, buttonHeight);
    private Rectangle exitButton = new Rectangle(centerX, startY + 140, buttonWidth, buttonHeight);
    private Rectangle saveButton = new Rectangle(centerX, startY + 210, buttonWidth, buttonHeight);


    private String[] mapFiles = {"map1.txt", "map2.txt", "map3.txt"};
    private BufferedImage[] backgrounds = {Assets.worldBackground1, Assets.worldBackground2, Assets.worldBackground3};

    private Hero hero;  /*!< Referinta catre obiectul animat erou (controlat de utilizator).*/
    private Map map;    /*!< Referinta catre harta curenta.*/

    private final ArrayList<Enemy> enemies = new ArrayList<>(); /*!< Lista inamicilor. */

    private ArrayList<Arrow> arrows = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    /*! \fn public PlayState(RefLinks refLink)
        \brief Constructorul de initializare al clasei

        \param refLink O referinta catre un obiect "shortcut", obiect ce contine o serie de referinte utile in program.
     */
    public PlayState(RefLinks refLink, boolean resetGame) {
        ///Apel al constructorului clasei de baza
        super(refLink);
        if (resetGame) {
            // Resetează totul pentru a începe jocul de la început
            currentLevel = 0;
            score = 0;
            consecutiveDeaths = 0;
        }
        ///Construieste harta jocului
        map = new Map(refLink, mapFiles[currentLevel]); // în loc de new Map(refLink)

        ///Referinta catre harta construita este setata si in obiectul shortcut pentru a fi accesibila si in alte clase ale programului.
        refLink.SetMap(map);
        coins = getCoinsForLevel(currentLevel);
        artefacts = new ArrayList<>();
        if (currentLevel == 0) {
            artefacts.add(new Artefact(1000, 250)); // sau poziția corectă
        }
        // play la muzica pentru primul nivel
        playMusic(Assets.levelMusic1);

        ///Construieste eroul
        Hero.resetSingleton();
        hero = Hero.getInstance(refLink, 2 * Tile.TILE_WIDTH, 6 * Tile.TILE_HEIGHT, bullets);
        hero.alignToGround();
        // Clamp pentru camera dupa ce stim cat de mare este aceasta
        int mapPxW = map.GetWidth() * Tile.TILE_WIDTH;
        int mapPxH = map.GetHeight() * Tile.TILE_HEIGHT;
        refLink.GetGameCamera().clamp(mapPxW, mapPxH);

        this.backgroundImage = Assets.worldBackground1;
    }

    // metoda pentru incarcarea muzicii
    private void playMusic(Clip clip) {
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
        currentMusic = clip;
        if (currentMusic != null) {
            currentMusic.setFramePosition(0);
            currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Încarcă toate datele și entitățile pentru un anumit nivel:
     * - harta asociată
     * - fundalul grafic
     * - poziția eroului
     * - lista inamicilor, săgeților și gloanțelor
     * - camera jocului
     * - muzica asociată nivelului
     *
     * @param mapNumber indexul nivelului (0 pentru primul, 1 pentru al doilea etc.)
     */
    private void loadMap(int mapNumber) {

        scoreBeforeLevel = score; // ✅ Salvăm scorul curent pentru a putea reveni la el dacă jucătorul moare

        // === 1. Inițializăm harta și o setăm în shortcut ===
        map = new Map(refLink, mapFiles[mapNumber]);
        refLink.SetMap(map);

        // === 2. Setăm fundalul grafic corespunzător nivelului ===
        backgroundImage = backgrounds[mapNumber];

        // === 3. Actualizăm nivelul curent ===
        currentLevel = mapNumber;

        // === 4. Resetăm poziția eroului (spawn point) ===
        hero.setPosition(0, 100);  // Poziție fixă, aproape de începutul hărții

        // === 5. Golește listele entităților dinamice ===
        enemies.clear();   // inamici
        arrows.clear();    // săgeți trase de inamici
        bullets.clear();   // gloanțe trase de erou

        // === 6. Recalculăm dimensiunea hărții în pixeli și actualizăm camera ===
        int mapPxW = map.GetWidth() * Tile.TILE_WIDTH;
        int mapPxH = map.GetHeight() * Tile.TILE_HEIGHT;
        refLink.GetGameCamera().clamp(mapPxW, mapPxH);  // limitează mișcarea camerei în cadrul hărții

        // === 7. Inițializăm listele de bănuți și artefacte pentru acest nivel ===
        coins = new ArrayList<>();
        artefacts = new ArrayList<>();

        // === 8. Încarcă resursele specifice nivelului ===
        if (currentLevel == 0) {
            artefacts.add(new Artefact(1000, 250));
            // Muzică specifică nivelului 1
            playMusic(Assets.levelMusic1);

        } else if (currentLevel == 1) {
            // Muzică pentru nivelul 2
            playMusic(Assets.levelMusic2);

            coins.add(new Coin(384, 640));  // Mapa 2
            coins.add(new Coin(96, 704));
            coins.add(new Coin(160, 704));
            coins.add(new Coin(160, 512));  // Mapa 2
            coins.add(new Coin(64, 448));
            coins.add(new Coin(480, 480));
            coins.add(new Coin(672, 640));  // Mapa 2
            coins.add(new Coin(1376, 352));
            coins.add(new Coin(1312, 320));
            coins.add(new Coin(896, 416));
            coins.add(new Coin(672, 480));
            coins.add(new Coin(1088, 352));
            coins.add(new Coin(1056, 576));
            coins.add(new Coin(896, 704));  //
            coins.add(new Coin(1290, 640));

            //adaugam inamicii pe nivel
            enemies.add(new FurnicaFactory(refLink, 700, 400, hero).createEnemy());
            enemies.add(new FurnicaFactory(refLink, 1300, 550, hero).createEnemy());

            enemies.add(new AlbinaFactory(refLink, 300, 500, 300, 600, hero).createEnemy());
            enemies.add(new AlbinaFactory(refLink, 700, 400, 700, 1100, hero).createEnemy());

        } else if (currentLevel == 2) {

            playMusic(Assets.levelMusic3);

            coins.add(new Coin(96, 480));  // Mapa 3
            coins.add(new Coin(224, 544));
            coins.add(new Coin(128, 672));
            coins.add(new Coin(384, 640));
            coins.add(new Coin(448, 640));
            coins.add(new Coin(416, 512));
            coins.add(new Coin(800, 480));
            coins.add(new Coin(608, 544));
            coins.add(new Coin(736, 672));
            coins.add(new Coin(1120, 448));
            coins.add(new Coin(832, 704));
            coins.add(new Coin(928, 704));
            coins.add(new Coin(1248, 640));


            //si aici la fel
            enemies.add(new TalibanFactory(refLink, 300, 500, 300, 600, hero).createEnemy());
            enemies.add(new TalibanFactory(refLink, 900, 500, 800, 1000, hero).createEnemy());
            enemies.add(new TalibanFactory(refLink, 1200, 500, 1200, 1400, hero).createEnemy());

            enemies.add(new AlbinaFactory(refLink, 400, 450, 400, 800, hero).createEnemy());
            enemies.add(new AlbinaFactory(refLink, 1000, 500, 1000, 1300, hero).createEnemy());

            enemies.add(new TalibanSefFactory(refLink, 600, 500, hero, arrows).createEnemy());
            enemies.add(new TalibanSefFactory(refLink, 900, 400, hero, arrows).createEnemy());
        }


    }
    /**
     * Reîncarcă nivelul curent fără a reseta progresul general.
     * Utilă când jucătorul pierde o viață, dar nu a ajuns la Game Over.
     */
    private void restartLevel() {
        score = scoreBeforeLevel; // ✅ Restaurăm scorul salvat la începutul nivelului
        lostLife = false;         // resetăm starea de viață pierdută
        loadMap(currentLevel);// reîncărcăm nivelul curent (cu poziție resetată, inamici etc.)
    }

    /**
     * Resetează complet jocul de la începutul primului nivel.
     * Folosită în cazul în care jucătorul a pierdut toate viețile (Game Over).
     */
    private void restartFromBeginning() {
        gameOver = false;         // ieșim din starea de Game Over
        score = 0;                // resetăm scorul total
        consecutiveDeaths = 0;    // resetăm contorul de vieți pierdute
        currentLevel = 0;         // revenim la primul nivel
        loadMap(currentLevel);
        hero.SetLife(3);// reîncărcăm prima hartă
    }


    /*! \fn public void Update()
        \brief Actualizeaza starea curenta a jocului.
     */

    @Override
    public void Update() {

        if (paused) {
            // Gestionăm doar click-urile în meniu când e pauză
            if (refLink.GetMouseManager().isLeftClicked() && !clickProcessed) {
                clickProcessed = true;
                int mx = refLink.GetMouseManager().getX();
                int my = refLink.GetMouseManager().getY();

                if (resumeButton.contains(mx, my)) {
                    if (!currentMusic.isRunning())
                        currentMusic.start(); // pornim din nou muzica cand iesim din meniul de pauza
                    paused = false;
                } else if (menuButton.contains(mx, my)) {
                    State.SetState(new MenuState(refLink));
                } else if (exitButton.contains(mx, my)) {
                    System.exit(0);
                } else if (saveButton.contains(mx, my)) {
                    saveGame();  // Apelează metoda pe care ai implementat-o
                }

                refLink.GetMouseManager().resetClick();
            }

            // Resetează clickProcessed când nu se apasă
            if (!refLink.GetMouseManager().isLeftClicked()) {
                clickProcessed = false;
            }

            return; // <<< === ÎNTRERUPE RESTUL JOCULUI DACĂ E PAUZĂ
        }

        // Oprim muzica cand se termina jocul
        if (gameOver || levelCompleted && currentLevel == mapFiles.length - 1) {
            if (currentMusic != null) currentMusic.stop();
            DatabaseManager.updateHighScore(score);
        }

        if (refLink.GetMouseManager().isLeftClicked() && !clickProcessed) {
            clickProcessed = true;
            int mx = refLink.GetMouseManager().getX();
            int my = refLink.GetMouseManager().getY();

            if (pauseButtonBounds.contains(mx, my)) {
                if (currentMusic != null) currentMusic.stop(); // punem pauza la muzica
                paused = true;
            }

            refLink.GetMouseManager().resetClick();
        }

        if (!refLink.GetMouseManager().isLeftClicked()) {
            clickProcessed = false;
        }


        // restul codului continuă normal doar dacă NU e pauză
        if (gameOver) {
            if (refLink.GetKeyManager().R) {
                restartFromBeginning();
            }
            if (refLink.GetKeyManager().ESC) {
                System.exit(0);
            }
            return;
        }

        if (lostLife) {
            if (refLink.GetKeyManager().R) {
                restartLevel();
            }
            if (refLink.GetKeyManager().ESC) {
                System.exit(0);
            }
            return;
        }
        map.Update();
        hero.Update();

        //Update pentru artefacte
        for (Iterator<Artefact> it = artefacts.iterator(); it.hasNext(); ) {
            Artefact a = it.next();
            if (a.getBounds().intersects(hero.GetBounds())) {
                it.remove(); // artefactul dispare
                System.out.println("Artefact colectat!");
                score += 100; // opțional: crește scorul
            }
        }


        // update pentru inamici + colizune inamic cu erou
        for (Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
            Enemy e = it.next();
            e.Update();
            if (hero.GetBounds().intersects(e.GetBounds())) {
                hero.SetLife(hero.GetLife() - 1);
                consecutiveDeaths++;
                lostLife = true;
                if (consecutiveDeaths >= MAX_DEATHS) {
                    gameOver = true;
                } else {
                    lostLife = true;
                    saveGame();
                }
            }
        }

        // update la sageti + coliziune cu eroul
        for (Iterator<Arrow> it2 = arrows.iterator(); it2.hasNext(); ) {
            Arrow a = it2.next();
            a.Update();

            if (!a.isActive()) {      // a lovit un perete sau a ieşit din cameră
                it2.remove();
                continue;
            }

            if (a.getBounds().intersects(hero.GetBounds())) {
                hero.SetLife(hero.GetLife() - 1);
                consecutiveDeaths++;
                lostLife = true;
                if (consecutiveDeaths >= MAX_DEATHS) {
                    gameOver = true;
                } else {
                    lostLife = true;
                    saveGame();
                }
                it2.remove();
                continue;
            }
        }


        /// update la gloantele trase de jucator si uciderea inamicilor
        for (Iterator<Bullet> it3 = bullets.iterator(); it3.hasNext(); ) {
            Bullet b = it3.next();
            b.Update();

            if (!b.isActive()) {           // a lovit perete / ieşit din ecran
                it3.remove();
                continue;
            }

            for (Iterator<Enemy> itA = enemies.iterator(); itA.hasNext(); ) {
                Enemy e = itA.next();
                if (b.getBounds().intersects(e.GetBounds())) {
                    itA.remove();          // elemina talibanul
                    it3.remove();          // scoate din listă
                    score += 50;
                    break;                 // ieşi din bucla inamicilor
                }
            }

        }


        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            if (hero.GetBounds().intersects(coin.getBounds())) {
                coins.remove(i);
                i--;
                score += 10;
            }
        }


        int heroTileX = hero.getTileX();
        int heroTileY = (int) (hero.getY() / Tile.TILE_HEIGHT);

        Tile tileBelow = map.GetTile(heroTileX, heroTileY);
        if (tileBelow instanceof waterTile || tileBelow instanceof lavaTile || tileBelow instanceof spikeTile || tileBelow instanceof spikeRotated180Tile || tileBelow instanceof obstacolTile || tileBelow instanceof spikeRotated90Tile) {
            hero.SetLife(hero.GetLife() - 1);
            if (hero.GetLife() <= 0) {
                gameOver = true;
            } else {
                lostLife = true;
                saveGame();
            }

        }


        ///Mutarea camerei pe erou
        refLink.GetGameCamera().centerOnEntity(hero);
        refLink.GetGameCamera().clamp(
                map.GetWidth() * Tiles.Tile.TILE_WIDTH,
                map.GetHeight() * Tiles.Tile.TILE_HEIGHT);

        /// Verifică dacă jucătorul a ajuns la capătul hărții
        int heroRightEdge = hero.getX() + hero.GetWidth();
        int mapPixelWidth = map.GetWidth() * Tile.TILE_WIDTH;

        /// Verificare sfârșit nivel
        if (!levelCompleted && heroRightEdge >= mapPixelWidth - 1) {
            if (currentLevel < mapFiles.length - 1) {
                levelCompleted = true;
                levelCompleteTime = System.currentTimeMillis();
            } else {
                State.SetState(new WinState(refLink)); // ← Aici se trece la ecranul de câștig
                return; // dacă e ultimul nivel
            }
        }
         ///verificare daca playerul cade in "groapa"
        if (hero.getY() > 1000 && !lostLife && !gameOver) {
            hero.SetLife(hero.GetLife() - 1);
            if (hero.GetLife() <= 0) {
                gameOver = true;
            } else {
                lostLife = true;
                saveGame();
            }

        }


        if (levelCompleted) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - levelCompleteTime > DISPLAY_DURATION) {
                consecutiveDeaths = 0;
                loadMap(currentLevel + 1);
                hero.SetLife(3);                  // ✅ Resetăm viața
                hero.alignToGround();
                levelCompleted = false;
            }
            return;
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza (randeaza) pe ecran starea curenta a jocului.

        \param g Contextul grafic in care trebuie sa deseneze starea jocului pe ecran.
     */
    @Override
    public void Draw(Graphics g) {
        // === Copie a contextului grafic pentru siguranță (evităm modificarea originalului) ===
        Graphics2D g2 = (Graphics2D) g.create();

        // === Aplicăm zoom-ul camerei ===
        float z = refLink.GetGameCamera().getZoom();
        g2.scale(z, z);  // toate elementele desenate mai jos vor fi scalate

        // === Calculăm ofseturile de poziționare ale camerei ===
        int xOffset = (int) refLink.GetGameCamera().getXOffset();
        int yOffset = (int) refLink.GetGameCamera().getYOffset();

        // === Desenăm fundalul (care acoperă întreaga hartă) ===
        g2.drawImage(backgroundImage,
                -xOffset, -yOffset,
                map.GetWidth() * Tile.TILE_WIDTH,
                map.GetHeight() * Tile.TILE_HEIGHT,
                null);

        // === Desenăm harta și eroul (la coordonate influențate de zoom/ofset) ===
        map.Draw(g2);
        hero.Draw(g2);

        // === Inamicii (toți din listă) ===
        for (Enemy e : enemies) {
            e.Draw(g2);
        }
        // === Proiectilele ===
        for (Arrow t : arrows) {
            t.Draw(g2);
        }
        for (Bullet t : bullets) {
            t.Draw(g2);
        }

        // === Bănuți și artefacte ===
        for (Coin coin : coins) {
            coin.Draw(g2, refLink);
        }
        for (Artefact a : artefacts) {
            a.Draw(g2, refLink);
        }

        // === Ecran Game Over sau pierdere viață ===
        if (gameOver || lostLife) {
            // Fundal de meniu peste ecranul curent
            g.drawImage(Assets.menuBg, 0, 0, refLink.GetWidth(), refLink.GetHeight(), null);

            g2.dispose(); // eliminăm g2 (camera)
            g2 = (Graphics2D) g.create(); // refacem pentru text static

            // Titlu + mesaje
            String title = gameOver ? "Game Over!" : "You lost a life!";
            String retry = "Apasă R pentru a " + (gameOver ? "reporni de la început" : "încerca din nou");
            String exit = "Apasă ESC pentru a ieși";

            // Stiluri text
            Font titleFont = new Font("Arial", Font.BOLD, 40);
            Font infoFont = new Font("Arial", Font.PLAIN, 20);

            FontMetrics titleMetrics = g2.getFontMetrics(titleFont);
            FontMetrics infoMetrics = g2.getFontMetrics(infoFont);
            int centerX = refLink.GetWidth() / 2;

            g2.setColor(Color.WHITE);
            g2.setFont(titleFont);
            g2.drawString(title, centerX - titleMetrics.stringWidth(title) / 2, refLink.GetHeight() / 2 - 50);

            g2.setFont(infoFont);
            g2.drawString(retry, centerX - infoMetrics.stringWidth(retry) / 2, refLink.GetHeight() / 2);
            g2.drawString(exit, centerX - infoMetrics.stringWidth(exit) / 2, refLink.GetHeight() / 2 + 30);
        }

        // === Afișăm scorul ===
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 70);

        g2.dispose();  // închidem contextul de zoom

        // === Desenăm viețile jucătorului (inimi) ===
        int heroLives = hero.GetLife(); // ← viața reală
        for (int i = 0; i < MAX_DEATHS; i++) {
            BufferedImage heartImage = i < heroLives ? Assets.fullHeart : Assets.emptyHeart;
            g.drawImage(heartImage, 20 + i * 40, 20, 32, 32, null);
        }


        // === Buton de pauză ===
        g.drawImage(Assets.pauseButton,
                pauseButtonBounds.x,
                pauseButtonBounds.y,
                pauseButtonBounds.width,
                pauseButtonBounds.height,
                null);

        // === Meniu de pauză ===
        if (paused) {
            // Fundal semi-transparent
            Graphics2D g2d = (Graphics2D) g;
            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, refLink.GetWidth(), refLink.GetHeight());
            g2d.setComposite(originalComposite);

            // Text mare: PAUSE
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            g2d.setColor(Color.WHITE);
            String pauseText = "PAUSE";
            int pauseTextWidth = g2d.getFontMetrics().stringWidth(pauseText);
            g2d.drawString(pauseText, (refLink.GetWidth() - pauseTextWidth) / 2, 150);

            // Butoane: Resume, Menu, Exit, Save
            Font buttonFont = new Font("Arial", Font.PLAIN, 30);
            g2d.setFont(buttonFont);
            int buttonWidth = 250;
            int buttonHeight = 50;
            int centerX = (refLink.GetWidth() - buttonWidth) / 2;
            int startY = 230;

            g2d.setColor(Color.GRAY);
            g2d.fillRect(centerX, startY, buttonWidth, buttonHeight);
            g2d.fillRect(centerX, startY + 70, buttonWidth, buttonHeight);
            g2d.fillRect(centerX, startY + 140, buttonWidth, buttonHeight);
            g2d.fillRect(centerX, startY + 210, buttonWidth, buttonHeight);

            g2d.setColor(Color.WHITE);
            g2d.drawString("Resume", centerX + 70, startY + 35);
            g2d.drawString("Main Menu", centerX + 45, startY + 105);
            g2d.drawString("Exit", centerX + 95, startY + 175);
            g2d.drawString("Save", centerX + 95, startY + 245);
        }

        // === Mesaj de final de nivel ===
        if (levelCompleted) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            String msg = "LEVEL COMPLETED!";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (refLink.GetWidth() - fm.stringWidth(msg)) / 2;
            int y = (refLink.GetHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2d.setColor(Color.BLACK);
            g2d.drawString(msg, x + 3, y + 3);  // umbră

            g2d.setColor(Color.WHITE);
            g2d.drawString(msg, x, y);         // text principal

            g2d.dispose();
        }

        // === Afișăm scorul maxim ===
        int high = DataBase.DatabaseManager.getHighScore();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("High Score: " + high, 20, 100);
    }

    /**
     * Salvează starea curentă a jocului în baza de date:
     * - Nivelul curent, poziția și viața eroului
     * - Inamicii existenți și dacă au fost omorâți
     * - Bănuții colectați
     * - Artefactele colectate
     */
    public void saveGame() {
        try (Connection conn = DataBase.DatabaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM GameState");
            conn.createStatement().execute("DELETE FROM Enemies");
            conn.createStatement().execute("DELETE FROM CollectedCoins");
            conn.createStatement().execute("DELETE FROM CollectedArtefacts");

            PreparedStatement ps = conn.prepareStatement("INSERT INTO GameState(id, level, score, lives, playerX, playerY) VALUES(1, ?, ?, ?, ?, ?)");
            ps.setInt(1, currentLevel);
            ps.setInt(2, score);
            ps.setInt(3, hero.GetLife());
            ps.setFloat(4, hero.GetX());
            ps.setFloat(5, hero.GetY());
            ps.executeUpdate();

            for (Enemy t : enemies) {

                // păstrează doar obiectele TalibanSef
                if (!(t instanceof TalibanSef)) {          // verifica daca este TalibanSef
                    continue;
                }

                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO Enemies(type, x, y, isKilled) VALUES (?, ?, ?, ?)");
                ps2.setString(1, "TalibanSef");
                ps2.setFloat(2, t.GetX());
                ps2.setFloat(3, t.GetY());
                ps2.setInt(4, t.GetLife() <= 0 ? 1 : 0);
                ps2.executeUpdate();
            }

            for (Coin c : getCoinsForLevel(currentLevel)) {
                if (!coins.contains(c)) {
                    PreparedStatement ps3 = conn.prepareStatement("INSERT INTO CollectedCoins(x, y) VALUES (?, ?)");
                    ps3.setInt(1, (int)c.getX());
                    ps3.setInt(2, (int)c.getY());
                    ps3.executeUpdate();
                }
            }

            for (Artefact a : getArtefactsForLevel(currentLevel)) {
                if (!artefacts.contains(a)) {
                    PreparedStatement ps4 = conn.prepareStatement("INSERT INTO CollectedArtefacts(x, y) VALUES (?, ?)");
                    ps4.setInt(1, (int)a.getX());
                    ps4.setInt(2, (int)a.getY());
                    ps4.executeUpdate();
                }
            }

            System.out.println("Joc salvat cu succes.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try (Connection conn = DataBase.DatabaseManager.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM GameState LIMIT 1");
            if (rs.next()) {
                currentLevel = rs.getInt("level");
                float x = rs.getFloat("playerX");
                float y = rs.getFloat("playerY");
                int lives = rs.getInt("lives");
                score = rs.getInt("score");

                // reconstruiește tot nivelul
                loadMap(currentLevel);

                Hero.resetSingleton();
                hero = Hero.getInstance(refLink, x, y, bullets);
                hero.SetLife(lives);
                hero.alignToGround();
            }

            // Scoate monedele colectate
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT * FROM CollectedCoins");
            while (rs2.next()) {
                int cx = rs2.getInt("x");
                int cy = rs2.getInt("y");
                coins.removeIf(c -> (int)c.getX() == cx && (int)c.getY() == cy);
            }

            // Scoate artefactele colectate
            ResultSet rs3 = conn.createStatement().executeQuery("SELECT * FROM CollectedArtefacts");
            while (rs3.next()) {
                int ax = rs3.getInt("x");
                int ay = rs3.getInt("y");
                artefacts.removeIf(a -> (int)a.getX() == ax && (int)a.getY() == ay);
            }

            // TalibanSef încărcați din DB
            enemies.clear();
            ResultSet rs4 = conn.createStatement().executeQuery("SELECT * FROM Enemies");
            while (rs4.next()) {
                String type = rs4.getString("type");
                float x = rs4.getFloat("x");
                float y = rs4.getFloat("y");
                int isKilled = rs4.getInt("isKilled");

                if (type.equals("TalibanSef") && isKilled == 0) {
                    enemies.add(new TalibanSefFactory(refLink, (int)x, (int)y, hero, arrows).createEnemy());
                }


            }

            System.out.println("Joc incarcat cu succes.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Artefact> getArtefactsForLevel(int level) {
        ArrayList<Artefact> list = new ArrayList<>();
        if (level == 0) {
            list.add(new Artefact(1000, 250));
        } else if (level == 1) {
            list.add(new Artefact(1200, 300));
        }
        return list;
    }
}
