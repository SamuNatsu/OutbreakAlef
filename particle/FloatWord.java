package particle;

import auxiliary.*;

import java.awt.*;

import com.Camera;

public final class FloatWord extends AbstractParticle {
    // String
    private final String str;
    // String color
    private final Color color;
    // Move velocity
    private final Vec2 velocity;
    // Font
    private final Font fnt;

    // Constructor
    public FloatWord(Vec2 pos, String s, Color c) {
        // Super
        super(Utils.spreadPosition(pos, 10));
        lastedTime = 3000;
        // This
        str = s;
        color = c;
        velocity = new Vec2();
        fnt = Shared.MSYH_B25;
    }
    public FloatWord(Vec2 pos, Vec2 v, String s, Color c) {
        // Super
        super(Utils.spreadPosition(pos, 10));
        lastedTime = 500;
        // This
        str = s;
        color = c;
        velocity = v.clone();
        fnt = Shared.MSYH_B15;
    }

    // Override abstract methods
    @Override
    public void draw(Graphics2D g2d) {
        // Time out
        if (System.currentTimeMillis() - createTime > lastedTime) {
            die();
            return;
        }
        // Move
        position = position.add(velocity);
        // Draw
        g2d.setFont(fnt);
        g2d.setColor(color);
        Vec2 tmp = fnt == Shared.MSYH_B15 ? Camera.trans2LPos(position) : position;
        g2d.drawString(str, tmp.intX(), tmp.intY());
    }
}
