package Maps;

import Tiles.Tile;

import java.awt.*;
import java.io.*;
import PaooGame.RefLinks;

/**
 * Clasa Map reprezintă o hartă a jocului, construită pe baza unei matrice de tile-uri (dale).
 * Fiecare poziție (x, y) din hartă conține un ID de tile, care este asociat cu o imagine și comportament (ex: solid, apă, platformă).
 */
public class Map {
    private RefLinks refLink;    // Referință către clasa centrală care reține toate referințele utile (joc, cameră, input etc.)
    private int width;           // Lățimea hărții în număr de tile-uri
    private int height;          // Înălțimea hărții în număr de tile-uri
    private int[][] tiles;       // Matricea care conține ID-urile tile-urilor de pe hartă

    /**
     * Constructor care inițializează harta și încarcă datele dintr-un fișier text.
     * @param refLink contextul jocului (acces la cameră, ecran etc.)
     * @param fileName numele fișierului din care se încarcă harta
     */
    public Map(RefLinks refLink, String fileName) {
        this.refLink = refLink;
        LoadWorldFromFile(fileName); // se încarcă matricea de tile-uri
    }

    /**
     * Metodă apelată pentru a actualiza harta (ex: dacă un tile se modifică dinamic).
     * În acest caz, nu se întâmplă nimic, dar poate fi extinsă pentru hărți dinamice.
     */
    public void Update() {
        // de implementat la nevoie (ex: distrugere copaci, apă curgătoare etc.)
    }

    /**
     * Metoda responsabilă de desenarea hărții în funcție de camera jocului.
     * Se desenează doar zona vizibilă pe ecran pentru optimizare.
     * @param g contextul grafic pe care se desenează
     */
    public void Draw(Graphics g) {
        // Coordonatele curente ale camerei (pentru scroll)
        float xOff = refLink.GetGameCamera().getXOffset();
        float yOff = refLink.GetGameCamera().getYOffset();

        // Calculează range-ul de tile-uri vizibile pe ecran
        int xStart = Math.max(0, (int)(xOff / Tile.TILE_WIDTH));
        int xEnd   = Math.min(width, (int)((xOff + refLink.GetWidth()) / Tile.TILE_WIDTH) + 1);
        int yStart = Math.max(0, (int)(yOff / Tile.TILE_HEIGHT));
        int yEnd   = Math.min(height, (int)((yOff + refLink.GetHeight()) / Tile.TILE_HEIGHT) + 1);

        // Desenează doar tile-urile aflate în zona vizibilă
        for(int y = yStart; y < yEnd; y++) {
            for(int x = xStart; x < xEnd; x++) {
                GetTile(x, y).Draw(g,
                        x * Tile.TILE_WIDTH  - (int)xOff,
                        y * Tile.TILE_HEIGHT - (int)yOff);
            }
        }
    }

    /**
     * Returnează lățimea hărții în tile-uri.
     */
    public int GetWidth() {
        return this.width;
    }

    /**
     * Returnează înălțimea hărții în tile-uri.
     */
    public int GetHeight() {
        return this.height;
    }

    /**
     * Returnează tile-ul de la coordonatele (x, y).
     * Dacă coordonatele sunt în afara hărții, returnează un tile "gol".
     * @param x coordonata X în tile-uri
     * @param y coordonata Y în tile-uri
     * @return obiect de tip Tile
     */
    public Tile GetTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return Tile.of(15); // Tile implicit (ex: gol sau nefolosit)
        }
        int id = tiles[y][x]; // ID-ul tile-ului la poziția cerută
        return Tile.of(id);   // se obține obiectul Tile asociat ID-ului
    }

    /**
     * Încarcă harta dintr-un fișier text.
     * Structura fișierului: prima linie conține lățimea și înălțimea,
     * urmate de linii care descriu ID-urile tile-urilor.
     * @param fileName numele fișierului text (relativ la directorul proiectului)
     */
    private void LoadWorldFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Prima linie conține dimensiunile hărții
            String line = reader.readLine();
            String[] dimensions = line.split(" ");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);

            // Inițializăm matricea de tile-uri
            tiles = new int[height][width];

            // Citim restul liniilor, care conțin valorile ID-urilor
            for (int y = 0; y < height; y++) {
                line = reader.readLine();
                String[] tokens = line.split(" ");
                for (int x = 0; x < width; x++) {
                    tiles[y][x] = Integer.parseInt(tokens[x]); // fiecare ID este transformat în int
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Afișează eroarea în consolă dacă fișierul nu poate fi citit
        }
    }
}
