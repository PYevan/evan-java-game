package haohaiTeam.game.element.transport.onRoute.stationRoad;

import haohaiTeam.game.element.GameElement;

import java.awt.*;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Crosswalk extends Road {
    public Crosswalk(int x, int y) {
        super(x, y);
        this.walkable = true;
    }

    @Override
    public void onBeingCollidedOnYou(GameElement gameElement) {
//        // Get the direction of the other element
//        Direction otherDirection = gameElement.getDirection();
//        gameElement.setBeingControlled(false); // Your free will has been stolen by a crosswalk :(
//        // Change the direction of the collided element to the direction of this element
//        gameElement.setDirection(Direction.UP);
//        System.out.println("Dont forget to look before crossing" );
//        // There is an issue with using thread sleep tick must be implemented
//        gameElement.setBeingControlled(true); // Your free will has been given back
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw black background square
        g2d.setColor(Color.BLACK);
        g2d.fillRect(renderX, renderY, CELL_SIZE, CELL_SIZE);

        // Draw white lines on the road for the crosswalk
        g2d.setColor(Color.WHITE);

        // Draw the first line of the crosswalk
        g2d.fillRect(renderX + CELL_SIZE / 4, renderY, CELL_SIZE / 10, CELL_SIZE);

        // Draw the second line of the crosswalk
        g2d.fillRect(renderX + CELL_SIZE / 2, renderY, CELL_SIZE / 10, CELL_SIZE);

        // Draw the third line of the crosswalk
        g2d.fillRect(renderX + 3 * CELL_SIZE / 4, renderY, CELL_SIZE / 10, CELL_SIZE);
    }
}