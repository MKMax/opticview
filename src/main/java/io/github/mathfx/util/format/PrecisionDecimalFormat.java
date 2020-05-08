package io.github.mathfx.util.format;

import java.text.DecimalFormat;

public class PrecisionDecimalFormat extends DecimalFormat {

    private static final int MIN_PRECISION = 0;
    private static final int DEF_PRECISION = 3;
    private static final int MAX_PRECISION = 16;

    static int clamp (int precision) {
        return Math.max (Math.min (precision, MAX_PRECISION), MIN_PRECISION);
    }

    static String createPattern (int precision) {
        precision = clamp (precision);
        StringBuilder pattern = new StringBuilder("0");
        if (precision > 0) pattern.append ('.');
        for (int i = 0; i < precision; ++i)
            pattern.append ('0');
        return pattern.toString ();
    }

    private int precision;

    public PrecisionDecimalFormat () {
        setPrecision (DEF_PRECISION);
    }

    public PrecisionDecimalFormat (int pPrecision) {
        setPrecision (pPrecision);
    }

    public int getPrecision () {
        return precision;
    }

    public void setPrecision (int nPrecision) {
        precision = clamp (nPrecision);
        super.applyPattern (createPattern (precision));
    }

    @Override
    public void applyPattern (String nPattern) {
        throw new UnsupportedOperationException ("cannot modify PrecisionDecimalFormat pattern");
    }

    @Override
    public void applyLocalizedPattern (String nPattern) {
        throw new UnsupportedOperationException ("cannot modify PrecisionDecimalFormat pattern");
    }
}
