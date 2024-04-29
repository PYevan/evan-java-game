package haohaiTeam.game.element;

import haohaiTeam.game.gui.GameWindow;

import java.awt.*;
import java.util.List;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Coin extends GameElement {
    private static final Color COIN_COLOR = Color.YELLOW;

    public Coin(int x, int y) {
        super(x, y);
        this.walkable = true;
        layer = 102;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw the fake shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(renderX + 12, renderY + 10, CELL_SIZE / 2, CELL_SIZE / 2);


        int centerX = renderX + CELL_SIZE / 4; // Calculate the x-coordinate of the center of the cell
        int centerY = renderY + CELL_SIZE / 4; // Calculate the y-coordinate of the center of the cell
        g2d.setColor(COIN_COLOR);
        g2d.fillOval(centerX, centerY, CELL_SIZE / 2, CELL_SIZE / 2); // Draw the circle at the center of the cell
    }

    @Override
    public void goingToBeWalkedOverBy(GameElement gameElement) {
        List<GameElement> elements = GameWindow.getElements();
        System.out.println("Element " + gameElement + " is starting to walk on me " + this);

        int newX = 0; // Start from the far right side of the screen
        int newY = 0; // top of the window
        boolean spotOccupied;

        do {
            spotOccupied = false; // Reset flag for each new position
            for (GameElement element : elements) {
                // Check if the current element is a Gem and its position matches the new position
                if (element instanceof Coin && element.X == newX && element.Y == newY) {
                    spotOccupied = true; // Set flag to true if spot is occupied by a Gem
                    newX = newX + CELL_SIZE; // Move to the next x position and continue trying
                    break; // Exit the loop to avoid unnecessary iterations
                }
            }
        } while (spotOccupied); // Repeat until an empty spot is found
        this.commandListener.onPickedCoin(this);
        // Set the position based on the found empty spot
        this.X = newX;
        this.Y = newY; // Adjust y position based on the grid size
    }
}