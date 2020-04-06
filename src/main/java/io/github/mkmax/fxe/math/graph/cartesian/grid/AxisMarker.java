package io.github.mkmax.fxe.math.graph.cartesian.grid;

import java.util.List;
import java.util.Objects;

public interface AxisMarker {

    enum Degree {
        MAJOR,
        MINOR
    }

    class Mark {
        public final Degree deg;
        public final double pos;

        protected Mark (
            Degree pDeg,
            double pPos)
        {
            deg = Objects.requireNonNull (pDeg);
            pos = pPos;
        }
    }

    List<Mark> getMarks (
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd);

}
