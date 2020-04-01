package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.LinearAlgebraStatics.det;

import org.joml.Matrix3x2d;
import org.joml.Vector2d;
import java.util.Objects;

public class Affine3Quad2dInterpolator implements Quad2dInterpolator {

    private static void genMapping (
        Quad2dc    from,
        Quad2dc    to,
        Matrix3x2d dest)
    {
        final double Ax = from.getBottomLeftX ();
        final double Ay = from.getBottomLeftY ();
        final double Bx = from.getBottomRightX ();
        final double By = from.getBottomRightY ();
        final double Cx = from.getTopRightX ();
        final double Cy = from.getTopRightY ();

        final double Dx = to.getBottomLeftX ();
        final double Dy = to.getBottomLeftY ();
        final double Ex = to.getBottomRightX ();
        final double Ey = to.getBottomRightY ();
        final double Fx = to.getTopRightX ();
        final double Fy = to.getTopRightY ();

        final double detBA = det (Bx, By, Ax, Ay);
        final double detCB = det (Cx, Cy, Bx, By);
        final double detAC = det (Ax, Ay, Cx, Cy);

        final double iQ = 1d / (detBA + detCB + detAC);

        /* T-vector, aka. X vector */
        final double Tx = iQ * (Fx * (By - Ay) + Dx * (Cy - By) + Ex * (Ay - Cy));
        final double Ty = iQ * (Fy * (By - Ay) + Dy * (Cy - By) + Ey * (Ay - Cy));

        /* U-vector, aka. Y vector */
        final double Ux = iQ * (Fx * (Ax - Bx) + Dx * (Bx - Cx) + Ex * (Cx - Ax));
        final double Uy = iQ * (Fy * (Ax - Bx) + Dy * (Bx - Cx) + Ey * (Cx - Ax));

        /* V-vector, aka. Z vector (or translation vector) */
        final double Vx = iQ * (Fx * detBA + Dx * detCB + Ex * detAC);
        final double Vy = iQ * (Fy * detBA + Dy * detCB + Ey * detAC);

        dest.set (
            Tx, Ty,
            Ux, Uy,
            Vx, Vy
        );
    }

    private Quad2dc src;
    private Quad2dc dest;

    private final Matrix3x2d projection = new Matrix3x2d ();
    private final Matrix3x2d inverse    = new Matrix3x2d ();

    public Affine3Quad2dInterpolator (
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
