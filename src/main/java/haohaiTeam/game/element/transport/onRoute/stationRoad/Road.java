package haohaiTeam.game.element.transport.onRoute.stationRoad;

import haohaiTeam.game.element.GameElement;
import haohaiTeam.game.element.Player;
import haohaiTeam.game.element.PopUp;

import java.awt.*;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Road extends GameElement {
    private static final Color ROAD_COLOR = Color.black;

    public Road(int x, int y) {
        super(x, y);
        this.walkable = false;
    }

    @Override
    public void handleNearbyElement(GameElement element) {
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw black background square
        g2d.setColor(ROAD_COLOR);
        g2d.fillRect(renderX, renderY, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void onBeingCollidedOnYou(GameElement gameElement) {
        System.out.println(this + " collision on the element " + gameElement);
        if (gameElement instanceof Player) {
            // If the collision is with a Wall, show a pop-up indicating inability to walk through walls
            new PopUp(this.X, this.Y, "Not using the crosswalk is dangerous!", 500);
        }
        // Notify the other element about the collision if needed
        gameElement.onBeingCollidedOnYou(this);
    }
}
