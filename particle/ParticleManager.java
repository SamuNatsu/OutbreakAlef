package particle;

import java.awt.*;
import java.util.*;

public final class ParticleManager {
    // Pool
    private final HashMap<Long, AbstractParticle> pool = new HashMap<>();
    // Delete queue
    private final LinkedList<Long> delQueue = new LinkedList<Long>();

    // Clear
    public void clear() {
        pool.clear();
        delQueue.clear();
    }
    // Maintain delete queue
    public void maintain() {
        while (delQueue.size() != 0)
            pool.remove(delQueue.poll());
    }
    // Add new particle
    public void add(AbstractParticle obj) {
        pool.put(obj.id, obj);
    }
    // Remove particles
    public void remove(long id) {
        delQueue.add(id);
    }
    // Draw particles
    public void draw(Graphics2D g2d) {
        pool.forEach((Long id, AbstractParticle obj)-> {
            obj.draw(g2d);
        });
    }
}
