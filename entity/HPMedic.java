package entity;

import auxiliary.*;
import com.*;

import java.awt.*;

public final class HPMedic extends AbstractEntity {
    // Entity ID accumulator
    static private long accumulator = 0;
    // Static 
    static private final Vec2 medicSize = Vec2.getInstance(40, 40);
    // Animate time counter
    private double totalTime = 0;

    // Constructor
    public HPMedic(Vec2 pos) {
        super(Type.Loot);
        id = accumulator++;
        rect = Rect.getInstance(pos, medicSize);
    }
    // Override abstract methods
    @Override
    public void onHit(AbstractEntity obj) {
        // If hit on player
        if (obj.type == Type.Player) {
            // Cure player
            ((Player)obj).cure(15);
            // Die
            onDeath(null);
        }
    }
    @Override
    public void onInjured(AbstractEntity obj) {}
    @Override
    public void onDeath(AbstractEntity obj) {
        // Remove self
        EntityPool.loot.remove(id);
    }
    @Override
    public void onMove(double time) {
        // Accumulate time
        totalTime += time;
    }
    @Override
    public void draw(Graphics2D g2d) {
        Vec2 pos = Camera.trans2LPos(rect.position).sub(20, 20).add(0, 5 * Math.sin(totalTime / 20));
        g2d.drawImage(Shared.hpMedicT, pos.intX(), pos.intY(), 40, 40, null);
    }
}
