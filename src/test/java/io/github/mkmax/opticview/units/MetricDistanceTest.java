package io.github.mkmax.opticview.units;

import org.junit.jupiter.api.Test;

import static io.github.mkmax.opticview.units.MetricDistance.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MetricDistanceTest {
    @Test
    public void testMetricConversionTo () {
        assertEquals (1d, Math.abs (METERS.convertTo (KILOMETERS, 1000d)));
    }

    @Test
    public void testMetricConversionFrom () {
        assertEquals (1000d, METERS.convertFrom (KILOMETERS, 1d));
    }
}
