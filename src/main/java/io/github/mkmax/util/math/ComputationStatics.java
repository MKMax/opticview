package io.github.mkmax.util.math;

public class ComputationStatics {

    /* +--------------------+ */
    /* | MIN/MAX EXTENSIONS | */
    /* +--------------------+ */

    public static float min (float... fs) {
        if (fs == null || fs.length == 0)
            return Float.NaN;
        float cMin = fs[0];
        for (int i = 1; i < fs.length; ++i)
            if (fs[i] < cMin)
                cMin = fs[i];
        return cMin;
    }

    public static double min (double... ds) {
        if (ds == null || ds.length == 0)
            return Double.NaN;
        double cMin = ds[0];
        for (int i = 1; i < ds.length; ++i)
            if (ds[i] < cMin)
                cMin = ds[i];
        return cMin;
    }

    public static float max (float... fs) {
        if (fs == null || fs.length == 0)
            return Float.NaN;
        float cMax = fs[0];
        for (int i = 1; i < fs.length; ++i)
            if (fs[i] > cMax)
                cMax = fs[i];
        return cMax;
    }

    public static double max (double... ds) {
        if (ds == null || ds.length == 0)
            return Double.NaN;
        double cMax = ds[0];
        for (int i = 1; i < ds.length; ++i)
            if (ds[i] > cMax)
                cMax = ds[i];
        return cMax;
    }

}
