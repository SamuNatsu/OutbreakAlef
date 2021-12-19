package particle;

import auxiliary.*;
import com.*;

import java.awt.*;

public abstract class AbstractParticle {
    // Particle ID accumulator
    static private long acc = 0;
    // Particle id & createTime
    public final long id, createTime = System.currentTimeMillis();
    // Callback function
    public final Callback callback;
    // Position
    public Vec2 position;
    // Lasted time
    public long lastedTime = 0;

    // Constructor
    public AbstractParticle(Vec2 pos) {
        this(pos, null);
    }
    public AbstractParticle(Vec2 pos, Callback cb) {
        id = acc++;
        position = pos.clone();
        callback = cb;
    }
    // Reset accumulator
    static public void resetAccumulator() {
        acc = 0;
    }
    // Delete self
    public void die() {
        EntityPool.particle.remove(id);
        if (callback != null)
            callback.run();
    }
    // Abstract methods
    public abstract void draw(Graphics2D g2d);
}
