package Tiles;

import Graphics.Assets;

/*! \class public class door7Tile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class door7Tile extends Tile {

    /*! \fn public door7Tile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public door7Tile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.door7, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
