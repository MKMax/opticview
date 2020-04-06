package io.github.mkmax.util;

import io.github.mkmax.util.math.Ints;

import java.text.DecimalFormat;

public class ScientificDecimalFormat extends DecimalFormat {

    private static String generatePattern (int significant) {
        final StringBuilder pattern = new StringBuilder("0");
        if (significant > 0) /* if significant == 0, we should avoid the decimal */
            pattern.append ('.');
        while (--significant >= 0)
            pattern.append ('#');
        return pattern.append ("E0").toString ();
    }

    private static final int MAX_SIGNIFICANT_DIGITS = 12;
    private static final int MIN_SIGNIFICANT_DIGITS = 0;

    private int significantDigits = 3;

    public ScientificDecimalFormat (int pSignificantDigits) {
        setSignificantDigits (pSignificantDigits);
    }

    public int getSignificantDigits () {
        return significantDigits;
    }

    public void setSignificantDigits (int nSignificantDigits) {
        significantDigits = Ints.clamp (nSignificantDigits, MIN_SIGNIFICANT_DIGITS, MAX_SIGNIFICANT_DIGITS);
        super.applyPattern (generatePattern (significantDigits));
    }

    @Override
    public void applyPattern (String nPattern) {
        /* disable the function to the outside world */
    }
}
