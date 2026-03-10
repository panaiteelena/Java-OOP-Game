package Tiles;

import Graphics.Assets;

/*! \class public class plantTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip tree.
 */
public class plantTile extends Tile
{
    /*! \fn public plantTile(int id)
        \brief Constructorul de initializare al clasei

        \param id Id-ul dalei util in desenarea hartii.
     */
    public plantTile(int id)
    {
        super(Assets.plant, id);
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */

}
