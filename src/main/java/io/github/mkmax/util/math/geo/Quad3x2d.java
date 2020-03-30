package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.LinearAlgebraStatics.*;

import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Matrix3x2d;
import org.joml.Vector2d;
import java.util.Objects;

/* A quad using 3 vectors of 2 components each (3x2) */
public class Quad3x2d implements Quad2d {

    public static final Quad3x2d NORMAL = new Quad3x2d (
        -1d, -1d,
         1d, -1d,
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

    /* +--------------+ */
    /* | INTERPOLATOR | */
    /* +--------------+ */

    public static final class Interpolator {

        private static void genMapping (
            Quad3x2d   from,
            Quad3x2d   to,
            Matrix3x2d dest)
        {
            final double Ax = from.a.x;
            final double Ay = from.a.y;
            final double Bx = from.b.x;
            final double By = from.b.y;
            final double Cx = from.c.x;
            final double Cy = from.c.y;

            final double Dx = to.a.x;
            final double Dy = to.a.y;
            final double Ex = to.b.x;
            final double Ey = to.b.y;
            final double Fx = to.c.x;
            final double Fy = to.c.y;

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

        private final Quad3x2d from;
        private final Quad3x2d to;

        private final Matrix3x2d projection = new Matrix3x2d ();
        private final Matrix3x2d inverse    = new Matrix3x2d ();

        private Interpolator (Quad3x2d pSrc, Quad3x2d pDest) {
            from = Objects.requireNonNull (pSrc);
            to   = Objects.requireNonNull (pDest);
            update ();
        }

        public Quad3x2d getFrom () {
            return from;
        }

        public Quad3x2d getTo () {
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

    private final Vector2d a = new Vector2d (); // lower-left
    private final Vector2d b = new Vector2d (); // lower-right
    private final Vector2d c = new Vector2d (); // upper-right

    public Quad3x2d () {
        a.set (-1d, -1d);
        b.set ( 1d, -1d);
        c.set ( 1d,  1d);
    }

    public Quad3x2d (Quad2d src) {
        src.getBottomLeft (a);
        src.getBottomRight (b);
        src.getTopRight (c);
    }

    public Quad3x2d (
        Vector2dc pA,
        Vector2dc pB,
        Vector2dc pC)
    {
        a.set (pA);
        b.set (pB);
        c.set (pC);
    }

    public Quad3x2d (
        double ax, double ay,
        double bx, double by,
        double cx, double cy)
    {
        a.set (ax, ay);
        b.set (bx, by);
        c.set (cx, cy);
    }

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    @Override
    public double getTopLeftX () {
        return c.x - (b.x - a.x);
    }

    @Override
    public double getTopLeftY () {
        return c.y - (b.y - a.y);
    }

    @Override
    public void setTopLeftX (double nx) {
        c.x = nx + (b.x - a.x);
    }

    @Override
    public void setTopLeftY (double ny) {
        c.y = ny + (b.y - a.y);
    }

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    @Override
    public double getTopRightX () {
        return c.x;
    }

    @Override
    public double getTopRightY () {
        return c.y;
    }

    @Override
    public void setTopRightX (double nx) {
        c.x = nx;
    }

    @Override
    public void setTopRightY (double ny) {
        c.y = ny;
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

    public Quad3x2d transform (Matrix3x2dc mat) {
        mat.transformPosition (a);
        mat.transformPosition (b);
        mat.transformPosition (c);
        return this;
    }

    public Quad3x2d transform (Matrix3x2dc mat, Quad3x2d dest) {
        mat.transformPosition (a, dest.a);
        mat.transformPosition (b, dest.b);
        mat.transformPosition (c, dest.c);
        return dest;
    }

    public Interpolator interpolate (Quad3x2d dest) {
        return new Interpolator (this, dest);
    }
}
