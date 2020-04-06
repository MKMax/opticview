package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.LinearAlgebraStatics.det;

import io.github.mkmax.util.math.Floats;
import org.joml.Vector2d;
import java.util.Objects;

public class UniBezierQuad2dInterpolator {

    private Quad2dc src;
    private Quad2dc dest;

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

    /* STVU is the quad that ABDC is mapped onto */
    private final Bezier2x2d ST = new Bezier2x2d ();
    private final Bezier2x2d UV = new Bezier2x2d ();

    /* Points of a bezier line linking AB and CD */
    private final Vector2d F = new Vector2d ();
    private final Vector2d G = new Vector2d ();

    protected UniBezierQuad2dInterpolator (
        Quad2dc pSource,
        Quad2dc pDestination)
    {
        set (pSource, pDestination);
    }

    public Quad2dc getSource () {
        return src;
    }

    protected void setSource (Quad2dc nSource) {
        src = Objects.requireNonNull (nSource);
        updateSourceQuadParameters ();
    }

    public Quad2dc getDestination () {
        return dest;
    }

    protected void setDestination (Quad2dc nDestination) {
        dest = Objects.requireNonNull (nDestination);
        updateDestinationQuadParameters ();
    }

    protected void set (
        Quad2dc nSource,
        Quad2dc nDestination)
    {
        src  = Objects.requireNonNull (nSource);
        dest = Objects.requireNonNull (nDestination);
        update ();
    }

    protected Vector2d map (Vector2d src) {
        return map (src, src);
    }

    protected Vector2d map (Vector2d src, Vector2d dest) {
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
        if (Floats.strictEq (Ma, 0d)) {
            /* When Mb is zero, but Mc is non-zero, then we have no
             * solution to the system. Because we want to avoid throwing
             * exceptions in critical code, we will yield a bogus result
             * in the hopes that the client recognizes their mistake.
             *
             * Besides, if the quads that the client is trying to map have no
             * solutions, it may be wise for the client to reconsider their
             * transforms and use a more suited structure.
             */
            if (Floats.strictEq (Mb, 0d) &&
                Floats.strictEq (Mc, 0d))
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

    private void update () {
        updateSourceQuadParameters ();
        updateDestinationQuadParameters ();
    }

    private void updateSourceQuadParameters () {
        Ax = src.getBottomLeftX ();
        Ay = src.getBottomLeftY ();
        Bx = src.getBottomRightX ();
        By = src.getBottomRightY ();
        Cx = src.getTopLeftX ();
        Cy = src.getTopLeftY ();
        Dx = src.getTopRightX ();
        Dy = src.getTopRightY ();

        Lx = Ax + Dx - Bx - Cx;
        Ly = Ay + Dy - By - Cy;

        detLC = det (Lx, Ly, Cx, Cy);
        detBC = det (Bx, By, Cx, Cy);
        detCA = det (Cx, Cy, Ax, Ay);
        detAB = det (Ax, Ay, Bx, By);
        detAL = det (Ax, Ay, Lx, Ly);

        Ma  = detAL + detLC;
        Ma2 = 2 * Ma;
    }

    private void updateDestinationQuadParameters () {
        ST.set (
            dest.getBottomLeftX (), dest.getBottomLeftY (),
            dest.getBottomRightX (), dest.getBottomRightY ()
        );

        UV.set (
            dest.getTopLeftX (), dest.getTopLeftY (),
            dest.getTopRightX (), dest.getTopRightY ()
        );
    }
}
