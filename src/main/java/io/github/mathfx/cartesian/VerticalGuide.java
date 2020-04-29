package io.github.mathfx.cartesian;

import io.github.mkmax.util.math.Float64;

import javafx.beans.NamedArg;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.shape.Rectangle;

public class VerticalGuide extends AbstractGuide {

    private final Rectangle guide = new Rectangle ();
    private final Label label = new Label ();

    public VerticalGuide () {
        layoutGroup.addAll (
            guide.widthProperty (),
            label.widthProperty (),
            label.heightProperty ()
        );
        setMaxWidth (0d);
        guide.setTranslateZ (0d);
        label.setTranslateZ (1d);
        getChildren ().addAll (guide, label);
        guideRestyle (); /* initialize the style for the guide */
    }

    public VerticalGuide (
        @NamedArg ("ortho")    double ortho,
        @NamedArg ("parallel") double parallel)
    {
        this ();
        setOrtho (ortho);
        setParallel (parallel);
    }

    public VerticalGuide (
        @NamedArg ("text") String text,
        @NamedArg ("ortho") double ortho,
        @NamedArg ("parallel") double parallel)
    {
        this (ortho, parallel);
        setText (text);
    }

    @Override
    protected void guideRelayout () {
        setWidth (0d);
        setHeight (getParentHeight ());

        setLayoutX (getOrtho ());
        setLayoutY (0d);

        guide.setHeight (getHeight ());
        guide.setLayoutX (-getSpan ().doubleValue () * 0.5d);
        guide.setLayoutY (0d);

        final LabelJustify justify = getLabelJustify ();

        double labelX = justify.X_OFFSET_MULTIPLIER * label.getWidth ();
        double labelY = getParallel () + justify.Y_OFFSET_MULTIPLIER * label.getHeight ();

        labelY = Float64.clamp (labelY, 0d, getHeight () - label.getHeight ());

        label.setLayoutX (labelX);
        label.setLayoutY (labelY);
    }

    @Override
    protected void guideRestyle () {
        if (!isVisible ())
            return;

        /* ensure that we do not trigger a re-layout event if the span doesn't change */
        if (!Float64.strictEq (guide.getHeight (), getSpan ().doubleValue ()))
            guide.setWidth (getSpan ().doubleValue ());
        guide.setFill   (getBackgroundColor ());

        label.setFont       (getFont ());
        label.setText       (getText ());
        label.setTextFill   (getForegroundColor ());
        label.setPadding    (getLabelPadding ());
        label.setBackground (new Background (new BackgroundFill (getBackgroundColor (), null, null)));
    }

}
