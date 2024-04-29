package haohaiTeam.game.map;


import haohaiTeam.game.element.*;
import haohaiTeam.game.element.transport.Bike;
import haohaiTeam.game.element.transport.onRoute.auto.*;
import haohaiTeam.game.element.transport.onRoute.stationRoad.*;
import haohaiTeam.game.gui.GameWindow;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static haohaiTeam.game.gui.GameWindow.gameStatus;

public class MapLoader {
    private static int currentLevel = 1; // Track the current level
    private static int maxLevel = 2; // Track the current level

    public static void loadNextLevel() {
        currentLevel++;  // Increment to next level
        if (currentLevel <= maxLevel) {
            loadCurrentLevel(); // Load the next level
        }
    }

    public static void loadCurrentLevel() {
        String levelFile = String.format("src/main/resources/MapElement/level_%d.json", currentLevel);
        try {
            loadMapFromJson(levelFile);
        } catch (Exception e) {
            System.err.println("Failed to load level: " + levelFile);
            e.printStackTrace();
            // Consider what to do if the level fails to load - maybe try to load a default level or handle the error gracefully
        }
    }

    public static void reloadCurrentLevel() {
        loadCurrentLevel(); // Reload the current level
    }


    private static void loadMapFromJson(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject json = new JSONObject(content);
            JSONArray mapLines = json.getJSONArray("map");
            loadMap(mapLines.toList().toArray(new String[0]));
        } catch (IOException e) {
            System.err.println("Error loading level file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadMap(String[] mapData) {
        GameWindow.clearElements();  // Clear existing elements from the game window
        ArrayList<Object> validGemCoordinates = new ArrayList<>();
        System.out.println("No Saved Games, starting first level!");

        for (int y = 0; y < mapData.length; y++) {
            String line = mapData[y];
            for (int x = 0; x < line.length(); x++) {
                char tile = line.charAt(x);
                int posX = x * GameWindow.CELL_SIZE;
                int posY = y * GameWindow.CELL_SIZE;
                processTile(tile, posX, posY, validGemCoordinates);
            }
        }
        placeRandomGems(validGemCoordinates);

        gameStatus.resetGameStatusForNewLevel();
        gameStatus.hideLevelScreen();
    }

    private static void processTile(char tile, int posX, int posY, ArrayList<Object> validGemCoordinates) {
        switch (tile) {
            case 'W':
                GameWindow.addElement(new Wall(posX, posY));
                break;
            case 'G':
                GameWindow.addElement(new Gem(posX, posY));
                break;
            case 'C':
                GameWindow.addElement(new Coin(posX, posY));
                break;
            case 'P':
                GameWindow.addElement(new Player(posX, posY));
                break;
            case '2':
                GameWindow.addElement(new Player2(posX, posY));
                break;
            case 'B':
                GameWindow.addElement(new Bike(posX, posY));
                break;
            case 'U':
                GameWindow.addElement(new Bus(posX, posY));
                break;
            case 'T':
                GameWindow.addElement(new Taxi(posX, posY));
                break;
            case 'L':
                GameWindow.addElement(new Luas(posX, posY));
                break;
            case 'r':
                GameWindow.addElement(new Road(posX, posY));
                break;
            case 'c':
                GameWindow.addElement(new Crosswalk(posX, posY));
                break;
            case 'E':
                GameWindow.addElement(new CameraEntity(posX, posY));
                break;
            case 'b':
                GameWindow.addElement(new BusStation(posX, posY));
                break;
            case 't':
                GameWindow.addElement(new TaxiStation(posX, posY));
                break;
            case 'l':
                GameWindow.addElement(new LuasStation(posX, posY));
                break;
            case 'a': GameWindow.addElement(new Car(posX, posY)); break;
            case ' ':
                validGemCoordinates.add(posX);
                validGemCoordinates.add(posY);
                break;
        }
    }

    private static void placeRandomGems(ArrayList<Object> validGemCoordinates) {
        Random random = new Random();
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < validGemCoordinates.size() / 2; i++) {indices.add(i);}
        Collections.shuffle(indices);
        int maxRandomGems = 45;
        int minRandomGems = 15;
        int randomGemCount = random.nextInt(maxRandomGems - minRandomGems + 1) + minRandomGems;
        System.out.println("Random gems produced: " + randomGemCount);
        for (int i = 0; i < randomGemCount; i++) {
            int index = indices.get(i);
            int gemPosX = (int) validGemCoordinates.get(index * 2);
            int gemPosY = (int) validGemCoordinates.get(index * 2 + 1);
            GameWindow.addElement(new Gem(gemPosX, gemPosY));
        }

    }

    private void placeReport(String placeName, int posX, int posY) {
        // This reports the map import is working correctly
        System.out.println(placeName);
        System.out.println("has been placed in the position X: " + posX + ", Y: " + posY);
    }
}
