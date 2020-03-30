package io.github.mkmax.fxe.math.graph.cartesian.camera;

import org.joml.Vector2d;

public interface Camera2d {

    void setViewport (
        double left,
        double right,
        double bottom,
        double top
    );

    Vector2d unproject (Vector2d vpSpace);

    Vector2d unproject (Vector2d vpSpace, Vector2d funcSpace);

    Vector2d project (Vector2d funcSpace);

    Vector2d project (Vector2d funcSpace, Vector2d vpSpace);

}
