package io.github.mkmax.util.math.geo;

import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Vector2d;

/**
 * The most flexible of all <code>Quad</code> figures by enabling
 * full control of all four vertices of the quad by sacrificing
 * some performance when performing interpolation.
 *
 * @author Maxim Kasyanenko
 */
public class Quad4x2d implements Quad2d {

    public static final Quad4x2d NORMAL = new Quad4x2d (
        -1d, -1d,
         1d, -1d,
        -1d,  1d,
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

    /* +--------------------+ */
    /* | CONSTRUCTOR + DATA | */
    /* +--------------------+ */

    private final Vector2d a = new Vector2d ();
    private final Vector2d b = new Vector2d ();
    private final Vector2d c = new Vector2d ();
    private final Vector2d d = new Vector2d ();

    public Quad4x2d () {
        a.set (-1d, -1d);
        b.set ( 1d, -1d);
        c.set ( 1d,  1d);
        d.set (-1d,  1d);
    }

    public Quad4x2d (Quad2d src) {
        src.getBottomLeft (a);
        src.getBottomRight (b);
        src.setTopLeft (c);
        src.setTopRight (d);
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

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    @Override
    public double getTopLeftX () {
        return c.x;
    }

    @Override
    public double getTopLeftY () {
        return c.y;
    }

    @Override
    public void setTopLeftX (double nx) {
        c.x = nx;
    }

    @Override
    public void setTopLeftY (double ny) {
        c.y = ny;
    }

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    @Override
    public double getTopRightX () {
        return d.x;
    }

    @Override
    public double getTopRightY () {
        return d.y;
    }

    @Override
    public void setTopRightX (double nx) {
        d.x = nx;
    }

    @Override
    public void setTopRightY (double ny) {
        d.y = ny;
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

    public Quad4x2d transform (Matrix3x2dc mat) {
        mat.transformPosition (a);
        mat.transformPosition (b);
        mat.transformPosition (c);
        mat.transformPosition (d);
        return this;
    }

    public Quad4x2d transform (Matrix3x2dc mat, Quad4x2d dest) {
        mat.transformPosition (a, dest.a);
        mat.transformPosition (b, dest.b);
        mat.transformPosition (c, dest.c);
        mat.transformPosition (d, dest.d);
        return dest;
    }
}
