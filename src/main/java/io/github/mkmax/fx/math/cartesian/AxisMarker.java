package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.math.DoubleRange;

public interface AxisMarker {

    Iterable<AxisMark> getMajorMarks (
        DoubleRange unitRange,
        DoubleRange fragmentRange);

    Iterable<AxisMark> getMinorMarks (
        DoubleRange unitRange,
        DoubleRange fragmentRange);

}
