package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.fxe.ResizableCanvas;
import io.github.mkmax.util.math.geo.Quad3x2d;

import org.joml.Matrix3x2d;

public class GraphView2a extends ResizableCanvas {

    private AxisMarker xAxisMarker = new DecimalAxisMarker ();
    private AxisMarker yAxisMarker = new DecimalAxisMarker ();

    /*
     *
     */
    private final Quad3x2d commit    = new Quad3x2d ();
    private final Quad3x2d provision = new Quad3x2d ();
    private final Quad3x2d viewport  = new Quad3x2d ();
    private final Matrix3x2d quadmap   = new Matrix3x2d ();

    public GraphView2a () {

    }

    public void render () {
        /* do any preparations here before rendering such as adjusting viewport */
        viewport.ortho (0d, getWidth (), getHeight (), 0d);

        /**/
    }
}
