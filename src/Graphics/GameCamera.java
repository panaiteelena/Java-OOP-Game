package Graphics;

// Importă clasa pentru personaj (sau orice entitate urmărită de cameră)
import Items.Character;
// Importă contextul general al jocului (fereastră, cameră, hartă etc.)
import PaooGame.RefLinks;

/**
 * Clasa GameCamera – gestionează mișcarea camerei în joc.
 * Poate fi centrată pe o entitate și limitată în interiorul hărții.
 */
public class GameCamera {

    private final RefLinks refLinks; // referință globală la obiectul cu stările jocului
    private float xOffset, yOffset;  // cât de mult este deplasată camera pe axele X și Y

    /** Factorul de mărire al camerei (1 = fără zoom, >1 = apropiere) */
    private float zoom = 1.5f; // zoom implicit (150%)

    /**
     * Constructorul camerei jocului.
     * @param refs referință către RefLinks (conține lățimea, înălțimea ferestrei, etc.)
     * @param xOffset deplasarea inițială pe axa X
     * @param yOffset deplasarea inițială pe axa Y
     */
    public GameCamera(RefLinks refs, float xOffset, float yOffset){
        this.refLinks = refs;
        this.xOffset  = xOffset;
        this.yOffset  = yOffset;
    }

    /* ===== Getteri pentru offset-uri și zoom ===== */

    /** @return offsetul camerei pe X */
    public float getXOffset(){ return xOffset; }

    /** @return offsetul camerei pe Y */
    public float getYOffset(){ return yOffset; }

    /** @return zoomul curent al camerei */
    public float getZoom()   { return zoom;   }

    /**
     * Schimbă factorul de zoom – util dacă vrei să implementezi zoom dinamic.
     * @param zoom noul nivel de mărire (ex: 2.0f pentru 200%)
     */
    public void setZoom(float zoom){ this.zoom = zoom; }

    /**
     * Centrează camera pe o entitate (ex: jucătorul).
     * @param e entitatea pe care camera trebuie să o urmărească
     */
    public void centerOnEntity(Character e){
        xOffset = e.GetX() - refLinks.GetWidth()  / (2f * zoom) + e.GetWidth()  / 2f;
        yOffset = e.GetY() - refLinks.GetHeight() / (2f * zoom) + e.GetHeight() / 2f;
    }

    /**
     * Limitează poziția camerei pentru a nu „ieși” în afara hărții.
     * Se bazează pe dimensiunile hărții și pe zoomul curent.
     * @param mapPxW lățimea hărții în pixeli
     * @param mapPxH înălțimea hărții în pixeli
     */
    public void clamp(float mapPxW, float mapPxH){
        float viewW = refLinks.GetWidth()  / zoom; // cât se vede pe orizontală
        float viewH = refLinks.GetHeight() / zoom; // cât se vede pe verticală

        if(xOffset < 0)                xOffset = 0;
        if(yOffset < 0)                yOffset = 0;
        if(xOffset > mapPxW - viewW)   xOffset = mapPxW - viewW;
        if(yOffset > mapPxH - viewH)   yOffset = mapPxH - viewH;
    }
}
