package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.IteratorUtils;
import io.github.mkmax.util.math.FloatingPoint;
import io.github.mkmax.util.math.DoubleRange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

public class StandardCartesianAxisProfileTest {

    static final Comparator<CartesianAxisPoint> WINDOW_SPACE_ASCENDING =
        (a, b) -> (int) Math.floor (a.windowSpace - b.windowSpace);

    @Test
    void testMajorAxisGenerationExpectedCase_0 () {
        final double MIN_MFPU = 128;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* the order of the points is not specified; for the sake of the test, they
         * must be ordered in window space ascending order.
         */
        points.sort (WINDOW_SPACE_ASCENDING);

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

    @Test
    void testMajorAxisGenerationExpectedCase_3 () {
        final double MIN_MFPU = 128;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);

        final DoubleRange window = new DoubleRange (+1, +3);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* the order of the points is not specified; for the sake of the test, they
         * must be ordered in window space ascending order.
         */
        points.sort (WINDOW_SPACE_ASCENDING);

        /* the following assertions are verified by hand computation */
        assertEquals (9, points.size ());

        assertTrue (FloatingPoint.leanEq (1.2d, points.get (0).windowSpace));
        assertTrue (FloatingPoint.leanEq (1.4d, points.get (1).windowSpace));
        assertTrue (FloatingPoint.leanEq (1.6d, points.get (2).windowSpace));
        assertTrue (FloatingPoint.leanEq (1.8d, points.get (3).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.0d, points.get (4).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.2d, points.get (5).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.4d, points.get (6).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.6d, points.get (7).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.8d, points.get (8).windowSpace));

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
    void testMajorAxisGenerationExpectedCase_4 () {
        final double MIN_MFPU = 128;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);

        final DoubleRange window = new DoubleRange (+3, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* the order of the points is not specified; for the sake of the test, they
         * must be ordered in window space ascending order.
         */
        points.sort (WINDOW_SPACE_ASCENDING);

        /* the following assertions are verified by hand computation */
        assertEquals (9, points.size ());

        assertTrue (FloatingPoint.leanEq (1.2d, points.get (0).windowSpace));
        assertTrue (FloatingPoint.leanEq (1.4d, points.get (1).windowSpace));
        assertTrue (FloatingPoint.leanEq (1.6d, points.get (2).windowSpace));
        assertTrue (FloatingPoint.leanEq (1.8d, points.get (3).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.0d, points.get (4).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.2d, points.get (5).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.4d, points.get (6).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.6d, points.get (7).windowSpace));
        assertTrue (FloatingPoint.leanEq (2.8d, points.get (8).windowSpace));

        assertTrue (FloatingPoint.leanEq (1152d, points.get (0).viewportSpace));
        assertTrue (FloatingPoint.leanEq (1024d, points.get (1).viewportSpace));
        assertTrue (FloatingPoint.leanEq (896d, points.get (2).viewportSpace));
        assertTrue (FloatingPoint.leanEq (768d, points.get (3).viewportSpace));
        assertTrue (FloatingPoint.leanEq (640d, points.get (4).viewportSpace));
        assertTrue (FloatingPoint.leanEq (512d, points.get (5).viewportSpace));
        assertTrue (FloatingPoint.leanEq (384d, points.get (6).viewportSpace));
        assertTrue (FloatingPoint.leanEq (256d, points.get (7).viewportSpace));
        assertTrue (FloatingPoint.leanEq (128d, points.get (8).viewportSpace));
    }

    @Test
    void testMajorAxisGenerationExpectedCase_5 () {
        final double MIN_MFPU = 200; /* expecting halves now, not fifths */

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* the order of the points is not specified; for the sake of the test, they
         * must be ordered in window space ascending order.
         */
        points.sort (WINDOW_SPACE_ASCENDING);

        /* the following assertions are verified by hand computation */
        assertEquals (3, points.size ());

        assertTrue (FloatingPoint.leanEq (-0.5d, points.get (0).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.0d, points.get (1).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.5d, points.get (2).windowSpace));

        assertTrue (FloatingPoint.leanEq (320d, points.get (0).viewportSpace));
        assertTrue (FloatingPoint.leanEq (640d, points.get (1).viewportSpace));
        assertTrue (FloatingPoint.leanEq (960d, points.get (2).viewportSpace));
    }

    @Test
    void testMajorAxisGenerationExpectedCase_6 () {
        final double MIN_MFPU = 1000; /* expecting wholes, not halves or fifths */

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMajorPoints (window, viewport));

        /* the order of the points is not specified; for the sake of the test, they
         * must be ordered in window space ascending order.
         */
        points.sort (WINDOW_SPACE_ASCENDING);

        /* the following assertions are verified by hand computation */
        assertEquals (1, points.size ());
        assertTrue (FloatingPoint.leanEq (0.0d, points.get (0).windowSpace));
        assertTrue (FloatingPoint.leanEq (640d, points.get (0).viewportSpace));
    }

