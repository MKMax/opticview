package io.github.mkmax.util.math.geo;

import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Vector2d;

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
}
