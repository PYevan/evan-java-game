package haohaiTeam.game.element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Grass extends GameElement {
    private static BufferedImage blockImage = null;
    
    static {
        try {
            URL resource = Grass.class.getClassLoader().getResource("Image" + File.separator + "grass.png");
            if (Objects.isNull(blockImage) && Objects.nonNull(resource)) {
                blockImage = ImageIO.read(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Grass(int x, int y) {
        super(x, y);
        walkable = true;
        layer = 0;
    }

    public void draw(Graphics2D g2d) {
        drawWithGrass(g2d);
    }

    protected void drawWithGrass(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(renderX + 2, renderY + 2, CELL_SIZE, CELL_SIZE);
        // Draw the block
        g2d.drawImage(blockImage, renderX, renderY, CELL_SIZE, CELL_SIZE, null);
    }
}
