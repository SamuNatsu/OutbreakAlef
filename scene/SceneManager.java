package scene;

import java.awt.*;
import java.util.*;

public final class SceneManager {
    // Scene pool
    static private final HashMap<String, AbstractScene> pool = new HashMap<>();
    // Now scene
    static private String nowScene = "";

    // Add scene
    static public void add(String name, AbstractScene obj) {
        pool.put(name, obj);
    }
    // Remove scene
    static public void remove(String name) {
        pool.remove(name);
    }
    // Set
    static public void set(String name) {
        nowScene = pool.containsKey(name) ? name : "";
    }
    // Transfer
    static synchronized public void transfer(String name) {
        if (nowScene.compareTo("") != 0)
            pool.get(nowScene).quit();
        nowScene = pool.containsKey(name) ? name : "";
        if (nowScene.compareTo("") != 0)
            pool.get(nowScene).init();
    }
    // Event
    static synchronized public void event() {
        if (pool.containsKey(nowScene))
            pool.get(nowScene).event();
    }
    // Draw
    static synchronized public void draw(Graphics2D g2d) {
        if (pool.containsKey(nowScene))
            pool.get(nowScene).draw(g2d);
    }
}
