package particle;

import auxiliary.*;
import misc.*;

import java.awt.*;

public final class BulletBreak extends AbstractParticle {
    // Static data
    static private final Sprite bulletS = Utils.getSprite("/assets/particle/bullet_break.png");
    static private final Vec2 aniSize = new Vec2(60, 60);
    // Properties
    private final Animate ani;

    // Static block
    static {
        for (int i = 0; i < 48; i += 24)
            for (int j = 0; j < 48; j += 24)
                bulletS.addFrame(new Rect(j, i, 24, 24));
    }
    // Constructor
    public BulletBreak(Vec2 pos) {
        super(pos);
        ani = new Animate(bulletS, Utils.spreadPosition(pos, 10), aniSize, 20);
    }
    // Override abstract methods
    @Override
    public void draw(Graphics2D g2d) {
        if (ani.stop) {
            die();
            return;
        }
        ani.rect.position.add(0, 2);
        ani.draw(g2d);
    }
}
