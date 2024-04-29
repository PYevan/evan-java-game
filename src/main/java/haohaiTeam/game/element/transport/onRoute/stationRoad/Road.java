package haohaiTeam.game.element.transport.onRoute.stationRoad;

import haohaiTeam.game.element.GameElement;
import haohaiTeam.game.element.Player;
import haohaiTeam.game.element.PopUp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Road extends GameElement {

    private static BufferedImage blockImage = null;

    static {
        try {
            URL resource = Road.class.getClassLoader().getResource("Image" + File.separator + "road.png");
            if (Objects.isNull(blockImage) && Objects.nonNull(resource)) {
                blockImage = ImageIO.read(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Road(int x, int y) {
        super(x, y);
        this.walkable = false;
    }

    @Override
    public void handleNearbyElement(GameElement element) {
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Draw the block
        g2d.drawImage(blockImage, renderX, renderY, CELL_SIZE, CELL_SIZE, null);
    }

    @Override
    public void onBeingCollidedOnYou(GameElement gameElement) {
        System.out.println(this + " collision on the element " + gameElement);
        if (gameElement instanceof Player) {
            // If the collision is with a Wall, show a pop-up indicating inability to walk through walls
            new PopUp(this.X, this.Y, "Not using the crosswalk is dangerous!", 3000);
        }
        // Notify the other element about the collision if needed
        gameElement.onBeingCollidedOnYou(this);
    }
}
