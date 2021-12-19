package com;

import auxiliary.*;

public final class Camera {
    // Static
    static private final Vec2 staticOffset = Application.size.div(2);
    // Top-left offset of camera
    static private final Vec2 topLeftOffset = Application.size.div(-2);

    // Look at global position
    static public void lookAt(Vec2 obj) {
        lookAt(obj.x, obj.y);
    }
    static public void lookAt(double x, double y) {
        topLeftOffset.set(x - staticOffset.x, y - staticOffset.y);
    }
    // Look at player
    static public void lookAtPlayer() {
        lookAt(EntityPool.nowPlayer.rect.position);
    }
    // Transfer local position to global position
    static public Vec2 trans2GPos(Vec2 obj) {
        return obj.add(topLeftOffset);
    }
    static public Vec2 trans2GPos(double x, double y) {
        return topLeftOffset.add(x, y);
    }
    // Transfer global position to local position
    static public Vec2 trans2LPos(Vec2 obj) {
        return obj.sub(topLeftOffset);
    }
    static public Vec2 trans2LPos(double x, double y) {
        return topLeftOffset.negate().add(x, y);
    }
}
