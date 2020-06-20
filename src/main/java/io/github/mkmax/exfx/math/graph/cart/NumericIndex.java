package io.github.mkmax.exfx.math.graph.cart;

import java.util.Objects;

/* a short-lived container for the necessary data to put
 * a styled numeric mark onto a cartesian graph axis produced
 * exclusively by a Spacer instance.
 */
public class NumericIndex {

    /* [nonull] describes the requested style for the mark */
    public final String style;

    /* indicates the position on the axis to position the mark on */
    public final double point;

    public NumericIndex (String pstyle, double ppoint) {
        style = Objects.requireNonNull (pstyle);
        point = ppoint;
    }
}
