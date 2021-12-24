package com;

import auxiliary.*;
import scene.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;

public final class World extends JPanel implements Runnable {
    // Static data
    static private final int fps = 120;
    static private final long delayTime = (long)(1e9 / fps);
    // Self reference
    static public World self;
    // Loop system
    static public final double timeSlice = 100d / fps;
    static public long frameDelta = 0;
    static public int rfps = 0;

    // Constructor
    public World() {
        // Set self
        self = this;
        // Initialize
        setPreferredSize(new Dimension(Application.size.intX(), Application.size.intY()));
        setLayout(null);
        WorldMap.bind(this);
        UIManager.put("OptionPane.buttonFont", new FontUIResource(Shared.MSYH_B15));
        UIManager.put("OptionPane.messageFont", new FontUIResource(Shared.MSYH_B15));
        // Add event listener
        addKeyListener(Keyboard.getInstance());
        addMouseListener(Mouse.getIntance());
        addMouseMotionListener(Mouse.getIntance());
        Application.self.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {}
            @Override
            public void windowGainedFocus(WindowEvent e) {
                Keyboard.reset();
                Mouse.reset();
            }
        });
        // Set focus
        setFocusable(true);
        // Register scene
        SceneManager.add("Menu", new MainMenu());
        SceneManager.add("CompMenu", new CSMenu());
        SceneManager.add("SvrMenu", new SvrMenu());
        SceneManager.add("CltMenu", new CltMenu());
        SceneManager.add("Game", new Game());
        SceneManager.add("End", new End());
        SceneManager.transfer("Menu");
    }
    // Event handling
    public void event() {
        SceneManager.event();
    }
    // Draw handling
    public void draw(Graphics2D g2d) {
        SceneManager.draw(g2d);
    }
    // Override abstract methods
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw
        draw((Graphics2D)g);
    }
    @Override
    public void addNotify() {
        super.addNotify();
        // Start painting loop
        new Thread(this).start();
    }
    @Override
    public void run() {
        long lastTime = System.nanoTime(), deltaTime, sleepTime;
        while (true) {
            event();
            repaint();
            // Delay control
            deltaTime = System.nanoTime() - lastTime;
            frameDelta = deltaTime / 1000000;
            sleepTime = delayTime - deltaTime;
            rfps = (int)(1e9 / deltaTime);
            if (rfps > fps)
                rfps = fps;
            if (sleepTime < 0)
                sleepTime = 0;
            try {
                Thread.sleep(sleepTime / 1000000, (int)(sleepTime % 1000000));
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
            lastTime = System.nanoTime();
        }
    }
}
