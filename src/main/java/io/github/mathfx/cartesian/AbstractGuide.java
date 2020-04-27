package io.github.mathfx.cartesian;

import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.ObservableGroup;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Objects;

public abstract class AbstractGuide extends Pane implements Disposable {

    private static final StyleablePropertyFactory<AbstractGuide> FACTORY =
        new StyleablePropertyFactory<> (Pane.getClassCssMetaData ());

    /* +---------------------------------+ */
    /* | CSS STYLING OPTION DECLARATIONS | */
    /* +---------------------------------+ */

    public enum LabelJustify {
        TOP_LEFT     (-1.0d, -1.0d),
        TOP          (-0.5d, -1.0d),
        TOP_RIGHT    ( 0.0d, -1.0d),
        LEFT         (-1.0d, -0.5d),
        CENTER       (-0.5d, -0.5d),
        RIGHT        ( 0.0d, -0.5d),
        BOTTOM_LEFT  (-1.0d,  0.0d),
        BOTTOM       (-0.5d,  0.0d),
        BOTTOM_RIGHT ( 0.0d,  0.0d);

        public final double X_OFFSET_MULTIPLIER;
        public final double Y_OFFSET_MULTIPLIER;

        LabelJustify (
            final double xOffsetMultiplier,
            final double yOffsetMultiplier)
        {
            X_OFFSET_MULTIPLIER = xOffsetMultiplier;
            Y_OFFSET_MULTIPLIER = yOffsetMultiplier;
        }
    }

    /* +----------------------------+ */
    /* | CSS META DATA DECLARATIONS | */
    /* +----------------------------+ */

    private static final class Default {
        private static final double       SPAN          = 2d;
        private static final Font         FONT          = Font.getDefault ();
        private static final LabelJustify LABEL_JUSTIFY = LabelJustify.CENTER;
        private static final Insets       LABEL_PADDING = new Insets (2.5d);
        private static final Color        BGCOLOR       = Color.LIGHTGRAY;
        private static final Color        FGCOLOR       = Color.BLACK;
    }

    static {
        FACTORY.createSizeCssMetaData   (                    "-mfx-span",             ag -> ag.span,            Default.SPAN);
        FACTORY.createFontCssMetaData   (                    "-mfx-font",             ag -> ag.font,            Default.FONT);
        FACTORY.createEnumCssMetaData   (LabelJustify.class, "-mfx-label-justify",    ag -> ag.labelJustify,    Default.LABEL_JUSTIFY);
        FACTORY.createInsetsCssMetaData (                    "-mfx-label-padding",    ag -> ag.labelPadding,    Default.LABEL_PADDING);
        FACTORY.createColorCssMetaData  (                    "-mfx-background-color", ag -> ag.backgroundColor, Default.BGCOLOR);
        FACTORY.createColorCssMetaData  (                    "-mfx-foreground-color", ag -> ag.foregroundColor, Default.FGCOLOR);
    }

    /* +----------------------+ */
    /* | STYLEABLE PROPERTIES | */
    /* +----------------------+ */

    /* SPAN */
    private final StyleableObjectProperty<Number> span =
        (StyleableObjectProperty<Number>) FACTORY.createStyleableNumberProperty (this, "span", "-mfx-span", ag -> ag.span, Default.SPAN);

    public Number getSpan () {
        return span.get ();
    }

    public void setSpan (Number nSpan) {
        span.set (nSpan == null || nSpan.doubleValue () < 0.5d ? 0.5d :nSpan.doubleValue ());
    }

    public StyleableObjectProperty<Number> spanProperty () {
        return span;
    }

    /* FONT */
    private final StyleableObjectProperty<Font> font =
        (StyleableObjectProperty<Font>) FACTORY.createStyleableFontProperty (this, "font", "-mfx-font", ag -> ag.font, Default.FONT);

    public Font getFont () {
        return font.get ();
    }

    public void setFont (Font nFont) {
        font.set (Objects.requireNonNullElse (nFont, Default.FONT));
    }

    public StyleableObjectProperty<Font> fontProperty () {
        return font;
    }

    /* LABEL JUSTIFY */
    private final StyleableObjectProperty<LabelJustify> labelJustify =
        (StyleableObjectProperty<LabelJustify>) FACTORY.createStyleableEnumProperty (this, "labelJustify", "-mfx-label-justify", ag -> ag.labelJustify, LabelJustify.class, Default.LABEL_JUSTIFY);

    public LabelJustify getLabelJustify () {
        return labelJustify.get ();
    }

    public void setLabelJustify (LabelJustify nJustify) {
        labelJustify.set (Objects.requireNonNullElse (nJustify, Default.LABEL_JUSTIFY));
    }

    public StyleableObjectProperty<LabelJustify> labelJustifyProperty () {
        return labelJustify;
    }

    /* LABEL PADDING */
    private final StyleableObjectProperty<Insets> labelPadding =
        (StyleableObjectProperty<Insets>) FACTORY.createStyleableInsetsProperty (this, "labelPadding", "-mfx-label-padding", ag -> ag.labelPadding, Default.LABEL_PADDING);

    public Insets getLabelPadding () {
        return labelPadding.get ();
    }

