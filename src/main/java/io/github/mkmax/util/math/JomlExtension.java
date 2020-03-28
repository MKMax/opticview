package io.github.mkmax.util.math;

import org.joml.Vector2dc;

public class JomlExtension {

    /* +--------------+ */
    /* | Determinants | */
    /* +--------------+ */

    public static double det (
        double ax, double ay,
        double bx, double by)
    {
        return ax * by - bx * ay;
    }

    public static double det (
        Vector2dc a,
        Vector2dc b)
    {
        return a.x () * b.y () - b.x () * a.y ();
    }

}
