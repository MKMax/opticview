package io.github.mkmax.util.math;

public class Int32 {

    public static int clog (int v) {
        if (v == 0)
            return 0x80_00_00_00;
        int exp = 31;
        while (((1 << exp) & v) == 0)
            --exp;
        return exp + ((((1 << exp) - 1) & v) != 0 ? 1 : 0);
    }

    public static int flog (int v) {
        if (v == 0)
            return 0x80_00_00_00;
        int exp = 31;
        while (((1 << exp) & v) == 0)
            --exp;
        return exp;
    }

    public static int clamp (int v, int l, int r) {
        if (l > r)
            return clamp (v, r, l);
        return Math.max (Math.min (v, r), l);
    }

}
