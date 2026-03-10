package Tiles;

import Graphics.Assets;

/*! \class public class MountainTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class grassGroundMiddleTile extends Tile {

    /*! \fn public MountainTile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public grassGroundMiddleTile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.grassGroundMiddle, id);
    }

     public boolean IsSolid()
     {
         return true;
     }


}
