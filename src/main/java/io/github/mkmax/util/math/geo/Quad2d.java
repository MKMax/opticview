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

    default Vector2d getTopLeft () {
        return getTopLeft (new Vector2d ());
    }

    default Vector2d getTopLeft (Vector2d dest) {
        return dest.set (getTopLeftX (), getTopLeftY ());
    }

    void setTopLeftX (double nx);

    void setTopLeftY (double ny);

    default void setTopLeft (double nx, double ny) {
        setTopLeftX (nx);
        setTopLeftY (ny);
    }

    default void setTopLeft (Vector2dc nPos) {
        setTopLeft (nPos.x (), nPos.y ());
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

    void setTopRightX (double nx);

    void setTopRightY (double ny);

    default void setTopRight (double nx, double ny) {
        setTopRightX (nx);
        setTopRightY (ny);
    }

    default void setTopRight (Vector2dc nPos) {
        setTopRight (nPos.x (), nPos.y ());
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

    void setBottomLeftX (double nx);

    void setBottomLeftY (double ny);

    default void setBottomLeft (double nx, double ny) {
        setBottomLeftX (nx);
        setBottomLeftY (ny);
    }

    default void setBottomLeft (Vector2dc nPos) {
        setBottomLeft (nPos.x (), nPos.y ());
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

    void setBottomRightX (double nx);

    void setBottomRightY (double ny);

    default void setBottomRight (double nx, double ny) {
        setBottomRightX (nx);
        setBottomRightY (ny);
    }

    default void setBottomRight (Vector2dc nPos) {
        setBottomRight (nPos.x (), nPos.y ());
    }

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
