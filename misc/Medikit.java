package misc;

import auxiliary.*;
import com.*;
import network.*;
import particle.*;

import java.awt.*;
import java.awt.event.*;

public final class Medikit {
    // Static data
    static private final Image medikitT = Utils.getImage("/assets/map/medikit.png");
    static private final Vec2 cdSize = new Vec2(420, 120);
    // If available
    static private boolean available = true;
    // Cooldown particle
    static private CountDown coolDown;

    // Reset
    static public void reset() {
        available = true;
    }
    // Event handling
    static public void onEvent() {
        if (Keyboard.isPressed(KeyEvent.VK_4) && available) {
            // Network
            if (Shared.enableNetwork)
                SvrClt.socket.send(Pack.getCUR(30));
            // Cure player
            EntityPool.nowPlayer.cure(30);
            // Start cool down
            coolDown = new CountDown(cdSize, 20000, Color.RED, ()->Medikit.reset());
            available = false;
        }
    }
    // Draw
    static public void draw(Graphics2D g2d) {
        g2d.drawImage(medikitT, 420, 10, 80, 80, null);
        g2d.setColor(Color.BLACK);
        g2d.drawString("4", 430, 80);
        if (!available) {
            g2d.setColor(Shared.darkLayer);
            int per = (int)(coolDown.percent() * 80);
            g2d.fillRect(420, 10 + per, 80, 80 - per);
            coolDown.draw(g2d);
        }
    }
}
