package auxiliary;

import com.*;
import misc.*;
import network.*;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public final class Shared {
    // Version
    static public final String version = "v1.1.0";
    static public final String builtTime = 
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
            new Date(new File("com/Application.class").lastModified())
        );
    // Fonts
    static public final Font MSYH_B15 = new Font("微软雅黑", Font.BOLD, 15);
    static public final Font MSYH_B25 = new Font("微软雅黑", Font.BOLD, 25);
    // Textures
    static public final Image iconT = new ImageIcon("assets/icon.png").getImage();
    static public final Image bulletT = new ImageIcon("assets/bullet.png").getImage();
    static public final Image graveT = new ImageIcon("assets/grave.png").getImage();
    static public final Image mediKitT = new ImageIcon("assets/mediKit.png").getImage();
    static public final Image hpMedicT = new ImageIcon("assets/hpMedic.png").getImage();
    static public final Image heartT = new ImageIcon("assets/heart.png").getImage();

    static public final Image grassAT = new ImageIcon("assets/grassA.png").getImage();
    static public final Image grassBT = new ImageIcon("assets/grassB.png").getImage();
    static public final Image grassCT = new ImageIcon("assets/grassC.png").getImage();
    static public final Image grassDT = new ImageIcon("assets/grassD.png").getImage();

    static public final Image rockAT = new ImageIcon("assets/rockA.png").getImage();
    static public final Image rockBT = new ImageIcon("assets/rockB.png").getImage();

    static public final Image ART = new ImageIcon("assets/AR.png").getImage();
    static public final Image SGT = new ImageIcon("assets/SG.png").getImage();
    static public final Image SMGT = new ImageIcon("assets/SMG.png").getImage();

    static public final Image p1T = new ImageIcon("assets/p1.png").getImage();
    static public final Image p1IT = new ImageIcon("assets/p1I.png").getImage();

    static public final Image p2T = new ImageIcon("assets/p2.png").getImage();
    static public final Image p2IT = new ImageIcon("assets/p2I.png").getImage();
    // Sprite
    static public final Sprite bulletS = new Sprite("assets/bulletBreak.png");
    static public final Sprite boxS = new Sprite("assets/box.png");
    static public final Sprite titleS = new Sprite("assets/title.png");
    static public final Sprite cloudS = new Sprite("assets/cloud.png");

    static public final Sprite mobS = new Sprite("assets/mob.png");
    static public final Sprite mobIS = new Sprite("assets/mobI.png");

    static public final Sprite p1S = new Sprite("assets/p1M.png");
    static public final Sprite p1IS = new Sprite("assets/p1MI.png");

    static public final Sprite p2S = new Sprite("assets/p2M.png");
    static public final Sprite p2IS = new Sprite("assets/p2MI.png");
    // Animate
    static public final Animate titleA = new Animate(
        Shared.titleS, 
        Application.size.div(-2).add(160, 100), 
        Vec2.getInstance(640, 256), 
        2
    );
    // Color
    static public final Color darkLayer = new Color(0x5F000000, true);
    // Selections
    static public final String[] okSelection = {"OK"};
    static public final String[] yesNoSelection = {"Yes", "No"};
    // Network
    static public final UdpServer server = new UdpServer();
    static public final UdpClient client = new UdpClient();
    static public boolean enableNetwork = false, isSvr = false, netDiscon = false, gameSet = false;
    // Misc
    static public boolean debugMode = false;

    // Initialize
    static public void init() {
        // Make bulletS
        for (int i = 0; i <= 24; i += 24)
            for (int j = 0; j <= 24; j += 24)
                bulletS.addFrame(Rect.getInstance(j, i, 24, 24));
        // Make boxS
        for (int i = 0, j = 0; i <= 72 && j < 10; i += 24)
            for (int k = 0; k <= 48 && j < 10; k += 24, ++j)
                boxS.addFrame(Rect.getInstance(k, i, 24, 24));
        // Make cloudS
        for (int i = 0; i < 64; i += 32)
            for (int j = 0; j < 96; j += 32)
                cloudS.addFrame(Rect.getInstance(j, i, 32, 32));
        // Make titleS
        titleS.addFrame(Rect.getInstance(0, 0, 160, 64));
        titleS.addFrame(Rect.getInstance(0, 64, 160, 64));
        titleA.loop = true;
        // Make mobS
        mobS.addFrame(Rect.getInstance(0, 0, 64, 64));
        mobS.addFrame(Rect.getInstance(0, 64, 64, 64));
        mobIS.addFrame(Rect.getInstance(0, 0, 64, 64));
        mobIS.addFrame(Rect.getInstance(0, 64, 64, 64));
        // Make p1S
        p1S.addFrame(Rect.getInstance(0, 0, 64, 64));
        p1S.addFrame(Rect.getInstance(0, 64, 64, 64));
        p1IS.addFrame(Rect.getInstance(0, 0, 64, 64));
        p1IS.addFrame(Rect.getInstance(0, 64, 64, 64));
        // Make p2S
        p2S.addFrame(Rect.getInstance(0, 0, 64, 64));
        p2S.addFrame(Rect.getInstance(0, 64, 64, 64));
        p2IS.addFrame(Rect.getInstance(0, 0, 64, 64));
        p2IS.addFrame(Rect.getInstance(0, 64, 64, 64));
    }
}
