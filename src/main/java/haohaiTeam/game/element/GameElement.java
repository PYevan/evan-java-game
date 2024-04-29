package haohaiTeam.game.element;

import haohaiTeam.game.gui.GameWindow;
import haohaiTeam.game.input.CommandListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static haohaiTeam.game.gui.GameWindow.CELL_SIZE;
import static haohaiTeam.game.gui.GameWindow.gameStatus;

public abstract class GameElement implements CommandListener {

    
    /// The basics of the Game element
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<GameElement> elements;
    public int renderX; // These x coordinates uses the pixel position for drawing
    public int renderY; // These y coordinates uses the pixel position for drawing
    public int previousRenderX; // These x coordinates uses the pixel position for drawing
    public int previousRenderY; // These x coordinates uses the pixel position for drawing
    public int X; // These y coordinates uses the pixel position for drawing
    public int Y; // These y coordinates uses the pixel position for drawing
    public int prevX; // These x coordinates uses the pixel position for drawing
    public int prevY; // These y coordinates uses the pixel position for drawing
    public int layer; // This refers if the that is going to be drawn, higher number higher preference.
    public boolean walkable; // This refers if the element can be walked though
    public int speed; // This refers to the maximum speed of the element
    public GameElement linkedElement; // This to link two element on the same cell so one follows the other, it also overdrives the control of the other
    public boolean beingControlled = false; // Flag to enable key control by keys
    public boolean isVisible; // hide the visibility
    public boolean playerOnTop;
    public Direction direction; // Direction the element is facing
    public CommandListener commandListener;
    public int tickCount = 0;
    private boolean canMove = true;
    
    protected boolean removed = false;

    private long lastMoveTime;
    private int moveInterval;  // Time in milliseconds required to pass before the next move can happen

    public GameElement(int x, int y) {
        this.renderX = x; // just for rendering
        this.renderY = y;
        this.speed = 0;
        this.walkable = false;
        this.layer = 99; // Default layer
        this.isVisible = true;
        this.playerOnTop = false;
        this.direction = Direction.DOWN; // Default direction
        this.commandListener = null; // we need to start this later
        this.X = x; // Real pixel position
        this.Y = y; // Real pixel position
        this.moveInterval = 200;  // default interval set as 200 ms

    }

    public void setCommandListener(CommandListener commandListener) { // set up a command listen
        this.commandListener = commandListener;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void toggleAutoMove() {
    }

    public void toggleAutoStation() {
    }

    // Method to set move interval
    public void setMoveInterval(int ms) {
        moveInterval = ms;
    }

    // Getter for moveInterval
    public int getMoveInterval() {
        return moveInterval;
    }

    public void clear() {
    }



    // direction that the element is facing
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }


    /// Moving and locating elements
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //These two can be used to locate the x and y of the element in the logical cell grid
    // Should be the only one using convert cell_size
    public int convertToLogicalPos(int unitToConvert) {
        return unitToConvert * CELL_SIZE;
    }

    public int getLogicalPosX() {
        return convertToLogicalPos(X);
    }

    public int getLogicalPosY() {
        return convertToLogicalPos(Y);
    }

    //These implement a return for the real grid position, for the logic implementation
    public void setToLogicalPosX(int posX) {
        X += convertToLogicalPos(posX);
    }

    public void setToLogicalPosY(int posY) {
        Y += convertToLogicalPos(posY);
    }

    public void setToLogicalPos(int posX, int posY) {
        setToLogicalPosX(posX);
        setToLogicalPosY(posY);
    }

    // Legacy move, this is to move the elements using pixels
    public void logicalMove(int dx, int dy) {
        // Update the actual position of the object to the calculated next position.
        if (checkCollision(dx, dy)) {
            setToLogicalPosX(dx);
            setToLogicalPosY(dy);
            if (this.linkedElement != null) {
                linkedElement.setToLogicalPosX(dx);
                linkedElement.setToLogicalPosY(dy);
            }
        }
    }

    public void moveLogical(int dx, int dy) {
        // Update the actual position of the object based on logic
        if (checkCollision(dx, dy)) {
            setToLogicalPosX(dx);
            setToLogicalPosY(dy);
        }
    }

