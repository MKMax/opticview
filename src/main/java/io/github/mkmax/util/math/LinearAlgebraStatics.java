package io.github.mkmax.util.math;

import org.joml.Vector2dc;

public class LinearAlgebraStatics {

    /* +--------------+ */
    /* | Determinants | */
    /* +--------------+ */

    public static double det (
        double ax, double ay,
        double bx, double by)
    {
        return Math.fma (ax, by, -bx * ay);
    }

    public static double det (
        Vector2dc a,
        Vector2dc b)
    {
        return Math.fma (a.x (), b.y (), - b.x () * a.y ());
    }

}
