package com;

import java.awt.event.*;
import java.util.*;

public final class Keyboard extends KeyAdapter {
    // Internal keyboard state
    static private final HashSet<Integer> pressed = new HashSet<Integer>();

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
    }
}
