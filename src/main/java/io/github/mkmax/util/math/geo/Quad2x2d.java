package io.github.mkmax.util.math.geo;

import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Vector2d;

public class Quad2x2d implements Quad2d {

    public static final Quad2x2d NORMAL = new Quad2x2d (
        -1d, -1d,
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

    public Quad2x2d () {
        a.set (-1d, -1d);
        b.set ( 1d,  1d);
    }

    public Quad2x2d (Quad2d src) {
        src.setBottomLeft (a);
        src.setTopRight (b);
    }

    public Quad2x2d (
        Vector2dc pA,
        Vector2dc pB)
    {
        a.set (pA);
        b.set (pB);
    }

    public Quad2x2d (
        double ax, double ay,
        double bx, double by)
    {
        a.set (ax, ay);
        b.set (bx, by);
    }

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    @Override
    public double getTopLeftX () {
        return a.x;
    }

    @Override
    public double getTopLeftY () {
        return b.y;
    }

    @Override
    public void setTopLeftX (double nx) {
        a.x = nx;
    }

    @Override
    public void setTopLeftY (double ny) {
        b.y = ny;
    }

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    @Override
    public double getTopRightX () {
        return b.x;
    }

    @Override
    public double getTopRightY () {
        return b.y;
    }

    @Override
    public void setTopRightX (double nx) {
        b.x = nx;
    }

    @Override
    public void setTopRightY (double ny) {
        b.y = ny;
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
        return a.y;
    }

    @Override
    public void setBottomRightX (double nx) {
        b.x = nx;
    }

    @Override
    public void setBottomRightY (double ny) {
        a.y = ny;
    }


    /* +--------------------+ */
    /* | LIGHT COMPUTATIONS | */
    /* +--------------------+ */

    @Override
    public double getLeft () {
        return Math.min (a.x, b.x);
    }

    @Override
    public double getRight () {
        return Math.max (a.x, b.x);
    }

    @Override
    public double getBottom () {
        return Math.min (a.y, b.y);
    }

    @Override
    public double getTop () {
        return Math.max (a.y, b.y);
    }

    /* +---------------------------------+ */
    /* | TRANFORMATION AND INTERPOLATION | */
    /* +---------------------------------+ */

    public Quad2x2d transform (Matrix3x2dc mat) {
        mat.transformPosition (a);
        mat.transformPosition (b);
        return this;
    }

    public Quad2x2d transform (Matrix3x2dc mat, Quad2x2d dest) {
        mat.transformPosition (a, dest.a);
        mat.transformPosition (b, dest.b);
        return dest;
    }
}
