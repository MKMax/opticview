package io.github.mkmax.fx.math.cartesian.c2d;

import org.joml.*;

import java.util.List;
import java.util.ArrayList;

public class CartesianTransform2D {

    public interface RecomputedListener {
        void onRecomputed ();
    }

    private final List<RecomputedListener> recomputedListeners = new ArrayList<> ();

    private final Rectangled window   = new Rectangled ();
    private final Rectangled viewport = new Rectangled ();

    private final Matrix3x2d projection = new Matrix3x2d ();
    private final Matrix3x2d inverse    = new Matrix3x2d ();

    public CartesianTransform2D () {
        /* initial state has already been reached */
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public Matrix3x2dc getProjection () {
        return projection;
    }

    public Matrix3x2dc getInverse () {
        return inverse;
    }

    /* +----------------------------------------+ */
    /* | Event registration + handling (if any) | */
    /* +----------------------------------------+ */

    public void register (RecomputedListener rl) {
        if (rl != null)
            recomputedListeners.add (rl);
    }

    public void unregister (RecomputedListener rl) {
        if (rl != null)
            recomputedListeners.remove (rl);
    }

    /* +--------------------------------+ */
    /* | Application of transformations | */
    /* +--------------------------------+ */

    public Vector2d project (Vector2d loc) {
        return projection.transformPosition (loc);
    }

    public Vector2d project (double x, double y) {
        return projection.transformPosition (x, y, new Vector2d ());
    }

    public double projectX (double x) {
        return projection.m00 * x + projection.m20;
    }

    public double projectY (double y) {
        return projection.m11 * y + projection.m21;
    }

    public Vector2d invert (Vector2d loc) {
        return inverse.transformPosition (loc);
    }

    public Vector2d invert (double x, double y) {
        return inverse.transformPosition (x, y, new Vector2d ());
    }

    public double invertX (double x) {
        return inverse.m00 * x + inverse.m20;
    }

    public double invertY (double y) {
        return inverse.m11 * y + inverse.m21;
    }

    /* +---------------------------------------+ */
    /* | Modification of the current transform | */
    /* +---------------------------------------+ */

    private static void map (
        Rectangled from,
        Rectangled to,
        Matrix3x2d out)
    {
        /* Compute the X-axis linear mapping */
        double dx = (to.maxX - to.minX) / (from.maxX - from.minX);
        double x0 = to.minX - dx * from.minX;

        /* Compute the Y-axis linear mapping */
        double dy = (to.maxY - to.minY) / (from.maxY - from.minY);
        double y0 = to.minY - dy * from.minY;

        out.set (
            dx, 0d,
            0d, dy,
            x0, y0
        );
    }

    public void setWindow (
        double left,
        double right,
        double bottom,
        double top)
    {
        window.minX = left;
        window.maxX = right;
        window.minY = bottom;
        window.maxY = top;
        recompute ();
    }

    public void setWindow (Rectangled nWindow) {
        setWindow (
            nWindow.minX,
            nWindow.maxX,
            nWindow.minY,
            nWindow.maxY
        );
    }

    public void setViewport (
        double left,
        double right,
        double bottom,
        double top)
    {
        viewport.minX = left;
        viewport.maxX = right;
        viewport.minY = bottom;
        viewport.maxY = top;
        recompute ();
    }

    public void setViewport (Rectangled nViewport) {
        setViewport (
            nViewport.minX,
            nViewport.maxX,
            nViewport.minY,
            nViewport.maxY
        );
    }

    private void recompute () {
        map (window, viewport, projection);
        map (viewport, window, inverse);

        recomputedListeners.forEach (RecomputedListener::onRecomputed);
    }
}
