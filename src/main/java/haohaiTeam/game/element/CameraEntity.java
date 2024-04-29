package haohaiTeam.game.element;

import java.awt.*;


public class CameraEntity extends GameElement {


    public CameraEntity(int x, int y) {
        super(x, y);
        layer = 80;
    }

    @Override
    public void draw(Graphics2D g2d) {
    }

    @Override
    public void helperDrawer(Graphics2D g2d) {
        // Define a constant step to get closer to objetive
        int stepSize = 3;

        // Calculate the direction of movement
        int dx = prevX - renderX;
        int dy = prevY - renderY;

        // Move renderX and renderY towards prevX and prevY by the step size
        if (Math.abs(dx) > stepSize) {
            renderX += (int) (Math.signum(dx) * stepSize);
        } else {
            renderX = prevX;
        }

        if (Math.abs(dy) > stepSize) {
            renderY += (int) (Math.signum(dy) * stepSize);
        } else {
            renderY = prevY;
        }

        // Draw using the updated positions
        draw(g2d);

        // Save the current position for next pass
        this.prevX = X;
        this.prevY = Y;
    }

    public int getRenderX() {
        return renderX;
    }

    public int getRenderY() {
        return renderY;
    }
}


