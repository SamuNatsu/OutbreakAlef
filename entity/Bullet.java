package entity;

import auxiliary.*;
import com.*;
import network.*;
import particle.*;

import java.awt.*;

public final class Bullet extends AbstractEntity {
    // Static data
    static private Image bulletT = Utils.getImage("/assets/gun/bullet.png");
    static private final Vec2 bulletSize = new Vec2(5, 5);
    // Entity ID accumulator
    static private long accumulator = 0;
    // Source position
    public final Vec2 sourcePos;
    // Shoot from which player
    public final Player shotFrom;
    // Bullet range
    public final double range;

    // Constructor
    public Bullet(Vec2 pos, Vec2 v, int dmg, double rng, Player fr) {
        // Super
        super(Type.Bullet, accumulator++);
        rect = new Rect(pos.add(v.resize(40)), bulletSize);
        damage = dmg;
        velocity = Utils.spreadDirection(v, Math.toRadians(-1.5), Math.toRadians(1.5));
        // This
        sourcePos = pos.add(v.resize(40));
        shotFrom = fr;
        range = rng;
        // Synchronize
        if (Shared.enableNetwork && EntityPool.nowPlayer == fr)
            SvrClt.socket.send(Pack.getGBL(pos, velocity, dmg, rng));
    }
    public Bullet(Vec2 pos, Vec2 v, int dmg, double rng, byte pl) {
        // Super
        super(Type.Bullet, accumulator++);
        rect = new Rect(pos.add(v.resize(40)), bulletSize);
        damage = dmg;
        velocity = v.clone();
        // This
        sourcePos = pos.add(v.resize(40));
        shotFrom = (pl == 0 ? EntityPool.p1 : EntityPool.p2);
        range = rng;
    }
    // Override abstract methods
    @Override
    public void onHit(AbstractEntity obj) {
        // Hit on mob or wall
        if (obj.type == Type.Mob || obj.type == Type.Wall) {
            // Give damage
            obj.onInjured(this);
            // Die
            onDeath(null);
        }
    }
    @Override
    public void onInjured(AbstractEntity obj) {}
    @Override
    public void onDeath(AbstractEntity obj) {
        // Remove self
        EntityPool.bullet.remove(id);
        // Generate particle
        EntityPool.particle.add(new BulletBreak(rect.position));
    }
    @Override
    public void onMove(double time) {
        // Move
        move(time);
        // If out of range
        if (sourcePos.distance(rect.position) > range)
            onDeath(null);
    }
    @Override
    public void draw(Graphics2D g2d) {
        Vec2 tmp = Camera.trans2LPos(rect.position);
        g2d.rotate(velocity.angle(), tmp.x, tmp.y);
        g2d.drawImage(bulletT, tmp.intX() - 10, tmp.intY() - 10, 20, 20, null);
        g2d.rotate(-velocity.angle(), tmp.x, tmp.y);
    }
}
