package Tiles;

import Graphics.Assets;

/*! \class public class blackTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class blackTile extends Tile {

    /*! \fn public blackTile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public blackTile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.black, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
