package haohaiTeam.game.element.transport.onRoute.stationRoad;

import haohaiTeam.game.element.GameElement;
import haohaiTeam.game.element.transport.onRoute.auto.Luas;

import java.awt.*;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class LuasStation extends Station {
    private static final double CO2_PER_CELL = 0.1;

    public LuasStation(int x, int y) {
        super(x, y);
    }

    @Override
    public void goingToBeWalkedOverBy(GameElement gameElement) {
        if (gameElement instanceof Luas transport) {
            correctStationMethod(transport);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw black background square
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(renderX, renderY, CELL_SIZE, CELL_SIZE);

        // Add "LUAS" text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10)); // Adjust font size to fit in the cell
        FontMetrics fm = g2d.getFontMetrics();
        String busText = "LUAS";
        int textWidth = fm.stringWidth(busText);
        int textX = renderX + (CELL_SIZE - textWidth) / 2; // Center the text horizontally
        int textY = renderY + (CELL_SIZE / 2) + fm.getAscent() / 2 - 5; // Position text in the upper half of the cell

        // Draw the "LUAS" text
        g2d.drawString(busText, textX, textY);

        // Add "STOP" text below "TAXI"
        String stopText = "STOP";
        textWidth = fm.stringWidth(stopText);
        textX = renderX + (CELL_SIZE - textWidth) / 2; // Center the text horizontally
        textY += fm.getHeight(); // Move "STOP" text below "BUS"

        // Draw the "STOP" text
        g2d.drawString(stopText, textX, textY);
    }

}
