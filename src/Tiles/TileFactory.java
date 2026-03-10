package Tiles;

/**
 * Clasa TileFactory este responsabilă de crearea obiectelor de tip Tile pe baza unui ID.
 * Aplică principiul Factory Method combinat cu Flyweight:
 *  - Primește un ID numeric.
 *  - Returnează o instanță specifică a unei subclase de Tile (ex: apă, piatră, iarbă).
 *
 * Este o clasă de utilitate (fără stare internă), folosită doar din clasa Tile.
 */
final class TileFactory {

    // Constructor privat – previne instanțierea clasei (utilitar static)
    private TileFactory() {}

    /**
     * Creează și returnează o instanță a subclasei de Tile potrivită ID-ului dat.
     * Este apelată doar o dată pentru fiecare ID, din `Tile.of(id)` → rezultatul este memorat în cache.
     *
     * @param id ID-ul tile-ului (venit din fișierul de hartă)
     * @return instanță corespunzătoare unei clase concrete care extinde Tile
     */
    static Tile create(int id) {
        return switch (id) {
            // === Uși ===
            case 0  -> new door1Tile(id);    // ușă tip 1
            case 1  -> new door2Tile(id);    // ușă tip 2
            case 2  -> new door3Tile(id);    // ușă tip 3
            case 3  -> new door4Tile(id);    // ușă tip 4
            case 4  -> new door5Tile(id);    // ușă tip 5
            case 5  -> new door6Tile(5);     // ușă tip 6 (id hardcodat)
            case 6  -> new door7Tile(6);
            case 7  -> new door8Tile(7);
            case 8  -> new door9Tile(8);

            // === Obstacole & teren ===
            case 9  -> new littleRockTile(9);        // piatră mică
            case 10 -> new obstacolTile(10);         // obstacol generic
            case 11 -> new plantTile(11);            // plantă decorativă
            case 12 -> new rockBrownTile(12);        // piatră maro

            // === Apă și gol ===
            case 14 -> new waterTile(14);            // apă
            case 15 -> new blackTile(15);            // gol/nefolosit (ex: fundal invizibil)

            // === Terenuri verzi ===
            case 16 -> new grassGroundMiddleTile(16); // iarbă - mijloc
            // case 17 -> new grassGroundLeftTile(17);  // iarbă - stânga (comentat)
            // case 18 -> new grassGroundRightTile(18); // iarbă - dreapta (comentat)

            // === Alte tipuri de teren ===
            case 19 -> new simpleGroundTile(19);      // pământ simplu
            case 24 -> new groundTile(24);            // pământ general

            // === Ramuri de copac ===
            case 25 -> new treeLimb1Tile(25);
            case 26 -> new treeLimb2Tile(26);
            case 27 -> new treeLimb3Tile(27);
            case 28 -> new treeLimb4Tile(28);
            case 29 -> new treeLimb5Tile(29);
            case 30 -> new treeLimb6Tile(30);
            case 31 -> new treeLimb7Tile(31);
            case 32 -> new treeLimb8Tile(32);

            // === Plante violet ===
            case 33 -> new purplePlant1Tile(33);
            case 34 -> new purplePlant2Tile(34);
            case 35 -> new purplePlant3Tile(35);
            case 36 -> new purplePlant4Tile(36);

            // === Plante verzi ===
            case 37 -> new greenPlant1Tile(37);
            case 38 -> new greenPlant2Tile(38);
            case 39 -> new greenPlant3Tile(39);
            case 40 -> new greenPlant4Tile(40);

            // === Capcane / obstacole periculoase ===
            case 41 -> new spikeTile(41);             // țepușă normală
            case 42 -> new lavaTile(42);              // lavă
            case 43 -> new spikeRotated180Tile(43);   // țepușă rotită 180°
            case 44 -> new spikeRotated90Tile(44);    // țepușă rotită 90°

            // === Implicit: dacă nu există tip definit, întoarce un tile negru (placeholder) ===
            default -> new blackTile(id);
        };
    }
}
