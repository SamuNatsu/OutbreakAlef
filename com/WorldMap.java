package com;

import auxiliary.*;
import entity.*;

import java.awt.*;
import javax.swing.*;

public final class WorldMap {
    // Static data
    static private final Image grass0T = Utils.getImage("/assets/map/grass_0.png");
    static private final Image grass1T = Utils.getImage("/assets/map/grass_1.png");
    static private final Image grass2T = Utils.getImage("/assets/map/grass_2.png");
    static private final Image grass3T = Utils.getImage("/assets/map/grass_3.png");
    static private final Color borderColor = new Color(0x7F7F0000, true);
    // Map settings
    static public final Rect mapRect = new Rect(-1000, -750, 2000, 1500);
    static public final double miniMapScale = mapRect.size.x / 120;
    // Map texture
    static private Image mapT;
    // Parent component
    static private JPanel parent;

    // Bind parent
    static public void bind(JPanel obj) {
        parent = obj;
    }
    // Reset map
    static public void reset() {
        mapT = null;
    }
    // Generate map
    static public void generateMapImage() {
        // Create image
        Vec2 tmp = mapRect.size.add(1000, 1000);
        mapT = parent.createImage(tmp.intX(), tmp.intY());
        // Get handle
        Graphics2D g2d = (Graphics2D)mapT.getGraphics();
        // Draw grass
        for (int i = 0; i < tmp.intX(); i += 100)
            for (int j = 0; j < tmp.intY(); j += 100)
                if (Utils.random.nextDouble() < 0.9)
                    g2d.drawImage(grass0T, i, j, 100, 100, null);
                else {
                    double rnd = Utils.random.nextDouble();
                    if (rnd < 0.03)
                        g2d.drawImage(grass3T, i, j, 100, 100, null);
                    else if (rnd < 0.6)
                        g2d.drawImage(grass1T, i, j, 100, 100, null);
                    else 
                        g2d.drawImage(grass2T, i, j, 100, 100, null);
                }
        // Draw boarder
        g2d.setColor(borderColor);
        g2d.fillRect(490, 490, mapRect.size.intX() + 20, 10);
        g2d.fillRect(490, 500, 10, mapRect.size.intY() + 10);
        g2d.fillRect(500, 500 + mapRect.size.intY(), mapRect.size.intX() + 10, 10);
        g2d.fillRect(500 + mapRect.size.intX(), 500, 10, mapRect.size.intY());
        // Generate rock
        EntityPool.wall.clear();
        for (int i = 0; i < 20; ++i) {
            tmp.x = Utils.random(mapRect.size.x / -2 - 100, mapRect.size.x / 2 + 100);
            tmp.y = Utils.random(mapRect.size.y / -2 - 100, mapRect.size.y / 2 + 100);
            if (tmp.length() < 200)
                continue;
            if (EntityPool.wall.forEach((AbstractEntity obj)-> {
                return obj.rect.position.distance(tmp) < 100;
            })) continue;
            EntityPool.wall.add(
                new Wall(tmp, -1, 
                    Utils.random.nextDouble() < 0.5 ? Wall.WallType.RockA : Wall.WallType.RockB));
        }
        // Generate box
        for (int i = 0; i < 10; ++i) {
            tmp.x = Utils.random(mapRect.size.x / -2, mapRect.size.x / 2);
            tmp.y = Utils.random(mapRect.size.y / -2, mapRect.size.y / 2);
            if (tmp.length() < 200)
                continue;
            if (EntityPool.wall.forEach((AbstractEntity obj)-> {
                return obj.rect.position.distance(tmp) < 100;
            })) continue;
            EntityPool.wall.add(new Wall(tmp, 300, Wall.WallType.Box));
        }
    }
    // Draw
    static public void drawMap(Graphics2D g2d) {
        // Regenerate map
        if (mapT == null)
            generateMapImage();
        // Draw map
        Vec2 ori = Camera.trans2GPos(mapT.getWidth(null) >> 1, mapT.getHeight(null) >> 1);
        g2d.drawImage(
            mapT, 
            0, 0, parent.getWidth(), parent.getHeight(), 
            ori.intX(), ori.intY(), 
            ori.intX() + parent.getWidth(), ori.intY() + parent.getHeight(), 
            null);
    }
    // Draw minimap
    static public void drawMiniMap(Graphics2D g2d) {
        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.fillRect(Application.size.intX() - 20, 17, 3, 96);
        g2d.fillRect(Application.size.intX() - 143, 17, 123, 3);
        g2d.fillRect(Application.size.intX() - 143, 20, 3, 93);
        g2d.fillRect(Application.size.intX() - 140, 110, 120, 3);
        // Draw mob position
        g2d.setColor(Color.RED);
        EntityPool.mob.forEach((Long id, AbstractEntity obj)-> {
            Vec2 tmp = obj.rect.position;
            if (!mapRect.contains(obj.rect.position))
                return;
            tmp = tmp.sub(mapRect.position).div(miniMapScale).add(Application.size.x - 139, 19);
            g2d.fillRect(tmp.intX(), tmp.intY(), 2, 2);
        });
        // Draw wall position
        EntityPool.wall.forEach((Long id, AbstractEntity obj)-> {
            Vec2 tmp = obj.rect.position;
            if (!mapRect.contains(obj.rect.position))
                return;
            tmp = tmp.sub(mapRect.position).div(miniMapScale).add(Application.size.x - 138.5, 18.5);
            if (obj.health == -1)
                g2d.setColor(Color.DARK_GRAY);
            else 
                g2d.setColor(Color.ORANGE);
            g2d.fillRect(tmp.intX(), tmp.intY(), 3, 3);
        });
        // Draw player position
        g2d.setColor(Color.WHITE);
        Vec2 t = EntityPool.p1.rect.position.sub(mapRect.position).div(miniMapScale).add(Application.size.x - 140, 20);
        g2d.fillRect(t.intX(), t.intY(), 2, 2);
        if (Shared.isMultiplayer) {
            t = EntityPool.p2.rect.position.sub(mapRect.position).div(miniMapScale).add(Application.size.x - 140, 20);
            g2d.fillRect(t.intX(), t.intY(), 2, 2);
        }
    }
}
