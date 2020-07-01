package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.util.Numbers;

import java.util.ArrayList;
import java.util.Collections;

public class GraphSpacers {

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
            int stylehint = GraphSpacer.StandardStyleHints.MINOR;
            double minpos = minstart + minpt * minstep;
            double majpos = majstart + majpt * majstep;

            if (Numbers.areEqual (minpos, majpos)) {
                stylehint = GraphSpacer.StandardStyleHints.MAJOR;
                ++majpt;
            }

            if (Numbers.areEqual (minpos, 0d))
                stylehint = GraphSpacer.StandardStyleHints.ORIGIN;

            indices.add (new GraphSpacer.Point (minpos, stylehint));
        }
        return indices;
    };
}
