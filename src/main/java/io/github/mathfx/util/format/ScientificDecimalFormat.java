package io.github.mathfx.util.format;

import java.text.DecimalFormat;

public class ScientificDecimalFormat extends DecimalFormat {

    private static final int MIN_PRECISION = 0;
    private static final int DEF_PRECISION = 3;
    private static final int MAX_PRECISION = 16;

    static int clamp (int precision) {
        return Math.max (Math.min (precision, MAX_PRECISION), MIN_PRECISION);
    }

    static String createPattern (int precision) {
        precision = clamp (precision);
        StringBuilder pattern = new StringBuilder ("0");
        if (precision > 0) pattern.append ('.');
        for (int i = 0; i < precision; ++i)
            pattern.append ('0');
        return pattern.append ("E0").toString ();
    }

    private int precision = DEF_PRECISION;

    public ScientificDecimalFormat () {
        setPrecision (precision);
    }

    public ScientificDecimalFormat (int pPrecision) {
        setPrecision (pPrecision);
    }

    public int getPrecision () {
        return precision;
    }

    public void setPrecision (int nPrecision) {
        if (nPrecision == precision)
            return;
        precision = clamp (nPrecision);
        super.applyPattern (createPattern (precision));
    }

    @Override
    public void applyPattern (String nPattern) {
        throw new UnsupportedOperationException ("cannot modify ScientificDecimalFormat pattern");
    }

    @Override
    public void applyLocalizedPattern (String nPattern) {
        throw new UnsupportedOperationException ("cannot modify ScientificDecimalFormat pattern");
    }
}
