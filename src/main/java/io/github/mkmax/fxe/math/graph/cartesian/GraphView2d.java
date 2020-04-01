package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.fxe.ResizableCanvas;
import io.github.mkmax.fxe.math.graph.cartesian.axes.AxisMarker;
import io.github.mkmax.fxe.math.graph.cartesian.axes.DecimalAxisMarker;
import io.github.mkmax.util.math.geo.Quad2d;
import io.github.mkmax.util.math.geo.Quad4x2d;

public class GraphView2d extends ResizableCanvas {

    private AxisMarker xAxisMarker = new DecimalAxisMarker ();
    private AxisMarker yAxisMarker = new DecimalAxisMarker ();

    private Quad2d camera = new Quad4x2d ();
    private Quad2d normal = Quad4x2d.NORMAL;

    public GraphView2d () {

    }

    public void render () {

    }
}
