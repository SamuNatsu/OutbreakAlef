package entity;

import auxiliary.*;
import com.*;

import java.awt.*;

public abstract class AbstractEntity {
    // Type define
    static protected enum Type {
        Player(Color.WHITE), 
        Mob(Color.RED),
        Bullet(Color.BLACK), 
        Wall(Color.CYAN), 
        Loot(Color.PINK);
        // Hitbox color
        public final Color hitboxColor;

        // Constructor
        private Type (Color c) {
            hitboxColor = c;
        }
    }
    // Entity type
    public final Type type;
    // Entity id
    public final long id;
    // Entity hitbox
    public Rect rect;
    // Entity health & damage
    public int health = 100, damage = 0;
    // Physics properties
    public double invMass = 1, damping = 1, minVelo = 0, maxVelo = Double.MAX_VALUE;
    public Vec2 velocity = new Vec2();

    // Constructor
    public AbstractEntity(Type tp, long i) {
        // Set type
        type = tp;
        // Set ID
        id = i;
    }
    // Entity move
    public void move(double time) {
        // Position transfer
        rect.position = rect.position.add(velocity.mul(time));
        // Damping
        if (velocity.mul(damping).length() > minVelo)
            velocity = velocity.mul(damping);
        else
            velocity.resize(minVelo);
        // Anti-shake
        if (velocity.length() < 1)
            velocity.reset();
    }
    // Add impulse to entity
    public void addImpulse(Vec2 obj) {
        velocity = velocity.add(obj.mul(invMass));
        // Velocity limitate
        if (velocity.length() > maxVelo)
            velocity = velocity.resize(maxVelo);
    }
    public void addImpulse(double x, double y) {
        velocity = velocity.add(x * invMass, y * invMass);
        // Velocity limitate
        if (velocity.length() > maxVelo)
            velocity = velocity.resize(maxVelo);
    }
    // Hit test
    public boolean hitTest(AbstractEntity obj) {
        Rect tmp1 = rect.clone(), tmp2 = obj.rect.clone();
        tmp1.position = tmp1.position.sub(rect.size.div(2));
        tmp2.position = tmp2.position.sub(obj.rect.size.div(2));
        return tmp1.intersects(tmp2);
    }
    // Draw hitbox
    public void drawHitbox(Graphics2D g2d) {
        // Paint border
        Vec2 tmp = Camera.trans2LPos(rect.position.sub(rect.size.div(2)));
        g2d.setColor(type.hitboxColor);
        g2d.drawRect(tmp.intX(), tmp.intY(), rect.size.intX(), rect.size.intY());
        // Paint center point
        g2d.setColor(Color.BLUE);
        tmp = Camera.trans2LPos(rect.position);
        g2d.fillRect(tmp.intX() - 1, tmp.intY() - 1, 2, 2);
    }
    // Abstract methods
    public abstract void onHit(AbstractEntity obj);
    public abstract void onInjured(AbstractEntity obj);
    public abstract void onDeath(AbstractEntity obj);
    public abstract void onMove(double time);
    public abstract void draw(Graphics2D g2d);
}