    @Test
    void testMinorAxisGenerationExpectedCase_0 () {
        final double MIN_MFPU = 128;

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMinorPoints (window, viewport));

        /* the order of the points is not specified; for the sake of the test, they
         * must be ordered in window space ascending order.
         */
        points.sort (WINDOW_SPACE_ASCENDING);

        /* the following assertions are verified by hand computation */
        assertEquals (30, points.size ());

        assertTrue (FloatingPoint.leanEq (-0.95d, points.get (0).windowSpace));
        assertTrue (FloatingPoint.leanEq (-0.90d, points.get (1).windowSpace));
        assertTrue (FloatingPoint.leanEq (-0.85d, points.get (2).windowSpace));
        /* ... */
        assertTrue (FloatingPoint.leanEq ( 0.85d, points.get (27).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.90d, points.get (28).windowSpace));
        assertTrue (FloatingPoint.leanEq ( 0.95d, points.get (29).windowSpace));

        assertTrue (FloatingPoint.leanEq (32d, points.get (0).viewportSpace));
        assertTrue (FloatingPoint.leanEq (64d, points.get (1).viewportSpace));
        assertTrue (FloatingPoint.leanEq (96d, points.get (2).viewportSpace));
        /* ... */
        assertTrue (FloatingPoint.leanEq (1184d, points.get (27).viewportSpace));
        assertTrue (FloatingPoint.leanEq (1216d, points.get (28).viewportSpace));
        assertTrue (FloatingPoint.leanEq (1248d, points.get (29).viewportSpace));
    }

    @Test
    void testMinorAxisGenerationExpectedCase_1 () {
        final double MIN_MFPU = 200; /* expecting fifths of halves now */

        CartesianAxisProfile profile = new StandardCartesianAxisProfile (MIN_MFPU);

        final DoubleRange window = new DoubleRange (-1, +1);
        final DoubleRange viewport = new DoubleRange (0, 1280);

        final List<CartesianAxisPoint> points =
            IteratorUtils.newArrayList (profile.computeMinorPoints (window, viewport));

        /* the order of the points is not specified; for the sake of the test, they
         * must be ordered in window space ascending order.
         */
        points.sort (WINDOW_SPACE_ASCENDING);

        /* the following assertions are verified by hand computation */
        assertEquals (16, points.size ());

        assertTrue (FloatingPoint.leanEq (-0.9d, points.get (0).windowSpace));
        assertTrue (FloatingPoint.leanEq (-0.8d, points.get (1).windowSpace));
        assertTrue (FloatingPoint.leanEq (-0.7d, points.get (2).windowSpace));
        /* ... */
        assertTrue (FloatingPoint.leanEq (0.7d, points.get (13).windowSpace));
        assertTrue (FloatingPoint.leanEq (0.8d, points.get (14).windowSpace));
        assertTrue (FloatingPoint.leanEq (0.9d, points.get (15).windowSpace));

        assertTrue (FloatingPoint.leanEq (64d, points.get (0).viewportSpace));
        assertTrue (FloatingPoint.leanEq (128d, points.get (1).viewportSpace));
        assertTrue (FloatingPoint.leanEq (192d, points.get (2).viewportSpace));
        /* ... */
        assertTrue (FloatingPoint.leanEq (1088d, points.get (13).viewportSpace));
        assertTrue (FloatingPoint.leanEq (1152d, points.get (14).viewportSpace));
        assertTrue (FloatingPoint.leanEq (1216d, points.get (15).viewportSpace));
    }
}