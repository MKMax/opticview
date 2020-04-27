package io.github.mathfx.cartesian;

import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.ObservableGroup;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public final class GraphView extends StackPane implements Disposable {

    private static final StyleablePropertyFactory<GraphView> FACTORY =
        new StyleablePropertyFactory<> (StackPane.getClassCssMetaData ());

    /* +--------------------------+ */
    /* | CSS METADATA DEFINITIONS | */
    /* +--------------------------+ */

    static {
        FACTORY.createFontCssMetaData  ("-mfx-guide-font", gv -> gv.guideFont, Font.getDefault ());
        FACTORY.createColorCssMetaData ("-mfx-origin-guide-foreground", gv -> gv.originGuideForeground, Color.WHITE);
        FACTORY.createColorCssMetaData ("-mfx-origin-guide-background", gv -> gv.originGuideBackground, Color.BLACK);
        FACTORY.createColorCssMetaData ("-mfx-major-guide-foreground", gv -> gv.majorGuideForeground, Color.WHITE);
        FACTORY.createColorCssMetaData ("-mfx-major-guide-background", gv -> gv.majorGuideBackground, Color.GRAY);
        FACTORY.createColorCssMetaData ("-mfx-minor-guide-foreground", gv -> gv.minorGuideForeground, Color.BLACK);
        FACTORY.createColorCssMetaData ("-mfx-minor-guide-background", gv -> gv.minorGuideBackground, Color.LIGHTGRAY);
    }

    /* +----------------------------+ */
    /* | STYLE PROPERTY DEFINITIONS | */
    /* +----------------------------+ */

    private final StyleableObjectProperty<Font> guideFont =
        (StyleableObjectProperty<Font>) FACTORY.createStyleableFontProperty (this, "guideFont", "-mfx-guide-font");
    public StyleableObjectProperty<Font> guideFontProperty () { return guideFont; }
    public Font getGuideFont () { return guideFont.get (); }
    public void setGuideFont (Font nGuideFont) { guideFont.set (nGuideFont); }

    /* ORIGIN GUIDE STYLES */
    private final StyleableObjectProperty<Color> originGuideForeground =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "originGuideForeground", "-mfx-origin-guide-foreground");
    public ObjectProperty<Color> originGuideForegroundProperty () { return originGuideForeground; }
    public Color getOriginGuideForeground () { return originGuideForeground.get (); }
    public void setOriginGuideForeground (Color nForeground) { originGuideForeground.set (nForeground); }

    private final StyleableObjectProperty<Color> originGuideBackground =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "originGuideBackground", "-mfx-origin-guide-background");
    public Color getOriginGuideBackground () { return originGuideBackground.get (); }
    public StyleableObjectProperty<Color> originGuideBackgroundProperty () { return originGuideBackground; }
    public void setOriginGuideBackground (Color nBackground) { originGuideBackground.set (nBackground); }

    /* MAJOR GUIDE STYLES */
    private final StyleableObjectProperty<Color> majorGuideForeground =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "majorGuideForeground", "-mfx-major-guide-foreground");
    public ObjectProperty<Color> majorGuideForegroundProperty () { return majorGuideForeground; }
    public Color getMajorGuideForeground () { return majorGuideForeground.get (); }
    public void setMajorGuideForeground (Color nForeground) { majorGuideForeground.set (nForeground); }

    private final StyleableObjectProperty<Color> majorGuideBackground =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "majorGuideBackground", "-mfx-major-guide-background");
    public Color getMajorGuideBackground () { return majorGuideBackground.get (); }
    public StyleableObjectProperty<Color> majorGuideBackgroundProperty () { return majorGuideBackground; }
    public void setMajorGuideBackground (Color nBackground) { majorGuideBackground.set (nBackground); }

    /* MINOR GUIDE STYLES */
    private final StyleableObjectProperty<Color> minorGuideForeground =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "minorGuideForeground", "-mfx-minor-guide-foreground");
    public Color getMinorGuideForeground () { return minorGuideForeground.get (); }
    public StyleableObjectProperty<Color> minorGuideForegroundProperty () { return minorGuideForeground; }
    public void setMinorGuideForeground (Color nForeground) { minorGuideForeground.set (nForeground); }

    private final StyleableObjectProperty<Color> minorGuideBackground =
        (StyleableObjectProperty<Color>) FACTORY.createStyleableColorProperty (this, "minorGuideBackground", "-mfx-minor-guide-background");
    public Color getMinorGuideBackground () { return minorGuideBackground.get (); }
    public StyleableObjectProperty<Color> minorGuideBackgroundProperty () { return minorGuideBackground; }
    public void setMinorGuideBackground (Color nBackground) { minorGuideBackground.set (nBackground); }

    /* +------------------+ */
    /* | STANDARD MEMBERS | */
    /* +------------------+ */

    private final DoubleProperty left   = new SimpleDoubleProperty (-1d);
    private final DoubleProperty right  = new SimpleDoubleProperty ( 1d);
    private final DoubleProperty bottom = new SimpleDoubleProperty (-1d);
    private final DoubleProperty top    = new SimpleDoubleProperty ( 1d);

    private final ObservableGroup<Number> orthoGroup      = new ObservableGroup<> (left, right, bottom, top);
    private final ObservableGroup<Number> dimensionsGroup = new ObservableGroup<> (widthProperty (), heightProperty ());
    private final ObservableGroup<Number> projectionGroup = new ObservableGroup<> (orthoGroup, dimensionsGroup);

    private double Mx = 1d;
    private double Kx = 0d;

    private double My = 1d;
    private double Ky = 0d;

    public GraphView () {
        projectionGroup.add (this::reproject);
    }

    /* +-----------------+ */
    /* | GETTERS/SETTERS | */
    /* +-----------------+ */

    /* LEFT */
    public double getLeft () { return left.get (); }
    public void setLeft (double nLeft) { left.set (nLeft); }
    public DoubleProperty leftProperty () { return left; }

    /* RIGHT */
    public double getRight () { return right.get (); }
    public void setRight (double nRight) { right.set (nRight); }
    public DoubleProperty rightProperty () { return right; }

    /* BOTTOM */
    public double getBottom () { return bottom.get (); }
    public void setBottom (double nBottom) { bottom.set (nBottom); }
    public DoubleProperty bottomProperty () { return bottom; }

    /* TOP */
    public double getTop () { return top.get (); }
    public void setTop (double nTop) { top.set (nTop); }
    public DoubleProperty topProperty () { return top; }

    /* +---------+ */
    /* | UTILITY | */
    /* +---------+ */

    /**
     * Maps a window space X coordinate to component or viewport
     * space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param p The window space X coordinate.
     * @return The viewport space X coordinate.
     */
    public double projectX (double p) {
        return Mx * p + Kx;
    }

    /**
     * Maps a window space Y coordinate to component or viewport
     * space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param p The window space Y coordinate.
     * @return The viewport space Y coordinate.
     */
    public double projectY (double p) {
        return My * p + Ky;
    }

    /**
     * Maps a component or viewport space X coordinate back to
     * window space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param q The viewport space X coordinate.
     * @return The window space X coordinate.
     */
    public double unprojectX (double q) {
        return (q - Kx) / Mx;
    }

    /**
     * Maps a component or viewport space Y coordinate back to
     * window space.
     * <p>
     * Viewport space refers to the set of coordinates for which
     * all X are in [0, getWidth()] and all Y are in [0, getHeight()].
     * Window space refers to the set of coordinates for which
     * all X are in [left, right] and all Y are in [bottom, top].
     *
     * @param q The viewport space Y coordinate.
     * @return The window space Y coordinate.
     */
    public double unprojectY (double q) {
        return (q - Ky) / My;
    }

    @Override
    public void dispose () {
        /* I opt to dispose each group individually instead of invoking
         * projectionGroup.dispose(true) to make the code more clear of
         * its purpose.
         */
        orthoGroup.dispose ();
        dimensionsGroup.dispose ();
        projectionGroup.dispose ();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
        return FACTORY.getCssMetaData ();
    }

    public void reproject () {
        final double width  = getWidth ();
        final double height = getHeight ();

        final double left   = getLeft ();
        final double right  = getRight ();
        final double bottom = getBottom ();
        final double top    = getTop ();

        Mx = width / (right - left);
        Kx = -Mx * left;

        My = height / (bottom - top);
        Ky = -My * top;
    }
}
