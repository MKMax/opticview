package io.github.mkmax.util.math;

import static io.github.mkmax.util.math.JomlExtension.*;

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
     * <code>Bezier2x2</code> figures.
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

        /** For some "source" quad DCAB, this represents the segment AB. */
        private final Bezier2x2d AB = new Bezier2x2d ();

        /** For some "source" quad DCAB, this represents the segment CD. */
        private final Bezier2x2d CD = new Bezier2x2d ();

        /* For some "destination" quad VUST, this represents the segment ST. */
        private final Bezier2x2d ST = new Bezier2x2d ();

        /* For some "destination" quad VUST, this represents the segment UV. */
        private final Bezier2x2d UV = new Bezier2x2d ();

        /* Vectors used in computations, pre-allocated for performance */

        private Interpolator (Quad4x2d pFrom, Quad4x2d pTo) {
            from = Objects.requireNonNull (pFrom);
            to   = Objects.requireNonNull (pTo);
            update ();

            AB = new Bezier2x2d (pFrom.a, pFrom.b);
            CD = new Bezier2x2d (pFrom.c, pFrom.d);

            ST = new Bezier2x2d (pTo.a, pTo.b);
            UV = new Bezier2x2d (pTo.c, pTo.d);
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

        public void update () {
            AB.
        }

        public Vector2d transform (Vector2d src) {
            return transform (src, src);
        }

        public Vector2d transform (Vector2d src, Vector2d dest) {
            final Vector2d A = from.a;
            final Vector2d B = from.b;
            final Vector2d C = from.c;
            final Vector2d D = from.d;

            final Vector2d R = new Vector2d (src);
            final Vector2d L = new Vector2d (
                A.x + D.x - B.x - C.x,
                A.y + D.y - B.y - C.y
            );

            final double Ma = det (A, L) + det (L, C);
            final double Mb = det (R, L) + det (L, A) + det (A, B) + det (B, C) + det (C, A);
            final double Mc = det (A, R) + det (R, B) + det (B, A);

            final double Qn = -Mb + Math.sqrt (Mb * Mb - 4 * Ma * Mc);

            double P;
            double Q;

            if (FloatingPoint.strictEq (Ma, 0d)) {
                P = (Mb * (R.x - A.x) + Mc * (C.x - A.x)) / (-Mc * L.x + Mb * (B.x - A.x));
                Q = -Mc / Mb;
                // P = (R.x - Q * (C.x - A.x) - A.x) / (Q * (A.x + D.x - B.x - C.x) + B.x - A.x);
            }
            else {
                P = (2 * Ma * (R.x - A.x) + (C.x - A.x) * Qn) / (2 * Ma * (B.x - A.x) + L.x * Qn);
                Q = Qn / (2 * Ma);
            }

            Vector2d F = ST.eval (P);
            Vector2d G = UV.eval (P);

            return dest.set (new Bezier2x2d (F, G).eval (Q));
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
