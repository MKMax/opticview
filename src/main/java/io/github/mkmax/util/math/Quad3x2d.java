package io.github.mkmax.util.math;

import org.joml.Matrix3x2d;
import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Vector2d;

/* A quad using 3 vectors of 2 components each (3x2) */
public class Quad3x2d {

    public static Quad3x2d newOrtho (
        double left,
        double right,
        double bottom,
        double top)
    {
        return new Quad3x2d ().ortho (left, right, bottom, top);
    }

    private final Vector2d qa = new Vector2d (); // lower-left
    private final Vector2d qb = new Vector2d (); // lower-right
    private final Vector2d qc = new Vector2d (); // upper-right

    public Quad3x2d (
        Vector2dc pQa,
        Vector2dc pQb,
        Vector2dc pQc)
    {
        parallelogram (pQa, pQb, pQc);
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

    public Quad3x2d () {
        normal ();
    }

    public Quad3x2d normal () {
        qa.set (-1d, -1d);
        qb.set ( 1d, -1d);
        qc.set ( 1d,  1d);
        return this;
    }

    public Quad3x2d rectangle (
        double px, double py,
        double qx, double qy)
    {
        qa.set (px, py);
        qb.set (qx, py);
        qc.set (qx, qy);
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
        qa.set (left, bottom);
        qb.set (right, bottom);
        qc.set (right, top);
        return this;
    }

    public Quad3x2d parallelogram (
        double ax, double ay,
        double bx, double by,
        double cx, double cy)
    {
        qa.set (ax, ay);
        qb.set (bx, by);
        qc.set (cx, cy);
        return this;
    }

    public Quad3x2d parallelogram (
        Vector2dc pQa,
        Vector2dc pQb,
        Vector2dc pQc)
    {
        qa.set (pQa);
        qb.set (pQb);
        qc.set (pQc);
        return this;
    }

    public double getLeft () {
        return Math.min (Math.min (qa.x, qb.x), qc.x);
    }

    public double getRight () {
        return Math.max (Math.max (qa.x, qb.x), qc.x);
    }

    public double getBottom () {
        return Math.min (Math.min (qa.y, qb.y), qc.y);
    }

    public double getTop () {
        return Math.max (Math.max (qa.y, qb.y), qc.y);
    }

    public Quad3x2d transform (Matrix3x2dc mat) {
        return transform (mat, this);
    }

    public Quad3x2d transform (Matrix3x2dc mat, Quad3x2d dest) {
        mat.transformPosition (qa, dest.qa);
        mat.transformPosition (qb, dest.qb);
        mat.transformPosition (qc, dest.qc);
        return dest;
    }

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
        final Vector2d A = qa;
        final Vector2d B = qb;
        final Vector2d C = qc;

        final Vector2d D = to.qa;
        final Vector2d E = to.qb;
        final Vector2d F = to.qc;

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
