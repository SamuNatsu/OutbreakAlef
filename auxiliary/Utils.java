package auxiliary;

import java.util.*;

public final class Utils {
    // Check a == b
    static public boolean feq(double a, double b) {
        return Math.abs(a - b) < 1e-10;
    }
    // Check a <= b
    static public boolean flq(double a, double b) {
        return a < b || feq(a, b);
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
        return obj.add(Vec2.getInstance(Math.cos(ang), Math.sin(ang)).resize(len));
    }
}
