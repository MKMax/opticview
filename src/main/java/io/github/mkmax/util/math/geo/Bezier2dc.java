package io.github.mkmax.util.math.geo;

import org.joml.Vector2d;

public interface Bezier2dc {

    /* +-------+ */
    /* | START | */
    /* +-------+ */

    double getStartX ();

    double getStartY ();

    default Vector2d getStart () {
        return getStart (new Vector2d ());
    }

    default Vector2d getStart (Vector2d dest) {
        return dest.set (getStartX (), getStartY ());
    }

    /* +-----+ */
    /* | END | */
    /* +-----+ */

    double getEndX ();

    double getEndY ();

    default Vector2d getEnd () {
        return getEnd (new Vector2d ());
    }

    default Vector2d getEnd (Vector2d dest) {
        return dest.set (getEndX (), getEndY ());
    }

    /* +-------------+ */
    /* | COMPUTATION | */
    /* +-------------+ */

    double evalX (double p);

    double evalY (double p);

    default Vector2d eval (double p) {
        return eval (p, new Vector2d ());
    }

    default Vector2d eval (double p, Vector2d dest) {
        return dest.set (evalX (p), evalY (p));
    }

}
