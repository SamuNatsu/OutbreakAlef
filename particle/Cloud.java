package particle;

import auxiliary.*;
import misc.*;

import java.awt.*;

public final class Cloud extends AbstractParticle {
    // Static data
    static private final Sprite cloudS = Utils.getSprite("/assets/particle/cloud.png");
    static private final Vec2 aniSize = new Vec2(90, 90);
    // Flag
    public boolean stop = false;
    // Animate
    private final Animate[] ani;

    // Static block
    static {
        for (int i = 0; i < 64; i += 32)
            for (int j = 0; j < 96; j += 32)
                cloudS.addFrame(new Rect(j, i, 32, 32));
    }
    // Constructor
    public Cloud(Vec2 pos, int cnt, double rng) {
        super(pos);
        ani = new Animate[cnt];
        for (int i = 0; i < cnt; ++i)
            ani[i] = new Animate(cloudS, Utils.spreadPosition(pos, rng), aniSize, 20);
    }
    // Override abstract methods
    @Override
    public void draw(Graphics2D g2d) {
        if (ani[0].stop) {
            stop = true;
            die();
            return;
        }
        for (Animate i : ani)
            i.draw(g2d);
    }
}
