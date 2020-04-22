package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.Float64;

public class VerticalGuide extends LabeledRectangleGuide {

    public VerticalGuide () {
        setMaxWidth (0d);
        guide.setWidth (getAppearance ().getSize ());
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */

    @Override
    protected void onSizeChanged (double old, double now) {
        guide.setWidth (now);
        repositionGuide ();
    }

    @Override
    protected void repositionSelf () {
        setLayoutX (getOrtho ());
        setLayoutY (0d);
        setWidth (0d);
        setHeight (getParentHeight ());

        repositionGuide ();
        repositionLabel ();
    }

    @Override
    protected void repositionGuide () {
        guide.setLayoutX (-guide.getWidth () / 2);
        guide.setLayoutY (0d);
        guide.setHeight (getHeight ());
    }

    @Override
    protected void repositionLabel () {
        final double labelWidth  = label.getLayoutBounds ().getWidth ();
        final double labelHeight = label.getLayoutBounds ().getHeight ();

        final Justify justify = getJustify () == null ? Justify.CENTER : getJustify ();

        double labelX = justify.X_OFFSET_MULTIPLIER * labelWidth;
        double labelY = justify.Y_OFFSET_MULTIPLIER * labelHeight + getParallel ();
        labelY = Float64.clamp (labelY, 0d, getHeight () - labelHeight);

        label.setLayoutX (labelX);
        label.setLayoutY (labelY);
    }
}
