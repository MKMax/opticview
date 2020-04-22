package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.Float64;

public class HorizontalGuide extends LabeledRectangleGuide {

    public HorizontalGuide () {
        setMaxHeight (0d);
        guide.setHeight (getAppearance ().getSize ());
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */

    @Override
    protected void onSizeChanged (double old, double now) {
        guide.setHeight (now);
        repositionGuide ();
    }

    @Override
    protected void repositionSelf () {
        setLayoutX (0d);
        setLayoutY (getOrtho ());
        setWidth (getParentWidth ());
        setHeight (0d);

        repositionGuide ();
        repositionLabel ();
    }

    @Override
    protected void repositionGuide () {
        guide.setLayoutX (0d);
        guide.setLayoutY (-guide.getHeight () / 2);
        guide.setWidth (getWidth ());
    }

    @Override
    protected void repositionLabel () {
        final double labelWidth  = label.getLayoutBounds ().getWidth ();
        final double labelHeight = label.getLayoutBounds ().getHeight ();

        final Justify justify = getJustify () == null ? Justify.CENTER : getJustify ();

        double labelX = justify.X_OFFSET_MULTIPLIER * labelWidth + getParallel ();
        labelX = Float64.clamp (labelX, 0d, getWidth () - labelWidth);
        double labelY = justify.Y_OFFSET_MULTIPLIER * labelHeight;

        label.setLayoutX (labelX);
        label.setLayoutY (labelY);
    }
}
