package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.geo.Segment1dv;

public interface AxisMarker {

    Iterable<AxisMark> getMajorMarks (
        Segment1dv unitRange,
        Segment1dv fragmentRange);

    Iterable<AxisMark> getMinorMarks (
        Segment1dv unitRange,
        Segment1dv fragmentRange);

}
