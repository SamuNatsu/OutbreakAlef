package scene;

import auxiliary.*;
import com.*;
import network.*;
import particle.*;

import java.awt.*;

public class End implements Scene {
    // Static data
    static private final MainMenu.MButton toMenuB = 
        new MainMenu.MButton(
            new Rect(230, 560, 500, 40),
            "Back to menu",
            ()->SceneManager.transfer("Menu"));
    static private Cloud cl;

    // Override abstruct methods
    @Override
    public void init() {
        // Add components
        World.self.add(toMenuB);
    }
    @Override
    public void event() {
        // Synchronize
        SvrClt.syncGame();
        if (!SvrClt.next)
            return;
        // Reset map
        Camera.lookAt(0, 0);
        // Reset player
        EntityPool.p1.health = EntityPool.p2.health = 100;
        EntityPool.p1.moving = false;
        EntityPool.p2.moving = false;
        EntityPool.p1.rect.position.set(-300, 0);
        EntityPool.p2.rect.position.set(300, 0);
        EntityPool.particle.clear();
        if (Shared.isSvr)
            SvrClt.rmp.set(-1, 0);
        else 
            SvrClt.rmp.set(1, 0);
        SvrClt.rmd = false;
        // Synchronize
        SvrClt.syncPack();
    }
    @Override
    public void draw(Graphics2D g2d) {
        // Draw map
        WorldMap.drawMap(g2d);
        // Draw stable entities
        EntityPool.drawStable(g2d);
        // Draw background
        g2d.setColor(Shared.darkLayer);
        g2d.fillRect(0, 0, Application.size.intX(), Application.size.intY());
        // Draw main
        g2d.setFont(Shared.MSYH_B25);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Game Over", 420, 200);
        if (Shared.isMultiplayer) {
            g2d.setColor(Color.ORANGE);
            g2d.drawString("P1 score: " + EntityPool.p1.score, 300, 300);
            g2d.drawString("P2 score: " + EntityPool.p2.score, 300, 330);
            g2d.setColor(Color.RED);
            g2d.drawString("P1 lives remain: " + EntityPool.p1.life + " (x10000)", 300, 360);
            g2d.drawString("P2 lives remain: " + EntityPool.p2.life + " (x10000)", 300, 390);
            g2d.setColor(Color.WHITE);
            g2d.drawString("P1 total score: " + (EntityPool.p1.score + EntityPool.p1.life * 10000), 300, 420);
            g2d.drawString("P2 total score: " + (EntityPool.p2.score + EntityPool.p2.life * 10000), 300, 450);
            g2d.setColor(Color.YELLOW);
            if ((EntityPool.p1.score + EntityPool.p1.life * 10000) > (EntityPool.p2.score + EntityPool.p2.life * 10000)) {
                g2d.drawString("P1 win!", 420, 500);
                if (cl == null || cl.stop)
                    cl = new Cloud(new Vec2(-300, 0), 5, 70);
                cl.draw(g2d);
            }
            else if ((EntityPool.p1.score + EntityPool.p1.life * 10000) < (EntityPool.p2.score + EntityPool.p2.life * 10000)) {
                g2d.drawString("P2 win!", 420, 500);
                if (cl == null || cl.stop)
                    cl = new Cloud(new Vec2(300, 0), 5, 70);
                cl.draw(g2d);
            }
            else 
                g2d.drawString("Game drawn!", 420, 500);
            EntityPool.p1.draw(g2d);
            EntityPool.p2.draw(g2d);
        }
        else {
            g2d.setColor(Color.ORANGE);
            g2d.drawString("Score: " + EntityPool.p1.score, 300, 300);
            g2d.setColor(Color.RED);
            g2d.drawString("Lives remain: " + EntityPool.p1.life + " (x10000)", 300, 330);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Total score: " + (EntityPool.p1.score + EntityPool.p1.life * 10000), 300, 360);
            if (cl == null || cl.stop)
                cl = new Cloud(new Vec2(-300, 0), 5, 70);
            cl.draw(g2d);
            EntityPool.p1.draw(g2d);
        }
        // Draw footer
        g2d.setFont(Shared.MSYH_B15);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Made by Samunatsu Rainiar", 730, 710);
    }
    @Override
    public void quit() {
        World.self.removeAll();
    }
}
