package particle;

import auxiliary.*;
import misc.*;

import java.awt.*;

public final class BulletBreak extends AbstractParticle {
    // Static
    static private final Vec2 aniSize = Vec2.getInstance(60, 60);
    // Properties
    private final Animate ani;

    // Constructor
    public BulletBreak(Vec2 pos) {
        super(pos);
        ani = new Animate(Shared.bulletS, Utils.spreadPosition(pos.sub(30, 30), 10), aniSize, 20);
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
