package io.github.mkmax.opticview.ui.sci;

import java.util.List;

public interface GraphSpacer {

    final class Point {
        /* indicates the calculated position of this point on some arbitrary real number axis */
        public final double position;
        /* hints at the appearance that this point should be visually displayed with (if such feature exists) */
        public final int appearanceclass;

        public Point (double pposition, int pappearanceclass) {
            position = pposition;
            appearanceclass = pappearanceclass;
        }
    }

    List<Point> computePoints (double start, double end, double codomainspan, double gap);
}
