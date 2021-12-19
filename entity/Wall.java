package entity;

import auxiliary.*;
import com.*;
import particle.*;

import java.awt.*;

public final class Wall extends AbstractEntity {
    // Entity ID accumulator
    static private long accumulator = 0;
    // Static 
    static private final Vec2 v1 = Vec2.getInstance(100, 100), v2 = Vec2.getInstance(50, 50), 
        v3 = Vec2.getInstance(80, 80), v4 = Vec2.getInstance(50, 30), v5 = Vec2.getInstance(65, 50),
        v6 = Vec2.getInstance(5, -5), v7 = Vec2.getInstance(0, -3), v8 = Vec2.getInstance(-2, -13), v9 = Vec2.getInstance(0, -30);
    // Type define
    static public enum WallType {
        // Item
        RockA(Shared.rockAT, v1, v2, v6), 
        RockB(Shared.rockBT, v1, v2, v7),
        Box(Shared.boxS.getTexture(), v3, v5, v8),
        Grave(Shared.graveT, v1, v4, v9);
        // External texture
        public final Image texture;
        // Texture size, Hitbox size & draw offset
        public final Vec2 sizeT, sizeH, offset;

        // Constructor
        private WallType(Image img, Vec2 szT, Vec2 szH, Vec2 off) {
            texture = img;
            sizeT = szT;
            sizeH = szH;
            offset = off;
        }
    }
    // Wall type
    public final WallType wallType;

    // Constructor
    public Wall(Vec2 pos, int hp, WallType tp) {
        // Super
        super(Type.Wall);
        id = accumulator++;
        rect = Rect.getInstance(pos, tp.sizeH);
        health = hp;
        // This
        wallType = tp;
    }
    // Override abstract methods
    @Override
    public void onHit(AbstractEntity obj) {}
    @Override
    public void onInjured(AbstractEntity obj) {
        if (health == -1)
            return;
        health -= obj.damage;
        if (health <= 0) {
            onDeath(obj);
            return;
        }
        // Box injured animate
        if (wallType == WallType.Box)
            EntityPool.particle.add(new BoxInjured(rect.position.add(wallType.offset).sub(wallType.sizeT.div(2))));
    }
    @Override
    public void onDeath(AbstractEntity obj) {
        EntityPool.wall.remove(id);
        EntityPool.particle.add(new Cloud(rect.position, 1, 1));
    }
    @Override
    public void onMove(double time) {}
    @Override
    public void draw(Graphics2D g2d) {
        Vec2 pos = Camera.trans2LPos(rect.position);
        pos = pos.sub(wallType.sizeT.div(2)).add(wallType.offset);
        g2d.drawImage(
            wallType.texture, 
            pos.intX(), pos.intY(), pos.intX() + wallType.sizeT.intX(), pos.intY() + wallType.sizeT.intY(), 
            0, 0, 24, 24, null);
    }
}
