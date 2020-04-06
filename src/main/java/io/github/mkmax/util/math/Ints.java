package io.github.mkmax.util.math;

public class Ints {

    public static int clamp (int v, int l, int r) {
        if (l > r)
            return clamp (v, r, l);
        return Math.max (Math.min (v, r), l);
    }

}
