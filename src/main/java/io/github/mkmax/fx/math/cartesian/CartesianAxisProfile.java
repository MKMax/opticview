package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.math.DoubleRange;

import java.util.Iterator;

public interface CartesianAxisProfile {

    void setMinimumFragmentsPerUnit (double nMfpu);

    double getMinimumFragmentsPerUnit ();

    void setComputeMajorAxisPoints (boolean enable);

    boolean wouldComputeMajorAxisPoints ();

    void setComputeMinorAxisPoints (boolean enable);

    boolean wouldComputeMinorAxisPoints ();

    Iterable<CartesianAxisPoint> computeMajorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange);

    Iterable<CartesianAxisPoint> computeMinorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange);

}
