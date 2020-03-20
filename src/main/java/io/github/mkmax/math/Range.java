package io.github.mkmax.math;

import java.util.Objects;

public class Range<T extends Number> {

    public final T min;
    public final T max;

    public Range (T pMin, T pMax) {
        if (pMin.doubleValue () > pMax.doubleValue ())
            throw new IllegalArgumentException ();
        min = Objects.requireNonNull (pMin);
        max = Objects.requireNonNull (pMax);
    }

}
