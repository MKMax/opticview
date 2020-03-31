package io.github.mkmax.fxe.math.graph.cartesian.camera;

import org.joml.Vector2d;

public interface Camera2d {

    void setViewport (
        double x,
        double y,
        double width,
        double height);

    void setViewport (
        double width,
        double height);

    Vector2d unproject (Vector2d dest);

    Vector2d unproject (Vector2d dest, Vector2d src);

    Vector2d project (Vector2d src);

    Vector2d project (Vector2d src, Vector2d dest);

}
