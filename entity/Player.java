package entity;

import auxiliary.*;
import com.*;
import entity.Wall.*;
import misc.*;
import network.*;
import particle.*;

import java.awt.*;
import java.awt.event.*;

public final class Player extends AbstractEntity {
    // Entity ID accumulator
    static private long accumulator = 0;
    // Static
    static private final Color cureColor = new Color(0x007F00);
    static private final Rect plSize = Rect.getInstance(0, 0, 30, 90);
    static private final Vec2 cdGun = Vec2.getInstance(100, 140), fwv = Vec2.getInstance(0, -2);
    // Gun type define
    static public enum Gun {AR, SG, SMG};
    // Gun type
    public Gun gunType = Gun.AR;
    // Last shoot time & score
    public long lastShootTime = System.currentTimeMillis();
    public int score = 0;
    // If gun released
    public boolean gunRelease = true;
    // Player state
    public boolean moving = false;
    public long injuring = 0;
    public int life = 0;
    // Player texture
    public final Image[] texture = new Image[2];
    public final Sprite[] sprite = new Sprite[2];
    private final Animate ani;

    // Constructor
    public Player(Image t1, Image t2, Sprite sp1, Sprite sp2) {
        // Super
        super(Type.Player, accumulator++);
        rect = plSize.clone();
        maxVelo = 15;
        damping = 0.8;
        invMass = 0.4;
        // This
        texture[0] = t1;
        texture[1] = t2;
        sprite[0] = sp1;
        sprite[1] = sp2;
        ani = new Animate(null, Vec2.getInstance(), Vec2.getInstance(128, 128), 8);
        ani.loop = true;
    }
    // Reset
    public void reset() {
        rect.position.reset();
        velocity.reset();
        gunType = Gun.AR;
        lastShootTime = System.currentTimeMillis();
        score = 0;
        health = 100;
        gunRelease = true;
        life = 3;
    }
    // Switch gun
    public void switchGun(Gun tp) {
        if (gunType == tp)
            return;
        gunType = tp;
        lastShootTime = System.currentTimeMillis() + 500l;
        EntityPool.particle.add(new CountDown(cdGun, 500, Color.RED));
        // Synchronize
        if (Shared.enableNetwork)
            SvrClt.socket.send(Pack.getSWG(tp));
    }
    public void switchGunN(byte b) {
        if (b == 0)
            gunType = Gun.AR;
        else if (b == 1)
            gunType = Gun.SG;
        else 
            gunType = Gun.SMG;
    }
    // Shoot
    public void shoot(Vec2 dir) {
        switch (gunType) {
            case AR:
                if (System.currentTimeMillis() - lastShootTime > 100l) {
                    EntityPool.bullet.add(new Bullet(rect.position, dir.resize(20), 10, 1000, this));
                    lastShootTime = System.currentTimeMillis();
                }
                break;
            case SG:
                if (System.currentTimeMillis() - lastShootTime > 500l && gunRelease) {
                    for (double i = -30; i <= 30; i += 5)
                        EntityPool.bullet.add(new Bullet(rect.position, dir.resize(20).rotate(Math.toRadians(i)), 15, 200, this));
                    Vec2 tmp = dir.resize(-15);
                    addImpulse(tmp.x, tmp.y);
                    lastShootTime = System.currentTimeMillis();
                    gunRelease = false;
                }
                break;
            case SMG:
                if (System.currentTimeMillis() - lastShootTime > 50l) {
                    EntityPool.bullet.add(new Bullet(rect.position, dir.resize(40), 7, 400, this));
                    lastShootTime = System.currentTimeMillis();
                }
        }
    }
    // Cure player
    public void cure(int hp) {
        health = Math.min(health + hp, 100);
        EntityPool.particle.add(new FloatWord(rect.position, fwv, "+" + hp, cureColor));
    }
    // Add score
    public void addScore(int sc) {
        // Network
        if (Shared.enableNetwork && EntityPool.nowPlayer == this)
            SvrClt.socket.send(Pack.getASC(sc));
        // Add
        if (EntityPool.nowPlayer == this)
            addScoreN(sc);
    }
    public void addScoreN(int sc) {
        score += sc;
        EntityPool.particle.add(new FloatWord(
            rect.position, 
            fwv,
            "+" + sc,
            Color.YELLOW));
    }
    // Event handling
    public void onEvent() {
        // Move
        if (Keyboard.isPressed(KeyEvent.VK_A))
            addImpulse(-5, 0);
        if (Keyboard.isPressed(KeyEvent.VK_S))
            addImpulse(0, 5);
        if (Keyboard.isPressed(KeyEvent.VK_D))
            addImpulse(5, 0);
        if (Keyboard.isPressed(KeyEvent.VK_W))
            addImpulse(0, -5);
        // Switch gun
        if (Keyboard.isPressed(KeyEvent.VK_1))
            switchGun(Gun.AR);
        if (Keyboard.isPressed(KeyEvent.VK_2))
            switchGun(Gun.SG);
        if (Keyboard.isPressed(KeyEvent.VK_3))
            switchGun(Gun.SMG);
        // Shoot
        if (Mouse.down)
            shoot(Camera.trans2GPos(Mouse.position).sub(rect.position));
        else
            gunRelease = true;
    }
    // Override abstract methods
    @Override
    public void addImpulse(Vec2 obj) {
        // Modify velocity
        velocity = velocity.add(obj.mul(invMass));
        // Velocity limitate
        if (velocity.length() > maxVelo)
            velocity = velocity.resize(maxVelo);
    }
    @Override
    public void addImpulse(double x, double y) {
        // Network
        if (Shared.enableNetwork)
            SvrClt.socket.send(Pack.getIMP(Vec2.getInstance(x, y)));
        // Modify velocity
        velocity = velocity.add(x * invMass, y * invMass);
        // Velocity limitate
        if (velocity.length() > maxVelo)
            velocity = velocity.resize(maxVelo);
    }
    @Override
    public void onHit(AbstractEntity obj) {
        // Hit on wall
        if (obj.type == Type.Wall)
            addImpulse(rect.position.sub(obj.rect.position).resize(20));
    }
    @Override
    public void onInjured(AbstractEntity obj) {
        addImpulse(rect.position.sub(obj.rect.position).resize(20 * ((Mob)obj).difficulty));
        if (SvrClt.frame - ((Mob)obj).lastAttack <= 30)
            return;
        ((Mob)obj).lastAttack = SvrClt.frame;
        health -= obj.damage;
        injuring = System.currentTimeMillis();
        EntityPool.particle.add(
            new FloatWord(
                rect.position, 
                fwv, 
                Long.toString(-obj.damage), 
                Color.RED));
        if (health <= 0) {
            onDeath(null);
            return;
        }
    }
    @Override
    public void onDeath(AbstractEntity obj) {
        // Add grave
        EntityPool.wall.add(new Wall(rect.position, 100, WallType.Grave));
        // Respawn
        rect.position.reset();
        velocity.reset();
        score *= 0.8;
        health = 100;
        EntityPool.particle.add(new Cloud(rect.position, 5, 30));
        // Update life
        --life;
        if (life == 0) 
            Shared.gameSet = true;
    }
    @Override
    public void onMove(double time) {
        move(time);
        moving = velocity.length() > 2;
        // Flip
        if (Shared.enableNetwork && EntityPool.nowPlayer == this)
            SvrClt.socket.send(Pack.getMPS());
        if (!Shared.enableNetwork || EntityPool.nowPlayer == this) {
            if (Mouse.down)
                ani.flip = Camera.trans2GPos(Mouse.position).x < rect.position.x;
            else {
                if (velocity.x > 0)
                    ani.flip = false;
                else if (velocity.x < 0)
                    ani.flip = true;
            }
        }
        else {
            if (SvrClt.rmd)
                ani.flip = SvrClt.rmp.x < rect.position.x;
            else {
                if (velocity.x > 0)
                    ani.flip = false;
                else if (velocity.x < 0)
                    ani.flip = true;
            }
        }
        // Constrain in map
        if (rect.position.x < WorldMap.mapRect.position.x)
            rect.position.x = WorldMap.mapRect.position.x;
        if (rect.position.x > WorldMap.mapRect.position.x + WorldMap.mapRect.size.x)
            rect.position.x = WorldMap.mapRect.position.x + WorldMap.mapRect.size.x;
        if (rect.position.y < WorldMap.mapRect.position.y)
            rect.position.y = WorldMap.mapRect.position.y;
        if (rect.position.y > WorldMap.mapRect.position.y + WorldMap.mapRect.size.y)
            rect.position.y = WorldMap.mapRect.position.y + WorldMap.mapRect.size.y;
        // Update state
        ani.rect.position = rect.position.sub(ani.rect.size.div(2));
    }
    @Override
    public void draw(Graphics2D g2d) {
        Vec2 tmp = Camera.trans2LPos(rect.position);
        if (moving) {
            ani.setSprite(System.currentTimeMillis() - injuring > 100 ? sprite[0] : sprite[1]);
            ani.draw(g2d);
        }
        else {
            Image tx = System.currentTimeMillis() - injuring > 100 ? texture[0] : texture[1];
            if (!Shared.enableNetwork || EntityPool.nowPlayer == this) {
                if (Camera.trans2GPos(Mouse.position).x < rect.position.x)
                    g2d.drawImage(tx, tmp.intX() + 64, tmp.intY() - 70, -128, 128, null);
                else
                    g2d.drawImage(tx, tmp.intX() - 64, tmp.intY() - 70, 128, 128, null);
            }
            else {
                if (SvrClt.rmp.x < rect.position.x)
                    g2d.drawImage(tx, tmp.intX() + 64, tmp.intY() - 70, -128, 128, null);
                else
                    g2d.drawImage(tx, tmp.intX() - 64, tmp.intY() - 70, 128, 128, null);
            }
        }
        // Draw gun
        double ang = 0;
        if (!Shared.enableNetwork || EntityPool.nowPlayer == this)
            ang = Camera.trans2GPos(Mouse.position).sub(rect.position).angle();
        else
            ang = SvrClt.rmp.sub(rect.position).angle();
        Image gunImg;
        switch (gunType) {
            case AR:
                gunImg = Shared.ART;
                break;
            case SG:
                gunImg = Shared.SGT;
                break;
            default:
                gunImg = Shared.SMGT;
        }
        g2d.rotate(ang, tmp.x, tmp.y);
        if (Math.abs(ang) <= Math.PI / 2.0)
            g2d.drawImage(gunImg, tmp.intX() - 55, tmp.intY() - 64, 128, 128, null);
        else 
            g2d.drawImage(gunImg, tmp.intX() - 55, tmp.intY() + 64, 128, -128, null);
        g2d.rotate(-ang, tmp.x, tmp.y);
        // Draw HP bar
        tmp = tmp.sub(20, 80);
        g2d.setColor(Color.RED);
        g2d.fillRect(tmp.intX(), tmp.intY(), (health << 1) / 5, 6);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(tmp.intX(), tmp.intY(), 40, 6);
    }
}
