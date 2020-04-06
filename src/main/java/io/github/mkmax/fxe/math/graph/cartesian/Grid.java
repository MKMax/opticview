package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.PrecisionDecimalFormat;
import io.github.mkmax.util.ScientificDecimalFormat;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Grid extends Pane implements Disposable {

    public enum LabelJustify {
        LEFT,
        RIGHT,
        BOTTOM,
        TOP,
        CENTER
    }

    /* +---------------------+ */
    /* | CONSTRUCTORS + DATA | */
    /* +---------------------+ */

    private final GraphView view;
    private final ChangeListener<? super Number> ON_WIDTH_CHANGED  = this::onContainerWidthChanged;
    private final ChangeListener<? super Number> ON_HEIGHT_CHANGED = this::onContainerHeightChanged;

    private final List<HorizontalGuide> hGuides = new ArrayList<> ();
    private final List<VerticalGuide>   vGuides = new ArrayList<> ();

    private final DoubleProperty               majorGuideWidth      = new SimpleDoubleProperty (2.5d);
    private final SimpleObjectProperty<Paint>  majorGuideFill       = new SimpleObjectProperty<> (Color.GRAY);
    private final DoubleProperty               minorGuideWidth      = new SimpleDoubleProperty (1.75d);
    private final ObjectProperty<Paint>        minorGuideFill       = new SimpleObjectProperty<> (Color.LIGHTGRAY);
    private final ObjectProperty<Paint>        majorLabelForeground = new SimpleObjectProperty<> (Color.WHITE);
    private final ObjectProperty<Paint>        majorLabelBackground = new SimpleObjectProperty<> (Color.GRAY);
    private final ObjectProperty<Paint>        minorLabelForeground = new SimpleObjectProperty<> (Color.BLACK);
    private final ObjectProperty<Paint>        minorLabelBackground = new SimpleObjectProperty<> (Color.LIGHTGRAY);
    private final ObjectProperty<LabelJustify> verLabelJustify      = new SimpleObjectProperty<> (LabelJustify.CENTER);
    private final ObjectProperty<LabelJustify> horLabelJustify      = new SimpleObjectProperty<> (LabelJustify.CENTER);
    private final IntegerProperty              significantDigits    = new SimpleIntegerProperty (3);
    private final IntegerProperty              decimalPlaces        = new SimpleIntegerProperty (3);
    private final DoubleProperty               sciNotationUpper     = new SimpleDoubleProperty (1e3d);
    private final DoubleProperty               sciNotationLower     = new SimpleDoubleProperty (1e-3d);

    private final ScientificDecimalFormat sdf = new ScientificDecimalFormat (significantDigits.get ());
    private final PrecisionDecimalFormat pdf = new PrecisionDecimalFormat (decimalPlaces.get ());

    Grid (GraphView pView) {
        view = Objects.requireNonNull (pView);

        setWidth (view.getWidth ());
        setHeight (view.getHeight ());

        view.widthProperty () .addListener (ON_WIDTH_CHANGED);
        view.heightProperty ().addListener (ON_HEIGHT_CHANGED);
    }

    /* +-----------------+ */
    /* | GUIDE UTILITIES | */
    /* +-----------------+ */

    String formatGuideValue (double value) {
        if (sciNotationLower.get () <= value && value <= sciNotationUpper.get ())
            return pdf.format (value);
        return sdf.format (value);
    }

    Paint getGuideFill (AbstractGuide.Degree deg) {
        switch (deg) {
        case MAJOR:
            return getMajorGuideFill ();
        case MINOR:
            return getMinorGuideFill ();
        }
        return null;
    }

    double getGuideWidth (AbstractGuide.Degree deg) {
        switch (deg) {
        case MAJOR:
            return getMajorGuideWidth ();
        case MINOR:
            return getMinorGuideWidth ();
        }
        return 0d;
    }

    Paint getLabelForeground (AbstractGuide.Degree deg) {
        switch (deg) {
        case MAJOR:
            return getMajorLabelForeground ();
        case MINOR:
            return getMinorLabelForeground ();
        }
        return null;
    }

    Background getLabelBackground (AbstractGuide.Degree deg) {
        switch (deg) {
        case MAJOR:
            return new Background (new BackgroundFill (getMajorLabelBackground (), null, null));
        case MINOR:
            return new Background (new BackgroundFill (getMinorLabelBackground (), null, null));
        }
        return null;
    }

    /* +------------+ */
    /* | PROPERTIES | */
    /* +------------+ */

    /* MAJOR GUIDE WIDTH */

    public double getMajorGuideWidth () {
        return majorGuideWidth.get ();
    }

    public void setMajorGuideWidth (double majorGuideWidth) {
        this.majorGuideWidth.set (majorGuideWidth);
    }

    public DoubleProperty majorGuideWidthProperty () {
        return majorGuideWidth;
    }

    /* MAJOR GUIDE FILL */

    public Paint getMajorGuideFill () {
        return majorGuideFill.get ();
    }

    public void setMajorGuideFill (Paint majorGuideFill) {
        this.majorGuideFill.set (majorGuideFill);
    }

    public SimpleObjectProperty<Paint> majorGuideFillProperty () {
        return majorGuideFill;
    }

    /* MINOR GUIDE WIDTH */

    public double getMinorGuideWidth () {
        return minorGuideWidth.get ();
    }

    public DoubleProperty minorGuideWidthProperty () {
        return minorGuideWidth;
    }

    public void setMinorGuideWidth (double minorGuideWidth) {
        this.minorGuideWidth.set (minorGuideWidth);
    }

    /* MINOR GUIDE FILL */

    public Paint getMinorGuideFill () {
        return minorGuideFill.get ();
    }

    public void setMinorGuideFill (Paint minorGuideFill) {
        this.minorGuideFill.set (minorGuideFill);
    }

    public ObjectProperty<Paint> minorGuideFillProperty () {
        return minorGuideFill;
    }

    /* MAJOR LABEL FOREGROUND */

    public Paint getMajorLabelForeground () {
        return majorLabelForeground.get ();
    }

    public void setMajorLabelForeground (Paint majorLabelForeground) {
        this.majorLabelForeground.set (majorLabelForeground);
    }

    public ObjectProperty<Paint> majorLabelForegroundProperty () {
        return majorLabelForeground;
    }

    /* MAJOR LABEL BACKGROUND */

    public Paint getMajorLabelBackground () {
        return majorLabelBackground.get ();
    }

    public void setMajorLabelBackground (Paint majorLabelBackground) {
        this.majorLabelBackground.set (majorLabelBackground);
    }

    public ObjectProperty<Paint> majorLabelBackgroundProperty () {
        return majorLabelBackground;
    }

    /* MINOR LABEL FOREGROUND */

    public Paint getMinorLabelForeground () {
        return minorLabelForeground.get ();
    }

    public void setMinorLabelForeground (Paint minorLabelForeground) {
        this.minorLabelForeground.set (minorLabelForeground);
    }

    public ObjectProperty<Paint> minorLabelForegroundProperty () {
        return minorLabelForeground;
    }

    /* MINOR LABEL BACKGROUND */

    public Paint getMinorLabelBackground () {
        return minorLabelBackground.get ();
    }

    public void setMinorLabelBackground (Paint minorLabelBackground) {
        this.minorLabelBackground.set (minorLabelBackground);
    }

    public ObjectProperty<Paint> minorLabelBackgroundProperty () {
        return minorLabelBackground;
    }

    /* VERTICAL LABEL JUSTIFY */

    public LabelJustify getVerLabelJustify () {
        return verLabelJustify.get ();
    }

    public void setVerLabelJustify (LabelJustify verLabelJustify) {
        this.verLabelJustify.set (verLabelJustify);
    }

    public ObjectProperty<LabelJustify> verLabelJustifyProperty () {
        return verLabelJustify;
    }

    /* HORIZONTAL LABEL JUSTIFY */

    public LabelJustify getHorLabelJustify () {
        return horLabelJustify.get ();
    }

    public void setHorLabelJustify (LabelJustify horLabelJustify) {
        this.horLabelJustify.set (horLabelJustify);
    }

    public ObjectProperty<LabelJustify> horLabelJustifyProperty () {
        return horLabelJustify;
    }

    /* SIGNIFICANT DIGITS */

    public int getSignificantDigits () {
        return significantDigits.get ();
    }

    public void setSignificantDigits (int significantDigits) {
        this.significantDigits.set (significantDigits);
    }

    public IntegerProperty significantDigitsProperty () {
        return significantDigits;
    }

    /* DECIMAL PLACES */

    public int getDecimalPlaces () {
        return decimalPlaces.get ();
    }

    public void setDecimalPlaces (int decimalPlaces) {
        this.decimalPlaces.set (decimalPlaces);
    }

    public IntegerProperty decimalPlacesProperty () {
        return decimalPlaces;
    }

    /* UPPER LIMIT FOR SCIENTIFIC NOTATION */

    public double getSciNotationUpper () {
        return sciNotationUpper.get ();
    }

    public void setSciNotationUpper (double sciNotationUpper) {
        this.sciNotationUpper.set (sciNotationUpper);
    }

    public DoubleProperty sciNotationUpperProperty () {
        return sciNotationUpper;
    }

    /* LOWER LIMIT FOR SCIENTIFIC NOTATION */

    public double getSciNotationLower () {
        return sciNotationLower.get ();
    }

    public void setSciNotationLower (double sciNotationLower) {
        this.sciNotationLower.set (sciNotationLower);
    }

    public DoubleProperty sciNotationLowerProperty () {
        return sciNotationLower;
    }

    /* +-------------------------------------+ */
    /* | LISTENERS + DISPOSE IMPLEMENTATIONS | */
    /* +-------------------------------------+ */

    @Override
    public void dispose () {
        view.widthProperty () .removeListener (ON_WIDTH_CHANGED);
        view.heightProperty ().removeListener (ON_HEIGHT_CHANGED);
        hGuides.forEach (HorizontalGuide::dispose);
        vGuides.forEach (VerticalGuide::dispose);
    }

    private void onContainerWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {

    }

    private void onContainerHeightChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {

    }
}
