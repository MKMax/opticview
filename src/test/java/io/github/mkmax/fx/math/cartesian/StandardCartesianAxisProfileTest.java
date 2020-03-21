package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.IteratorUtils;
import org.junit.jupiter.api.Test;

import io.github.mkmax.util.math.DoubleRange;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandardCartesianAxisProfileTest {

    @Test
    void testAxisGeneratingExpectedCase_0 () {
        final double MIN_MFPU = 128;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);
        profile.setComputeMajorAxisPoints (true);
        profile.setComputeMinorAxisPoints (true);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        for (CartesianAxisPoint cap : points) {
            System.out.println (cap.windowSpace);
        }

        /* the following assertions are verified by hand computation */
        // assertEquals (9, points.size ());
    }

}
