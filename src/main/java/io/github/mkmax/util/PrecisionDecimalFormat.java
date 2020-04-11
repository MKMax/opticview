package io.github.mkmax.util;

import io.github.mkmax.util.math.Int32;

import java.text.DecimalFormat;

public class PrecisionDecimalFormat extends DecimalFormat {

    private static String generatePattern (int precision) {
        final StringBuilder pattern = new StringBuilder ("0");
        if (precision > 0)
            pattern.append ('.');
        while (--precision >= 0)
            pattern.append ('0');
        return pattern.toString ();
    }

    private static final int MAX_PRECISION = 16;
    private static final int MIN_PRECISION = 0;

    private int precision;

    public PrecisionDecimalFormat (int pPrecision) {
        setPrecision (pPrecision);
    }

    public int getPrecision () {
        return precision;
    }

    public void setPrecision (int nPrecision) {
        precision = Int32.clamp (nPrecision, MIN_PRECISION, MAX_PRECISION);
        super.applyPattern (generatePattern (precision));
    }

    @Override
    public void applyPattern (String pattern) {
        /* disable the function to the outside world */
    }
}
