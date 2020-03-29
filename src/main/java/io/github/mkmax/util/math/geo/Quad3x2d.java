package io.github.mkmax.util.math.geo;

import static io.github.mkmax.util.math.ComputationStatics.*;

import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Matrix3x2d;
import org.joml.Vector2d;

/* A quad using 3 vectors of 2 components each (3x2) */
public class Quad3x2d implements Quad2d {

    public static final class Interpolator3x2d implements Quad2d.Interpolator2d {

        private Quad3x2d from;
        private Quad3x2d to;

        public Interpolator3x2d (Quad3x2d src, Quad3x2d dest) {

        }

        @Override
        public <T extends Quad2d> T getSource () {
            return null;
        }

        @Override
        public <T extends Quad2d> T getDestination () {
            return null;
        }

        @Override
        public Vector2d interpolate (Vector2d src) {
            return null;
        }

        @Override
        public Vector2d interpolate (Vector2d src, Vector2d dest) {
            return null;
        }

    }

    private final Vector2d a = new Vector2d (); // lower-left
    private final Vector2d b = new Vector2d (); // lower-right
    private final Vector2d c = new Vector2d (); // upper-right

    public Quad3x2d (
        Vector2dc pA,
        Vector2dc pB,
        Vector2dc pC)
    {
        parallelogram (pA, pB, pC);
    }

    public Quad3x2d (
        double ax, double ay,
        double bx, double by,
        double cx, double cy)
    {
        parallelogram (ax, ay, bx, by, cx, cy);
    }

    public Quad3x2d (
        double px, double py,
        double qx, double qy)
    {
        rectangle (px, py, qx, qy);
    }

    public Quad3x2d (
        Vector2dc p,
        Vector2dc q)
    {
        rectangle (p, q);
    }

    public Quad3x2d (Quad2d src) {
        set (src);
    }

    public Quad3x2d () {
        normal ();
    }

    public Quad3x2d normal () {
        a.set (-1d, -1d);
        b.set ( 1d, -1d);
        c.set ( 1d,  1d);
        return this;
    }

    public Quad3x2d rectangle (
        double px, double py,
        double qx, double qy)
    {
        a.set (px, py);
        b.set (qx, py);
        c.set (qx, qy);
        return this;
    }

    public Quad3x2d rectangle (
        Vector2dc p,
        Vector2dc q)
    {
        return rectangle (p.x (), p.y (), q.x (), q.y ());
    }

    public Quad3x2d ortho (
        double left,
        double right,
        double bottom,
        double top)
    {
        a.set (left, bottom);
        b.set (right, bottom);
        c.set (right, top);
        return this;
    }

    public Quad3x2d parallelogram (
        double ax, double ay,
        double bx, double by,
        double cx, double cy)
    {
        a.set (ax, ay);
        b.set (bx, by);
        c.set (cx, cy);
        return this;
    }

    public Quad3x2d parallelogram (
        Vector2dc pA,
        Vector2dc pB,
        Vector2dc pC)
    {
        a.set (pA);
        b.set (pB);
        c.set (pC);
        return this;
    }


    /* +---------+ */
    /* | GENERAL | */
    /* +---------+ */

