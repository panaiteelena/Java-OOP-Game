package Input;

// Importă clasele necesare pentru ascultarea evenimentelor de mouse
import java.awt.event.*;

/**
 * Clasa MouseManager – gestionează input-ul de la mouse:
 * - coordonatele cursorului
 * - apăsarea și eliberarea click-ului stâng
 * - poziția mouse-ului pentru hover/click pe butoane
 */
public class MouseManager implements MouseListener, MouseMotionListener {

    // Variabile interne pentru starea click-ului stâng
    private boolean leftPressed;   // butonul stâng este apăsat
    private boolean leftClicked;   // s-a efectuat un click complet (pressed + release)
    private boolean leftReleased;  // opțional, nu e folosit în codul curent

    // Coordonatele cursorului pe ecran
    private int mouseX, mouseY;

    /* ===== Getteri ===== */

    /** @return poziția X curentă a mouse-ului */
    public int getX() {
        return mouseX;
    }

    /** @return poziția Y curentă a mouse-ului */
    public int getY() {
        return mouseY;
    }

    /** @return true dacă a avut loc un click complet stânga în acest frame */
    public boolean isLeftClicked() {
        return leftClicked;
    }

    /**
     * Se apelează la finalul fiecărui frame pentru a reseta starea de click.
     * Este necesar pentru a preveni înregistrarea multiplă a aceluiași click.
     */
    public void resetClick() {
        leftClicked = false;
        leftReleased = false;
    }

    /* ===== Implementare MouseListener ===== */

    /** Se apelează când mouse-ul este apăsat */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) // doar butonul stâng
            leftPressed = true;
    }

    /** Se apelează când mouse-ul este eliberat */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && leftPressed) {
            leftClicked = true;   // marchează click-ul
            leftPressed = false;  // oprește starea de apăsare
        }
    }

    // Evenimente nefolosite dar necesare pentru interfața MouseListener
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {} // nu este folosit direct

    /* ===== Implementare MouseMotionListener ===== */

    /** Se apelează când mouse-ul este mișcat fără click */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX(); // actualizează coordonata X
        mouseY = e.getY(); // actualizează coordonata Y
    }

    /** Se apelează când mouse-ul este mișcat cu click apăsat */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e); // tratează ca o mișcare normală
    }

    /**
     * Setează manual starea de click – poate fi folosit pentru simulare.
     * @param clicked true dacă vrei să forțezi un click artificial
     */
    public void setLeftClicked(boolean clicked) {
        this.leftClicked = clicked;
    }
}
