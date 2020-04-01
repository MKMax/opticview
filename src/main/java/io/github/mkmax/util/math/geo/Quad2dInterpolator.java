package io.github.mkmax.util.math.geo;

import org.joml.Vector2d;

public interface Quad2dInterpolator {

    Quad2dc getSource ();

    void setSource (Quad2dc nSource);

    Quad2dc getDestination ();

    void setDestination (Quad2dc nDestination);

    void set (
        Quad2dc nSource,
        Quad2dc nDestination);

    Vector2d unmap (Vector2d dest);

    Vector2d unmap (Vector2d dest, Vector2d src);

    Vector2d map (Vector2d src);

    Vector2d map (Vector2d src, Vector2d dest);

}