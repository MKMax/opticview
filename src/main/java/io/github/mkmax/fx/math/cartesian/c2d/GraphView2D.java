package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.AxisMarker;
import io.github.mkmax.fx.math.cartesian.DecimalAxisMarker;
import io.github.mkmax.fx.util.ResizableCanvas;

import org.joml.Matrix3d;

import java.util.ArrayDeque;
import java.util.Deque;

public class GraphView2D extends ResizableCanvas {

    private AxisMarker xAxisMarker = new DecimalAxisMarker ();
    private AxisMarker yAxisMarker = new DecimalAxisMarker ();

    private final Deque<Matrix3d> committed = new ArrayDeque<> ();
    private final Deque<Matrix3d> staged    = new ArrayDeque<> ();

    public GraphView2D () {

    }

}
