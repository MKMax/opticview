package io.github.mkmax.util.math.geo;

import org.joml.Vector2dc;
import org.joml.Vector2d;

import static io.github.mkmax.util.math.ComputationStatics.max;
import static io.github.mkmax.util.math.ComputationStatics.min;

public interface Quad2d {

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    double getTopLeftX ();

    double getTopLeftY ();

    Vector2d getTopLeft ();

    Vector2d getTopLeft (Vector2d dest);

    void setTopLeftX (double nx);

    void setTopLeftY (double ny);

    void setTopLeft (double nx, double ny);

    void setTopLeft (Vector2dc nPos);

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    double getTopRightX ();

    double getTopRightY ();

    Vector2d getTopRight ();

    Vector2d getTopRight (Vector2d dest);

    void setTopRightX (double nx);

    void setTopRightY (double ny);

    void setTopRight (double nx, double ny);

    void setTopRight (Vector2dc nPos);

    /* +--------------------+ */
    /* | BOTTOM LEFT VERTEX | */
    /* +--------------------+ */

    double getBottomLeftX ();

    double getBottomLeftY ();

    Vector2d getBottomLeft ();

    Vector2d getBottomLeft (Vector2d dest);

    void setBottomLeftX (double nx);

    void setBottomLeftY (double ny);

    void setBottomLeft (double nx, double ny);

    void setBottomLeft (Vector2dc nPos);

    /* +---------------------+ */
    /* | BOTTOM RIGHT VERTEX | */
    /* +---------------------+ */

    double getBottomRightX ();

    double getBottomRightY ();

    Vector2d getBottomRight ();

    Vector2d getBottomRight (Vector2d dest);

    void setBottomRightX (double nx);

    void setBottomRightY (double ny);

    void setBottomRight (double nx, double ny);

    void setBottomRight (Vector2dc nPos);

    /* +--------------------+ */
    /* | LIGHT COMPUTATIONS | */
    /* +--------------------+ */

    default double getLeft () {
        return min (
            getTopLeftX (),
            getTopRightX (),
            getBottomLeftX (),
            getBottomRightX ()
        );
    }

    default double getRight () {
        return max (
            getTopLeftX (),
            getTopRightX (),
            getBottomLeftX (),
            getBottomRightX ()
        );
    }

    default double getBottom () {
        return min (
            getTopLeftY (),
            getTopRightY (),
            getBottomLeftY (),
            getBottomRightY ()
        );
    }

    default double getTop () {
        return max (
            getTopLeftY (),
            getTopRightY (),
            getBottomLeftY (),
            getBottomRightY ()
        );
    }

}
