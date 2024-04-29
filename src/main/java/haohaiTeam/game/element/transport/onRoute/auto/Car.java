package haohaiTeam.game.element.transport.onRoute.auto;

import haohaiTeam.game.element.GameElement;

import java.awt.*;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Car extends AutoMoveTransport {

    public Car(int x, int y) {
        super(x, y);
        layer = 102; // Drawn over player and roads
    }
    @Override
    public void goingToBeWalkedOverBy(GameElement gameElement) {
        //nothing here as no bus stop needed
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw the fake shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(renderX + 5, renderY + 5, CELL_SIZE, CELL_SIZE);

        // Draw a green circle
        g2d.setColor(Color.RED);
        int circleSize = CELL_SIZE; // Adjust the size of the circle as needed
        int circleX = renderX + CELL_SIZE / 2 - circleSize / 2; // Calculate the x-coordinate of the circle
        int circleY = renderY + CELL_SIZE / 2 - circleSize / 2; // Calculate the y-coordinate of the circle
        g2d.fillOval(circleX, circleY, circleSize, circleSize);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12)); // Adjust font size as needed
        FontMetrics fm = g2d.getFontMetrics();
        String taxiText = "Car";
        int textWidth = fm.stringWidth(taxiText);
        int textX = renderX + (CELL_SIZE - textWidth) / 2; // Center the text in the circle
        int textY = renderY + (CELL_SIZE / 2) + fm.getAscent() / 2; // Vertically center the text in the circle
        g2d.drawString(taxiText, textX, textY);
    }
}
