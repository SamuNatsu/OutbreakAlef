package particle;

import auxiliary.*;
import misc.*;

import java.awt.*;

public final class BoxInjured extends AbstractParticle {
    // Static
    static private final Vec2 aniSize = Vec2.getInstance(80, 80);
    // Animate
    private final Animate ani;

    // Constructor
    public BoxInjured(Vec2 pos) {
        super(pos);
        ani = new Animate(Shared.boxS, pos, aniSize, 120);
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
