package scene;

import auxiliary.*;
import com.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class MainMenu extends AbstractScene {
    // Menu button define
    static public final class MButton extends JButton {
        // Callback function
        private final Callback callback;

        // Constructor
        public MButton(Rect rect, String text, Callback cb) {
            super();
            callback = cb;
            setBounds(rect.position.intX(), rect.position.intY(), rect.size.intX(), rect.size.intY());
            setFont(Shared.MSYH_B15);
            setText(text);
            setFocusPainted(false);
            addActionListener((ActionEvent e)->callback.run());
        }
    }
    // Static
    static private final MButton soloGameB = 
        new MButton(
            Rect.getInstance(230, 400, 500, 40),
            "Solo Mode",
            ()-> {
                Shared.enableNetwork = false;
                EntityPool.nowPlayer = EntityPool.p1;
                SceneManager.transition("Game");
            });
    static private final MButton compGameB = 
        new MButton(
            Rect.getInstance(230, 480, 500, 40),
            "Competition Mode",
            ()-> {
                Shared.enableNetwork = true;
                SceneManager.transition("CompMenu");
            });
    static private final MButton exitB = 
        new MButton(
            Rect.getInstance(230, 560, 500, 40),
            "Quit",
            ()-> {
                if (JOptionPane.showOptionDialog(
                    Application.self, 
                    "Sure to quit?", 
                    "Oops",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    Shared.yesNoSelection,
                    null) == 0)
                    System.exit(0);
            });

    // Override abstract methods
    @Override
    public void init() {
        // Error
        if (Shared.netDiscon)
            new Thread(()-> {
                JOptionPane.showOptionDialog(
                    Application.self,
                    "Remote connection closed",
                    "Oops", 
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    Shared.okSelection,
                    null);
            }).start();
        Shared.netDiscon = false;
        // Reset network
        Shared.server.reset();
        Shared.client.reset();
        // Add components
        World.self.add(soloGameB);
        World.self.add(compGameB);
        World.self.add(exitB);
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
