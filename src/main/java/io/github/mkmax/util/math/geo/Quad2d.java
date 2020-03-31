package io.github.mkmax.util.math.geo;

import org.joml.Vector2dc;

public interface Quad2d extends Quad2dc {

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

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

    void setBottomRightX (double nx);

    void setBottomRightY (double ny);

    default void setBottomRight (double nx, double ny) {
        setBottomRightX (nx);
        setBottomRightY (ny);
    }

    default void setBottomRight (Vector2dc nPos) {
        setBottomRight (nPos.x (), nPos.y ());
    }

}
