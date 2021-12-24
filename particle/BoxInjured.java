package particle;

import auxiliary.*;
import misc.*;

import java.awt.*;

public final class BoxInjured extends AbstractParticle {
    // Static data
    static private final Sprite boxS = Utils.getSprite("/assets/particle/box_injured.png");
    static private final Vec2 aniSize = new Vec2(80, 80);
    // Animate
    private final Animate ani;

    // Static block
    static {
        for (int i = 0, j = 0, k = 0; k < 10; ++k) {
            boxS.addFrame(new Rect(i, j, 24, 24));
            i += 24;
            if (i >= 48) {
                i = 0;
                j += 24;
            }
        }
    }
    // Constructor
    public BoxInjured(Vec2 pos) {
        super(pos);
        ani = new Animate(boxS, pos, aniSize, 120);
    }
    // Get texture
    static public Image getTexture() {
        return boxS.getTexture();
    }
    // Override abstract methods
    @Override
    public void draw(Graphics2D g2d) {
        if (ani.stop) {
            die();
            return;
        }
        ani.draw(g2d);
    }
}
