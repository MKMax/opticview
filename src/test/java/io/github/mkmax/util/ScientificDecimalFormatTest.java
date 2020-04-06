package io.github.mkmax.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScientificDecimalFormatTest {

    @Test
    void testFormat () {
        ScientificDecimalFormat sdf = new ScientificDecimalFormat (6);
        String result = sdf.format (0.1234567d);

        assertEquals ("1.234567E-1", result);
    }

    @Test
    void testApplyOverridden () {
        ScientificDecimalFormat sdf = new ScientificDecimalFormat (6);
        sdf.applyPattern ("000");
        String result = sdf.format (0.1234567d);

        assertEquals ("1.234567E-1", result);
    }
}
