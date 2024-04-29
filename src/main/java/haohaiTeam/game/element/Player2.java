package haohaiTeam.game.element;

import haohaiTeam.game.gui.GameWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Player2 extends Player {
    private Player playerLink; // Declare playerLink as a class member

    public Player2(int x, int y) {
        super(x, y);
        beingControlled = true; // Set beingControlled to true in the constructor
        layer = 200; // Set the layer value higher than the road's layer
        walkable = true;
        Player playerLink = null;
        checkForPlayer1();

    }

    public void checkForPlayer1() {
        if (playerLink == null) {
            List<GameElement> elements = GameWindow.getElements();
            for (GameElement element : elements) {
                if (element instanceof Player) {
                    playerLink = (Player) element; // Assign the found Player instance to playerLink
                    foundByPlayer2(this);
                    ((Player) element).foundByPlayer(this);
                    break; // Stop searching once Player is found
                }
            }
        }
    }

    public void foundByPlayer2(Player gameElement) {
        System.out.println("Linking to player");
        playerLink = gameElement;
    }


    @Override
    public void handleKeyEvent(KeyEvent e) {
        ensurePlayerClose();
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0); // Exit the program gracefully
        }
        if (beingControlled) {
            System.out.println("Key pressed - Key Code: " + key); // Print the pressed key code
            switch (key) {
                case KeyEvent.VK_A:
                    direction = Direction.LEFT;
                    moveFacing();
                    break;
                case KeyEvent.VK_D:
                    direction = Direction.RIGHT;
                    moveFacing();
                    break;
                case KeyEvent.VK_W:
                    direction = Direction.UP;
                    moveFacing();
                    break;
                case KeyEvent.VK_S:
                    direction = Direction.DOWN;
                    moveFacing();
                    break;
            }


        }
    }

    private void ensurePlayerClose() {
        if (playerLink != null) {
            // Calculate the distance between this element and playerLink
            int dx = this.getLogicalPosX() - playerLink.getLogicalPosX();
            int dy = this.getLogicalPosY() - playerLink.getLogicalPosY();
            int distance = Math.abs(dx) + Math.abs(dy); // Manhattan distance for simplicity


            // Max distance allowed
            int maxDistance = 900 * 3;

            if (distance > maxDistance) { // Check distance in terms of cells, not just cells width/height
                // Calculate the direction towards playerLink
                int moveX = (int) Math.signum(-dx);
                int moveY = (int) Math.signum(-dy);
                // Move this element closer to playerLink
                this.logicalMove(moveX, moveY);
            }
        }
    }


    @Override
    protected boolean checkCollision(int nextX, int nextY) {
        List<GameElement> elements = GameWindow.getElements();
        for (GameElement element : elements) {
            // Calculate the next step
            int nextPosX = convertToLogicalPos(nextX) + this.X;
            int nextPosY = convertToLogicalPos(nextY) + this.Y;

            // Check if the next position collides with the current position of the other element
            if (nextPosX == element.X && nextPosY == element.Y) {
                element.goingToBeWalkedOverBy(this);

            }
        }
        return true; // No collision for Player2
    }

    @Override
    public void interactKeyPressedByYou() {
        // No implementation needed for Player2
    }

    @Override
    public void interactKeyPressedOnYou(GameElement gameElement) {
        // No implementation needed for Player2
    }

    @Override
    public void onBeingCollidedByYou(GameElement gameElement) {
        // No implementation needed for Player2
    }

    @Override
    public void onBeingCollidedOnYou(GameElement gameElement) {
        // No implementation needed for Player2
    }


    @Override
    public void draw(Graphics2D g2d) {
        ensurePlayerClose();
        int newCellSize = CELL_SIZE * 2 / 3;
        int newRenderX = renderX + (CELL_SIZE - newCellSize) / 2;
        int newRenderY = renderY + (CELL_SIZE - newCellSize) / 2;

        g2d.setColor(new Color(255, 0, 0, 100));
        g2d.fillOval(newRenderX, newRenderY, newCellSize, newCellSize);

        g2d.setColor(Color.BLACK);

        switch (direction) {
            case UP:
                g2d.fillOval(newRenderX + newCellSize * 8 / 30, newRenderY + newCellSize * 2 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                g2d.fillOval(newRenderX + newCellSize * 16 / 30, newRenderY + newCellSize * 2 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                break;
            case DOWN:
                g2d.fillOval(newRenderX + newCellSize * 8 / 30, newRenderY + newCellSize * 20 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                g2d.fillOval(newRenderX + newCellSize * 16 / 30, newRenderY + newCellSize * 20 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                break;
            case LEFT:
                g2d.fillOval(newRenderX + newCellSize * 4 / 30, newRenderY + newCellSize * 6 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                g2d.fillOval(newRenderX + newCellSize * 12 / 30, newRenderY + newCellSize * 6 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                break;
            case RIGHT:
                g2d.fillOval(newRenderX + newCellSize * 12 / 30, newRenderY + newCellSize * 6 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                g2d.fillOval(newRenderX + newCellSize * 20 / 30, newRenderY + newCellSize * 6 / 30, newCellSize * 4 / 30, newCellSize * 4 / 30);
                break;
            default:
                break;
        }
        g2d.setColor(new Color(200, 55, 55, 150));
        g2d.fillOval(newRenderX + newCellSize / 2 - 2, newRenderY + newCellSize - 2, newCellSize * 4 / 20, newCellSize * 4 / 20);
    }

    @Override
    public void helperDrawer(Graphics2D g2d) {
        double stepSizeScaled = 2.8;

        // Calculate the direction of movement
        int dx = (prevX - renderX) * 2;
        int dy = (prevY - renderY) * 2;

        // Move renderX and renderY towards prevX and prevY by the scaled step size
        if (Math.abs(dx) > stepSizeScaled) {
            renderX += (int) (Math.signum(dx) * (stepSizeScaled * 2 / 3)); // Adjusted calculation for x
        } else {
            renderX = prevX;
        }

        if (Math.abs(dy) > stepSizeScaled) {
            renderY += (int) (Math.signum(dy) * (stepSizeScaled * 2 / 3)); // Adjusted calculation for y
        } else {
            renderY = prevY;
        }

        // Draw using the updated positions
        draw(g2d);

        // Save the current position for the next pass
        this.prevX = X;
        this.prevY = Y;
    }

}
