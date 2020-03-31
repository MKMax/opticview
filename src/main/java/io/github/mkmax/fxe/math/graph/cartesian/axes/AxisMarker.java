package io.github.mkmax.fxe.math.graph.cartesian.axes;

public interface AxisMarker {

    Iterable<AxisMark> getMarks (
        double          intervalBegin,
        double          intervalEnd,
        AxisMark.Degree degree);

}
