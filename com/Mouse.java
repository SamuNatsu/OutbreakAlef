package com;

import auxiliary.*;

import java.awt.event.*;
import javax.swing.event.*;

public final class Mouse extends MouseInputAdapter {
    // Instance
    static private Mouse instance = new Mouse();
    // Mouse state
    static public final Vec2 position = new Vec2();
    static public boolean down = false;

    // Get instance
    static public Mouse getIntance() {
        return instance;
    }
    // Set mouse position
    static private void setPosition(MouseEvent e) {
        position.set(e.getX(), e.getY());
    }
    // Reset
    static public void reset() {
        down = false;
    }
    // Override MouseInputAdapter
    @Override
    public void mouseClicked(MouseEvent e) {
        setPosition(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        setPosition(e);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        setPosition(e);
    }
    @Override
    public void mousePressed(MouseEvent e) {
        down = true;
        setPosition(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        down = false;
        setPosition(e);
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        setPosition(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        setPosition(e);
    }
}
