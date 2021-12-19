package particle;

import auxiliary.*;

import java.awt.*;

public class CountDown extends AbstractParticle {
    // Countdown color
    private final Color color;

    // Constructor
    public CountDown(Vec2 pos, long tm, Color c) {
        this(pos, tm, c, null);
    }
    public CountDown(Vec2 pos, long tm, Color c, Callback cb) {
        // Super
        super(pos, cb);
        lastedTime = tm;
        // This
        color = c;
    }
    //Get percent
    public double percent() {
        return (double)(System.currentTimeMillis() - createTime) / lastedTime;
    }
    // Override abstract methods
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setFont(Shared.MSYH_B25);
        g2d.setColor(color);
        long tmp = createTime + lastedTime - System.currentTimeMillis();
        if (tmp <= 0) {
            die();
            return;
        }
        else if (tmp < 10000)
            g2d.drawString(
                Long.toString(tmp / 1000) + '.' + Long.toString(tmp / 100 % 10), 
                position.intX(), position.intY());
        else 
            g2d.drawString(Long.toString(tmp / 1000) , position.intX(), position.intY());
    }
}
