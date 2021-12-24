package entity;

import auxiliary.*;
import com.*;

import java.awt.*;

public final class Medicine extends AbstractEntity {
    // Static data
    static private final Image medicineT = Utils.getImage("/assets/map/medicine.png");
    static private final Vec2 medicSize = new Vec2(40, 40);
    // Entity ID accumulator
    static private long accumulator = 0;
    // Animate time counter
    private double totalTime = 0;

    // Constructor
    public Medicine(Vec2 pos) {
        super(Type.Loot, accumulator++);
        rect = new Rect(pos, medicSize);
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
        g2d.drawImage(medicineT, pos.intX(), pos.intY(), 40, 40, null);
    }
}
