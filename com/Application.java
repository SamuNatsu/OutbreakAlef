package com;

import auxiliary.*;

import java.awt.*;
import javax.swing.*;

public final class Application extends JFrame {
    // Self
    static public Application self;
    // Window size
    static public final Vec2 size = Vec2.getInstance(960, 720);

    // Constructor
    public Application() {
        // Set self
        self = this;
        // Initialize
        setIconImage(Shared.iconT);
        setResizable(false);
        setTitle("Outbreak Alef");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Add component
        getContentPane().add(new World());
        // Fix size & reset position
        pack();
        setLocationRelativeTo(null);
    }
    // Main function
    public static void main(String[] args) {
        // Run application in event dispatch thread
        EventQueue.invokeLater(() -> {
            new Application().setVisible(true);
        });
    }
}
