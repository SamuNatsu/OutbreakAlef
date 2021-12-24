package scene;

import auxiliary.*;
import com.*;
import network.*;

import java.awt.*;

public final class SvrMenu implements Scene {
    // Static properties
    static private boolean connectFlag = false;
    // Static data
    static private final MainMenu.MButton startGameB = 
        new MainMenu.MButton(
            new Rect(230, 480, 500, 40),
            "Start Game",
            ()-> {
                EntityPool.nowPlayer = EntityPool.p1;
                Shared.server.send(Pack.getSST());
                SceneManager.transfer("Game");
            });
    static private final MainMenu.MButton backB = 
        new MainMenu.MButton(
            new Rect(230, 560, 500, 40),
            "Back",
            ()->SceneManager.transfer("CompMenu"));

    // Override abstract methods
    @Override
    public void init() {
        // Start server
        connectFlag = false;
        Shared.isSvr = true;
        Shared.server.start();
        // Reset components
        startGameB.setEnabled(false);
        // Add components
        World.self.add(startGameB);
        World.self.add(backB);
        // Reset map
        Utils.genSeed();
        WorldMap.reset();
        Camera.lookAt(0, 0);
    }
    @Override
    public void event() {
        if (Shared.server.isConnected()) {
            if (!connectFlag)
                startGameB.setEnabled(true);
            connectFlag = true;
        }
        else if (connectFlag) {
            connectFlag = false;
            Shared.server.start();
            startGameB.setEnabled(false);
        }
    }
    @Override
    public void draw(Graphics2D g2d) {
        // Draw background
        WorldMap.drawMap(g2d);
        g2d.setColor(Shared.darkLayer);
        g2d.fillRect(0, 0, Application.size.intX(), Application.size.intY());
        // Draw main
        Shared.titleA.draw(g2d);
        g2d.setColor(Color.WHITE);
        g2d.setFont(Shared.MSYH_B25);
        g2d.drawString("Server opens at port " + Shared.server.getLocalPort(), 300, 400);
        if (Shared.server.isConnected())
            g2d.drawString("Client connected from " + Shared.server.getRemoteAddress(), 250, 450);
        else 
            g2d.drawString("Waiting for connection...", 300, 450);
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
