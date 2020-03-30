package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.Intervald;

public interface AxisMarker {

    Iterable<AxisMark> getMajorMarks (
        Intervald unitInterval,
        Intervald fragmentInterval);

    Iterable<AxisMark> getMinorMarks (
        Intervald unitInterval,
        Intervald fragmentInterval);

}
