package PaooGame;

import GameWindow.GameWindow;

public class Main
{
    public static void main(String[] args)
    {

        try {
            Class.forName("org.sqlite.JDBC"); // 👈 Încarcă driverul manual
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        DataBase.DatabaseInit.initialize(); // Apelează metoda o singură dată
        Game paooGame = new Game("The race of the fearless adventurer game", 1440, 800);
        paooGame.StartGame();
    }
}