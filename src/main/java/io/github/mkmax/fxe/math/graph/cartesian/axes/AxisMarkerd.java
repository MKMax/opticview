package io.github.mkmax.fxe.math.graph.cartesian.axes;

public interface AxisMarkerd {

    class Markd {
        protected double  position;
        protected boolean merged;
        protected boolean origin;

        protected Markd () { }

        public double getPosition () {
            return position;
        }

        public boolean isMerged () {
            return merged;
        }

        public boolean isOrigin () {
            return origin;
        }
    }

    Iterable<Markd> computeMajorMarks (
        double intervalBegin,
        double intervalEnd
    );

    Iterable<Markd> computeMinorMarks (
        double intervalBegin,
        double intervalEnd
    );
}
