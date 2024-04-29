package haohaiTeam.game.element.transport;


import haohaiTeam.game.element.GameElement;

import java.awt.*;
import java.awt.image.BufferedImage;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;

public class Bike extends TransportMode {
    private static final Color BIKE_COLOR = Color.darkGray;
    private BufferedImage bikeImage; // this will be used later when we need use the real pics

    public Bike(int x, int y) {
        super(x, y); // assume 15km per hour and carbon footprint of 1.0 kg per km
        speed = 10;
        carbonFootprint = 1;
        walkable = true;
        layer = 105; // Default layer
    }

    @Override
    public void interactKeyPressedOnYou(GameElement gameElement) {
        System.out.println(gameElement + " wants to interact with" + this);
        if (gameElement.getLinkedElement() == this) {
            // If gameElement is already linked to this, unlink them
            gameElement.unlinkElement();
            gameElement.setMoveInterval(200);
        } else {
            // If gameElement is not linked to this, link them
            gameElement.linkElement(this);
            gameElement.setMoveInterval(100);
        }
    }

    // We need to implement something for the real stepping on
//    @Override
//    public void goingToBeWalkedOverBy(GameElement gameElement) {
//        this.interactKeyPressedOnYou(gameElement);
//    }
    @Override
    public void draw(Graphics2D g2d) {
        int wheelRadius = CELL_SIZE / 4;
        int frameSize = wheelRadius * 2;

        // Calculate the positions of the wheel centers and other key points
        int frontWheelCenterX = renderX + (CELL_SIZE / 4);
        int rearWheelCenterX = renderX + (3 * CELL_SIZE / 4);
        int wheelCenterY = renderY + CELL_SIZE - wheelRadius;
        int seatX = renderX + (CELL_SIZE / 2);
        int seatY = renderY + (CELL_SIZE / 2);

        // Draw the wheels
        g2d.setColor(Color.BLACK);
        g2d.fillOval(frontWheelCenterX - wheelRadius, wheelCenterY - wheelRadius, 2 * wheelRadius, 2 * wheelRadius);
        g2d.fillOval(rearWheelCenterX - wheelRadius, wheelCenterY - wheelRadius, 2 * wheelRadius, 2 * wheelRadius);

        // Draw the bike frame
        g2d.setColor(BIKE_COLOR);
        g2d.drawLine(frontWheelCenterX, wheelCenterY, seatX, seatY); // Front triangle
        g2d.drawLine(rearWheelCenterX, wheelCenterY, seatX, seatY); // Rear triangle
        g2d.drawLine(seatX, seatY, seatX, renderY); // Seat post
        g2d.drawLine(frontWheelCenterX, wheelCenterY, rearWheelCenterX, wheelCenterY); // Down tube
        g2d.drawLine(seatX, seatY, frontWheelCenterX, renderY + CELL_SIZE / 3); // Top tube

        // Draw the bike seat
        g2d.fillRect(seatX - (wheelRadius / 2), renderY + (CELL_SIZE / 2) - (wheelRadius / 4), wheelRadius, wheelRadius / 4);

        // Draw the handlebars
        int handleBarY = renderY + CELL_SIZE / 3;
        g2d.drawLine(frontWheelCenterX, handleBarY, frontWheelCenterX - wheelRadius, handleBarY - wheelRadius / 2);
        g2d.drawLine(frontWheelCenterX, handleBarY, frontWheelCenterX + wheelRadius, handleBarY - wheelRadius / 2);
        g2d.drawLine(seatX, seatY, frontWheelCenterX, handleBarY); // Handlebar stem

        // Draw the pedals
        int pedalCenterX = seatX;
        int pedalCenterY = wheelCenterY;
        int pedalWidth = wheelRadius / 2;
        g2d.fillRect(pedalCenterX - (pedalWidth / 2), pedalCenterY - (pedalWidth / 4), pedalWidth, pedalWidth / 2);

        // Connect the pedals to the wheels
        g2d.drawLine(pedalCenterX, pedalCenterY, rearWheelCenterX, wheelCenterY);
    }


}


