package DataBase;

// Importă clasele necesare pentru lucrul cu baza de date (JDBC)
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clasa DatabaseInit se ocupă cu inițializarea tabelelor necesare în baza de date SQLite.
 * Este apelată o singură dată la pornirea jocului.
 */
public class DatabaseInit {

    /**
     * Creează tabelele necesare pentru salvarea stării jocului, dacă acestea nu există deja.
     */
    public static void initialize() {
        try (
                // Creează conexiunea și un statement pentru executarea comenzilor SQL
                Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            // Tabela GameState – salvează nivelul, scorul, viețile și poziția jucătorului
            stmt.execute("CREATE TABLE IF NOT EXISTS GameState (" +
                    "id INTEGER PRIMARY KEY, " +
                    "level INTEGER, " +
                    "score INTEGER, " +
                    "lives INTEGER, " +
                    "playerX REAL, " +
                    "playerY REAL)");

            // Tabela CollectedCoins – reține coordonatele monedelor deja colectate
            stmt.execute("CREATE TABLE IF NOT EXISTS CollectedCoins (" +
                    "x INTEGER, " +
                    "y INTEGER)");

            // Tabela CollectedArtefacts – reține coordonatele artefactelor deja colectate
            stmt.execute("CREATE TABLE IF NOT EXISTS CollectedArtefacts (" +
                    "x INTEGER, " +
                    "y INTEGER)");

            // Tabela Enemies – salvează tipul, poziția și starea (omorât sau nu) a fiecărui inamic
            stmt.execute("CREATE TABLE IF NOT EXISTS Enemies (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "type TEXT, " +
                    "x REAL, " +
                    "y REAL, " +
                    "isKilled INTEGER)");

            // Tabela HighScore – păstrează cel mai mare scor atins vreodată
            stmt.execute("CREATE TABLE IF NOT EXISTS HighScore (" +
                    "id INTEGER PRIMARY KEY, " +
                    "score INTEGER)");

            // Verifică dacă tabela HighScore este goală
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM HighScore");

            // Dacă nu există nicio intrare, inserează scorul inițial cu valoarea 0
            if (rs.next() && rs.getInt("count") == 0) {
                stmt.execute("INSERT INTO HighScore (id, score) VALUES (1, 0)");
            }

            // Afișează un mesaj de confirmare în consolă
            System.out.println("Tabele create sau deja existente.");

        } catch (Exception e) {
            // Afișează detalii dacă apare o eroare la inițializarea tabelelor
            e.printStackTrace();
        }
    }
}
