package haohaiTeam.game;

import haohaiTeam.game.gui.GameWindow;
import haohaiTeam.game.map.MapLoader;

public class GameStarter {

    public void startGame() { // Previously startApp, now renamed to startGame
        System.out.println("Welcome to Dublin!");
        GameWindow gameWindow = new GameWindow(); // Create a new GameWindow (empty initially)

        // Instead of loading static level_1, initiate loading the first level from JSON
        MapLoader.loadCurrentLevel(); // This will load level_1.json for the first call, and prepare for the next levels

        gameWindow.openWindow(); // Open the window with all elements set up
    }

    public static void main(String[] args) {
        GameStarter gameStarter = new GameStarter(); // Create the game starter instance
        gameStarter.startGame(); // Start the game by setting up the window and loading the first level
    }
}
