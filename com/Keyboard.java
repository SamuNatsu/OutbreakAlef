package com;

import auxiliary.*;

import java.awt.event.*;
import java.util.*;

public final class Keyboard extends KeyAdapter {
    // Instance
    static private final Keyboard instance = new Keyboard();
    // Internal keyboard state
    static private final HashSet<Integer> pressed = new HashSet<Integer>();

    // Get instance
    static public Keyboard getInstance() {
        return instance;
    }
    // Check is key pressed
    static public boolean isPressed(int k) {
        return pressed.contains(k);
    }
    // Reset
    static public void reset() {
        pressed.clear();
    }
    // Override KeyAdapter
    @Override
    public void keyPressed(KeyEvent e) {
        pressed.add(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            Shared.debugMode = !Shared.debugMode;
            Application.self.setTitle(Shared.debugMode ? "Outbreak Alef - Debug" : "Outbreak Alef");
        }
    }
}
