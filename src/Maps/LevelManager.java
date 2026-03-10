package Maps;  // Plasează-l în pachetul corespunzător

import Maps.Map;
import PaooGame.RefLinks;

public class LevelManager {
    private int currentLevel;
    private Map[] maps;
    private RefLinks refLink;

    public LevelManager(RefLinks refLink) {
        this.refLink = refLink;
        this.maps = new Map[3];  // Vom avea 3 hărți
        this.currentLevel = 0;   // Începem de la primul nivel

        // Încărcăm hărțile
        maps[0] = new Map(refLink, "map1.txt"); // Prima hartă
        maps[1] = new Map(refLink, "map2.txt"); // A doua hartă
        maps[2] = new Map(refLink, "map3.txt"); // A treia hartă
    }

    // Obține harta curentă
    public Map getCurrentMap() {
        return maps[currentLevel];
    }

    // Schimbă nivelul
    public void nextLevel() {
        if (currentLevel < maps.length - 1) {
            currentLevel++;
        }
    }

    // Revine la nivelul anterior
    public void previousLevel() {
        if (currentLevel > 0) {
            currentLevel--;
        }
    }
}
