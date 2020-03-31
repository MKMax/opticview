package io.github.mkmax.util.math.geo;

import org.joml.Vector2d;
import org.joml.Vector2dc;

/* A bezier curve using 2 vectors of 2 components each (2x2).
 * This type of curve is essentially a linear interpolation.
 *
 * https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Linear_B%C3%A9zier_curves
 */
public class Bezier2x2d implements Bezier2d {

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

    public Bezier2x2d (Bezier2x2d copy) {
        start.set (copy.start);
        end.set (copy.end);
    }

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

    /* +-------+ */
    /* | START | */
    /* +-------+ */

    @Override
    public double getStartX () {
        return start.x;
    }

    @Override
    public double getStartY () {
        return start.y;
    }

    @Override
    public void setStartX (double nx) {
        start.x = nx;
    }

    @Override
    public void setStartY (double ny) {
        start.y = ny;
    }

    /* +-----+ */
    /* | END | */
    /* +-----+ */

    @Override
    public double getEndX () {
        return end.x;
    }

    @Override
    public double getEndY () {
        return end.y;
    }

    @Override
    public void setEndX (double nx) {
        end.x = nx;
    }

    @Override
    public void setEndY (double ny) {
        end.y = ny;
    }

    /* +-------------+ */
    /* | COMPUTATION | */
    /* +-------------+ */

    @Override
    public double evalX (double p) {
        return Math.fma (end.x - start.x, p, start.x);
    }

    @Override
    public double evalY (double p) {
        return Math.fma (end.y - start.y, p, start.y);
    }

    public boolean intersect (Bezier2x2d other) {
        return intersect (other, null);
    }

    public boolean intersect (Bezier2x2d other, Vector2d dest) {
        final double Ax = start.x;
        final double Ay = start.y;
        final double Bx = end.x;
        final double By = end.y;
        final double Cx = other.start.x;
        final double Cy = other.start.y;
        final double Dx = other.end.x;
        final double Dy = other.end.y;

        final double a = Bx - Ax;
        final double b = Cx - Dx;
        final double c = Cx - Ax;
        final double x = By - Ay;
        final double y = Cy - Dy;
        final double z = Cy - Ay;

        final double P = (c * y - b * z) / (a * y - b * x);
        final double Q = (c * x - a * z) / (b * x - a * y);

        if (0.0d <= P && P <= 1.0d &&
            0.0d <= Q && Q <= 1.0d)
        {
            if (dest != null)
                eval (P, dest);
            return true;
        }

        return false;
    }
}