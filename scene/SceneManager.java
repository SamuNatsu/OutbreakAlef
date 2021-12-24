package scene;

import java.awt.*;
import java.util.*;

public final class SceneManager {
    // Scene pool
    static private final HashMap<String, Scene> pool = new HashMap<>();
    // Now scene
    static private String nowScene = "";
    // Next scene
    static private boolean flag = false;
    static private String nextScene = "";

    // Add scene
    static public void add(String name, Scene obj) {
        pool.put(name, obj);
    }
    // Remove scene
    static public void remove(String name) {
        pool.remove(name);
    }
    // Transfer
    static public void transfer(String name) {
        flag = true;
        nextScene = name;
    }
    static public void quickTransfer(String name) {
        if (nowScene.compareTo("") != 0)
            pool.get(nowScene).quit();
        nowScene = pool.containsKey(name) ? name : "";
        if (nowScene.compareTo("") != 0) {
            pool.get(nowScene).init();
            pool.get(nowScene).event();
        }
    }
    // Event
    static public void event() {
        if (flag) {
            flag = false;
            if (nowScene.compareTo("") != 0)
                pool.get(nowScene).quit();
            nowScene = pool.containsKey(nextScene) ? nextScene : "";
            if (nowScene.compareTo("") != 0)
                pool.get(nowScene).init();
        }
        if (pool.containsKey(nowScene))
            pool.get(nowScene).event();
    }
    // Draw
    static public void draw(Graphics2D g2d) {
        if (pool.containsKey(nowScene))
            pool.get(nowScene).draw(g2d);
    }
}
