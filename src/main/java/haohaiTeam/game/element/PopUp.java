package haohaiTeam.game.element;

import haohaiTeam.game.gui.GameWindow;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class PopUp extends GameElement {

    private String message;
    private int renderCount;


    public PopUp(int x, int y, String message, int Duration) {
        // Call the first constructor with adjusted coordinates
        super(x, y);

        // Initialize other properties
        layer = 120;
        isVisible = true;
        walkable = true;
        renderCount = 0;
        int popUpDuration = Duration;

        // Add the PopUp to the GameWindow elements
        GameWindow.addElement(this);

        // Set the message
        this.message = message;
        setMessage(message);
        System.out.println("Showing message: " + message);

        Timer PopupSelfRemovetimer = new Timer();
        PopupSelfRemovetimer.schedule(new TimerTask() {
            @Override
            public void run() {
                PopUp.this.setRemoved(Boolean.TRUE);
            }
        }, popUpDuration);
    }


    public void setMessage(String message) {
        this.message = message;
    }


    // Method to show the PopUp with a message
    public void show(String message) {
        setMessage(message);
        System.out.println("Showing message: " + message);
    }

    // Method to hide the PopUp if needed
    public void hide() {
        this.isVisible = false;
    }


    @Override
    public void draw(Graphics2D g2d) {
        if (isVisible) {
            int boxWidth = 300; // Set the width of the box
            int boxHeight = 30; // this should adapt to message length

            // Calculate the current opacity based on the render count
            float opacity = Math.max(1.0f - ((float) renderCount / 100.0f), 0.0f);

            // Save the current composite to restore it later
            Composite originalComposite = g2d.getComposite();

            // Set the composite with the calculated opacity
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            // Set the color to black
            g2d.setColor(Color.BLACK);
            // Fill the rectangle with the black color
            g2d.fillRect(getRenderX(), getRenderY(), boxWidth, boxHeight);

            // Set the color to white
            g2d.setColor(Color.WHITE);
            // Draw the message inside the box
            g2d.drawString(message, getRenderX() + 10, getRenderY() + 20);

            // Reset the composite to its original value
            g2d.setComposite(originalComposite);

            // Increment the render count
            renderCount++;
        }
    }
}
