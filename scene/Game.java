package scene;

import auxiliary.*;
import com.*;
import misc.Medikit;
import network.*;

import java.awt.*;
import java.awt.event.*;

public final class Game implements Scene {
    // Static data
    static private final Image heartT = Utils.getImage("/assets/misc/heart.png");
    // Properties
    private long lstSum = 0;

    // Override abstract methods
    @Override
    public void init() {
        // Reset
        lstSum = 0;
        reset();
        Utils.genSeed();
        Keyboard.reset();
        Mouse.reset();
        Shared.isGameOver = false;
        // Synchronize
        SvrClt.init();
        SvrClt.syncRandom();
        WorldMap.generateMapImage();
    }
    @Override
    public void event() {
        // Synchronize
        if (!SvrClt.checkConnection())
            return;
        SvrClt.updateFlow();
        SvrClt.syncGame();
        if (!SvrClt.next)
            return;
        // Frame accumulate
        ++SvrClt.frame;
        // Check game set
        if (Shared.isGameOver) {
            SceneManager.quickTransfer("End");
            return;
        }
        // Entity update
        EntityPool.move(World.timeSlice);
        EntityPool.collide();
        EntityPool.maintain();
        // Camera follow
        Camera.lookAtPlayer();
        // Summon mob
        //if (!Shared.isMultiplayer || Shared.isSvr)
            if (SvrClt.frame - lstSum > 600l) {
                if (EntityPool.mob.summary() <= 50) {
                    for (int i = 0, lmt = Shared.isMultiplayer ? 10 : 5; i < lmt; ++i)
                        EntityPool.generateMob();
                    //if (Shared.isSvr)
                    //    SvrClt.socket.send(Pack.getGMB());
                }
                lstSum = SvrClt.frame;
            }
        // Escape
        if (Keyboard.isPressed(KeyEvent.VK_ESCAPE)) {
            Shared.isGameOver = true;
            if (Shared.isMultiplayer)
                SvrClt.socket.send(Pack.getSST());
        }
        // Player event
        EntityPool.nowPlayer.onEvent();
        // Gadgets event
        Medikit.onEvent();
        // Synchronize
        SvrClt.syncPack();
    }
    @Override
    public void draw(Graphics2D g2d) {
        // Draw map
        WorldMap.drawMap(g2d);
        // Draw entities
        EntityPool.draw(g2d);
        // Draw GUI
        drawGUI(g2d);
        // Draw mini map
        WorldMap.drawMiniMap(g2d);
        // Draw debug info
        drawDebug(g2d);
    }
    @Override
    public void quit() {}
    // Reset world
    public void reset() {
        EntityPool.reset();
        WorldMap.reset();
        Medikit.reset();
        if (Shared.isMultiplayer) {
            EntityPool.p1.rect.position.x = -20;
            EntityPool.p2.rect.position.x = 20;
        }
    }
    // Draw GUI
    public void drawGUI(Graphics2D g2d) {
        // Draw info
        g2d.setColor(Color.BLACK);
        g2d.fillRect(25, 25, 310, 5);
        g2d.fillRect(25, 30, 5, 20);
        g2d.fillRect(30, 45, 305, 5);
        g2d.fillRect(330, 30, 5, 15);
        g2d.setColor(Color.RED);
        g2d.fillRect(30, 30, EntityPool.nowPlayer.health * 3, 15);
        g2d.setFont(Shared.MSYH_B25);
        g2d.drawString("HP " + EntityPool.nowPlayer.health + "/100", 30, 80);
        g2d.setColor(Color.ORANGE);
        g2d.drawString("Score: " + EntityPool.nowPlayer.score, 30, 110);
        g2d.setColor(Color.BLACK);
        g2d.drawString(EntityPool.nowPlayer.gunType.toString(), 30, 140);
        if (Shared.isMultiplayer) {
            g2d.setColor(Color.BLUE);
            if (EntityPool.nowPlayer == EntityPool.p1)
                g2d.drawString("Opponent: " + EntityPool.p2.score, 30, 170);
            else 
                g2d.drawString("Opponent: " + EntityPool.p1.score, 30, 170);
        }
        // Draw life
        for (int i = 0, j = Application.size.intX() - 140; i < EntityPool.nowPlayer.life; ++i, j += 40)
            g2d.drawImage(heartT, j, 120, null);
        // Draw gadgets
        Medikit.draw(g2d);
        // Draw footer
        g2d.setFont(Shared.MSYH_B15);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Made by Samunatsu Rainiar", 730, 710);
    }
    // Draw debug info
    public void drawDebug(Graphics2D g2d) {
        // Draw hitbox
        if (Shared.debugMode)
            EntityPool.drawHitbox(g2d);
        // Draw info list
        g2d.setFont(Shared.MSYH_B15);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Outbreak Alef - " + Shared.version, 10, 560);
        g2d.drawString("Built time: " + Shared.builtTime, 10, 580);
        g2d.drawString("E: " + EntityPool.summary() + " A: " + (EntityPool.summary() - EntityPool.wall.summary()), 10, 600);
        g2d.drawString("Network: " + Shared.isMultiplayer, 10, 620);
        g2d.drawString("Seed: " + Utils.getSeed(), 10, 640);
        g2d.drawString("FPS: " + World.rfps + " FP: " + World.frameDelta + "ms", 10, 660);
        g2d.drawString("FA: " + SvrClt.frame, 10, 680);
        if (Shared.isMultiplayer)
            g2d.drawString("Up: " + SvrClt.socket.getUpload() + "B/s | Down: " + SvrClt.socket.getDownload() + "B/s", 10, 700);
    }
}
