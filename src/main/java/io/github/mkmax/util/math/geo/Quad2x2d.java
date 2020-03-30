package io.github.mkmax.util.math.geo;

import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Matrix3x2d;
import org.joml.Vector2d;

import java.util.Objects;

public class Quad2x2d implements Quad2d {

    /* +--------------+ */
    /* | INTERPOLATOR | */
    /* +--------------+ */

    public static final class Interpolator {

        private static void genMapping (
            Quad2x2d   from,
            Quad2x2d   to,
            Matrix3x2d dest)
        {
            final double dx = (to.b.x - to.a.x) / (from.b.x - from.a.x);
            final double Cx = to.a.x - dx * from.a.x;

            final double dy = (to.b.y - to.a.y) / (from.b.y - from.a.y);
            final double Cy = to.a.y - dy * from.a.y;

            dest.set (
                dx, 0d,
                0d, dy,
                Cx, Cy
            );
        }

        private final Quad2x2d from;
        private final Quad2x2d to;

        private final Matrix3x2d projection = new Matrix3x2d ();
        private final Matrix3x2d inverse    = new Matrix3x2d ();

        public Interpolator (Quad2x2d pFrom, Quad2x2d pTo) {
            from = Objects.requireNonNull (pFrom);
            to   = Objects.requireNonNull (pTo);
            update ();
        }

        public Quad2x2d getFrom () {
            return from;
        }

        public Quad2x2d getTo () {
            return to;
        }

        public void update () {
            genMapping (from, to, projection);
            genMapping (to, from, inverse);
        }

        public Vector2d unmap (Vector2d dest) {
            return inverse.transformPosition (dest);
        }

        public Vector2d unmap (Vector2d dest, Vector2d src) {
            return inverse.transformPosition (dest, src);
        }

        public Vector2d map (Vector2d src) {
            return projection.transformPosition (src);
        }

        public Vector2d map (Vector2d src, Vector2d dest) {
            return projection.transformPosition (src, dest);
        }
    }

    /* +--------------------+ */
    /* | CONSTRUCTOR + DATA | */
    /* +--------------------+ */

    private final Vector2d a = new Vector2d ();
    private final Vector2d b = new Vector2d ();

    public Quad2x2d () {
        a.set (-1d, -1d);
        b.set ( 1d,  1d);
    }

    public Quad2x2d (Quad2d src) {
        src.setBottomLeft (a);
        src.setTopRight (b);
    }

    public Quad2x2d (
        Vector2dc pA,
        Vector2dc pB)
    {
        a.set (pA);
        b.set (pB);
    }

    public Quad2x2d (
        double ax, double ay,
        double bx, double by)
    {
        a.set (ax, ay);
        b.set (bx, by);
    }

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    @Override
    public double getTopLeftX () {
        return a.x;
    }

    @Override
    public double getTopLeftY () {
        return b.y;
    }

    @Override
    public void setTopLeftX (double nx) {
        a.x = nx;
    }

    @Override
    public void setTopLeftY (double ny) {
        b.y = ny;
    }

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    @Override
    public double getTopRightX () {
        return b.x;
    }

    @Override
    public double getTopRightY () {
        return b.y;
    }

    @Override
    public void setTopRightX (double nx) {
        b.x = nx;
    }

    @Override
    public void setTopRightY (double ny) {
        b.y = ny;
    }

    /* +--------------------+ */
    /* | BOTTOM LEFT VERTEX | */
    /* +--------------------+ */

    @Override
    public double getBottomLeftX () {
        return a.x;
    }

    @Override
    public double getBottomLeftY () {
        return a.y;
    }

    @Override
    public void setBottomLeftX (double nx) {
        a.x = nx;
    }

    @Override
    public void setBottomLeftY (double ny) {
        a.y = ny;
    }

    /* +---------------------+ */
    /* | BOTTOM RIGHT VERTEX | */
    /* +---------------------+ */

    @Override
    public double getBottomRightX () {
        return b.x;
    }

    @Override
    public double getBottomRightY () {
        return a.y;
    }

    @Override
    public void setBottomRightX (double nx) {
        b.x = nx;
    }

    @Override
    public void setBottomRightY (double ny) {
        a.y = ny;
    }


    /* +--------------------+ */
    /* | LIGHT COMPUTATIONS | */
    /* +--------------------+ */

    @Override
    public double getLeft () {
        return Math.min (a.x, b.x);
    }

    @Override
    public double getRight () {
        return Math.max (a.x, b.x);
    }

    @Override
    public double getBottom () {
        return Math.min (a.y, b.y);
    }

    @Override
    public double getTop () {
        return Math.max (a.y, b.y);
    }

    /* +---------------------------------+ */
    /* | TRANFORMATION AND INTERPOLATION | */
    /* +---------------------------------+ */

    public Quad2x2d transform (Matrix3x2dc mat) {
        mat.transformPosition (a);
        mat.transformPosition (b);
        return this;
    }

    public Quad2x2d transform (Matrix3x2dc mat, Quad2x2d dest) {
        mat.transformPosition (a, dest.a);
        mat.transformPosition (b, dest.b);
        return dest;
    }

    public Interpolator interpolate (Quad2x2d to) {
        return new Interpolator (this, to);
    }
}
