package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.IteratorUtils;
import io.github.mkmax.util.math.FloatingPoint;
import io.github.mkmax.util.math.DoubleRange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StandardCartesianAxisProfileTest {

    @Test
    void testMajorAxisGenerationExpectedCase_0 () {
        final double MIN_MFPU = 128;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);
        profile.setComputeMajorAxisPoints (true);
        profile.setComputeMinorAxisPoints (true);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* the following assertions are verified by hand computation */
        assertEquals (9, points.size ());

        assertTrue (FloatingPoint.leanEq (-0.8d, points.get (0).windowSpace));
        assertTrue (FloatingPoint.leanEq (-0.6d, points.get (1).windowSpace));
        assertTrue (FloatingPoint.leanEq (-0.4d, points.get (2).windowSpace));
        assertTrue (FloatingPoint.leanEq (-0.2d, points.get (3).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.0d, points.get (4).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.2d, points.get (5).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.4d, points.get (6).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.6d, points.get (7).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.8d, points.get (8).windowSpace));

        assertTrue (FloatingPoint.leanEq (128d, points.get (0).viewportSpace));
        assertTrue (FloatingPoint.leanEq (256d, points.get (1).viewportSpace));
        assertTrue (FloatingPoint.leanEq (384d, points.get (2).viewportSpace));
        assertTrue (FloatingPoint.leanEq (512d, points.get (3).viewportSpace));
        assertTrue (FloatingPoint.leanEq (640d, points.get (4).viewportSpace));
        assertTrue (FloatingPoint.leanEq (768d, points.get (5).viewportSpace));
        assertTrue (FloatingPoint.leanEq (896d, points.get (6).viewportSpace));
        assertTrue (FloatingPoint.leanEq (1024d, points.get (7).viewportSpace));
        assertTrue (FloatingPoint.leanEq (1152d, points.get (8).viewportSpace));
    }

    @Test
    void testMajorAxisGenerationExpectedCase_1 () {
        final double MIN_MFPU = 512d;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);
        profile.setComputeMajorAxisPoints (true);
        profile.setComputeMinorAxisPoints (true);

        final DoubleRange window = new DoubleRange (0.25d, 0.75d);
        final DoubleRange viewport = new DoubleRange (0, 128d);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* We have chosen a MIN_MFPU purposely so that no axis points could be constructed. */
        assertEquals (0, points.size ());
    }

    @Test
    void testMajorAxisGenerationExpectedCase_2 () {
        final double MIN_MFPU = 128;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);
        profile.setComputeMajorAxisPoints (false);
        profile.setComputeMinorAxisPoints (false);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* Because we disabled major axis computation, we*/
        assertEquals (0, points.size ());
    }
}
