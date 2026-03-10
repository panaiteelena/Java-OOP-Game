package Tiles;

import Graphics.Assets;

/*! \class public class TreeTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip tree.
 */
public class purplePlant2Tile extends Tile
{
    /*! \fn public TreeTile(int id)
        \brief Constructorul de initializare al clasei

        \param id Id-ul dalei util in desenarea hartii.
     */
    public purplePlant2Tile(int id)
    {
        super(Assets.purplePlant2, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
