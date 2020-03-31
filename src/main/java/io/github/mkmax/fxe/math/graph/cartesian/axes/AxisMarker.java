package io.github.mkmax.fxe.math.graph.cartesian.axes;

public interface AxisMarker {

    Iterable<AxisMark> getMajorMarks (
        double min,
        double max,
        double afpu);

    Iterable<AxisMark> getMinorMarks (
        double min,
        double max,
        double afpu);

}
