package io.github.mkmax.opticview.util;

import java.math.BigDecimal;

public class BigMath {

    /* @TODO(max): extend BigDecimal to provide support for NaN and Infinity (they are needed) */
    public static <T extends Number> BigDecimal toBigDecimal (
        T          num,
        BigDecimal defaultOnNull,
        BigDecimal defaultOnNaN,
        BigDecimal defaultOnInf)
    {
        if (num instanceof BigDecimal)
            return (BigDecimal) num;
        if (num instanceof Double) {
            final double dNum = (double) num;
            if (Double.isNaN (dNum))
                return defaultOnNaN;
            if (Double.isInfinite (dNum))
                return defaultOnInf;
        }
        if (num instanceof Float) {
            final float fNum = (float) num;
            if (Float.isNaN (fNum))
                return defaultOnNaN;
            if (Float.isInfinite (fNum))
                return defaultOnInf;
        }
        return num == null ? defaultOnNull : new BigDecimal (num.toString ());
    }

}
