# Elias the Fearless 

**Elias the Fearless** is a 2D minimalist platformer that follows the journey of a 20-year-old explorer in the heart of the Amazon jungle. After recovering a legendary artifact stolen centuries ago, Elias must survive ancient traps, giant insects, and hostile ambushes to make it home alive.

---

## Storyline
Elias is a young adventurer with a passion for uncharted territories and ancient history. While studying the lore of lost artifacts, he discovers the location of a relic stolen long ago by a dangerous tribe. The game starts as Elias reaches a hidden temple deep within the Amazon. 

The moment he claims the artifact, the temple's defenses awaken. Traps are triggered, and guardians are on his trail. Will he overcome his fears and escape the jungle with the treasure?

---

## Controls

| Action | Key 
| **Move Left** | `A` 
| **Move Right** | `D` 
| **Jump** | `Space`
| **Attack (Arrows)** | `K` 

---
##  Main Menu
The game starts with a clean, functional interface featuring:
* **Start Button:** Begin the adventure from Level 1.
* **Load Game Button:** Resume progress from a saved state.
* **Exit Button:** Close the game application.
  
<img width="899" height="482" alt="Screenshot 2026-03-12 072427" src="https://github.com/user-attachments/assets/61650f50-3d35-4e98-b54c-eab812c158fd" />

---
##  Secondary Menus & UI Feedback
The game provides continuous feedback and navigation through specific overlay menus:
* **Level Transition:** Progression is seamless. Once a level is successfully completed, the game automatically transitions to the next stage.
* **Victory Screen:** After defeating the final enemy (the warrior), a **"YOU WON"** message is displayed. The victory screen includes a dedicated button to return the player safely to the **Main Menu**
* **Failure Screen (Life Lost):** If the explorer dies, the screen displays:
    * **"YOU LOST A LIFE"**
    * **"Press R to try again"**
    * **"Press ESC to exit"**
 <img width="676" height="364" alt="Screenshot 2026-03-12 072438" src="https://github.com/user-attachments/assets/5da5a137-c08f-410d-ba62-5bdb06584987" />
      
### **Lives & Failure System**
* **Lives System:** Elias starts with **3 lives**. 
* **Life Lost:** If the explorer dies, one life is deducted, the message **"YOU LOST A LIFE"** is displayed, and the current level restarts.
    * **Press R:** To try again.
    * **Press ESC:** To exit.
* **Game Over:** If all 3 lives are lost, a **"Game Over"** message appears, and the player must restart the entire game from the beginning.
<img width="579" height="337" alt="Screenshot 2026-03-12 072406" src="https://github.com/user-attachments/assets/2f84fc7a-31a3-4576-89c8-1c4cefd1cd32" />


---

## Level Design & Progression

The game is a linear side-scroller consisting of **three progressive levels**.

### **Level 1: The Artifact Chamber**
* **Setting:** Inside the ancient temple.
* **Gameplay:** Pure platforming. Elias must jump across suspended platforms to find the exit.
* **Hazards:** Floor traps, bottomless pits, and water. Touching any hazard results in death and a level restart.
* **Objective:** Collect coins to increase the score and reach the next level.
<img width="971" height="526" alt="Screenshot 2026-03-12 071446" src="https://github.com/user-attachments/assets/cc94791d-4015-4bc1-9cc9-5fc4626aae10" />

### **Level 2: Insect Attack**
* **Setting:** The transition from the temple to the jungle.
* **Gameplay:** Introduction of combat. Elias encounters giant insects (Bees and Ants).
* **Mechanics:** Players can shoot arrows (Key: `K`) to eliminate enemies or jump over them to avoid conflict.
* **Objective:** Clear the path to reach the final stage.
<img width="982" height="532" alt="Screenshot 2026-03-12 071509" src="https://github.com/user-attachments/assets/7670c31b-b901-4d48-a493-ef7e2bb342a5" />

### **Level 3: The Ambush**
* **Setting:** The deep jungle.
* **Gameplay:** High-intensity survival against tribal warriors.
* **Hazards:** Lava pools, spikes, and environmental traps.
* **Scoring:** * **100 Points:** Defeating aggressive warriors (those firing arrows).
    * **50 Points:** Defeating non-attacking warriors.
<img width="1045" height="561" alt="Screenshot 2026-03-12 071539" src="https://github.com/user-attachments/assets/f87c3d9d-1b74-45de-ab7e-184c6cbd6f05" />

---

## Technical Architecture (Design Patterns)

To ensure a clean, optimized, and scalable codebase, the following design patterns were implemented:

### **1. Singleton Pattern**
* **Application:** `Hero` Class.
* **Purpose:** Ensures only one instance of the protagonist exists globally. It provides a secure access point for tracking player lives, score, and position throughout all levels.

### **2. Factory Method Pattern**
* **Application:** `Enemy` and `Item` (Fruits/Coins) generation.
* **Purpose:** Decouples object creation from the main game logic. This allows the easy addition of new enemy types (e.g., different insects or warriors) without modifying existing code.

### **3. Flyweight Pattern**
* **Application:** Map Tiles and Projectiles.
* **Purpose:** Reduces memory consumption by sharing heavy data (textures and physics properties) across multiple identical objects. This prevents unnecessary memory usage for repetitive environmental elements.

---

##  Scoring & Mechanics
* **Score:** Calculated based on coins collected and enemies defeated.
* **Lives:** Touching hazards (Water, Lava, Spikes) or enemies results in losing a life.
* **Perspective:** Side-view linear projection with the camera centered on the player.
* **Art Style:** Minimalist aesthetic for a clean and focused gameplay experience.
