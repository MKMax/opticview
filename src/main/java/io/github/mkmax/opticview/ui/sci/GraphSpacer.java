package io.github.mkmax.opticview.ui.sci;

import java.util.List;

public interface GraphSpacer {

    final class StandardStyleHints {
        public static final int ORIGIN = 0x0;
        public static final int MAJOR  = 0x1;
        public static final int MINOR  = 0x2;
    }

    final class Point {
        /* indicates the calculated position of this point on some arbitrary real number axis */
        public final double position;
        /* hints at the style that this point should be visually displayed with (if such feature exists) */
        public final int stylehint;

        public Point (double pposition, int pstylehint) {
            position = pposition;
            stylehint = pstylehint;
        }
    }

    List<Point> computePoints (double start, double end, double ratio, double gap);
}
