package auxiliary;

import com.*;
import misc.*;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;

public final class Shared {
    // Version
    static public final String version = "v1.6.0";
    static public final String builtTime = 
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
            new Date(new File("OutbreakAlef.jar").lastModified())
        );
    // Fonts
    static public final Font MSYH_B15 = new Font("微软雅黑", Font.BOLD, 15);
    static public final Font MSYH_B25 = new Font("微软雅黑", Font.BOLD, 25);
    // Sprite
    static private final Sprite titleS = Utils.getSprite("/assets/misc/title.png");
    // Animate
    static public final Animate titleA = new Animate(
        titleS, 
        Application.size.div(-2).add(480, 250), 
        new Vec2(640, 256), 
        2
    );
    // Color
    static public final Color darkLayer = new Color(0x5F000000, true);
    // Network
    static public boolean isMultiplayer = false, isSvr = false, isNetIrrupt = false, isGameOver = false;
    // Misc
    static public boolean debugMode = false;

    // Static block
    static {
        titleS.addFrame(new Rect(0, 0, 160, 64));
        titleS.addFrame(new Rect(0, 64, 160, 64));
        titleA.loop = true;
    }
}
