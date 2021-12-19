package misc;

import auxiliary.*;
import com.*;

import java.awt.*;

public class Animate {
    // External sprite
    private Sprite sprite;
    // Position & size
    public Rect rect;
    // Loop flag & stop flag & flip flag
    public boolean loop = false, stop = false, flip = false;
    // Frame control
    private long frameTime, lastTime = -1, nowFrame = 0;

    // Constructor
    public Animate(Sprite sp, Vec2 pos, Vec2 sz, long fps) {
        sprite = sp;
        rect = Rect.getInstance(pos, sz);
        frameTime = (long)(1000d / fps);
    }
    // Set sprite
    public void setSprite(Sprite sp) {
        sprite = sp;
    }
    // Paint
    public void draw(Graphics2D g2d) {
        // If stopped
        if (stop)
            return;
        // If not start
        if (lastTime == -1)
            lastTime = System.currentTimeMillis();
        // Frame update
        if (System.currentTimeMillis() - lastTime >= frameTime) {
            nowFrame += (System.currentTimeMillis() - lastTime) / frameTime;
            if (nowFrame >= sprite.frameCount()) {
                if (!loop) {
                    stop = true;
                    return;
                }
                nowFrame %= sprite.frameCount();
            }
            lastTime = System.currentTimeMillis();
        }
        // Paint
        Rect tmp = rect.clone();
        tmp.position = Camera.trans2LPos(tmp.position);
        if (flip) {
            tmp.position.x += tmp.size.x;
            tmp.size.x = -tmp.size.x;
        }
        sprite.paint(g2d, nowFrame, tmp);
    }
}
