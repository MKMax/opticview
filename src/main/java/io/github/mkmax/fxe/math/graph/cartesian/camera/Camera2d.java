package io.github.mkmax.fxe.math.graph.cartesian.camera;

import io.github.mkmax.util.math.geo.ReadBox2d;
import org.joml.Vector2d;

public interface Camera2d {

    ReadBox2d getViewport ();

    void setViewport (ReadBox2d box);

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
