package scene;

import auxiliary.*;
import com.*;
import network.*;

import java.awt.*;

public final class CSMenu implements Scene {
    // Static data
    static private final MainMenu.MButton asServer = 
        new MainMenu.MButton(
            new Rect(230, 400, 500, 40),
            "Create a server",
            ()->SceneManager.transfer("SvrMenu"));
    static private final MainMenu.MButton asClient = 
        new MainMenu.MButton(
            new Rect(230, 480, 500, 40),
            "Connect to server",
            ()->SceneManager.transfer("CltMenu"));
    static private final MainMenu.MButton backB = 
        new MainMenu.MButton(
            new Rect(230, 560, 500, 40),
            "Back",
            ()->SceneManager.transfer("Menu"));

    // Override abstract methods
    @Override
    public void init() {
        // Reset network
        SvrClt.server.reset();
        SvrClt.client.reset();
        // Add components
        World.self.add(asServer);
        World.self.add(asClient);
        World.self.add(backB);
        // Reset map
        Utils.genSeed();
        WorldMap.reset();
        Camera.lookAt(0, 0);
    }
    @Override
    public void event() {}
    @Override
    public void draw(Graphics2D g2d) {
        // Draw background
        WorldMap.drawMap(g2d);
        g2d.setColor(Shared.darkLayer);
        g2d.fillRect(0, 0, Application.size.intX(), Application.size.intY());
        // Draw main
        Shared.titleA.draw(g2d);
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
