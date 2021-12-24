package misc;

import auxiliary.*;

import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public final class Sprite {
    // External texture
    private final Image texture;
    // Frames
    private long accumulator = 0;
    private HashMap<Long, Rect> frame = new HashMap<>();

    // Constructor
    public Sprite(String path) {
        texture = new ImageIcon(path).getImage();
    }
    public Sprite(URL url) {
        texture = new ImageIcon(url).getImage();
    }
    // Get texture
    public Image getTexture() {
        return texture;
    }
    // Add a frame & get frame id
    public long addFrame(Rect obj) {
        frame.put(accumulator, obj);
        return accumulator++;
    }
    // Remove a frame
    public void removeFrame(long id) {
        frame.remove(id);
    }
    // Remove all frame
    public void removeAll() {
        accumulator = 0;
        frame.clear();
    }
    // Get frame count
    public int frameCount() {
        return frame.size();
    }
    // Paint frame
    public void paint(Graphics2D g2d, long id, Vec2 pos) {
        Rect tmp = frame.get(id);
        g2d.drawImage(
            texture,
            pos.intX(), pos.intY(),
            pos.intX() + tmp.size.intX(), pos.intY() + tmp.size.intY(),
            tmp.position.intX(), tmp.position.intY(),
            tmp.position.intX() + tmp.size.intX(), tmp.position.intY() + tmp.size.intY(),
            null);
    }
    public void paint(Graphics2D g2d, long id, Rect rect) {
        Rect tmp = frame.get(id);
        g2d.drawImage(
            texture, 
            rect.position.intX(), rect.position.intY(),
            rect.position.intX() + rect.size.intX(), rect.position.intY() + rect.size.intY(),
            tmp.position.intX(), tmp.position.intY(),
            tmp.position.intX() + tmp.size.intX(), tmp.position.intY() + tmp.size.intY(),
            null);
    }
}