    /// Check for the direction of the element
    // Getter method for direction
    public Direction getDirection() {
        return direction;
    }

    // Setter method for direction
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private boolean isWithinBounds(int nextX, int nextY) {
        // Check if the next position is within the game window bounds
        return (nextX >= 0 && nextX < GameWindow.FRAME_WIDTH && nextY >= 0 && nextY < GameWindow.FRAME_HEIGHT);
    }

    // Collision checker and element parsing to create reactions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected boolean checkCollision(int nextX, int nextY) {
        List<GameElement> elements = GameWindow.getElements();
        if (beingControlled) {
            for (GameElement element : elements) {
                if (element instanceof Grass) {
                    continue;
                }
                // Calculate the next step
                int nextPosX = convertToLogicalPos(nextX) + this.X;
                int nextPosY = convertToLogicalPos(nextY) + this.Y;

                // Check if the next position collides with the current position of the other element
                if (nextPosX == element.X && nextPosY == element.Y) {
                    if (element.walkable) {
                        System.out.println("Collision expected with element but is walkable: " + element);
                        if (element instanceof Player && element.X == this.X && element.Y == this.Y) {
                            element.playerOnTop = true;
                        }
                        System.out.println("Element " + element + " is being stepped by" + this);
                        element.goingToBeWalkedOverBy(this);
                        return true; // No collision detected, return false
                    } else {
                        onBeingCollidedByYou(element); // Tell the element that something is crashing onto him
                        return false; // Collision detected, return true
                    }
                }
            }
        }
        return true; // No collision detected, return false
    }

    public void nearbyDetectorCall() {
        List<GameElement> elements = GameWindow.getElements();
        int x = this.X;
        int y = this.Y;
        int detectionRange = CELL_SIZE;

        for (GameElement element : elements) {
            // Check if the element is within the detection range
            if (Math.abs(element.X - x) <= detectionRange && Math.abs(element.Y - y) <= detectionRange) {
                if (element != this) {
                    element.handleNearbyElement(this);
                }
            }
        }
    }

    public void handleNearbyElement(GameElement element) {
        System.out.println("Nearby element detected: " + element);
    }

    ////  Linking elements
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void linkElement(GameElement other) {
        this.linkedElement = other;
        System.out.println(this + " has added linked to " + other);
        this.linkedElement = other; // Link the other element back
    }

    // Get the linkedElement
    public GameElement getLinkedElement() {
        return this.linkedElement;
    }

    // Unlink elements to release the link, so it can be used again
    public void unlinkElement() {
        if (this.linkedElement != null) {
            this.linkedElement = null;
        }
    }

    public void moveToLinked() {
        // Move this element to the same position as the linked element
        this.X = linkedElement.X;
        this.Y = linkedElement.Y;
    }

    // Toggle the link state
    public void toggleLink(GameElement other) {
        if (this.linkedElement == other) {
            unlinkElement();
        } else {
            linkElement(other);
        }
    }

    ////  Keys controls
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isBeingControlled() {
        return beingControlled;
    }

    public void setBeingControlled(boolean beingControlled) {
        this.beingControlled = beingControlled;
    }

    public void handleKeyEvent(KeyEvent e) {

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastMoveTime < moveInterval) {
            return;  // Skip this move because the interval has not passed
        }

        int key = e.getKeyCode();

        if (beingControlled) {
            System.out.println("Key pressed - Key Code: " + key); // Print the pressed key code

            boolean validKey = false;
            switch (key) {
                case KeyEvent.VK_LEFT:
                    direction = Direction.LEFT;
                    validKey = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    direction = Direction.RIGHT;
                    validKey = true;
                    break;
                case KeyEvent.VK_UP:
                    direction = Direction.UP;
                    validKey = true;
                    break;
                case KeyEvent.VK_DOWN:
                    direction = Direction.DOWN;
                    validKey = true;
                    break;
                case KeyEvent.VK_SPACE:
                    interactKeyPressedByYou(); // Trigger interaction
                    validKey = true;
                    break;
            }
            if (validKey) {
                nearbyDetectorCall();
                resetMovementControl();  // Reset movement control after a delay
                canMove = false;  // Disable further movement until reset
            }

        }
        lastMoveTime = currentTime;
    }

    private long keyPressTimestamp = 0;

    public void resetMovementControl() {
        if (canMove) {
            gameStatus.co2increase = false;
            final Direction lastDirection = direction;
            moveFacing();
            keyPressTimestamp = System.currentTimeMillis(); // Capture the timestamp when keypress occurs
            long currentTime = System.currentTimeMillis();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            long elapsedTime = currentTime - keyPressTimestamp;
                            if (lastDirection != direction) {
                                moveFacing();
                            }
                            if (elapsedTime < 150) {
                                resetMovementControl();
                                System.out.println("Less than 150ms passed since keypress, missing input inserted");
                            }
                            canMove = true;
                        }
                    },
                    149 // Set delay this is to be forgiving about missing inputs.
            );
        }
    }

    public void moveFacing() {
        int[] direction = getDirectionBasedMovement();
        logicalMove(direction[0], direction[1]);
    }

    public void getFacingXY() {
        int[] direction = getDirectionBasedMovement();
        // This could implement giving back the item that is looking at or the XY
    }

    private int[] getDirectionBasedMovement() {
        int[] movement = new int[2];
        // Adjust movement direction based on the player's direction
        switch (direction) {
            case UP:
                movement[1] = -1; // dy = -1
                break;
            case DOWN:
                movement[1] = 1;  // dy = 1
                break;
            case LEFT:
                movement[0] = -1; // dx = -1
                break;
            case RIGHT:
                movement[0] = 1;  // dx = 1
                break;
        }
        return movement;
    }




    ////  Drawing methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void draw(Graphics2D g2d) {
    }

    public int getLayer() {
        return layer;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean bool) {
        if (isVisible != bool) { // Check if the visibility is actually changing
            isVisible = bool;
            System.out.println("Visibility has changed for: " + this);
        }
    }

    public void toggleVisibility() {
        // Toggle the visibility of the element
        isVisible = !isVisible;
        System.out.println("Visibility of the element has changed to: " + isVisible);
    }


    //// Interactions with the rest of the elements
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void goingToBeWalkedOverBy(GameElement gameElement) {
        // Triggered when something walks over this element, probably a player
        //     @Override on your class, someone / something on top
    }

    public void interactKeyPressedByYou() {
        System.out.println(this + " wants to interact");
        // this.moveFacing();
        int currentLogicalPosX = this.getLogicalPosX();
        int currentLogicalPosY = this.getLogicalPosY();
        List<GameElement> elements = GameWindow.getElements();
        for (GameElement element : elements) {
            // Check the element that this is on
            if (element != this && element.getLogicalPosX() == currentLogicalPosX && element.getLogicalPosY() == currentLogicalPosY) {
                // Call a method on the element to handle the interaction
                element.interactKeyPressedOnYou(this);
            }
        }
    }
    public void interactKeyPressedOnYou(GameElement gameElement) {
        System.out.println(gameElement + " wants to interact with" + this);
        // Override in your class
    }
    public void onBeingCollidedByYou(GameElement gameElement) {
        System.out.println(this + " collision on the element " + gameElement);
        //// Hey other class, this silly guy wants to go through you! , and you are not walkable
        gameElement.onBeingCollidedOnYou(this); /// tell him!

    }
    public void onBeingCollidedOnYou(GameElement gameElement) {
        // Create a reaction here if needed
    }


    /// Fancy stupid hack for moving things smooth, I will see you all in git blame
    public void helperDrawer(Graphics2D g2d) {
        // Define a constant step to get closer to objetive
        int stepSize = 4;

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

    @Override
    public void onPickedCoin(GameElement element) {

    }

    @Override
    public void onPickedGem(GameElement element) {

    }

    @Override
    public void onTick() {
        tickCount++;
        if (tickCount % 12000 == 0) {
            System.out.println("Tic is working but only for the game element ");
        }
    }

    @Override
    public void onCO2Generated(int value) {

    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}