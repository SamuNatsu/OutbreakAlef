package auxiliary;

public final class Rect implements Cloneable {
    // Static factory base
    static private final Rect base = new Rect();
    // Position & size
    public Vec2 position = Vec2.getInstance(), size = Vec2.getInstance();

    // Constructor
    private Rect() {}
    // Factories
    static synchronized public Rect getInstance() {
        return getInstance(0, 0, 0, 0);
    }
    static synchronized public Rect getInstance(Vec2 pos, Vec2 sz) {
        base.set(pos, sz);
        return base.clone();
    }
    static synchronized public Rect getInstance(double x, double y, double w, double h) {
        base.set(x, y, w, h);
        return base.clone();
    }
    // Clone
    @Override
    public Rect clone() {
        try {
            Rect tmp = (Rect)super.clone();
            tmp.set(position, size);
            return tmp;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Reset
    public void reset() {
        set(0, 0, 0, 0);
    }
    // Set data
    public void set(Vec2 pos, Vec2 sz) {
        position = pos.clone();
        size = sz.clone();
    }
    public void set(double x, double y, double w, double h) {
        position.set(x, y);
        size.set(w, h);
    }
    // Check if rectangle contains point
    public boolean contains(Vec2 obj) {
        return 
            Utils.flq(position.x, obj.x) &&
            Utils.flq(position.y, obj.y) &&
            Utils.flq(obj.x, position.x + size.x) &&
            Utils.flq(obj.y, position.y + size.y);
    }
    // Check if rectangle intersects with another rectangle
    public boolean intersects(Rect obj) {
        return !(
            position.x > obj.position.x + obj.size.x ||
            position.x + size.x < obj.position.x ||
            position.y > obj.position.y + obj.size.y ||
            position.y + size.y < obj.position.y);
    }
    // To string
    public String toString() {
        return "[" + position + ',' + size + ']';
    }
}
