package haohaiTeam.game.element;

import haohaiTeam.game.gui.GameWindow;

import java.awt.*;
import java.util.List;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Player extends GameElement {
    private Player2 player2Link; // Declare player2Link as a class member

    public Player(int x, int y) {
        super(x, y);
        beingControlled = true; // Set beingControlled to true in the constructor
        layer = 100; // Set the layer value higher than the road's layer
        checkForPlayer2();
    }

    public void checkForPlayer2() {
        if (player2Link == null) {
            List<GameElement> elements = GameWindow.getElements();
            for (GameElement element : elements) {
                if (element instanceof Player2) {
                    player2Link = (Player2) element; // Assign the found Player2 instance to player2Link
                    foundByPlayer(player2Link);
                    ((Player2) element).foundByPlayer2(this);
                    break; // Stop searching once Player2 is found
                }
            }
        }
    }

    public void foundByPlayer(Player2 gameElement) {
        System.out.println("Linking to player2");
        player2Link = gameElement;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw line to Player2 if player2Link is not null
        if (player2Link != null) {
            g2d.setColor(Color.darkGray);
            g2d.drawLine(renderX + 3, renderY + CELL_SIZE / 2,
                    player2Link.renderX + CELL_SIZE / 2, player2Link.renderY + 10 + CELL_SIZE / 2);
        }
        // Draw the fake shadow
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(renderX + 5, renderY + 5, CELL_SIZE, CELL_SIZE);

        // Draw the balloon body
        g2d.setColor(Color.CYAN);
        g2d.fillOval(renderX, renderY, CELL_SIZE, CELL_SIZE);


        // Draw eyes and nose based on movement direction
        g2d.setColor(Color.BLACK);
        switch (direction) {
            case UP:
                g2d.fillOval(renderX + 8, renderY + 2, 4, 4); // Left eye
                g2d.fillOval(renderX + 16, renderY + 2, 4, 4); // Right eye
                // Nose
                g2d.setColor(Color.RED);
                g2d.fillOval(renderX + CELL_SIZE / 2 - 3, renderY - 3, 6, 6);
                break;
            case DOWN:
                g2d.fillOval(renderX + 8, renderY + 20, 4, 4); // Left eye
                g2d.fillOval(renderX + 16, renderY + 20, 4, 4); // Right eye
                // Nose
                g2d.setColor(Color.RED);
                g2d.fillOval(renderX + CELL_SIZE / 2 - 3, renderY + CELL_SIZE - 3, 6, 6);
                break;
            case LEFT:
                g2d.fillOval(renderX + 4, renderY + 6, 4, 4); // Left eye
                g2d.fillOval(renderX + 12, renderY + 6, 4, 4); // Right eye
                // Nose
                g2d.setColor(Color.RED);
                g2d.fillOval(renderX + 3, renderY + CELL_SIZE / 2 - 3, 6, 6);
                break;
            case RIGHT:
                g2d.fillOval(renderX + 12, renderY + 6, 4, 4); // Left eye
                g2d.fillOval(renderX + 20, renderY + 6, 4, 4); // Right eye
                // Nose
                g2d.setColor(Color.RED);
                g2d.fillOval(renderX + CELL_SIZE - 9, renderY + CELL_SIZE / 2 - 3, 6, 6);
                break;
            default:
                break;

        }

    }


}
