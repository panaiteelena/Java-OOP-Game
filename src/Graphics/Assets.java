package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import PaooGame.RefLinks;
import Tiles.Tile;
import Tiles.greenPlant4Tile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

/*! \class public class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.

    Game assets include tot ce este folosit intr-un joc: imagini, sunete, harti etc.
 */
public class Assets
{

    private static BufferedImage rotateImage(BufferedImage image, double angleDegrees) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, image.getType());
        Graphics2D g2d = rotated.createGraphics();
        g2d.rotate(Math.toRadians(angleDegrees), w / 2.0, h / 2.0);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotated;
    }
    /// Referinte catre elementele grafice (dale) utilizate in joc.
    public static BufferedImage rockBrown;
    public static BufferedImage plant;
    public static BufferedImage littleRock;
    public static BufferedImage door1;
    public static BufferedImage door2;
    public static BufferedImage door3;
    public static BufferedImage door4;
    public static BufferedImage door5;
    public static BufferedImage door6;
    public static BufferedImage door7;
    public static BufferedImage door8;
    public static BufferedImage door9;
    public static BufferedImage water;
    public static BufferedImage heroRight;
    public static BufferedImage heroLeft;
    public static BufferedImage heroJump;
    public static BufferedImage talibanRight;
    public static BufferedImage talibanLeft;
    public static BufferedImage talibanSefRight;
    public static BufferedImage talibanSefLeft;
    public static BufferedImage albinaRight;
    public static BufferedImage albinaLeft;
    public static BufferedImage furnica;
    public static BufferedImage obstacol;
    public static BufferedImage arrowRight;
    public static BufferedImage arrowLeft;
    public static BufferedImage bulletRight;
    public static BufferedImage bulletLeft;
    public static BufferedImage black;
    public static BufferedImage menuBg;
    public static BufferedImage worldBackground1;
    public static BufferedImage worldBackground2;
    public static BufferedImage worldBackground3;
    public static BufferedImage grassGroundMiddle;
    public static BufferedImage grassGroundLeft;
    public static BufferedImage grassGroundRight;
    public static BufferedImage simpleGround;
    public static BufferedImage ground;
    public static BufferedImage treeLimb1;
    public static BufferedImage treeLimb2;
    public static BufferedImage treeLimb3;
    public static BufferedImage treeLimb4;
    public static BufferedImage treeLimb5;
    public static BufferedImage treeLimb6;
    public static BufferedImage treeLimb7;
    public static BufferedImage treeLimb8;
    public static BufferedImage purplePlant1;
    public static BufferedImage purplePlant2;
    public static BufferedImage purplePlant3;
    public static BufferedImage purplePlant4;
    public static BufferedImage greenPlant1;
    public static BufferedImage greenPlant2;
    public static BufferedImage greenPlant3;
    public static BufferedImage greenPlant4;
    public static BufferedImage spike;
    public static BufferedImage lava;
    public static BufferedImage spikeRotated90;
    public static BufferedImage spikeRotated180;
    public static BufferedImage fullHeart;
    public static BufferedImage emptyHeart;
    public static BufferedImage pauseButton;
    public static BufferedImage heroIdle;
    public static BufferedImage coin;
    public static BufferedImage artefact;

    // fisierele audio pentru muzica
    public static Clip levelMusic1, levelMusic2, levelMusic3;

    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()

    {
        /// Incarcarea fisierlor pentru muzica
        levelMusic1 = loadClip("/audio/level1_music.wav");
        levelMusic2 = loadClip("/audio/level2_music.wav");
        levelMusic3 = loadClip("/audio/level3_music.wav");

        fullHeart = ImageLoader.LoadImage("/textures/redHeart.png");
        emptyHeart = ImageLoader.LoadImage("/textures/heartDead.png");
        pauseButton = ImageLoader.LoadImage("/textures/pause_icon.png");
        spike = ImageLoader.LoadImage("/textures/Spike_Pixel.png");
        lava = ImageLoader.LoadImage("/textures/lava.png");
        coin = ImageLoader.LoadImage("/textures/coin.png");


        /// Incarcarea imaginii de fundal pentru meniu
        menuBg = ImageLoader.LoadImage("/textures/menuFundal.png");

        /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/PaooGameSpriteSheet.png"),32,32);
        SpriteSheet sheet1 = new SpriteSheet(ImageLoader.LoadImage("/textures/tilesheet_basic.png"),32,32);
        SpriteSheet sheet2 = new SpriteSheet(ImageLoader.LoadImage("/textures/tile_jungle_ground_brown.png"),32,32);
        SpriteSheet sheet3 = new SpriteSheet(ImageLoader.LoadImage("/textures/tile_jungle_slopes_brown.png"),32,32);
        SpriteSheet sheet4 = new SpriteSheet(ImageLoader.LoadImage("/textures/tile_jungle_treelimb.png"),32,32);
        SpriteSheet sheet6 = new SpriteSheet(ImageLoader.LoadImage("/textures/sprite_sheet_32x32.png"),32,32);
        SpriteSheet sheet7 = new SpriteSheet(ImageLoader.LoadImage("/textures/tile_jungle_plants_objects.png"),32,32);
        SpriteSheet talibanSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/taliban_spritesheet.png"),48,51); //spritesheet taliban
        SpriteSheet talibanSefSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/talibanSef_spritesheet.png"), 32, 32);
        SpriteSheet albinaSheet = new SpriteSheet(ImageLoader.LoadImage(("/textures/albina_spritesheet.png")),32,32); //spritesheet albina
        SpriteSheet furnicaSprite = new SpriteSheet(ImageLoader.LoadImage(("/textures/furnica_sprite.png")),32,32);
        SpriteSheet artefactSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/chest2.png"),32,32);
        SpriteSheet arrowSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/arrow_spritesheet.png"), 32, 32);
        SpriteSheet bulletSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/bullet_spritesheet.png"),32, 32);
        worldBackground2 = ImageLoader.LoadImage("/textures/harta2.png"); // asigură-te că ai imaginea în resurse
        worldBackground1 = ImageLoader.LoadImage("/textures/fundal.png"); // asigură-te că ai imaginea în resurse
        worldBackground3 = ImageLoader.LoadImage("/textures/harta3.png");
        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        littleRock = sheet.crop(3, 3);
        rockBrown = sheet.crop(6, 2);
        water = sheet1.crop(13, 1);
        plant=sheet.crop(4,12);
        obstacol = sheet.crop(5,11);
        door1=sheet.crop(0,8);
        door2=sheet.crop(1,8);
        door3=sheet.crop(2,8);
        door4=sheet.crop(5,9);
        door5=sheet.crop(6,9);
        door6=sheet.crop(7,9);
        door7=sheet.crop(5,12);
        door8=sheet.crop(6,12);
        door9=sheet.crop(7,12);
        heroRight = sheet.crop(4,14);
        heroLeft = sheet6.crop(6,0);
        heroJump = sheet.crop(7,14);
        heroIdle = sheet.crop(6,14);
        artefact = artefactSheet.crop(0,0);
        talibanRight = talibanSheet.crop(2,0);
        talibanLeft = talibanSheet.crop(6,1);
        talibanSefRight = talibanSefSheet.crop(1,0);
        talibanSefLeft = talibanSefSheet.crop(0,0);
        albinaRight = albinaSheet.crop(0,0);
        albinaLeft = albinaSheet.crop(1,0);
        furnica = furnicaSprite.crop(0,0);
        arrowRight = arrowSheet.crop (1, 0);
        arrowLeft = arrowSheet.crop (0, 0);
        bulletRight = bulletSheet.crop (1,0);
        bulletLeft = bulletSheet.crop (0,0);
        black = sheet.crop(6,17);
        grassGroundMiddle = sheet2.crop(1,1);
        grassGroundLeft = sheet2.crop(0,1);
        grassGroundRight = sheet2.crop(2,1);
        simpleGround = sheet2.crop(1, 5);
        ground = sheet3.crop(4,2);
        treeLimb1 = sheet4.crop(5, 1);
        treeLimb2 = sheet4.crop(6, 1);
        treeLimb3 = sheet4.crop(7, 1);
        treeLimb4 = sheet4.crop(8, 1);
        treeLimb5 = sheet4.crop(5, 2);
        treeLimb6 = sheet4.crop(6, 2);
        treeLimb7 = sheet4.crop(8, 2);
        treeLimb8 = sheet4.crop(7, 2);
        purplePlant1 = sheet7.crop(8, 0);
        purplePlant2 = sheet7.crop(9, 0);
        purplePlant3 = sheet7.crop(8, 1);
        purplePlant4 = sheet7.crop(9, 1);
        greenPlant1 = sheet7.crop(4, 2);
        greenPlant2 = sheet7.crop(5, 2);
        greenPlant3 = sheet7.crop(4, 3);
        greenPlant4 = sheet7.crop(5, 3);
        spikeRotated180 = rotateImage(spike, 180);
        spikeRotated90 = rotateImage(spike, 270);

    }
    /**
     * Încarcă un fișier audio (.wav) și returnează un obiect Clip care poate fi redat.
     *
     * @param path calea către fișierul audio, relativă la resursele din proiect (ex: "/sounds/jump.wav")
     * @return un obiect Clip gata de redare sau null dacă a apărut o eroare
     */
    private static Clip loadClip(String path) {
        try (
                // Deschide un flux audio din fișierul specificat (încărcat din resursele proiectului)
                AudioInputStream in = AudioSystem.getAudioInputStream(Assets.class.getResource(path))
        ) {
            // Creează un obiect Clip (un sunet scurt ce poate fi redat/întrerupt)
            Clip clip = AudioSystem.getClip();

            // Deschide clip-ul cu datele audio din fluxul încărcat
            clip.open(in);

            // Returnează clip-ul gata de redare
            return clip;
        } catch (Exception e) {
            // Dacă apare o eroare (fișier inexistent, format greșit etc.), se afișează eroarea în consolă
            e.printStackTrace();
            return null;
        }
    }

}