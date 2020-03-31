package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.ComputationStatics.min;
import static io.github.mkmax.util.math.ComputationStatics.max;

import org.joml.Vector2d;

public interface Quad2dc {

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    double getTopLeftX ();

    double getTopLeftY ();

    default Vector2d getTopLeft () {
        return getTopLeft (new Vector2d ());
    }

    default Vector2d getTopLeft (Vector2d dest) {
        return dest.set (getTopLeftX (), getTopLeftY ());
    }

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    double getTopRightX ();

    double getTopRightY ();

    default Vector2d getTopRight () {
        return getTopRight (new Vector2d ());
    }

    default Vector2d getTopRight (Vector2d dest) {
        return dest.set (getTopRightX (), getTopRightY ());
    }

    /* +--------------------+ */
    /* | BOTTOM LEFT VERTEX | */
    /* +--------------------+ */

    double getBottomLeftX ();

    double getBottomLeftY ();

    default Vector2d getBottomLeft () {
        return getBottomLeft (new Vector2d ());
    }

    default Vector2d getBottomLeft (Vector2d dest) {
        return dest.set (getBottomLeftX (), getBottomLeftY ());
    }

    /* +---------------------+ */
    /* | BOTTOM RIGHT VERTEX | */
    /* +---------------------+ */

    double getBottomRightX ();

    double getBottomRightY ();

    default Vector2d getBottomRight () {
        return getBottomRight (new Vector2d ());
    }

    default Vector2d getBottomRight (Vector2d dest) {
        return dest.set (getBottomRightX (), getBottomRightY ());
    }

    /* +------------+ */
    /* | BOUNDARIES | */
    /* +------------+ */

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
