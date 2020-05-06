package io.github.mathfx.cartesian;

import io.github.mathfx.cartesian.part.DecimalPartitionScheme;
import io.github.mathfx.cartesian.part.PartitionScheme;
import io.github.mathfx.cartesian.part.PartitionScheme.Type;
import io.github.mathfx.cartesian.part.PartitionScheme.Index;
import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.ObservableGroup;
import io.github.mathfx.util.css.StyleableFactory;
import io.github.mathfx.util.format.PrecisionDecimalFormat;
import io.github.mathfx.util.format.ScientificDecimalFormat;
import io.github.mathfx.util.fx.BackgroundUtil;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GuideContainer extends StackPane implements Disposable {

    /* +-------------------------------------------------------------------------------------------+ */
    /* |                                SINGLE GUIDE IMPLEMENTATIONS                               | */
    /* +-------------------------------------------------------------------------------------------+ */

    /* +----------------+ */
    /* | ABSTRACT GUIDE | */
    /* +----------------+ */

    public static abstract class AbstractGuide extends Pane implements Disposable {

        protected static final double MIN_SIZE = 1d;
        protected static final double DEF_SIZE = 2d;
        protected static final double MAX_SIZE = 32d;

        protected static final Color DEF_BACKGROUND_COLOR = Color.BLACK;
        protected static final Color DEF_FOREGROUND_COLOR = Color.WHITE;

        private static final StyleableFactory<AbstractGuide> FACTORY
            = new StyleableFactory<> (Pane.getClassCssMetaData ());

        /* +---------------------------+ */
        /* | CSS META DATA DECLARATION | */
        /* +---------------------------+ */

        static {
            FACTORY.buildSizeCssMetaData ()
                .setProperty ("-mfx-size")
                .setInitialValue (DEF_SIZE)
                .setGetStyleablePropertyImpl (ag -> ag.size)
                .build ();
            FACTORY.buildColorCssMetaData ()
                .setProperty ("-mfx-background-color")
                .setInitialValue (DEF_BACKGROUND_COLOR)
                .setGetStyleablePropertyImpl (ag -> ag.backgroundColor)
                .build ();
            FACTORY.buildColorCssMetaData ()
                .setProperty ("-mfx-foreground-color")
                .setInitialValue (DEF_FOREGROUND_COLOR)
                .setGetStyleablePropertyImpl (ag -> ag.foregroundColor)
                .build ();
        }

        public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
            return FACTORY.getCssMetaData ();
        }

        /* +----------------------------------+ */
        /* | STYLEABLE PROPERTIES DECLARATION | */
        /* +----------------------------------+ */

        /* SIZE */
        private final StyleableObjectProperty<Number> size = FACTORY.<Number>buildStyleableProperty ()
            .setBean (this)
            .setName ("size")
            .setProperty ("-mfx-size")
            .build ();

        public double getSize () {
            return size.get () == null ? DEF_SIZE : Math.max(Math.min(size.get ().doubleValue (), MAX_SIZE), MIN_SIZE);
        }

        public void setSize (double nSize) {
            size.set (Math.max (Math.min (nSize, MAX_SIZE), MIN_SIZE));
        }

        /* BACKGROUND COLOR */
        private final StyleableObjectProperty<Color> backgroundColor = FACTORY.<Color>buildStyleableProperty ()
            .setBean (this)
            .setName ("backgroundColor")
            .setProperty ("-mfx-background-color")
            .build ();

        public Color getBackgroundColor () {
            return backgroundColor.get () == null ? DEF_BACKGROUND_COLOR : backgroundColor.get ();
        }

        public void setBackgroundColor (Color nColor) {
            backgroundColor.set (nColor);
        }

        /* FOREGROUND COLOR */
        private final StyleableObjectProperty<Color> foregroundColor = FACTORY.<Color>buildStyleableProperty ()
            .setBean (this)
            .setName ("foregroundColor")
            .setProperty ("-mfx-foreground-color")
            .build ();

        public Color getForegroundColor () {
            return foregroundColor.get ();
        }

        public void setForegroundColor (Color nColor) {
            foregroundColor.set (nColor);
        }

        /* +---------------------+ */
        /* | STANDARD PROPERTIES | */
        /* +---------------------+ */

        private static final double DEF_GUIDE_X = 0d;
        private static final double DEF_GUIDE_Y = 0d;

        /* X */
        protected final DoubleProperty x = new SimpleDoubleProperty (DEF_GUIDE_X);

        public double getGuideX () {
            return x.get ();
        }

        public void setGuideX (double nX) {
            x.set (nX);
        }

        /* Y */
        protected final DoubleProperty y = new SimpleDoubleProperty (DEF_GUIDE_Y);

        public double getGuideY () {
            return y.get ();
        }

        public void setGuideY (double nY) {
            y.set (nY);
        }

        /* +------------------+ */
        /* | STANDARD MEMBERS | */
        /* +------------------+ */

        /* The guide and label members will be accessed by GuideContainer to add them to respective panels */
        protected final Rectangle               guide           = new Rectangle ();
        protected final Label                   label           = new Label ();

        protected final ObservableGroup<Object> appearanceGroup = new ObservableGroup<> (
            backgroundColor, foregroundColor);

        protected final ObservableGroup<Object> positionGroup   = new ObservableGroup<> (x, y);

        protected final DoubleProperty          parentWidth     = new SimpleDoubleProperty ();
        protected final DoubleProperty          parentHeight    = new SimpleDoubleProperty ();
        protected final ObservableGroup<Object> parentGroup     = ObservableGroup.observeParentSize (this, parentWidth, parentHeight);

        protected final ObservableGroup<Object> labelGroup      = new ObservableGroup<> (
            label.widthProperty (), label.heightProperty ());

        protected final DoubleProperty          ortho           = new SimpleDoubleProperty ();
        protected final DoubleProperty          parallel        = new SimpleDoubleProperty ();
        protected final ObservableGroup<Object> layoutGroup     = ObservableGroup.builder ()
            .addAll (size, ortho, parallel)
            .addAll (positionGroup, parentGroup, labelGroup)
            .build ();

        private final ChangeListener<Number> opacityListener = (__obs, __old, now) -> {
            guide.setOpacity (now == null ? 0d : now.doubleValue ());
            label.setOpacity (now == null ? 0d : now.doubleValue ());
        };

        private final ChangeListener<Boolean> visibleListener = (__obs, __old, now) -> {
            guide.setVisible (now);
            label.setVisible (now);
        };

        {
            opacityProperty ().addListener (opacityListener);
            visibleProperty ().addListener (visibleListener);
            appearanceGroup.add (this::adaptAppearance);
            layoutGroup    .add (this::adaptLayout);
        }

        @Override
        public void dispose () {
            opacityProperty ().removeListener (opacityListener);
            visibleProperty ().removeListener (visibleListener);
            appearanceGroup.dispose ();
            parentGroup.dispose ();
            layoutGroup.dispose ();
        }

        @Override
        public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
            return getClassCssMetaData ();
        }

        protected /*abstract*/ void adaptAppearance () {
            final Color bg = getBackgroundColor ();
            final Color fg = getForegroundColor ();
            guide.setFill (bg);
            label.setBackground (BackgroundUtil.newFillBackground (bg));
            label.setTextFill (fg);
        }

        protected abstract void adaptLayout ();
    }

    /* +------------------------+ */
    /* | ABSTRACT NUMERIC GUIDE | */
    /* +------------------------+ */

    public static abstract class AbstractNumericGuide extends AbstractGuide {

        public enum Format {
            SCIENTIFIC,
            DECIMAL
        }


        protected static final double DEF_VALUE    = 0d;
        protected static final int    DEF_SCI_PREC = 3;
        protected static final int    DEF_DEC_PREC = 3;

        protected static final Format DEF_NUM_FORMAT = Format.DECIMAL;

        private static final StyleableFactory<AbstractNumericGuide> FACTORY
            = new StyleableFactory<> (AbstractGuide.getClassCssMetaData ());

        /* +---------------------------+ */
        /* | CSS META DATA DECLARATION | */
        /* +---------------------------+ */

        static {
            FACTORY.buildSizeCssMetaData ()
                .setProperty ("-mfx-value")
                .setInitialValue (DEF_VALUE)
                .setGetStyleablePropertyImpl (ang -> ang.value)
                .build ();
            FACTORY.buildEnumCssMetaData (Format.class)
                .setProperty ("-mfx-format")
                .setInitialValue (DEF_NUM_FORMAT)
                .setGetStyleablePropertyImpl (ang -> ang.format)
                .build ();
            FACTORY.buildSizeCssMetaData ()
                .setProperty ("-mfx-scientific-precision")
                .setInitialValue (DEF_SCI_PREC)
                .setGetStyleablePropertyImpl (ang -> ang.scientificPrecision)
                .build ();
            FACTORY.buildSizeCssMetaData ()
                .setProperty ("-mfx-decimal-precision")
                .setInitialValue (DEF_DEC_PREC)
                .setGetStyleablePropertyImpl (ang -> ang.decimalPrecision)
                .build ();
        }

        public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
            return FACTORY.getCssMetaData ();
        }

        /* +---------------------------------+ */
        /* | STYLEABLE PROPERTY DECLARATIONS | */
        /* +---------------------------------+ */

        /* VALUE */
        private final StyleableObjectProperty<Number> value = FACTORY.<Number>buildStyleableProperty ()
            .setBean (this)
            .setName ("value")
            .setProperty ("-mfx-value")
            .build ();

        public double getValue () {
            return value.get () == null ? DEF_VALUE : value.get ().doubleValue ();
        }

        public void setValue (double nValue) {
            value.set (nValue);
        }

        /* FORMAT */
        private final StyleableObjectProperty<Format> format = FACTORY.<Format>buildStyleableProperty ()
            .setBean (this)
            .setName ("value")
            .setProperty ("-mfx-format")
            .build ();

        public Format getFormat () {
            return format.get () == null ? DEF_NUM_FORMAT : format.get ();
        }

        public void setFormat (Format nFormat) {
            format.set (nFormat);
        }

        /* SCIENTIFIC PRECISION */
        private final StyleableObjectProperty<Number> scientificPrecision = FACTORY.<Number>buildStyleableProperty ()
            .setBean (this)
            .setName ("scientificPrecision")
            .setProperty ("-mfx-scientific-precision")
            .build ();

        public int getScientificPrecision () {
            return scientificPrecision.get () == null ? DEF_SCI_PREC : scientificPrecision.get ().intValue ();
        }

        public void setScientificPrecision (int nPrecision) {
            scientificPrecision.set (nPrecision);
        }

        /* DECIMAL PRECISION */
        private final StyleableObjectProperty<Number> decimalPrecision = FACTORY.<Number>buildStyleableProperty ()
            .setBean (this)
            .setName ("decimalPrecision")
            .setProperty ("-mfx-decimal-precision")
            .build ();

        public int getDecimalPrecision () {
            return decimalPrecision.get () == null ? DEF_DEC_PREC : decimalPrecision.get ().intValue ();
        }

        public void setDecimalPrecision (int decimalPrecision) {
            this.decimalPrecision.set (decimalPrecision);
        }

        /* +------------------+ */
        /* | STANDARD MEMBERS | */
        /* +------------------+ */

        protected final ScientificDecimalFormat sFormat        = new ScientificDecimalFormat ();
        protected final PrecisionDecimalFormat  dFormat        = new PrecisionDecimalFormat ();
        protected final ObservableGroup<Object> precisionGroup = new ObservableGroup<> (
            scientificPrecision, decimalPrecision);
        protected final ObservableGroup<Object> updateGroup    = ObservableGroup.builder ()
            .addAll (precisionGroup)
            .addAll (value, format)
            .build ();

        {
            precisionGroup.add (this::updateFormats);
            updateGroup.add (this::updateLabel);
            layoutGroup.add (updateGroup);
        }

        @Override
        public void dispose () {
            super.dispose ();
            precisionGroup.dispose ();
            updateGroup.dispose ();
        }

        @Override
        public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
            return getClassCssMetaData ();
        }

        private void updateFormats () {
            sFormat.setPrecision (getScientificPrecision ());
            dFormat.setPrecision (getDecimalPrecision ());
        }

        private void updateLabel () {
            if (getFormat () == Format.SCIENTIFIC)
                label.setText (sFormat.format (getValue ()));
            else
                label.setText (dFormat.format (getValue ()));
        }
    }

    /* +------------------+ */
    /* | HORIZONTAL GUIDE | */
    /* +------------------+ */

    public static final class HorizontalNumericGuide extends AbstractNumericGuide {
        @Override
        protected void adaptLayout () {
            guide.setWidth (parentWidth.get ());
            guide.setHeight(getSize ());
            guide.setLayoutX (0d);
            guide.setLayoutY (getGuideY () - getSize () * 0.5d);

            label.setLayoutX (getGuideX () - label.getWidth () * 0.5d);
            label.setLayoutY (getGuideY () - label.getHeight () * 0.5d);
        }
    }

    /* +----------------+ */
    /* | VERTICAL GUIDE | */
    /* +----------------+ */

    public static final class VerticalNumericGuide extends AbstractNumericGuide {
        @Override
        protected void adaptLayout () {
            guide.setWidth (getSize ());
            guide.setHeight (parentHeight.get ());
            guide.setLayoutX (getGuideX () - getSize () * 0.5d);

            label.setLayoutX (getGuideX () - label.getWidth () * 0.5d);
            label.setLayoutY (getGuideY () - label.getHeight () * 0.5d);
        }
    }

    /* +-------------------------------------------------------------------------------------------+ */
    /* |                               GUIDE CONTAINER IMPLEMENTATION                              | */
    /* +-------------------------------------------------------------------------------------------+ */

    public enum SelectedPartitionScheme {
        DECIMAL (DecimalPartitionScheme.getInstance ());

        public final PartitionScheme IMPL;

        SelectedPartitionScheme (PartitionScheme impl) {
            IMPL = Objects.requireNonNull (impl);
        }
    }

    private static final StyleableFactory<GuideContainer> FACTORY
        = new StyleableFactory<> (StackPane.getClassCssMetaData ());

    /* +---------------------------+ */
    /* | CSS META DATA DECLARATION | */
    /* +---------------------------+ */


    private static final String HORIZONTAL_STYLE_CLASS = "horizontal";
    private static final String VERTICAL_STYLE_CLASS   = "vertical";
    private static final String MINOR_STYLE_CLASS      = "minor";
    private static final String MAJOR_STYLE_CLASS      = "major";
    private static final String ORIGIN_STYLE_CLASS     = "origin";

    private static final Map<Type, String> INDEX_TYPE_TO_STYLE_CLASS = Map.of (
        Type.MINOR, MINOR_STYLE_CLASS,
        Type.MAJOR, MAJOR_STYLE_CLASS,
        Type.ORIGIN, ORIGIN_STYLE_CLASS
    );

    private static final double MIN_PIXELS_PER_UNIT = 64d;
    private static final double DEF_PIXELS_PER_UNIT = 256d;
    private static final double MAX_PIXELS_PER_UNIT = 65536d;

    private static final SelectedPartitionScheme DEF_PARTITION_SCHEME = SelectedPartitionScheme.DECIMAL;

    static {
        FACTORY.buildSizeCssMetaData ()
            .setProperty ("-mfx-horizontal-pixels-per-unit")
            .setInitialValue (DEF_PIXELS_PER_UNIT)
            .setGetStyleablePropertyImpl (gc -> gc.horizontalPixelsPerUnit)
            .build ();
        FACTORY.buildSizeCssMetaData ()
            .setProperty ("-mfx-vertical-pixels-per-unit")
            .setInitialValue (DEF_PIXELS_PER_UNIT)
            .setGetStyleablePropertyImpl (gv -> gv.verticalPixelsPerUnit)
            .build ();
        FACTORY.buildEnumCssMetaData (SelectedPartitionScheme.class)
            .setProperty ("-mfx-partition-scheme")
            .setInitialValue (DEF_PARTITION_SCHEME)
            .setGetStyleablePropertyImpl (gc -> gc.partitionScheme)
            .build ();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
        return FACTORY.getCssMetaData ();
    }

    /* +---------------------------------+ */
    /* | STYLEABLE PROPERTY DECLARATIONS | */
    /* +---------------------------------+ */

    /* HORIZONTAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> horizontalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("horizontalPixelsPerUnit")
        .setProperty ("-mfx-horizontal-pixels-per-unit")
        .build ();

    public double getHorizontalPixelsPerUnit () {
        return horizontalPixelsPerUnit.get () == null ? DEF_PIXELS_PER_UNIT : horizontalPixelsPerUnit.get ().doubleValue ();
    }

    public void setHorizontalPixelsPerUnit (double nHppu) {
        horizontalPixelsPerUnit.set (nHppu);
    }

    /* VERTICAL PIXELS PER UNIT */
    private final StyleableObjectProperty<Number> verticalPixelsPerUnit = FACTORY.<Number>buildStyleableProperty ()
        .setBean (this)
        .setName ("verticalPixelsPerUnit")
        .setProperty ("-mfx-vertical-pixels-per-unit")
        .build ();

    public double getVerticalPixelsPerUnit () {
        return verticalPixelsPerUnit.get () == null ? DEF_PIXELS_PER_UNIT : verticalPixelsPerUnit.get ().doubleValue ();
    }

    public void setVerticalPixelsPerUnit (double nVppu) {
        verticalPixelsPerUnit.set (nVppu);
    }

    /* PARTITION SCHEME */
    private final StyleableObjectProperty<SelectedPartitionScheme> partitionScheme
        = FACTORY.<SelectedPartitionScheme>buildStyleableProperty ()
        .setBean (this)
        .setName ("selectedPartitionScheme")
        .setProperty ("-mfx-partition-scheme")
        .build ();

    public SelectedPartitionScheme getPartitionScheme () {
        return partitionScheme.get () == null ? DEF_PARTITION_SCHEME : partitionScheme.get ();
    }

    public void setPartitionScheme (SelectedPartitionScheme nScheme) {
        partitionScheme.set (nScheme);
    }

    /* +------------------+ */
    /* | STANDARD MEMBERS | */
    /* +------------------+ */

    private final List<HorizontalNumericGuide> hGuides           = new ArrayList<> ();
    private final List<VerticalNumericGuide>   vGuides           = new ArrayList<> ();

    private final Pane                         guidePane          = new Pane ();
    private final Pane                         labelPane          = new Pane ();

    private final ObservableGroup<Object>      pixelsPerUnitGroup = new ObservableGroup<> (
        horizontalPixelsPerUnit, verticalPixelsPerUnit);

    private final DoubleProperty               parentWidth        = new SimpleDoubleProperty ();
    private final DoubleProperty               parentHeight       = new SimpleDoubleProperty ();
    private final ObservableGroup<Object>      parentGroup        = ObservableGroup.observeParentSize (this, parentWidth, parentHeight);

    private final ObservableGroup<Object>      updateGroup        = new ObservableGroup<> (
        pixelsPerUnitGroup, parentGroup);

    {
        getChildren ().addAll (guidePane, labelPane);

        updateGroup.add (this::update);
    }

    @Override
    public void dispose () {
        pixelsPerUnitGroup.dispose ();
        parentGroup.dispose ();
        updateGroup.dispose ();
        hGuides.forEach (AbstractGuide::dispose); hGuides.clear ();
        vGuides.forEach (AbstractGuide::dispose); vGuides.clear ();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
        return getClassCssMetaData ();
    }

    private void update () {
        setWidth (parentWidth.get ());
        setHeight (parentHeight.get ());

        if (getParent () instanceof GraphView) {
            final GraphView gv = (GraphView) getParent ();
            final List<Index> hIndices = getPartitionScheme ().IMPL.partition (
                gv.getLeft (),
                gv.getRight (),
                0d, getWidth (),
                getHorizontalPixelsPerUnit ()
            );
            final List<Index> vIndices = getPartitionScheme ().IMPL.partition (
                gv.getBottom (),
                gv.getTop (),
                getHeight (), 0d,
                getVerticalPixelsPerUnit ()
            );

            /* Ensure that we have enough guides actually present */
            while (vGuides.size () < hIndices.size ()) {
                final VerticalNumericGuide n = new VerticalNumericGuide ();
                guidePane.getChildren ().add (n.guide);
                labelPane.getChildren ().add (n.label);
                vGuides.add (n);
                getChildren ().add (n);
            }
            while (hGuides.size () < vIndices.size ()) {
                final HorizontalNumericGuide n = new HorizontalNumericGuide ();
                guidePane.getChildren ().add (n.guide);
                labelPane.getChildren ().add (n.label);
                hGuides.add (n);
                getChildren ().add (n);
            }

            /* Now we configure the guides */
            for (int i = 0; i < hIndices.size (); ++i) { /* we configure the vertical guides in this */
                final AbstractNumericGuide guide = vGuides.get (i);
                final Index ind = hIndices.get (i);
                guide.setVisible (true);
                guide.setValue (ind.pos);
                /* TODO: SET FORMAT HERE */
                guide.setGuideX (gv.projectX (ind.pos));
                guide.setGuideY (gv.projectY (0d));
                /* There is a visual bug here because the observable list automatically notifies the
                 * UI system to redraw the components whenever the contents change, so we will just deal
                 * with it by hiding the component and then revealing it again. */
                guide.getStyleClass ().clear ();
                guide.getStyleClass ().addAll (
                    VERTICAL_STYLE_CLASS,
                    INDEX_TYPE_TO_STYLE_CLASS.get (ind.type) /* if exception is thrown here, there is a problem with partitioner */
                );
            }
            for (int i = 0; i < vIndices.size (); ++i) { /* we configure the horizontal guide in this */
                final AbstractNumericGuide guide = hGuides.get (i);
                final Index ind = vIndices.get (i);
                guide.setVisible (true);
                guide.setValue (ind.pos);
                /* TODO: SET FORMAT HERE */
                guide.setGuideX (gv.projectX (0d));
                guide.setGuideY (gv.projectY (ind.pos));
                /* There is a visual bug here because the observable list automatically notifies the
                 * UI system to redraw the components whenever the contents change, so we will just deal
                 * with it by hiding the component and then revealing it again. */
                guide.getStyleClass ().clear ();
                guide.getStyleClass ().addAll (
                    HORIZONTAL_STYLE_CLASS,
                    INDEX_TYPE_TO_STYLE_CLASS.get (ind.type) /* if exception is thrown here, there is a problem with partitioner */
                );
            }

            /* hide the guides that are not used/visible */
            for (int i = hGuides.size () - 1; i >= vIndices.size (); --i)
                hGuides.get (i).setVisible (false);
            for (int i = vGuides.size () - 1; i >= hIndices.size (); --i)
                vGuides.get (i).setVisible (false);
        }
        else {
            /* TODO: HIDE EVERYTHING */
        }
    }
}