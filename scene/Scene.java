package scene;

import java.awt.*;

public interface Scene {
    // Initialize
    public void init();
    // Event handling
    public void event();
    // Draw handling
    public void draw(Graphics2D g2d);
    // Quit action
    public void quit();
}
