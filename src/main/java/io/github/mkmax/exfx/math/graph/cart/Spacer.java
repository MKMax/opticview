package io.github.mkmax.exfx.math.graph.cart;

import java.util.Collection;

/* computes tick marks on a cartesian graph */
public interface Spacer {

    Collection<NumericIndex> compute (
        double begin,   /* the function space starting value */
        double end,     /* the function space ending value */
        double ratio,   /* pixels per function space interval unit, i.e. (pixels / (end - begin)) */
        double mingap   /* minimum number of pixels required between tick marks */
    );

}
