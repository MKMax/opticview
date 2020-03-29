package io.github.mkmax.util.math.geo;

import org.joml.Matrix3x2dc;
import org.joml.Vector2dc;
import org.joml.Vector2d;

public interface Quad2d {

    interface Interpolator2d {

        <T extends Quad2d> T getSource ();

        <T extends Quad2d> T getDestination ();

        Vector2d interpolate (Vector2d src);

        Vector2d interpolate (Vector2d src, Vector2d dest);

    }

    /* +---------+ */
    /* | GENERAL | */
    /* +---------+ */

    void set (Quad2d q);

    /* Returns the number of vertices used to represent the
     * quad implementation. Any value that is not 2, 3, or 4
     * is deemed undefined.
     */
    int getOrder ();

    /* +-----------------+ */
    /* | TOP LEFT VERTEX | */
    /* +-----------------+ */

    double getTopLeftX ();

    double getTopLeftY ();

    Vector2d getTopLeft ();

    Vector2d getTopLeft (Vector2d dest);

    void setTopLeftX (double nx);

    void setTopLeftY (double ny);

    void setTopLeft (double nx, double ny);

    void setTopLeft (Vector2dc nPos);

    /* +------------------+ */
    /* | TOP RIGHT VERTEX | */
    /* +------------------+ */

    double getTopRightX ();

    double getTopRightY ();

    Vector2d getTopRight ();

    Vector2d getTopRight (Vector2d dest);

    void setTopRightX (double nx);

    void setTopRightY (double ny);

    void setTopRight (double nx, double ny);

    void setTopRight (Vector2dc nPos);

    /* +--------------------+ */
    /* | BOTTOM LEFT VERTEX | */
    /* +--------------------+ */

    double getBottomLeftX ();

    double getBottomLeftY ();

    Vector2d getBottomLeft ();

    Vector2d getBottomLeft (Vector2d dest);

    void setBottomLeftX (double nx);

    void setBottomLeftY (double ny);

    void setBottomLeft (double nx, double ny);

    void setBottomLeft (Vector2dc nPos);

    /* +---------------------+ */
    /* | BOTTOM RIGHT VERTEX | */
    /* +---------------------+ */

    double getBottomRightX ();

    double getBottomRightY ();

    Vector2d getBottomRight ();

    Vector2d getBottomRight (Vector2d dest);

    void setBottomRightX (double nx);

    void setBottomRightY (double ny);

    void setBottomRight (double nx, double ny);

    void setBottomRight (Vector2dc nPos);

    /* +--------------------+ */
    /* | LIGHT COMPUTATIONS | */
    /* +--------------------+ */

    double getLeft ();

    double getRight ();

    double getBottom ();

    double getTop ();

    /* +---------------------------------+ */
    /* | TRANFORMATION AND INTERPOLATION | */
    /* +---------------------------------+ */

    <T extends Quad2d> T transform (Matrix3x2dc mat);

    <T extends Quad2d> T transform (Matrix3x2dc mat, Quad2d dest);

    <T extends Interpolator2d> T interpolate (Quad2d to);
}
