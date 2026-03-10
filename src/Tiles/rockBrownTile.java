package Tiles;

import Graphics.Assets;

/*! \class public class GrassTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip iarba.
 */
public class rockBrownTile extends Tile
{
    /*! \fn public GrassTile(int id)
        \brief Constructorul de initializare al clasei

        \param id Id-ul dalei util in desenarea hartii.
     */
    public rockBrownTile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.rockBrown, id);
    }

       public boolean IsSolid()
       {
           return true;
       }


}