    public void setLabelPadding (Insets nPadding) {
        labelPadding.set (Objects.requireNonNullElse (nPadding, Default.LABEL_PADDING));
    }

    public StyleableObjectProperty<Insets> labelPaddingProperty () {
        return labelPadding;
    }

    /* BACKGROUND COLOR */
    private final StyleableObjectProperty<Color> backgroundColor =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "backgroundColor", "-mfx-background-color", ag -> ag.backgroundColor, Default.BGCOLOR);

    public Color getBackgroundColor () {
        return backgroundColor.get ();
    }

    public void setBackgroundColor (Color nBg) {
        backgroundColor.set (Objects.requireNonNullElse (nBg, Default.BGCOLOR));
    }

    public StyleableObjectProperty<Color> backgroundColorProperty () {
        return backgroundColor;
    }

    /* FOREGROUND COLOR */
    private final StyleableObjectProperty<Color> foregroundColor =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "foregroundColor", "-mfx-foreground-color", ag -> ag.foregroundColor, Default.FGCOLOR);

    public Color getForegroundColor () {
        return foregroundColor.get ();
    }

    public void setForegroundColor (Color nFg) {
        foregroundColor.set (Objects.requireNonNullElse (nFg, Default.FGCOLOR));
    }

    public StyleableObjectProperty<Color> foregroundColorProperty () {
        return foregroundColor;
    }

    /* +------------------+ */
    /* | STANDARD MEMBERS | */
    /* +------------------+ */

    protected final StringProperty text = new SimpleStringProperty ();

    protected final DoubleProperty ortho    = new SimpleDoubleProperty ();
    protected final DoubleProperty parallel = new SimpleDoubleProperty ();

    protected final DoubleProperty width  = new SimpleDoubleProperty ();
    protected final DoubleProperty height = new SimpleDoubleProperty ();

    private final ChangeListener<Number> L_PWIDTH  = (__obs, __old, now) -> width.set (now.doubleValue ());
    private final ChangeListener<Number> L_PHEIGHT = (__obs, __old, now) -> height.set (now.doubleValue ());
    private final ChangeListener<Parent> L_PARENT  = (__obs, old, now) -> {
        if (old instanceof Region) {
            final Region oRegion = (Region) old;
            oRegion.widthProperty ().removeListener (L_PWIDTH);
            oRegion.heightProperty ().removeListener (L_PHEIGHT);
        }
        if (now instanceof Region) {
            final Region nRegion = (Region) now;
            nRegion.widthProperty ().addListener (L_PWIDTH);
            nRegion.heightProperty ().addListener (L_PHEIGHT);
            L_PWIDTH.changed (null, Double.NaN, nRegion.getWidth ());
            L_PHEIGHT.changed (null, Double.NaN, nRegion.getHeight ());
        }
    };

    {
        parentProperty ().addListener (L_PARENT);
    }

    protected final ObservableGroup<Number> positionGroup = new ObservableGroup<> (ortho, parallel);
    protected final ObservableGroup<Number> sizeGroup     = new ObservableGroup<> (width, height);
    protected final ObservableGroup<Number> layoutGroup   = new ObservableGroup<> (positionGroup, sizeGroup);
    protected final ObservableGroup<Object> styleGroup    = new ObservableGroup<> (
        text, span, font, labelJustify, labelPadding, backgroundColor, foregroundColor);

    public AbstractGuide () {
        layoutGroup.add (this::guideRelayout);
        styleGroup.add (this::guideRestyle);
    }

    /* +----------------+ */
    /* | GETTER/SETTERS | */
    /* +----------------+ */

    /* TEXT */

    public String getText () {
        return text.get ();
    }

    public void setText (String nText) {
        text.set (nText);
    }

    public StringProperty textProperty () {
        return text;
    }


    /* ORTHO */

    public double getOrtho () {
        return ortho.get ();
    }

    public void setOrtho (double nOrtho) {
        ortho.set (nOrtho);
    }

    public DoubleProperty orthoProperty () {
        return ortho;
    }

    /* PARALLEL */

    public double getParallel () {
        return parallel.get ();
    }

    public void setParallel (double nParallel) {
        parallel.set (nParallel);
    }

    public DoubleProperty parallelProperty () {
        return parallel;
    }

    /* +------+ */
    /* | REST | */
    /* +------+ */

    @Override
    public void dispose () {
        positionGroup.dispose ();
        sizeGroup.dispose ();
        layoutGroup.dispose ();
        styleGroup.dispose ();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
        return FACTORY.getCssMetaData ();
    }

    /* Layouts the guide based on the current dimensional and
     * positional parameters. */
    protected abstract void guideRelayout ();


    /* Should restyle the guide as necessary. If restyling
     * changes dimensions, an observable should capture it.
     * and call guideRelayout(). */
    protected abstract void guideRestyle ();

    /* +---------+ */
    /* | UTILITY | */
    /* +---------+ */

    protected double getParentWidth () {
        final Parent parent = getParent ();
        if (parent instanceof Region)
            return ((Region) parent).getWidth ();
        return 0d;
    }

    protected double getParentHeight () {
        final Parent parent = getParent ();
        if (parent instanceof Region)
            return ((Region) parent).getHeight ();
        return 0d;
    }
}