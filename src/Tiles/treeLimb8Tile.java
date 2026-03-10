package Tiles;

import Graphics.Assets;

/*! \class public class MountainTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class treeLimb8Tile extends Tile {

    /*! \fn public MountainTile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public treeLimb8Tile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.treeLimb8, id);
        if (Assets.treeLimb8 == null) {
            System.out.println("Imaginea treeLimb8 nu a fost încărcată corect.");
        }
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */
    public boolean IsSolid()
    {
        return false;
    }

}
