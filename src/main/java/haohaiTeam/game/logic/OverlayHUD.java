package haohaiTeam.game.logic;

import java.awt.*;

public class OverlayHUD {
    private GameStatus gameStatus;

    public OverlayHUD(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void render(Graphics2D g) {
        // Render the HUD overlay
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + gameStatus.getScore(), 20, 20);
        g.drawString("Coins: " + gameStatus.getCoinsCollected(), 20, 50);
        g.drawString("Gems: " + gameStatus.getGemsAcquired(), 20, 80);
        g.drawString("CO2: ", 20, 110);
        g.drawString("Time: " + gameStatus.getCutDownTime(), 20, 140);

        // CO2 level indicator as a rectangle
        int currentWidth = (int) ((double) gameStatus.getCO2Collected() / 100 * 100);
        g.setColor(Color.RED);
        g.fillRect(80, 92, currentWidth, 20);
        g.setColor(Color.BLACK);
        g.drawRect(80, 92, 100, 20);
    }
}