package Tiles;

import Graphics.Assets;

/*! \class public class groundTile extends Tile

 */
public class groundTile extends Tile {

    /*! \fn public groundTile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public groundTile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.ground, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
