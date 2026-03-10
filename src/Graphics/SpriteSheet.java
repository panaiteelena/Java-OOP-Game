package Graphics;

import java.awt.image.BufferedImage;
import PaooGame.RefLinks;

/*! \class public class SpriteSheet
    \brief Clasa retine o referinta catre o imagine formata din dale (sprite sheet)

    Metoda crop() returneaza o dala de dimensiuni fixe (o subimagine) din sprite sheet
    de la adresa (x * latimeDala, y * inaltimeDala)
 */
public class SpriteSheet
{
    private BufferedImage       spriteSheet;        /*!< Referinta catre obiectul BufferedImage ce contine sprite sheet-ul.*/
    private int    tileWidth;   /*!< Latimea unei dale din sprite sheet.*/
    private int    tileHeight;   /*!< Inaltime unei dale din sprite sheet.*/

    /*! \fn public SpriteSheet(BufferedImage sheet)
        \brief Constructor, initializeaza spriteSheet.

        \param buffImg Un obiect BufferedImage valid.
     */

    // Retine referinta catre BufferedImage object.
    public SpriteSheet(BufferedImage buffImg, int tileWidth, int tileHeight) {
        this.spriteSheet = buffImg;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }


    /*      Returneaza un obiect BufferedImage ce contine o subimage (dala).
            Subimaginea este localizata avand ca referinta punctul din stanga sus.*/
    public BufferedImage crop(int x, int y) {
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }

}
