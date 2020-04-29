package io.github.mathfx.cartesian;

import io.github.mkmax.util.math.Float64;

import javafx.beans.NamedArg;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.shape.Rectangle;

public class HorizontalGuide extends AbstractGuide {

    private final Rectangle guide = new Rectangle ();
    private final Label     label = new Label ();

    public HorizontalGuide () {
        layoutGroup.addAll (
            guide.heightProperty (),
            label.widthProperty (),
            label.heightProperty ()
        );
        setMaxHeight (0d);
        guide.setTranslateZ (0d);
        label.setTranslateZ (1d);
        getChildren ().addAll (guide, label);
        guideRestyle (); /* initialize the style for the guide */
    }

    public HorizontalGuide (
        @NamedArg ("ortho")    double ortho,
        @NamedArg ("parallel") double parallel)
    {
        this ();
        setOrtho (ortho);
        setParallel (parallel);
    }

    public HorizontalGuide (
        @NamedArg ("text") String text,
        @NamedArg ("ortho") double ortho,
        @NamedArg ("parallel") double parallel)
    {
        this (ortho, parallel);
        setText (text);
    }

    @Override
    protected void guideRelayout () {
        setWidth (getParentWidth ());
        setHeight (0d);

        setLayoutX (0d);
        setLayoutY (getOrtho ());

        guide.setWidth (getWidth ());
        guide.setLayoutX (0d);
        guide.setLayoutY (-getSpan ().doubleValue () * 0.5d);

        final LabelJustify justify = getLabelJustify ();

        double labelX = getParallel () + justify.X_OFFSET_MULTIPLIER * label.getWidth ();
        double labelY = justify.Y_OFFSET_MULTIPLIER * label.getHeight ();

        labelX = Float64.clamp (labelX, 0d, getWidth () - label.getWidth ());

        label.setLayoutX (labelX);
        label.setLayoutY (labelY);
    }

    @Override
    protected void guideRestyle () {
        if (!isVisible ())
            return;

        /* ensure that we do not trigger a re-layout event if the span doesn't change */
        if (!Float64.strictEq (guide.getHeight (), getSpan ().doubleValue ()))
            guide.setHeight (getSpan ().doubleValue ());
        guide.setFill   (getBackgroundColor ());

        label.setFont       (getFont ());
        label.setText       (getText ());
        label.setTextFill   (getForegroundColor ());
        label.setPadding    (getLabelPadding ());
        label.setBackground (new Background (new BackgroundFill (getBackgroundColor (), null, null)));
    }
}
