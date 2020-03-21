package io.github.mkmax.fx.math.cartesian;

import org.junit.jupiter.api.Test;

import io.github.mkmax.util.math.DoubleRange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StandardCartesianAxisProfileTest {

    @Test
    void testAxisGeneratingExpectedCase () {
        CartesianAxisProfile profile = new StandardCartesianAxisProfile (128);
        profile.setComputeMajorAxisPoints (true);
        profile.setComputeMinorAxisPoints (true);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> enumeratedPoints = new ArrayList<> ();
        for (CartesianAxisPoint point : profile.computeMajorPoints (window, viewport))


    }

}
