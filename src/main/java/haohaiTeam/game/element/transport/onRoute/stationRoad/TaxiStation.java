package haohaiTeam.game.element.transport.onRoute.stationRoad;

import haohaiTeam.game.element.GameElement;
import haohaiTeam.game.element.transport.onRoute.auto.Taxi;

import java.awt.*;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class TaxiStation extends Station {
    public static final double CO2_PER_CELL = 0.3;

    public TaxiStation(int x, int y) {
        super(x, y);
    }

    @Override
    public void goingToBeWalkedOverBy(GameElement gameElement) {
        if (gameElement instanceof Taxi transport) {
            correctStationMethod(transport);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw black background square
        g2d.setColor(Color.orange);
        g2d.fillRect(renderX, renderY, CELL_SIZE, CELL_SIZE);

        // Add "taxi" text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10)); // Adjust font size to fit in the cell
        FontMetrics fm = g2d.getFontMetrics();
        String busText = "TAXI";
        int textWidth = fm.stringWidth(busText);
        int textX = renderX + (CELL_SIZE - textWidth) / 2; // Center the text horizontally
        int textY = renderY + (CELL_SIZE / 2) + fm.getAscent() / 2 - 5; // Position text in the upper half of the cell

        // Draw the "BUS" text
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
