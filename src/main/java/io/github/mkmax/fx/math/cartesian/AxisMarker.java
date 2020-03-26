package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.math.Segment1dv;

public interface AxisMarker {

    Iterable<AxisMark> getMajorMarks (
        Segment1dv unitRange,
        Segment1dv fragmentRange);

    Iterable<AxisMark> getMinorMarks (
        Segment1dv unitRange,
        Segment1dv fragmentRange);

}
