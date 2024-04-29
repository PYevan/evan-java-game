package haohaiTeam.game.element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Wall extends GameElement {

    private static BufferedImage blockImage = null;

    static {
        try {
            URL resource = Wall.class.getClassLoader().getResource("Image" + File.separator + "wall.png");
            if (Objects.isNull(blockImage) && Objects.nonNull(resource)) {
                blockImage = ImageIO.read(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Wall(int x, int y) {
        super(x, y);
        layer = 101;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(renderX + 2, renderY + 2, CELL_SIZE, CELL_SIZE);
        // Draw the block
        g2d.drawImage(blockImage, renderX, renderY, CELL_SIZE, CELL_SIZE, null);
    }

    @Override
    public void onBeingCollidedOnYou(GameElement gameElement) {
        System.out.println(this + " collision on the element " + gameElement);
        // Notify the other element about the collision if needed
        gameElement.onBeingCollidedOnYou(this);
    }
}
