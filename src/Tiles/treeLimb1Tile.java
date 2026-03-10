package Tiles;

import Graphics.Assets;

/*! \class public class MountainTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip munte sau piatra.
 */
public class treeLimb1Tile extends Tile {

    /*! \fn public MountainTile(int id)
       \brief Constructorul de initializare al clasei

       \param id Id-ul dalei util in desenarea hartii.
    */
    public treeLimb1Tile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.treeLimb1, id);
    }

    public boolean IsSolid()
    {
        return true;
    }




}
