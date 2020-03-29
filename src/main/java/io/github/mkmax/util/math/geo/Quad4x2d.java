package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.LinearAlgebraStatics.*;

import io.github.mkmax.util.math.FloatingPoint;
import org.joml.Vector2d;
import org.joml.Vector2dc;

import java.util.Objects;

/**
 * The most flexible of all <code>Quad</code> figures by enabling
 * full control of all four vertices of the quad by sacrificing
 * some performance when performing interpolation.
 *
 * @author Maxim Kasyanenko
 */
public class Quad4x2d {

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
        private Quad4x2d from;

        /**
         * Represents the "destination" quad, i.e., the quad
         * onto which input vertices will be mapped onto.
         */
        private Quad4x2d to;

        /* For some "destination" quad STVU, this represents the segment ST. */
        private final Bezier2x2d ST = new Bezier2x2d ();

        /* For some "destination" quad STVU, this represents the segment UV. */
        private final Bezier2x2d UV = new Bezier2x2d ();

        /**
         * A pre-allocated vector that will store the "source" vector being
         * transformed.
         */
        private final Vector2d R = new Vector2d ();

        /**
         * A pre-allocated vector used in the computation of the
         * transformation.
         */
        private final Vector2d L = new Vector2d ();

        /**
         * A pre-allocated vector used in the final stages of
         * computation of the transformation.
         */
        private final Vector2d F = new Vector2d ();

        /**
         * A pre-allocated vector used in the final stages of
         * computation of the transformation.
         */
        private final Vector2d G = new Vector2d ();

        private Interpolator (Quad4x2d pFrom, Quad4x2d pTo) {
            from = Objects.requireNonNull (pFrom);
            to   = Objects.requireNonNull (pTo);
        }

        public Quad4x2d getFrom () {
            return from;
        }

        public void setFrom (Quad4x2d nFrom) {
            from = Objects.requireNonNull (nFrom);
        }

        public Quad4x2d getTo () {
            return to;
        }

        public void setTo (Quad4x2d nTo) {
            to = Objects.requireNonNull (nTo);
        }

        public Vector2d transform (Vector2d src) {
            return transform (src, src);
        }

        public Vector2d transform (Vector2d src, Vector2d dest) {
            final double Ax = from.a.x;
            final double Ay = from.a.y;
            final double Bx = from.b.x;
            final double By = from.b.y;
            final double Cx = from.c.x;
            final double Cy = from.c.y;
            final double Dx = from.d.x;
            final double Dy = from.d.y;

            ST.set (to.a, to.b);
            UV.set (to.c, to.d);

            final double Rx = src.x;
            final double Ry = src.y;

            final double Lx = Ax + Dx - Bx - Cx;
            final double Ly = Ay + Dy - By - Cy;

            /* Following determinants used only once. */
            final double detLC = det (Lx, Ly, Cx, Cy);
            final double detRL = det (Rx, Ry, Lx, Ly);
            final double detBC = det (Bx, By, Cx, Cy);
            final double detCA = det (Cx, Cy, Ax, Ay);
            final double detAR = det (Ax, Ay, Rx, Ry);
            final double detRB = det (Rx, Ry, Bx, By);

            /* Following determinants used multiple times. */
            final double detAB = det (Ax, Ay, Bx, By);
            final double detAL = det (Ax, Ay, Lx, Ly);

            /* Coefficients of the quadratic equation to solve for Q. */
            final double Ma = detAL + detLC;
            final double Mb = detRL - detAL + detAB + detBC + detCA;
            final double Mc = detAR + detRB - detAB;

            double P, Q;

            /* If our equation for Q becomes linear, we can avoid trying to compute
             * the quadratic formula. We now solve a trivial linear equation.
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
                final double Ma2 = 2 * Ma;
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

    private final Vector2d a = new Vector2d ();
    private final Vector2d b = new Vector2d ();
    private final Vector2d c = new Vector2d ();
    private final Vector2d d = new Vector2d ();

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

    public Quad4x2d () {
        a.set (-1d, -1d);
        b.set ( 1d, -1d);
        c.set ( 1d,  1d);
        d.set (-1d,  1d);
    }

    public Interpolator interpolate (Quad4x2d to) {
        return new Interpolator (this, to);
    }
}
