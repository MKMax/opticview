package io.github.mkmax.exfx.math.graph.cart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/* the de facto standard implementation of a Spacer */
public class DecimalSpacer implements Spacer {

    public static final class Holder {
        private static final DecimalSpacer INSTANCE = new DecimalSpacer ();
    }

    public static DecimalSpacer getInstance () {
        return Holder.INSTANCE;
    }




    @Override
    public Collection<NumericIndex> compute (
        double begin,
        double end,
        double ratio,
        double mingap)
    {
        final double min = Math.min (begin, end);
        final double max = Math.max (begin, end);

        final double unit  = mingap / Math.abs (ratio);
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



        if (f64eq (min, majstart))
            majstart += majstep;
        if (f64eq (min, minstart))
            minstart += minstep;
        if (minstart >= max)
            return Collections.emptyList ();



        /* TODO: refactor later for clarity and readability */
        final int total = (int) Math.floor ((max - minstart) / minstep) + 1;
        final var indices = new ArrayList<NumericIndex> (total);
        int majpoint = 0;
        for (int minpoint = 0; minpoint < total; ++minpoint) {
            String style = CoreMarkStyles.MINOR;
            double minpos = minstart + minpoint * minstep;
            double majpos = majstart + majpoint * majstep;

            if (f64eq (minpos, majpos)) {
                style = CoreMarkStyles.MAJOR;
                ++majpoint;
            }

            if (f64eq (minpos, 0d))
                style = CoreMarkStyles.ORIGIN;

            indices.add (new NumericIndex (style, minpos));
        }
        return indices;
    }

    private static boolean f64eq (double a, double b) {
        return Math.abs (a - b) <= 1e-15d;
    }
}
