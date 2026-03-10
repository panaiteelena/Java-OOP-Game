package Tiles;

import Graphics.Assets;

/*! \class public class dooor8Tile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class door8Tile extends Tile {

    /*! \fn public MountainTile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public door8Tile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.door8, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
