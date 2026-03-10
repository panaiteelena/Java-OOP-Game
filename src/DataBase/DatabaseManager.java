package DataBase;

// Importă clasele JDBC necesare pentru conectare și execuție de SQL
import java.sql.*;

/**
 * Clasa DatabaseManager gestionează conexiunea la baza de date și operațiile legate de high score.
 */
public class DatabaseManager {

    // Calea către fișierul bazei de date SQLite
    private static final String DB_URL = "jdbc:sqlite:savegame.db";

    /**
     * Obține o conexiune la baza de date.
     * @return obiect Connection către baza SQLite
     * @throws SQLException dacă apare o eroare de conexiune
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Returnează scorul maxim salvat în baza de date.
     */
    public static int getHighScore() {
        try (
                Connection conn = getConnection();                 // Deschide conexiune
                Statement stmt = conn.createStatement()           // Creează obiect pentru comenzi SQL
        ) {
            // Execută interogare pentru a obține scorul
            ResultSet rs = stmt.executeQuery("SELECT score FROM HighScore WHERE id = 1");

            // Dacă există o înregistrare, returnează scorul
            if (rs.next()) return rs.getInt("score");
        } catch (Exception e) {
            // Afișează eroarea în consolă (pentru depanare)
            e.printStackTrace();
        }

        // Dacă nu există scor salvat, returnează 0
        return 0;
    }

    public static void updateHighScore(int newScore) {
        int current = getHighScore();  // Citește scorul curent salvat

        // Verifică dacă noul scor este mai mare
        if (newScore > current) {
            try (Connection conn = getConnection()) {
                // Pregătește comanda de actualizare a scorului
                PreparedStatement ps = conn.prepareStatement("UPDATE HighScore SET score = ? WHERE id = 1");
                ps.setInt(1, newScore);       // Setează noul scor
                ps.executeUpdate();           // Execută actualizarea
            } catch (Exception e) {
                // Prinde excepții dar nu face nimic (poți adăuga e.printStackTrace() dacă vrei debug)
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualizează high score-ul în baza de date doar dacă noul scor este mai mare decât cel existent.
     * @param newScore scorul curent care trebuie comparat cu scorul maxim
     */


}
