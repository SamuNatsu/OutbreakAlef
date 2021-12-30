package auxiliary;

import misc.*;

import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public final class Utils {
    // Jar
    static private Utils instance = new Utils();
    static private URL getJarURL(String path) {
        return instance.getClass().getResource(path);
    }
    static public Image getImage(String path) {
        return new ImageIcon(getJarURL(path)).getImage();
    }
    static public Sprite getSprite(String path) {
        return new Sprite(getJarURL(path));
    }
    // Check a == b
    static public boolean feq(double a, double b) {
        return Math.abs(a - b) < 1e-8;
    }
    // Fit a number in range [l, r]
    static public double fitRange(double l, double i, double r) {
        return Math.min(Math.max(l, i), r);
    }
    // Random
    static private long randomSeed = System.currentTimeMillis();
    static public final Random random = new Random(randomSeed);
    static public void genSeed() {
        setSeed(System.currentTimeMillis());
    }
    static public void setSeed(long seed) {
        randomSeed = seed;
        random.setSeed(seed);
    }
    static public long getSeed() {
        return randomSeed;
    }
    static public double random(double l, double r) {
        return l + random.nextDouble() * (r - l);
    }
    // Spread
    static public Vec2 spreadDirection(Vec2 obj, double l, double r) {
        double ang = l + Math.random() * (r - l);
        return obj.rotate(ang);
    }
    static public Vec2 spreadPosition(Vec2 obj, double r) {
        double ang = Math.random() * 2 * Math.PI, len = Math.random() * r;
        return obj.add(new Vec2(len * Math.cos(ang), len * Math.sin(ang)));
    }
}
