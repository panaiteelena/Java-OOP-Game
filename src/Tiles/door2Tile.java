package Tiles;

import Graphics.Assets;

/*! \class public class door2Tile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class door2Tile extends Tile {

    /*! \fn public door2Tile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public door2Tile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.door2, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
