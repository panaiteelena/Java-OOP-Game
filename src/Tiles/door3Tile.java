package Tiles;

import Graphics.Assets;

/*! \class public class door3Tile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class door3Tile extends Tile {

    /*! \fn public door3Tile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public door3Tile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.door3, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
