package io.github.mkmax.util.math.geo;

import org.joml.Matrix3x2d;
import org.joml.Vector2d;

import java.util.Objects;

public class Affine2Quad2dInterpolator implements Quad2dInterpolator {

    public static void genMapping (
        Quad2dc    from,
        Quad2dc    to,
        Matrix3x2d dest)
    {
        final double Ax = from.getBottomLeftX ();
        final double Ay = from.getBottomLeftY ();
        final double Bx = from.getTopRightX ();
        final double By = from.getTopRightY ();

        final double Cx = to.getBottomLeftX ();
        final double Cy = to.getBottomLeftY ();
        final double Dx = to.getTopRightX ();
        final double Dy = to.getTopRightY ();

        final double dx = (Dx - Cx) / (Bx - Ax);
        final double Kx = Cx - dx * Ax;

        final double dy = (Dy - Cy) / (By - Ay);
        final double Ky = Cy - dy * Ay;

        dest.set (
            dx, 0d,
            0d, dy,
            Kx, Ky
        );
    }

    private Quad2dc src;
    private Quad2dc dest;

    private final Matrix3x2d projection = new Matrix3x2d ();
    private final Matrix3x2d inverse    = new Matrix3x2d ();

    public Affine2Quad2dInterpolator (
        Quad2dc pSource,
        Quad2dc pDestination)
    {
        set (pSource, pDestination);
    }

    @Override
    public Quad2dc getSource () {
        return src;
    }

    @Override
    public void setSource (Quad2dc nSource) {
        src = Objects.requireNonNull (nSource);
        update ();
    }

    @Override
    public Quad2dc getDestination () {
        return dest;
    }

    @Override
    public void setDestination (Quad2dc nDestination) {
        dest = Objects.requireNonNull (nDestination);
        update ();
    }

    @Override
    public void set (Quad2dc nSource, Quad2dc nDestination) {
        src  = Objects.requireNonNull (nSource);
        dest = Objects.requireNonNull (nDestination);
        update ();
    }

    @Override
    public Vector2d unmap (Vector2d dest) {
        return inverse.transformPosition (dest);
    }

    @Override
    public Vector2d unmap (Vector2d dest, Vector2d src) {
        return inverse.transformPosition (dest, src);
    }

    @Override
    public Vector2d map (Vector2d src) {
        return projection.transformPosition (src);
    }

    @Override
    public Vector2d map (Vector2d src, Vector2d dest) {
        return projection.transformPosition (src, dest);
    }

    private void update () {
        genMapping (src, dest, projection);
        genMapping (dest, src, inverse);
    }
}
