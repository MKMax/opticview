package io.github.mkmax.util.math.geo;

import org.joml.Vector2d;
import org.joml.Vector2dc;

/* A bezier curve using 2 vectors of 2 components each (2x2).
 * This type of curve is essentially a linear interpolation.
 *
 * https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Linear_B%C3%A9zier_curves
 */
public class Bezier2x2d {

    public static Vector2d quickEval (
        Vector2d start,
        Vector2d end,
        double   p)
    {
        return quickEval (start, end, p, new Vector2d ());
    }

    public static Vector2d quickEval (
        Vector2d start,
        Vector2d end,
        double   p,
        Vector2d dest)
    {
        dest.x = Math.fma (end.x - start.x, p, start.x);
        dest.y = Math.fma (end.y - start.y, p, start.y);
        return dest;
    }

    private final Vector2d start = new Vector2d ();
    private final Vector2d end   = new Vector2d ();

    public Bezier2x2d (
        double ax, double ay,
        double bx, double by)
    {
        start.set (ax, ay);
        end.set (bx, by);
    }

    public Bezier2x2d (
        Vector2dc pA,
        Vector2dc pB)
    {
        start.set (pA);
        end.set (pB);
    }

    public Bezier2x2d () {
        start.set (0d, 0d);
        end.set (1d, 1d);
    }

    /* +---------+ */
    /* | Getters | */
    /* +---------+ */

    public double getStartX () {
        return start.x;
    }

    public double getStartY () {
        return start.y;
    }

    public Vector2d getStart () {
        return new Vector2d (start);
    }

    public Vector2d getStart (Vector2d dest) {
        return dest.set (start);
    }

    public double getEndX () {
        return end.x;
    }

    public double getEndY () {
        return end.y;
    }

    public Vector2d getEnd () {
        return new Vector2d (end);
    }

    public Vector2d getEnd (Vector2d dest) {
        return dest.set (end);
    }

    /* +---------+ */
    /* | Setters | */
    /* +---------+ */

    public void setStart (double ax, double ay) {
        start.set (ax, ay);
    }

    public void setStart (Vector2dc pA) {
        start.set (pA);
    }

    public void setEnd (double bx, double by) {
        end.set (bx, by);
    }

    public void setEnd (Vector2dc pB) {
        end.set (pB);
    }

    public void set (
        double ax, double ay,
        double bx, double by)
    {
        start.set (ax, ay);
        end.set (bx, by);
    }

    public void set (
        Vector2dc pA,
        Vector2dc pB)
    {
        start.set (pA);
        end.set (pB);
    }

    /* +------------+ */
    /* | Evaluators | */
    /* +------------+ */

    public double evalX (double p) {
        return Math.fma (end.x - start.x, p, start.x);
    }

    public double evalY (double p) {
        return Math.fma (end.y - start.y, p, start.y);
    }

    public Vector2d eval (double p) {
        return eval (p, new Vector2d ());
    }

    public Vector2d eval (double p, Vector2d dest) {
        dest.x = evalX (p);
        dest.y = evalY (p);
        return dest;
    }
}