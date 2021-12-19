package particle;

import auxiliary.*;
import misc.*;

import java.awt.*;

public final class Cloud extends AbstractParticle {
    // Static
    static private final Vec2 aniSize = Vec2.getInstance(90, 90);
    // Animate
    private final Animate[] ani;

    // Constructor
    public Cloud(Vec2 pos, int cnt, double rng) {
        super(pos);
        ani = new Animate[cnt];
        for (int i = 0; i < cnt; ++i)
            ani[i] = new Animate(Shared.cloudS, Utils.spreadPosition(pos, rng).sub(aniSize.div(2)), aniSize, 20);
    }
    // Override abstract methods
    @Override
    public void draw(Graphics2D g2d) {
        if (ani[0].stop) {
            die();
            return;
        }
        for (Animate i : ani)
            i.draw(g2d);
    }
}
