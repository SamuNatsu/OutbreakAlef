package auxiliary;

public final class Rect implements Cloneable {
    // Position & size
    public Vec2 position, size;

    // Constructor
    public Rect() {
        position = new Vec2();
        size = new Vec2();
    }
    public Rect(Vec2 pos, Vec2 sz) {
        position = pos.clone();
        size = sz.clone();
    }
    public Rect(double x, double y, double w, double h) {
        position = new Vec2(x, y);
        size = new Vec2(w, h);
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
        return !(
            position.x > obj.x ||
            position.x + size.x < obj.x ||
            position.y > obj.y ||
            position.y + size.y < obj.y
        );
    }
    // Check if rectangle intersects with another rectangle
    public boolean intersects(Rect obj) {
        return !(
            position.x > obj.position.x + obj.size.x ||
            position.x + size.x < obj.position.x ||
            position.y > obj.position.y + obj.size.y ||
            position.y + size.y < obj.position.y
        );
    }
    // To string
    public String toString() {
        return "[" + position + ',' + size + ']';
    }
}
