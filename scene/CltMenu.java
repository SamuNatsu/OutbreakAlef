package scene;

import auxiliary.*;
import com.*;
import network.*;

import java.awt.*;
import javax.swing.*;

// Custom textarea
final class MTextArea extends JTextArea {
    public MTextArea() {
        super();
        setFont(Shared.MSYH_B15);
        setBounds(230, 400, 500, 30);
    }
}

public final class CltMenu extends AbstractScene {
    // Static properties
    static private boolean start = false, connectFlag = false;
    static private String msg = "";
    // Static data
    static private final MTextArea ipT = new MTextArea();
    static private final MainMenu.MButton connB = 
        new MainMenu.MButton(
            Rect.getInstance(230, 480, 500, 40),
            "Connect",
            ()-> {
                start = true;
                connectFlag = false;
                msg = "Connecting...";
                String[] ls = ipT.getText().split(":");
                try {
                    if (ls[0].compareTo("") == 0)
                        ls[0] = "localhost";
                    Shared.client.connect(ls[0], Integer.parseInt(ls[1]));
                }
                catch (Exception e) {
                    Shared.client.setError("Invalid IP/port");
                }
            });
    static private final MainMenu.MButton backB = 
        new MainMenu.MButton(
            Rect.getInstance(230, 560, 500, 40),
            "Back",
            ()->SceneManager.transfer("CompMenu"));

    // Override abstract methods
    @Override
    public void init() {
        // Reset client
        Shared.isSvr = false;
        start = false;
        connectFlag = false;
        // Reset components
        connB.setEnabled(true);
        ipT.setEditable(true);
        ipT.setText("");
        msg = "";
        // Add components
        World.self.add(connB);
        World.self.add(backB);
        World.self.add(ipT);
        // Reset map
        Utils.genSeed();
        Camera.lookAt(0, 0);
        WorldMap.reset();
    }
    @Override
    public void event() {
        if (start) {
            ipT.setEditable(false);
            connB.setEnabled(false);
            if (Shared.client.isError()) {
                msg = Shared.client.getError();
                start = false;
                ipT.setEditable(true);
                connB.setEnabled(true);
                return;
            }
            if (Shared.client.isConnected()) {
                connectFlag = true;
                msg = "Remote server connected, waiting for game start";
                while (Shared.client.hasMessage())
                    if (Shared.client.get()[0] == Pack.STT) {
                        EntityPool.nowPlayer = EntityPool.p2;
                        SceneManager.transfer("Game");
                        return;
                    }
            }
            else if (connectFlag) {
                msg = "Disconnected from remote server";
                start = false;
                connectFlag = false;
                ipT.setEditable(true);
                connB.setEnabled(true);
            }
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
        g2d.setFont(Shared.MSYH_B15);
        g2d.drawString("Input remote server IP & port:", 230, 390);
        g2d.setColor(Color.RED);
        g2d.drawString(msg, 230, 360);
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
