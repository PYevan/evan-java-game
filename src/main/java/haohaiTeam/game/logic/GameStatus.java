package haohaiTeam.game.logic;

import haohaiTeam.game.element.GameElement;
import haohaiTeam.game.input.CommandListener;
import haohaiTeam.game.map.MapLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static haohaiTeam.game.element.GameElement.elements;

public class GameStatus implements CommandListener {
    private int score = 0;
    private static int lives = 3; // assume 3 lives at start
    private int coinsCollected = 0;
    private int gemsAcquired = 0;
    private int co2Collected = 0;
    private volatile long elapsedTimeInMileSeconds = 0; // Variable to track elapsed time
    private boolean gameOver = false;
    private int tickCount;
    private static final int MAX_CO2_LEVEL = 100;
    private Timer timer;

    private volatile boolean started = false;
    private static final long TIMER_DELAY = 300;
    public static boolean co2increase = false;
    private static final int REQUIRED_GEMS = 3; // Number of gems required to win
    private boolean gameWon = false;

    // a timer for checking game time
    public static final long TIME_LIMIT_IN_MILESECONDS = 100 * 1000; // this is for limiting the player to pass current level in 600 seconds1

    private boolean showLevelScreen = false;

    public static boolean saveGame = false;
    
    
    public GameStatus() {
        // Initialize the CO2 timer
        gameTimer();
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public long getElapsedTimeInMileSeconds() {
        return elapsedTimeInMileSeconds;
    }

    public int getCutDownTime() {
        if (TIME_LIMIT_IN_MILESECONDS - elapsedTimeInMileSeconds > 0) {
            return (int) (TIME_LIMIT_IN_MILESECONDS - elapsedTimeInMileSeconds) / 1000;
        }
        return 0;

    }

    private void setScore(int newScore) {
        score = newScore;
    }
    public void setGemsAcquired(int newGems) {
        gemsAcquired = newGems;
    }
    public void setCO2Collected(int newco2) {
        co2Collected = newco2;
    }
    public void setCoinsCollected(int newCoins) {
        coinsCollected = newCoins;
    }

    // when win or lose then call the level screen
    public void triggerLevelScreen() {
        showLevelScreen = true;
    }

    // loading level then call
    public void hideLevelScreen() {
        showLevelScreen = false;
    }

    public boolean shouldShowLevelScreen() {
        return showLevelScreen;
    }
    public void gameTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (started) {
                    if (!gameOver) {  // Only update game time if the game isn't over
                        updateElapsedTime(TIMER_DELAY);
                        if (co2increase) {
                            increaseCO2();
                        }
                        if (saveGame) {
                            saveGame();
                            saveGame = false;
                        }
                    }
                    if (GameStatus.this.gemsAcquired >= REQUIRED_GEMS) {
                        checkGameConditions();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, TIMER_DELAY, TIMER_DELAY);
    }

    public void start() {
        if (!started) {
            started = true;
        }
    }


    // Method to update elapsed time
    public void updateElapsedTime(long elapsedTimeInMileSeconds) {
        this.elapsedTimeInMileSeconds += elapsedTimeInMileSeconds;
        // check if time limit is exceeded
        if (this.elapsedTimeInMileSeconds > TIME_LIMIT_IN_MILESECONDS) {
            System.out.println("Time's up! Game over!");
            checkGameConditions();
        }
    }

    public void trackCO2Level() {
        if (this.getCO2Collected() > MAX_CO2_LEVEL) {
            this.setGameOver(true);
            checkGameConditions();
            System.out.println("CO2 level exceeded maximum amount. Game Over!");
        }
    }

    public void increaseCO2() {
        this.co2Collected++;
    }

    public void addCO2(int co2Cost) {
        this.co2Collected += co2Cost;
        System.out.println(co2Cost + " CO2(s) added to the game status. Total Co2 costed: " + co2Collected);
    }

    public void addCoins(int numCoins) {
        this.coinsCollected += numCoins;
        int points = numCoins * 10;
        addScore(points);
        System.out.println(numCoins + " coin(s) added to the game status. Total coins collected: " + coinsCollected);
    }


    public void addGems(int numGems) {
        this.gemsAcquired += numGems;
        int points = numGems * 50;
        addScore(points);
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public int getGemsAcquired() {
        return gemsAcquired;
    }

    public int getCO2Collected() {
        return co2Collected;
    }


    public void checkGameConditions() {
        if (gameOver) {  // If the game is already over, avoid rechecking conditions
            return;
        }

        if (lives <= 0 || elapsedTimeInMileSeconds > TIME_LIMIT_IN_MILESECONDS || co2Collected > MAX_CO2_LEVEL) {
            gameOver = true;
            triggerLevelScreen();
            System.out.println("Game Over! You have lost.");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            MapLoader.reloadCurrentLevel(); // Keep this to reload the level if needed
        } else if (gemsAcquired >= REQUIRED_GEMS) {
            gameOver = true;
            gameWon = true;
            triggerLevelScreen();
            System.out.println("Congratulations! You have won.");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            MapLoader.loadNextLevel(); // Keep this to reload the level if needed
        }
    }


    // Add a method to reset game conditions for a new level if needed
    public void resetGameStatusForNewLevel() {
        score = 0;
        lives = 3;
        gemsAcquired = 0;
        coinsCollected = 0;
        co2Collected = 0;  // Depending on whether you want to reset per level

        gameOver = false;
        gameWon = false;
        co2increase = false;
        showLevelScreen = false;
        saveGame = false;
        tickCount = 0;

        started = false;
        elapsedTimeInMileSeconds = 0; // Reset time if it tracks overall game time, not per level
        // Optionally reset other conditions as needed
    }
    @Override
    public void onPickedCoin(GameElement element) {
        addCoins(1);
        System.out.println("Coin received signal");
    }

    @Override
    public void onPickedGem(GameElement element) {
        // Implementation for handling picked gems if needed
        addGems(1);
        System.out.println("Gem received signal");
    }

    @Override
    public void onTick() {
        tickCount++;
        if (tickCount % 12000 == 0) {
            checkGameConditions();
        }
    }

    @Override
    public void onCO2Generated(int value) {
        addCO2(value);
    }

    public void saveGame() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = dateFormat.format(new Date());
        String filePath = "Saved_Game_" + dateTime + ".json";

        JSONObject savedGame = new JSONObject();

        savedGame.put("coinsCollected", coinsCollected);
        savedGame.put("gemsAcquired", gemsAcquired);
        savedGame.put("score", score);
        savedGame.put("co2Collected", co2Collected);
        savedGame.put("lives", lives);

        JSONArray elementsArray = new JSONArray(elements);
        savedGame.put("map", elementsArray);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(savedGame.toString());
            System.out.println("Game saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Save Game Error: " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void loadScoreFromJson(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject json = new JSONObject(content);

            int coins = json.getInt("coinsCollected");
            int gems = json.getInt("gemsAcquired");
            int score = json.getInt("score");
            int co2 = json.getInt("co2Collected");
            int lives = json.getInt("lives");

            this.setCoinsCollected(coins);
            this.setGemsAcquired(gems);
            this.setScore(score);
            this.setCO2Collected(co2);

            System.out.println("Game loaded from: " + filePath);
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
        }
    }
}