package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.LinearAlgebraStatics.*;

import io.github.mkmax.util.math.FloatingPoint;
import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Vector2d;
import java.util.Objects;

/**
 * The most flexible of all <code>Quad</code> figures by enabling
 * full control of all four vertices of the quad by sacrificing
 * some performance when performing interpolation.
 *
 * @author Maxim Kasyanenko
 */
public class Quad4x2d implements Quad2d {

    public static final Quad4x2d NORMAL = new Quad4x2d (
        -1d, -1d,
         1d, -1d,
        -1d,  1d,
         1d,  1d)
    {
        @Override
        public void setTopLeftX (double nx)
        { /* do not allow modification but do not throw exception. */ }

        @Override
        public void setTopLeftY (double ny)
        { /* do not allow modification but do not throw exception. */ }

        @Override
        public void setTopRightX (double nx)
        { /* do not allow modification but do not throw exception. */ }

        @Override
        public void setTopRightY (double ny)
        { /* do not allow modification but do not throw exception. */ }

        @Override
        public void setBottomLeftX (double nx)
        { /* do not allow modification but do not throw exception. */ }

        @Override
        public void setBottomLeftY (double ny)
        { /* do not allow modification but do not throw exception. */ }

        @Override
        public void setBottomRightX (double nx)
        { /* do not allow modification but do not throw exception. */ }

        @Override
        public void setBottomRightY (double ny)
        { /* do not allow modification but do not throw exception. */ }
    };

    /* +---------------+ */
    /* | INTERPOLATORS | */
    /* +---------------public + */

    private static final class BezierInterpolator {

        private Quad4x2d from;
        private Quad4x2d to;

        private double
            Ax, Ay,
            Bx, By,
            Cx, Cy,
            Dx, Dy,
            Lx, Ly;

        private double
            detLC,
            detBC,
            detCA,
            detAB,
            detAL;

        private double
            Ma,
            Ma2;

        private final Bezier2x2d ST = new Bezier2x2d ();
        private final Bezier2x2d UV = new Bezier2x2d ();

        private final Vector2d F = new Vector2d ();
        private final Vector2d G = new Vector2d ();

        private BezierInterpolator (Quad4x2d pFrom, Quad4x2d pTo) {
            from = Objects.requireNonNull (pFrom);
            to   = Objects.requireNonNull (pTo);
            update ();
        }

        public Quad4x2d getFrom () {
            return from;
        }

        public Quad4x2d getTo () {
            return to;
        }

        public void update () {
            Ax = from.a.x;
            Ay = from.a.y;
            Bx = from.b.x;
            By = from.b.y;
            Cx = from.c.x;
            Cy = from.c.y;
            Dx = from.d.x;
            Dy = from.d.y;

            Lx = Ax + Dx - Bx - Cx;
            Ly = Ay + Dy - By - Cy;

            detLC = det (Lx, Ly, Cx, Cy);
            detBC = det (Bx, By, Cx, Cy);
            detCA = det (Cx, Cy, Ax, Ay);
            detAB = det (Ax, Ay, Bx, By);
            detAL = det (Ax, Ay, Lx, Ly);

            Ma  = detAL + detLC;
            Ma2 = 2 * Ma;

            ST.set (to.a, to.b);
            UV.set (to.c, to.d);
        }

        public Vector2d map (Vector2d src) {
            return map (src, src);
        }

        public Vector2d map (Vector2d src, Vector2d dest) {
            final double Rx = src.x;
            final double Ry = src.y;

            /* These determinants must be recomputed each time as they depend on R */
            final double detRL = det (Rx, Ry, Lx, Ly);
            final double detAR = det (Ax, Ay, Rx, Ry);
            final double detRB = det (Rx, Ry, Bx, By);

            /* Remaining coefficients of the quadratic equation to solve for Q. */
            final double Mb = detRL - detAL + detAB + detBC + detCA;
            final double Mc = detAR + detRB - detAB;

            double P, Q;

            /* If our equation for Q becomes linear, we can avoid trying to compute
             * the quadratic formula. We now solve a trivial linear equation.
             *
             * TODO: Perhaps revise this if statement as we compute 'Ma' once per
             *       update() and not per function call.
             */
            if (FloatingPoint.strictEq (Ma, 0d)) {
                /* When Mb is zero, but Mc is non-zero, then we have no
                 * solution to the system. Because we want to avoid throwing
                 * exceptions in critical code, we will yield a bogus result
                 * in the hopes that the client recognizes their mistake.
                 *
                 * Besides, if the quads that the client is trying to map have no
                 * solutions, it may be wise for the client to reconsider their
                 * transforms and use a more suited structure.
                 */
                if (FloatingPoint.strictEq (Mb, 0d) &&
                    FloatingPoint.strictEq (Mc, 0d))
                {
                    P = (Rx - Ax) / (Bx - Ax);
                    Q = 0d;
                }
                else {
                    P = (Mb * (Rx - Ax) + Mc * (Cx - Ax)) / (-Mc * Lx + Mb * (Bx - Ax));
                    Q = -Mc / Mb;
                }
            }
            /* If at least Ma is non-zero, we may solve this as a quadratic
             * solution. If the quadratic has no solution, a bogus result is
             * returned to reduce overhead of exceptions. The solution will
             * attempt to keep Q within [0, 1].
             */
            else {
                final double Qd  = Math.sqrt (Mb * Mb - 4 * Ma * Mc);

                /* We check to see if Mb < -0.5d (or -Mb > 0.5d) to decide
                 * whether to opt into using the "negative" numerator version
                 * of the quadratic formula to try and keep Q within [0, 1].
                 */
                final double Qn  = -Mb + (Mb < -0.5d ? -Qd : Qd);

                P = (Ma2 * (Rx - Ax) + (Cx - Ax) * Qn) / (Ma2 * (Bx - Ax) + Lx * Qn);
                Q = Qn / Ma2;
            }

            ST.eval (P, F);
            UV.eval (P, G);

            return Bezier2x2d.quickEval (F, G, Q, dest);
        }
    }

