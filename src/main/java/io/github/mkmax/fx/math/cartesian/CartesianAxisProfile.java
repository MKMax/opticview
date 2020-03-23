package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.math.DoubleRange;

import java.util.Iterator;

public interface CartesianAxisProfile {

    void setComputeMajorAxisPoints (boolean enable);

    boolean shouldComputeMajorAxisPoints ();

    void setComputeMinorAxisPoints (boolean enable);

    boolean shouldComputeMinorAxisPoints ();

    void setLabelsEnabled (boolean enable);

    boolean areLabelsEnabled ();

    void setLabelDecimalPrecision (int dp);

    int getLabelDecimalPrecision ();

    void setScientificNotationLowerBound (int base10);

    int getScientificNotationLowerBound ();

    void setScientificNotationUpperBound (int base10);

    int getScientificNotationUpperBound ();

    Iterable<CartesianAxisPoint> computeMajorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange);

    Iterable<CartesianAxisPoint> computeMinorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange);

}
