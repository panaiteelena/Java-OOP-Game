package Tiles;

import Graphics.Assets;

/*! \class public class WaterTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip apa.
 */public class waterTile extends Tile
{
    /*! \fn public WaterTile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public waterTile(int id)
    {
        super(Assets.water, id);
    }

    /*! \fn public boolean IsSolid()
       \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
    */
    /*@Override
    public boolean IsSolid()
    {
        return true;
    }*/
    public boolean IsSolid() {
        return false;  // Apa nu este solidă, dar poate fi periculoasă pentru erou
    }
}
