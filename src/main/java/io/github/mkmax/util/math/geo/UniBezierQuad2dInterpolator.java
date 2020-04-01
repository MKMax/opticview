package io.github.mkmax.util.math.geo;

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
        update ();
    }

    public Quad2dc getDestination () {
        return dest;
    }

    protected void setDestination (Quad2dc nDestination) {
        dest = Objects.requireNonNull (nDestination);
        update ();
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

    }

    protected Vector2d map (Vector2d src, Vector2d dest) {

    }

    private void update () {

    }
}
