package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.math.Range2d;

public interface AxisMarker {

    Iterable<AxisMark> getMajorMarks (
        Range2d unitRange,
        Range2d fragmentRange);

    Iterable<AxisMark> getMinorMarks (
        Range2d unitRange,
        Range2d fragmentRange);

}
