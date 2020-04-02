package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.fxe.ResizableCanvas;
import io.github.mkmax.fxe.math.graph.cartesian.axes.AxisMarkerd;
import io.github.mkmax.fxe.math.graph.cartesian.axes.DecimalAxisMarkerd;
import io.github.mkmax.util.math.geo.Quad2dInterpolator;
import io.github.mkmax.util.math.geo.Affine2Quad2dInterpolator;
import io.github.mkmax.util.math.geo.Quad2dc;
import io.github.mkmax.util.math.geo.Quad2x2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.joml.Vector2d;

public class GraphView2d extends ResizableCanvas {

    private final GraphicsContext gc = getGraphicsContext2D ();

    /* Axis parameters */
    private AxisMarkerd xAxisMarker = new DecimalAxisMarkerd ();
    private AxisMarkerd yAxisMarker = new DecimalAxisMarkerd ();

    /* "Camera" parameters */
    private Quad2x2d           window     = new Quad2x2d ();
    private Quad2x2d           viewport   = new Quad2x2d ();
    private Quad2dInterpolator projection = new Affine2Quad2dInterpolator (window, viewport);
    private Quad2dInterpolator inverse    = new Affine2Quad2dInterpolator (viewport, window);

    public GraphView2d () {
        /* Everything initialized already */
    }

    /* +--------+ */
    /* | PUBLIC | */
    /* +--------+ */

    public AxisMarkerd getXAxisMarker () {
        return xAxisMarker;
    }

    public AxisMarkerd getYAxisMarker () {
        return yAxisMarker;
    }

    public Quad2dc getWindow () {
        return window;
    }

    public Quad2dc getViewport () {
        return viewport;
    }

    public void setWindow (
        double sx,
        double sy,
        double ex,
        double ey)
    {
        window.setBottomLeft (sx, sy);
        window.setTopRight (ex, ey);
        updateInterpolators ();
    }

    public void setViewport (
        double sx,
        double sy,
        double ex,
        double ey)
    {
        viewport.setBottomLeft (sx, sy);
        viewport.setTopRight (ex, ey);
        updateInterpolators ();
    }

    public void render () {
        render_axes ();
        render_functions ();
    }

    /* +---------+ */
    /* | PRIVATE | */
    /* +---------+ */

    private void updateInterpolators () {
        projection.set (window, viewport);
        inverse.set (viewport, window);
    }

    private void render_axes () {
        Vector2d gs = new Vector2d ();
        Vector2d ge = new Vector2d ();
        render_xAxis (gs, ge);
        render_yAxis (gs, ge);
    }

    private void render_xAxis (
        Vector2d gs,
        Vector2d ge)
    {
        final double bottom = window.getBottom ();
        final double top    = window.getTop ();
        xAxisMarker
            .computeMajorMarks (window.getLeft (), window.getRight ())
            .forEach (mark -> {

            });
    }

    private void render_yAxis () {

    }

    private void render_functions () {

    }
}
