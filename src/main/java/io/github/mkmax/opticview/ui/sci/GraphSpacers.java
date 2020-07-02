package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.util.Numbers;

import java.util.ArrayList;
import java.util.Collections;

public class GraphSpacers {

    private static final int
        STANDARD_ORIGIN_CLASS = 0,
        STANDARD_MAJOR_CLASS  = 1,
        STANDARD_MINOR_CLASS  = 2;

    /* +----------------+ */
    /* | DECIMAL SPACER | */
    /* +----------------+ */
    public static final GraphSpacer DECIMAL = (start, end, codomainspan, gap) -> {
        final double min = Math.min (start, end);
        final double max = Math.max (start, end);

        final double ratio = codomainspan / (end - start);
        final double unit  = gap / Math.abs (ratio);
        final double log10 = Math.ceil (Math.log10 (unit));
        final double pow10 = Math.pow (10, log10);
        final double norm  = unit / pow10;

        double majstep, minstep;
        if (norm <= 0.2d) {
            majstep = 0.2d * pow10;
            minstep = 0.25d * majstep;
        }
        else if (norm <= 0.5d) {
            majstep = 0.5d * pow10;
            minstep = 0.2d * majstep;
        }
        else {
            majstep = 1.0d * pow10;
            minstep = 0.2d * majstep;
        }

        double majstart, minstart;
        if (min < 0) {
            majstart = min - (min % majstep);
            minstart = min - (min % minstep);
        }
        else {
            majstart = min + (majstep - min % majstep);
            minstart = min + (minstep - min % minstep);
        }

        if (Numbers.areEqual (min, majstart))
            majstart += majstep;
        if (Numbers.areEqual (min, minstart))
            minstart += minstep;
        if (minstart >= max)
            return Collections.emptyList ();

        final int total = (int) Math.floor ((max - minstart) / minstep) + 1;
        final var indices = new ArrayList<GraphSpacer.Point> (total);
        int majpt = 0;
        for (int minpt = 0; minpt < total; ++minpt) {
            int styleclass = STANDARD_MINOR_CLASS;
            double minpos = minstart + minpt * minstep;
            double majpos = majstart + majpt * majstep;

            if (Numbers.areEqual (minpos, majpos)) {
                styleclass = STANDARD_MAJOR_CLASS;
                ++majpt;
            }

            if (Numbers.areEqual (minpos, 0d))
                styleclass = STANDARD_ORIGIN_CLASS;

            indices.add (new GraphSpacer.Point (minpos, styleclass));
        }
        return indices;
    };
}
