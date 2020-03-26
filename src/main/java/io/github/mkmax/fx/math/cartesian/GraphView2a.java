package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.fx.util.ResizableCanvas;
import io.github.mkmax.util.math.Quad2dv;

import org.joml.Matrix3x2d;

import java.util.ArrayDeque;
import java.util.Deque;

public class GraphView2a extends ResizableCanvas {

    private AxisMarker xAxisMarker = new DecimalAxisMarker ();
    private AxisMarker yAxisMarker = new DecimalAxisMarker ();

    /*
     *
     */
    private final Quad2dv    commit    = new Quad2dv ();
    private final Quad2dv    provision = new Quad2dv ();
    private final Quad2dv    viewport  = new Quad2dv ();
    private final Matrix3x2d quadmap   = new Matrix3x2d ();

    public GraphView2a () {

    }

    public void render () {
        /* do any preparations here before rendering such as adjusting viewport */
        viewport.ortho (0d, getWidth (), getHeight (), 0d);

        /**/
    }
}
