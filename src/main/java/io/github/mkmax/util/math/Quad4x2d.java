package io.github.mkmax.util.math;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import java.util.Objects;

public class Quad4x2d {

    private static double det (
        double ax, double ay,
        double bx, double by)
    {
        return ax * by - bx * ay;
    }

    private static double det (
        Vector2dc a,
        Vector2dc b)
    {
        return a.x () * b.y () - b.x () * a.y ();
    }

    public static final class Interpolator {

        private final Quad4x2d from;
        private final Quad4x2d to;

        /* 'from' beziers */
        private final Bezier2x2d ab;
        private final Bezier2x2d cd;

        /* 'to' beziers */
        private final Bezier2x2d xy;
        private final Bezier2x2d zw;

        private Interpolator (Quad4x2d from, Quad4x2d to) {
            this.from = Objects.requireNonNull (from);
            this.to   = Objects.requireNonNull (to);

            ab = new Bezier2x2d (from.a, from.b);
            cd = new Bezier2x2d (from.c, from.d);

            xy = new Bezier2x2d (to.a, to.b);
            zw = new Bezier2x2d (to.c, to.d);
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

            double P = (2 * Ma * (R.x - A.x) + (C.x - A.x) * Qn) / (2 * Ma * (B.x - A.x) + L.x * Qn);
            double Q = Qn / (2 * Ma);

            if (FloatingPoint.strictEq (Ma, 0d)) {
                Q = -Mc / Mb;
                P = (R.x - Q * (C.x - A.x) - A.x) / (Q * (A.x + D.x - B.x - C.x) + B.x - A.x);
            }

            Vector2d F = xy.eval (P);
            Vector2d G = zw.eval (P);

            System.out.println ("Ma: " + Ma);
            System.out.println ("Mb: " + Mb);
            System.out.println ("Mc: " + Mc);
            System.out.println ("P: " + P);
            System.out.println ("Q: " + Q);
            System.out.println ();

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
