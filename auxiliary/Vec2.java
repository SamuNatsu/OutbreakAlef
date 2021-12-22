package auxiliary;

public final class Vec2 implements Cloneable {
    // Static factory base
    static private final Vec2 base = new Vec2();
    // Data
    public double x = 0, y = 0;

    // Constructor
    private Vec2() {}
    // Factories
    static synchronized public Vec2 getInstance() {
        return getInstance(0, 0);
    }
    static synchronized public Vec2 getInstance(double tx, double ty) {
        base.set(tx, ty);
        return base.clone();
    }
    // Clone
    @Override
    public Vec2 clone() {
        try {
            return (Vec2)super.clone();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // Reset
    public void reset() {
        set(0, 0);
    }
    // Set data
    public void set(double tx, double ty) {
        x = tx;
        y = ty;
    }
    // Negate
    public Vec2 negate() {
        return getInstance(-x, -y);
    }
    // Add
    public Vec2 add(Vec2 obj) {
        return getInstance(x + obj.x, y + obj.y);
    }
    public Vec2 add(double tx, double ty) {
        return getInstance(x + tx, y + ty);
    }
    // Substract
    public Vec2 sub(Vec2 obj) {
        return getInstance(x - obj.x, y - obj.y);
    }
    public Vec2 sub(double tx, double ty) {
        return getInstance(x - tx, y - ty);
    }
    // Multiplicate
    public Vec2 mul(double a) {
        return getInstance(x * a, y * a);
    }
    // Divide
    public Vec2 div(double a) {
        return getInstance(x / a, y / a);
    }
    // Dot
    public double dot(Vec2 obj) {
        return x * obj.x + y * obj.y;
    }
    public double dot(double tx, double ty) {
        return x * tx + y * ty;
    }
    // Cross
    public double cross(Vec2 obj) {
        return x * obj.y - y * obj.x;
    }
    public double cross(double tx, double ty) {
        return x * ty - y * tx;
    }
    // Get length
    public double length() {
        return Math.hypot(x, y);
    }
    // Normalize self
    public Vec2 normalize() {
        if (Utils.feq(length(), 0))
            return clone();
        return div(length());
    }
    // Create a rotated copy
    public Vec2 rotate(double a) {
        return getInstance(x * Math.cos(a) - y * Math.sin(a), x * Math.sin(a) + y * Math.cos(a));
    }
    // Resize self
    public Vec2 resize(double a) {
        return normalize().mul(a);
    }
    // Get angle
    public double angle() {
        return Math.atan2(y, x);
    }
    // Get distance from another point
    public double distance(Vec2 obj) {
        return Math.hypot(x - obj.x, y - obj.y);
    }
    // Get X data as integer
    public int intX() {
        return (int)x;
    }
    // Get Y data as integer
    public int intY() {
        return (int)y;
    }
    // To string
    public String toString() {
        return "(" + x + ',' + y + ')';
    }
}
