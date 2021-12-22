package entity;

import auxiliary.*;
import misc.*;

import com.*;
import network.*;
import particle.*;

import java.awt.*;

public final class Mob extends AbstractEntity {
    // Entity ID accumulator
    static private long accumulator = 0;
    // Static data
    static private final Vec2 mobSize = Vec2.getInstance(30, 90);
    static private final Vec2 fwv = Vec2.getInstance(0, -2);
    // Difficulty & stable velocity
    public final double difficulty, stableVelo;
    // Full HP
    public final int fullHP;
    // Attack cool down
    public long lastAttack = SvrClt.frame;
    // Trace target
    public Player target;
    // Mob status
    public long injure = 0;
    public boolean dead = false;
    // Animate
    private final Animate ani;

    // Constructor
    public Mob(Vec2 pos, double veloAmp, double hpAmp, Player lka) {
        // Super
        super(Type.Mob, accumulator++);
        rect = Rect.getInstance(pos, mobSize.mul(Utils.fitRange(1, hpAmp, 3)));
        health = (int)(hpAmp * 100);
        damping = 0.9;
        invMass = 0.3 / Utils.fitRange(1, hpAmp, 6);
        // This
        stableVelo = veloAmp * 1.3;
        difficulty = (veloAmp + hpAmp) / 1.5;
        fullHP = health;
        target = lka;
        ani = new Animate(
            Shared.mobS, 
            pos, 
            Vec2.getInstance(128, 128).mul(Utils.fitRange(1, hpAmp, 3)), 
            (long)(4 * veloAmp)
        );
        ani.loop = true;
        // Super
        damage = (int)(difficulty * 5);
    }
    // Update target
    public void lookAt(Player pl) {
        target = pl;
    }
    // Override abstract methods
    @Override
    public void onHit(AbstractEntity obj) {
        // Attack player
        if (obj.type == Type.Player)
            obj.onInjured(this);
        // Hit on mob
        else if (obj.type == Type.Mob) {
            addImpulse(rect.position.sub(obj.rect.position).resize(5));
            obj.addImpulse(obj.rect.position.sub(rect.position).resize(5));
        }
        // Hit on wall
        else if (obj.type == Type.Wall)
            addImpulse(rect.position.sub(obj.rect.position).resize(5));
    }
    @Override
    public void onInjured(AbstractEntity obj) {
        health -= obj.damage;
        injure = System.currentTimeMillis();
        if (health <= 0) {
            onDeath(obj);
            return;
        }
        // Bullet hit back
        addImpulse(obj.velocity.resize(10));
        // Damage number particle
        EntityPool.particle.add(
            new FloatWord(
                rect.position,
                fwv,
                Long.toString(-obj.damage), 
                Color.RED));
            // Todo: Damaged animation
    }
    @Override
    public void onDeath(AbstractEntity obj) {
        // Add score for killer
        if (dead)
            return;
        ((Bullet)obj).shotFrom.addScore((int)(100 * difficulty));
        dead = true;
        // Loot
        if (Math.random() < 0.02) {
            if (Shared.enableNetwork && Shared.isSvr)
                SvrClt.socket.send(Pack.getGHM(rect.position));
            if (!Shared.enableNetwork || Shared.isSvr)
                EntityPool.loot.add(new HPMedic(rect.position));
        }
        // Remove
        EntityPool.mob.remove(id);
        EntityPool.particle.add(new Cloud(rect.position, 3, 40));
    }
    @Override
    public void onMove(double time) {
        if (target == null)
            return;
        // Custom move
        Vec2 tmp = target.rect.position.sub(rect.position).resize(stableVelo);
        velocity = velocity.add(tmp);
        rect.position = rect.position.add(velocity.mul(time));
        velocity = velocity.sub(tmp);
        // Maintain velocity
        if (velocity.mul(damping).length() > minVelo)
            velocity = velocity.mul(damping);
        else
            velocity.resize(minVelo);
        // Anti-shake
        if (velocity.length() < 1)
            velocity.reset();
        // Update image
        ani.rect.position = rect.position.sub(ani.rect.size.div(2));
    }
    @Override
    public void draw(Graphics2D g2d) {
        // Draw mob
        ani.setSprite(System.currentTimeMillis() - injure > 100 ? Shared.mobS : Shared.mobIS);
        ani.flip = rect.position.x > target.rect.position.x;
        ani.draw(g2d);
        // Draw HP bar
        Vec2 tmp = Camera.trans2LPos(rect.position.sub(20, 70));
        g2d.setColor(Color.RED);
        g2d.fillRect(tmp.intX(), tmp.intY(), health * 40 / fullHP, 6);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(tmp.intX(), tmp.intY(), 40, 6);
    }
}