    /**
     * Provides a state-based mechanism or interpolating between different
     * <code>Quad4x2</code> figures.
     * <p>
     * State is required to minimize heap allocation of vectors and additional
     * <code>Bezier2x2</code> curves.
     *
     * @author Maxim Kasyanenko
     */
    public static final class Interpolator {

        /**
         * Represents the "source" quad, i.e., the quad
         * whose points are mapped onto <code>to</code>.
         */
        private final Quad4x2d from;

        /**
         * Represents the "destination" quad, i.e., the quad
         * onto which input vertices will be mapped onto.
         */
        private final Quad4x2d to;

        private final BezierInterpolator projection;

        private final BezierInterpolator inverse;

        private Interpolator (
            Quad4x2d pFrom,
            Quad4x2d pTo)
        {
            from = Objects.requireNonNull (pFrom);
            to   = Objects.requireNonNull (pTo);

            projection = new BezierInterpolator (from, to);
            inverse    = new BezierInterpolator (to, from);
        }

        public Quad4x2d getFrom () {
            return from;
        }

        public Quad4x2d getTo () {
            return to;
        }

        public void update () {
            projection.update ();
            inverse.update ();
        }

        public Vector2d unmap (Vector2d dest) {
            return inverse.map (dest);
        }

        public Vector2d unmap (Vector2d dest, Vector2d src) {
            return inverse.map (dest, src);
        }

        public Vector2d map (Vector2d src) {
            return projection.map (src);
        }

        public Vector2d map (Vector2d src, Vector2d dest) {
            return projection.map (src, dest);
        }
    }

    /* +--------------------+ */
    /* | CONSTRUCTOR + DATA | */
    /* +--------------------+ */

    private final Vector2d a = new Vector2d ();
    private final Vector2d b = new Vector2d ();
    private final Vector2d c = new Vector2d ();
    private final Vector2d d = new Vector2d ();

    public Quad4x2d () {
        a.set (-1d, -1d);
        b.set ( 1d, -1d);
        c.set ( 1d,  1d);
        d.set (-1d,  1d);
    }

    public Quad4x2d (Quad2d src) {
        src.getBottomLeft (a);
        src.getBottomRight (b);
        src.setTopLeft (c);
        src.setTopRight (d);
    }

    public Quad4x2d (
        Vector2dc pA,
        Vector2dc pB,
        Vector2dc pC,
        Vector2dc pD)
    {
        a.set (pA);
        b.set (pB);
        c.set (pC);
        d.set (pD);
    }

    public Quad4x2d (
        double ax, double ay,
        double bx, double by,
        double cx, double cy,
        double dx, double dy)
    {
        a.set (ax, ay);
        b.set (bx, by);
        c.set (cx, cy);
        d.set (dx, dy);
    }

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    @Override
    public double getTopLeftX () {
        return c.x;
    }

    @Override
    public double getTopLeftY () {
        return c.y;
    }

    @Override
    public void setTopLeftX (double nx) {
        c.x = nx;
    }

    @Override
    public void setTopLeftY (double ny) {
        c.y = ny;
    }

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    @Override
    public double getTopRightX () {
        return d.x;
    }

    @Override
    public double getTopRightY () {
        return d.y;
    }

    @Override
    public void setTopRightX (double nx) {
        d.x = nx;
    }

    @Override
    public void setTopRightY (double ny) {
        d.y = ny;
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
        return b.y;
    }

    @Override
    public void setBottomRightX (double nx) {
        b.x = nx;
    }

    @Override
    public void setBottomRightY (double ny) {
        b.y = ny;
    }

    /* +---------------------------------+ */
    /* | TRANFORMATION AND INTERPOLATION | */
    /* +---------------------------------+ */

    public Quad4x2d transform (Matrix3x2dc mat) {
        mat.transformPosition (a);
        mat.transformPosition (b);
        mat.transformPosition (c);
        mat.transformPosition (d);
        return this;
    }

    public Quad4x2d transform (Matrix3x2dc mat, Quad4x2d dest) {
        mat.transformPosition (a, dest.a);
        mat.transformPosition (b, dest.b);
        mat.transformPosition (c, dest.c);
        mat.transformPosition (d, dest.d);
        return dest;
    }

    public Interpolator interpolate (Quad4x2d to) {
        return new Interpolator (this, to);
    }
}
