package io.github.mkmax.util.math.geo;

import org.joml.Vector2dc;

public interface Bezier2d extends Bezier2dc {

    /* +-------+ */
    /* | START | */
    /* +-------+ */

    void setStartX (double nx);

    void setStartY (double ny);

    default void setStart (double nx, double ny) {
        setStartX (nx);
        setStartY (ny);
    }

    default void setStart (Vector2dc nPos) {
        setStart (nPos.x (), nPos.y ());
    }

    /* +-----+ */
    /* | END | */
    /* +-----+ */

    void setEndX (double nx);

    void setEndY (double ny);

    default void setEnd (double nx, double ny) {
        setEndX (nx);
        setEndY (ny);
    }

    default void setEnd (Vector2dc nPos) {
        setEnd (nPos.x (), nPos.y ());
    }

    /* +------+ */
    /* | BOTH | */
    /* +------+ */

    default void set (
        double sx, double sy,
        double ex, double ey)
    {
        setStart (sx, sy);
        setEnd (ex, ey);
    }

    default void set (
        Vector2dc pStart,
        Vector2dc pEnd)
    {
        set (
            pStart.x (), pStart.y (),
            pEnd.x (), pEnd.y ()
        );
    }
}
