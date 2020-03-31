package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.fxe.ResizableCanvas;
import io.github.mkmax.fxe.math.graph.cartesian.axes.AxisMarker;
import io.github.mkmax.fxe.math.graph.cartesian.axes.DecimalAxisMarker;
import io.github.mkmax.fxe.math.graph.cartesian.camera.Camera2d;
import io.github.mkmax.util.math.geo.Quad2d;
import io.github.mkmax.util.math.geo.Quad4x2d;

public class GraphView2d extends ResizableCanvas {

    private AxisMarker xAxisMarker = new DecimalAxisMarker ();
    private AxisMarker yAxisMarker = new DecimalAxisMarker ();

    public GraphView2d () {

    }

    public void render () {

    }
}
