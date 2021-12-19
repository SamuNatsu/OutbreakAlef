package scene;

import auxiliary.*;
import com.*;

import java.awt.*;

public final class CSMenu extends AbstractScene {
    // Static
    static private final MainMenu.MButton asServer = 
        new MainMenu.MButton(
            Rect.getInstance(230, 400, 500, 40),
            "Create a server",
            ()->SceneManager.transition("SvrMenu"));
    static private final MainMenu.MButton asClient = 
        new MainMenu.MButton(
            Rect.getInstance(230, 480, 500, 40),
            "Connect to server",
            ()->SceneManager.transition("CltMenu"));
    static private final MainMenu.MButton backB = 
        new MainMenu.MButton(
            Rect.getInstance(230, 560, 500, 40),
            "Back",
            ()->SceneManager.transition("Menu"));

    // Override abstract methods
    @Override
    public void init() {
        // Reset network
        Shared.server.reset();
        Shared.client.reset();
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
        WorldMap.drawMap(g2d);
        g2d.setColor(Shared.darkLayer);
        g2d.fillRect(0, 0, Application.size.intX(), Application.size.intY());
        Shared.titleA.draw(g2d);
        g2d.setFont(Shared.MSYH_B15);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Made by Samunatsu Rainiar", 730, 710);
    }
    @Override
    public void quit() {
        World.self.removeAll();
    }
}
