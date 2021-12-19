package com;

import auxiliary.*;
import entity.*;
import particle.*;

import java.awt.*;
import java.util.*;

public final class EntityPool {
    // Managers
    static public final EntityManager bullet = new EntityManager();
    static public final EntityManager loot = new EntityManager();
    static public final EntityManager mob = new EntityManager();
    static public final EntityManager wall = new EntityManager();
    static public final ParticleManager particle = new ParticleManager();
    // Player manage
    static public final Player p1 = new Player(Shared.p1T, Shared.p1IT, Shared.p1S, Shared.p1IS), 
        p2 = new Player(Shared.p2T, Shared.p2IT, Shared.p2S, Shared.p2IS);
    static public Player nowPlayer;
    // Boss generate posibility
    static public double giantPercentage = 0.01, speedPercentage = 0.05;
    static public long mobAcc = 0;

    // Reset entity pools
    static public void reset() {
        // Managers
        bullet.clear();
        loot.clear();
        mob.clear();
        wall.clear();
        particle.clear();
        // Player
        p1.reset();
        p2.reset();
        // Posibility
        giantPercentage = 0.01;
        speedPercentage = 0.05;
        mobAcc = 0;
    }
    // Generate mob
    static private void addMob(Vec2 pos, double va, double ha) {
        if (!Shared.enableNetwork) {
            mob.add(new Mob(pos, va, ha, p1));
            return;
        }
        boolean tmp = pos.distance(p1.rect.position) < pos.distance(p2.rect.position);
        mob.add(new Mob(pos, va, ha, tmp ? p1 : p2));
    }
    static public void generateMob() {
        // Generate position
        double rnd = Utils.random(0, 8);
        Vec2 tmp;
        if (rnd < 1.0)
            tmp = WorldMap.mapRect.position.sub(500, 500)
                .add(Utils.random(0, 500), Utils.random(0, 500));
        else if (rnd < 2.0)
            tmp = WorldMap.mapRect.position.sub(0, 500)
                .add(Utils.random(0, WorldMap.mapRect.size.x), Utils.random(0, 500));
        else if (rnd < 3.0)
            tmp = WorldMap.mapRect.position.add(WorldMap.mapRect.size.x, -500)
                .add(Utils.random(0, 500), Utils.random(0, 500));
        else if (rnd < 4.0)
            tmp = WorldMap.mapRect.position.add(WorldMap.mapRect.size.x, 0)
                .add(Utils.random(0, 500), Utils.random(0, WorldMap.mapRect.size.y));
        else if (rnd < 5.0)
            tmp = WorldMap.mapRect.position.add(WorldMap.mapRect.size)
                .add(Utils.random(0, 500), Utils.random(0, 500));
        else if (rnd < 6.0)
            tmp = WorldMap.mapRect.position.add(0, WorldMap.mapRect.size.y)
                .add(Utils.random(0, WorldMap.mapRect.size.x), Utils.random(0, 500));
        else 
            tmp = WorldMap.mapRect.position.sub(500, 0)
                .add(Utils.random(0, 500), Utils.random(0, WorldMap.mapRect.size.y));
        // Generate mob
        if (Utils.random.nextDouble() < giantPercentage)
            addMob(tmp, 0.4, Utils.random(8, 13));
        else {
            if (Utils.random.nextDouble() < speedPercentage)
                addMob(tmp, Utils.random(1.5, 2.5), 0.3);
            else
                addMob(tmp, 1, Utils.random(0.2, 0.4));
        }
        // Mob count
        ++mobAcc;
        if (mobAcc > 50) {
            giantPercentage += 0.01;
            speedPercentage += 0.01;
            mobAcc = 0;
            particle.add(new particle.FloatWord(Vec2.getInstance(30, 360), "More zombies are coming!", Color.RED));
        }
    }
    // Move all entities
    static synchronized public void move(double time) {
        bullet.move(time);
        loot.move(time);
        mob.move(time);
        // Player move
        p1.onMove(time);
        if (Shared.enableNetwork)
            p2.onMove(time);
    }
    // Collide all entities
    static synchronized public void collide() {
        // Bullet collide -> Mob Wall
        bullet.collideWith(mob);
        bullet.collideWith(wall);
        // Mob collide -> Mob Player Wall
        mob.collideSelf();
        mob.collideWith(p1);
        if (Shared.enableNetwork)
            mob.collideWith(p2);
        mob.collideWith(wall);
        // Player collide -> Wall Loot
        wall.collideFrom(p1);
        loot.collideWith(p1);
        if (Shared.enableNetwork) {
            wall.collideFrom(p2);
            loot.collideWith(p2);
        }
    }
    // Maintain pools
    static synchronized public void maintain() {
        bullet.maintain();
        mob.maintain();
        loot.maintain();
        wall.maintain();
        particle.maintain();
    }
    // Summary
    static public int summary() {
        return bullet.summary() + loot.summary() + mob.summary() + wall.summary();
    }
    // Draw
    static synchronized public void draw(Graphics2D g2d) {
        // Paint loot
        loot.forEach((Long id, AbstractEntity obj)-> {
            obj.draw(g2d);
        });
        // Setup draw queue
        PriorityQueue<AbstractEntity> queue = new PriorityQueue<>(new Comparator<AbstractEntity>() {
            public int compare(AbstractEntity a, AbstractEntity b) {
                return a.rect.position.y < b.rect.position.y ? -1 : 1;
            }
        });
        bullet.output(queue);
        mob.output(queue);
        wall.output(queue);
        queue.add(p1);
        if (Shared.enableNetwork)
            queue.add(p2);
        // Draw entities
        while (!queue.isEmpty())
            queue.poll().draw(g2d);
        // Draw particles
        particle.draw(g2d);
    }
    // Draw hitbox
    static public void drawHitbox(Graphics2D g2d) {
        bullet.drawHitbox(g2d);
        loot.drawHitbox(g2d);
        mob.drawHitbox(g2d);
        wall.drawHitbox(g2d);
        p1.drawHitbox(g2d);
        if (Shared.enableNetwork)
            p2.drawHitbox(g2d);
    }
}
