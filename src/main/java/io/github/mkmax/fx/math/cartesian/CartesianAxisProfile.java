package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.math.DoubleRange;

import java.util.Iterator;

public interface CartesianAxisProfile {

    void setComputeMajorAxisPoints (boolean enable);

    boolean wouldComputeMajorAxisPoints ();

    void setComputeMinorAxisPoints (boolean enable);

    boolean wouldComputeMinorAxisPoints ();

    Iterator<CartesianAxisPoint> computeMajorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange);

    Iterator<CartesianAxisPoint> computeMinorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange);

}
