package entity;

import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public final class EntityManager {
    // Entity pool
    private final HashMap<Long, AbstractEntity> pool = new HashMap<>(100);
    // Entity delete queue
    private final LinkedList<Long> delQueue = new LinkedList<>();

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
    // Add new entity
    public void add(AbstractEntity obj) {
        pool.put(obj.id, obj);
    }
    // Remove entity
    public void remove(long id) {
        delQueue.push(id);
    }
    // Push all entity references to drawing queue
    public void push(PriorityQueue<AbstractEntity> obj) {
        obj.addAll(pool.values());
    }
    // Move entities
    public void move(double time) {
        pool.forEach((Long id, AbstractEntity obj)-> {
            obj.onMove(time);
        });
    }
    // For each function
    public boolean forEach(Predicate<AbstractEntity> action) {
        AtomicBoolean ret = new AtomicBoolean(false);
        pool.forEach((Long id, AbstractEntity obj)-> {
            if (ret.get())
                return;
            ret.set(action.test(obj));
        });
        return ret.get();
    }
    public void forEach(BiConsumer<Long, AbstractEntity> action) {
        pool.forEach(action);
    }
    // Collide with self pool entities
    public void collideSelf() {
        pool.forEach((Long id1, AbstractEntity src)-> {
            pool.forEach((Long id2, AbstractEntity dst)-> {
                if (id1 < id2 && src.hitTest(dst))
                    src.onHit(dst);
            });
        });
    }
    // Collide with another manager
    public void collideWith(EntityManager obj) {
        pool.forEach((Long id1, AbstractEntity src)-> {
            obj.pool.forEach((Long id2, AbstractEntity dst)-> {
                if (src.hitTest(dst))
                    src.onHit(dst);
            });
        });
    }
    // Collide with an entity
    public void collideWith(AbstractEntity obj) {
        pool.forEach((Long id, AbstractEntity src)-> {
            if (src.hitTest(obj))
                src.onHit(obj);
        });
    }
    // Collide from an entity
    public void collideFrom(AbstractEntity obj) {
        pool.forEach((Long id, AbstractEntity dst)-> {
            if (obj.hitTest(dst))
                obj.onHit(dst);
        });
    }
    // Summary
    public int summary() {
        return pool.size();
    }
    // Draw hitbox
    public void drawHitbox(Graphics2D g2d) {
        pool.forEach((Long id, AbstractEntity obj)-> {
            obj.drawHitbox(g2d);
        });
    }
}
