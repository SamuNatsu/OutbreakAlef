package scene;

import java.awt.*;

public abstract class AbstractScene {
    // Abstract methods
    public abstract void init();
    public abstract void event();
    public abstract void draw(Graphics2D g2d);
    public void quit() {}
}
