package io.github.mkmax.opticview.util;

import java.math.BigDecimal;

public class BigMath {

    public static <T extends Number> BigDecimal toBigDecimal (T num, BigDecimal defaultOnNull) {
        if (num instanceof BigDecimal)
            return (BigDecimal) num;
        return num == null ? defaultOnNull : new BigDecimal (num.toString ());
    }

}
