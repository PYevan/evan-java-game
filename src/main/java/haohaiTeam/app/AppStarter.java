package haohaiTeam.app;

import haohaiTeam.game.GameStarter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AppStarter {

    public static void main(String[] args) {
        // Create an instance of the AppStarter class and run it
        System.out.println("App is starting...");
        startApp();
    }

    public static void startApp() {
        // font
        try {
            File fontFile = new File("src/main/resources/Text/Quinquefive-ALoRM.ttf");
            Font gameFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(gameFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("EnviroVenture");
        // buttons
        JButton runGameButton = new JButton("Play Game");
        JButton aboutButton = new JButton("About");
        JButton howToPlayButton = new JButton("How to Play");
        // Area for displaying text with different buttons
        JTextArea infoTextArea = new JTextArea();
        // Set the custom font for all Swing components
        Font buttonFont = loadCustomFont(10);
        Font textFont = loadCustomFont(10);
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        //Loading text
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Text/initial_text.txt"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            infoTextArea.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            infoTextArea.setText("Failed to load initial text.");
        }
        // add button functionality (Play Game)
        Color bgButtonColour = new Color(0, 102, 17);
        runGameButton.setBackground(bgButtonColour);
        runGameButton.setForeground(Color.WHITE);
        runGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // main method of the GameStarter class
                GameStarter.main(new String[]{});
            }
        });
        // add button functionality (About)
        aboutButton.setBackground(bgButtonColour);
        aboutButton.setForeground(Color.WHITE);
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTextFromFile("src/main/resources/Text/about.txt", infoTextArea);
            }
        });
        // add button functionality (How to Play)
        howToPlayButton.setBackground(bgButtonColour);
        howToPlayButton.setForeground(Color.WHITE);
        howToPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTextFromFile("src/main/resources/Text/how_to_play.txt", infoTextArea);
            }
        });
        // panel to hold buttons
        JPanel buttonPanel = new JPanel();
        runGameButton.setFont(buttonFont);
        aboutButton.setFont(buttonFont);
        howToPlayButton.setFont(buttonFont);
        infoTextArea.setFont(textFont);
        buttonPanel.setLayout(new GridLayout(3, 1));
        buttonPanel.add(runGameButton);
        runGameButton.setFocusable(false);
        aboutButton.setFocusable(false);
        howToPlayButton.setFocusable(false);
        infoTextArea.setFocusable(false);
        buttonPanel.add(aboutButton);
        buttonPanel.add(howToPlayButton);
        frame.getContentPane().add(buttonPanel, BorderLayout.WEST);
        frame.getContentPane().add(infoTextArea, BorderLayout.CENTER);
        frame.setSize(450, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void loadTextFromFile(String filePath, JTextArea textArea) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            textArea.setText(sb.toString());
        } catch (IOException error) {
            error.printStackTrace();
            textArea.setText("Failed to load text from file.");
        }
    }

    private static Font loadCustomFont(float fontSize) {
        try {
            File fontFile = new File("src/main/resources/Text/Quinquefive-ALoRM.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return baseFont.deriveFont(fontSize);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}