    @Override
    public void set (Quad2d q) {
        q.getBottomLeft (a);
        q.getBottomRight (b);
        q.getTopRight (c);
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
    public Vector2d getTopLeft () {
        return getTopLeft (new Vector2d ());
    }

    @Override
    public Vector2d getTopLeft (Vector2d dest) {
        return dest.set (getTopLeftX (), getTopLeftY ());
    }

    @Override
    public void setTopLeftX (double nx) {
        c.x = nx + (b.x - a.x);
    }

    @Override
    public void setTopLeftY (double ny) {
        c.y = ny + (b.y - a.y);
    }

    @Override
    public void setTopLeft (double nx, double ny) {
        setTopLeftX (nx);
        setTopLeftY (ny);
    }

    @Override
    public void setTopLeft (Vector2dc nPos) {
        setTopLeft (nPos.x (), nPos.y ());
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
    public Vector2d getTopRight () {
        return getTopRight (new Vector2d ());
    }

    @Override
    public Vector2d getTopRight (Vector2d dest) {
        return dest.set (getTopRightX (), getTopRightY ());
    }

    @Override
    public void setTopRightX (double nx) {
        c.x = nx;
    }

    @Override
    public void setTopRightY (double ny) {
        c.y = ny;
    }

    @Override
    public void setTopRight (double nx, double ny) {
        setTopRightX (nx);
        setTopRightY (ny);
    }

    @Override
    public void setTopRight (Vector2dc nPos) {
        setTopRight (nPos.x (), nPos.y ());
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
    public Vector2d getBottomLeft () {
        return getBottomLeft (new Vector2d ());
    }

    @Override
    public Vector2d getBottomLeft (Vector2d dest) {
        return dest.set (getBottomLeftX (), getBottomLeftY ());
    }

    @Override
    public void setBottomLeftX (double nx) {
        a.x = nx;
    }

    @Override
    public void setBottomLeftY (double ny) {
        a.y = ny;
    }

    @Override
    public void setBottomLeft (double nx, double ny) {
        setBottomLeftX (nx);
        setBottomLeftX (ny);
    }

    @Override
    public void setBottomLeft (Vector2dc nPos) {
        setBottomLeft (nPos.x (), nPos.y ());
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
    public Vector2d getBottomRight () {
        return getBottomRight (new Vector2d ());
    }

    @Override
    public Vector2d getBottomRight (Vector2d dest) {
        return dest.set (getBottomRightX (), getBottomRightY ());
    }

    @Override
    public void setBottomRightX (double nx) {
        b.x = nx;
    }

    @Override
    public void setBottomRightY (double ny) {
        b.y = ny;
    }

    @Override
    public void setBottomRight (double nx, double ny) {
        setBottomRightX (nx);
        setBottomRightY (ny);
    }

    @Override
    public void setBottomRight (Vector2dc nPos) {
        setBottomRight (nPos.x (), nPos.y ());
    }

    /* +--------------------+ */
    /* | LIGHT COMPUTATIONS | */
    /* +--------------------+ */

    @Override
    public double getLeft () {
        return min (
            getTopLeftX (),
            getTopRightX (),
            getBottomLeftX (),
            getBottomRightX ()
        );
    }

    @Override
    public double getRight () {
        return max (
            getTopLeftX (),
            getTopRightX (),
            getBottomLeftX (),
            getBottomRightX ()
        );
    }

    @Override
    public double getBottom () {
        return min (
            getTopLeftY (),
            getTopRightY (),
            getBottomLeftY (),
            getBottomRightY ()
        );
    }

    @Override
    public double getTop () {
        return max (
            getTopLeftY (),
            getTopRightY (),
            getBottomLeftY (),
            getBottomRightY ()
        );
    }

    /* +---------------------------------+ */
    /* | TRANFORMATION AND INTERPOLATION | */
    /* +---------------------------------+ */

    @Override
    @SuppressWarnings ("unchecked")
    public <T extends Quad2d> T transform (Matrix3x2dc mat) {
        mat.transformPosition (a);
        mat.transformPosition (b);
        mat.transformPosition (c);
        return (T) this;
    }

    @Override
    @SuppressWarnings ("unchecked")
    public <T extends Quad2d> T transform (Matrix3x2dc mat, Quad2d dest) {
        if (dest == this)
            return transform (mat);
        dest.set (this);
        return (T) dest.transform (mat);
    }

    @Override
    public <T extends Interpolator2d> T interpolate (Quad2d to) {
        if (to.getOrder () >)
        return null;
    }

    /* --------------------- */

    public Matrix3x2d unmap (Quad3x2d from) {
        return from.map (this);
    }

    public Matrix3x2d unmap (Quad3x2d from, Matrix3x2d dest) {
        return from.map (this, dest);
    }

    public Matrix3x2d map (Quad3x2d to) {
        return map (to, new Matrix3x2d ());
    }

    public Matrix3x2d map (Quad3x2d to, Matrix3x2d dest) {
        final Vector2d A = a;
        final Vector2d B = b;
        final Vector2d C = c;

        final Vector2d D = to.a;
        final Vector2d E = to.b;
        final Vector2d F = to.c;

        final double detBxyAxy = B.x * A.y - A.x * B.y;
        final double detCxyBxy = C.x * B.y - B.x * C.y;
        final double detAxyCxy = A.x * C.y - C.x * A.y;

        final double iQ = 1d / (detBxyAxy + detCxyBxy + detAxyCxy);

        /* T-vector, aka. X vector */
        final double Tx = iQ * (F.x * (B.y - A.y) + D.x * (C.y - B.y) + E.x * (A.y - C.y));
        final double Ty = iQ * (F.y * (B.y - A.y) + D.y * (C.y - B.y) + E.y * (A.y - C.y));

        /* U-vector, aka. Y vector */
        final double Ux = iQ * (F.x * (A.x - B.x) + D.x * (B.x - C.x) + E.x * (C.x - A.x));
        final double Uy = iQ * (F.y * (A.x - B.x) + D.y * (B.x - C.x) + E.y * (C.x - A.x));

        /* V-vector, aka. Z vector (or translation vector) */
        final double Vx = iQ * (F.x * detBxyAxy + D.x * detCxyBxy + E.x * detAxyCxy);
        final double Vy = iQ * (F.y * detBxyAxy + D.y * detCxyBxy + E.y * detAxyCxy);

        return dest.set (
            Tx, Ty,
            Ux, Uy,
            Vx, Vy
        );
    }
}
