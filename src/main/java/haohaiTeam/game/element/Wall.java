package haohaiTeam.game.element;

import java.awt.*;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Wall extends GameElement {
    private static final Color WALL_COLOR = Color.GRAY;

    public Wall(int x, int y) {
        super(x, y);
        layer = 101;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw the fake shadow
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(renderX + 10, renderY + 10, CELL_SIZE, CELL_SIZE);
        g2d.setColor(WALL_COLOR);
        g2d.fillRect(renderX, renderY, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void onBeingCollidedOnYou(GameElement gameElement) {
        System.out.println(this + " collision on the element " + gameElement);
        // Notify the other element about the collision if needed
        gameElement.onBeingCollidedOnYou(this);
    }
}
