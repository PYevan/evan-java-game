package haohaiTeam.game.gui;

import haohaiTeam.game.element.*;
import haohaiTeam.game.logic.GameStatus;
import haohaiTeam.game.logic.LevelScreen;
import haohaiTeam.game.logic.OverlayHUD;
import haohaiTeam.game.logic.TickGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameWindow {
    public static final int CELL_SIZE = 30; // Size of each cell in pixels
    private static final int GRID_WIDTH = 32; // Number of grid cells horizontally
    private static final int GRID_HEIGHT = 18; // Number of grid cells vertically
    public static final int FRAME_WIDTH = CELL_SIZE * GRID_WIDTH; // This is equal to 720p resolution
    public static final int FRAME_HEIGHT = CELL_SIZE * GRID_HEIGHT;
    private final TickGenerator tickGenerator; // Declare tickGenerator variable

    private double cameraOffsetX = 0;
    private double cameraOffsetY = 0;
    private final GamePanel gamePanel;
    private static List<GameElement> elements = null;

    private final OverlayHUD overlayHUD; // Reference to the HUD overlay
    private static LevelScreen levelScreen; // Reference Level Screen
    public static GameStatus gameStatus = new GameStatus(); // we should only have a GameStatus object

    private double scaleX = 1.0; // Scale factor for X-axis
    private double scaleY = 1.0; // Scale factor for Y-axis

    public GameWindow() {
        elements = new ArrayList<>();
        levelScreen = new LevelScreen(gameStatus);
        this.gamePanel = new GamePanel();
        this.overlayHUD = new OverlayHUD(gameStatus); // use the same GameStatus object


        // Initialize TickGenerator
        tickGenerator = new TickGenerator();
        tickGenerator.start(); // Start the TickGenerator

        elements.sort(Comparator.comparingInt(GameElement::getLayer));

    }


    public static void clearElements() {
        elements.forEach(GameElement::clear);
        elements.clear();
    }

    // For logic checking, game elements can access this
    public static List<GameElement> getElements() {
        return elements;
    }

    public static void addElement(GameElement element) {
        element.setCommandListener(gameStatus);
        elements.add(element);
    }


    public static void removeElement(GameElement element) {
        element.clear();
        elements.remove(element);
    }

    public void openWindow() {
        JFrame frame = new JFrame("EnviroVenture");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true); // Allow frame to be resized
        frame.setLocationRelativeTo(null); // Center frame on the screen
        frame.setFocusable(true); // Ensure frame is focusable
        frame.setContentPane(gamePanel); // Set custom GamePanel as content pane
        frame.setVisible(true);

        // Start listening to key events
        gamePanel.requestFocusInWindow();
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Forward the key event to the game window
                handleKeyEvent(e);
            }
        });

        // Add a component listener to listen for resize events
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Recalculate scale factors when the frame is resized
                scaleX = (double) frame.getContentPane().getWidth() / FRAME_WIDTH;
                scaleY = (double) frame.getContentPane().getHeight() / FRAME_HEIGHT;
            }
        });

        Timer timer = new Timer(10, e -> {
            gamePanel.repaint();
        });
        timer.start();


    }

    private class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT)); // Set preferred size
            setFocusable(true); // Allow panel to receive focus for key events
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            // Apply scaling transformation
            g2d.scale(scaleX, scaleY);
            g2d.translate(cameraOffsetX, cameraOffsetY);

            renderElements(g2d); // Render game elements
            renderHUD(g2d); // Render the HUD overlay
            renderLevelScreen(g2d);
            g2d.dispose();
        }
    }

    // Update camera position
    public void updateCameraPosition(GameElement element) {
        // Calculate the center of the screen
        double centerX = FRAME_WIDTH / 2.0;
        double centerY = FRAME_HEIGHT / 2.0;

        // Calculate the offset to center the camera
        cameraOffsetX = centerX - element.getRenderX(); // Assuming getX() returns camera's x-coordinate
        cameraOffsetY = centerY - element.getRenderY(); // Assuming getY() returns camera's y-coordinate

    }

    private void renderElements(Graphics2D g) {
        // Sort elements based on their layer
        elements.sort(Comparator.comparingInt(GameElement::getLayer));

        GameElement player = null;
        GameElement camera = null;

        // Find the player and camera in the list of elements
        for (GameElement element : elements) {
            if (element.isVisible()) {
                // Draw the element itself
                element.helperDrawer(g);
            }
            if (element instanceof Player && !(element instanceof Player2)) {
                player = element;
            } else if (element instanceof CameraEntity) {
                camera = element;
            }
        }

        // Ensure camera is linked to player only if they are not already linked
        if (player != null && camera != null && camera.getLinkedElement() != player) {
            camera.linkElement(player);
        }

        // Move the camera to the linked player's position
        if (camera != null && camera.getLinkedElement() != null) {
            camera.moveToLinked();
            updateCameraPosition(camera);
        }
    }



    private void renderHUD(Graphics g) {
        if (overlayHUD != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Translate by the negative of the camera offset
            g2d.translate(-cameraOffsetX, -cameraOffsetY);
            overlayHUD.render(g2d);

            g2d.dispose();
        }
    }

    private void renderLevelScreen(Graphics g) {
        if (levelScreen != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(-cameraOffsetX, -cameraOffsetY);
            levelScreen.render(g2d);
            g2d.dispose();
        }
    }

    private void handleKeyEvent(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } // Exit the program gracefully

        if (gameStatus.shouldShowLevelScreen()) {
            return;
        } else {
            gameStatus.start();
        }

        for (GameElement element : elements) {
            element.handleKeyEvent(e);
        }

    }

}