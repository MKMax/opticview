package io.github.mathfx.cartesian;

import io.github.mathfx.util.Disposable;
import io.github.mathfx.util.ObservableGroup;
import javafx.css.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public final class GraphView extends StackPane implements Disposable {

    private static final StyleablePropertyFactory<GraphView> STYLEABLE_FACTORY =
        new StyleablePropertyFactory<> (StackPane.getClassCssMetaData ());

    /* +--------------------------+ */
    /* | CSS METADATA DEFINITIONS | */
    /* +--------------------------+ */

    static {
        STYLEABLE_FACTORY.createFontCssMetaData  ("-mfx-guide-font", gv -> gv.guideFont, Font.getDefault ());
        STYLEABLE_FACTORY.createColorCssMetaData ("-mfx-origin-guide-foreground", gv -> gv.originGuideForeground, Color.WHITE);
        STYLEABLE_FACTORY.createColorCssMetaData ("-mfx-origin-guide-background", gv -> gv.originGuideBackground, Color.BLACK);
        STYLEABLE_FACTORY.createColorCssMetaData ("-mfx-major-guide-foreground", gv -> gv.majorGuideForeground, Color.WHITE);
        STYLEABLE_FACTORY.createColorCssMetaData ("-mfx-major-guide-background", gv -> gv.majorGuideBackground, Color.GRAY);
        STYLEABLE_FACTORY.createColorCssMetaData ("-mfx-minor-guide-foreground", gv -> gv.minorGuideForeground, Color.BLACK);
        STYLEABLE_FACTORY.createColorCssMetaData ("-mfx-minor-guide-background", gv -> gv.minorGuideBackground, Color.LIGHTGRAY);
    }

    /* +----------------------------+ */
    /* | STYLE PROPERTY DEFINITIONS | */
    /* +----------------------------+ */

    private final StyleableObjectProperty<Font> guideFont =
        (StyleableObjectProperty<Font>) STYLEABLE_FACTORY.createStyleableFontProperty (this, "guideFont", "-mfx-guide-font");
    public StyleableObjectProperty<Font> guideFontProperty () { return guideFont; }
    public Font getGuideFont () { return guideFont.get (); }
    public void setGuideFont (Font nGuideFont) { guideFont.set (nGuideFont); }

    /* ORIGIN GUIDE STYLES */
    private final StyleableObjectProperty<Color> originGuideForeground =
        (StyleableObjectProperty<Color>) STYLEABLE_FACTORY.createStyleableColorProperty (this, "originGuideForeground", "-mfx-origin-guide-foreground");
    public ObjectProperty<Color> originGuideForegroundProperty () { return originGuideForeground; }
    public Color getOriginGuideForeground () { return originGuideForeground.get (); }
    public void setOriginGuideForeground (Color nForeground) { originGuideForeground.set (nForeground); }

    private final StyleableObjectProperty<Color> originGuideBackground =
        (StyleableObjectProperty<Color>) STYLEABLE_FACTORY.createStyleableColorProperty (this, "originGuideBackground", "-mfx-origin-guide-background");
    public Color getOriginGuideBackground () { return originGuideBackground.get (); }
    public StyleableObjectProperty<Color> originGuideBackgroundProperty () { return originGuideBackground; }
    public void setOriginGuideBackground (Color nBackground) { originGuideBackground.set (nBackground); }

    /* MAJOR GUIDE STYLES */
    private final StyleableObjectProperty<Color> majorGuideForeground =
        (StyleableObjectProperty<Color>) STYLEABLE_FACTORY.createStyleableColorProperty (this, "majorGuideForeground", "-mfx-major-guide-foreground");
    public ObjectProperty<Color> majorGuideForegroundProperty () { return majorGuideForeground; }
    public Color getMajorGuideForeground () { return majorGuideForeground.get (); }
    public void setMajorGuideForeground (Color nForeground) { majorGuideForeground.set (nForeground); }

    private final StyleableObjectProperty<Color> majorGuideBackground =
        (StyleableObjectProperty<Color>) STYLEABLE_FACTORY.createStyleableColorProperty (this, "majorGuideBackground", "-mfx-major-guide-background");
    public Color getMajorGuideBackground () { return majorGuideBackground.get (); }
    public StyleableObjectProperty<Color> majorGuideBackgroundProperty () { return majorGuideBackground; }
    public void setMajorGuideBackground (Color nBackground) { majorGuideBackground.set (nBackground); }

    /* MINOR GUIDE STYLES */
    private final StyleableObjectProperty<Color> minorGuideForeground =
        (StyleableObjectProperty<Color>) STYLEABLE_FACTORY.createStyleableColorProperty (this, "minorGuideForeground", "-mfx-minor-guide-foreground");
    public Color getMinorGuideForeground () { return minorGuideForeground.get (); }
    public StyleableObjectProperty<Color> minorGuideForegroundProperty () { return minorGuideForeground; }
    public void setMinorGuideForeground (Color nForeground) { minorGuideForeground.set (nForeground); }

    private final StyleableObjectProperty<Color> minorGuideBackground =
        (StyleableObjectProperty<Color>) STYLEABLE_FACTORY.createStyleableColorProperty (this, "minorGuideBackground", "-mfx-minor-guide-background");
    public Color getMinorGuideBackground () { return minorGuideBackground.get (); }
    public StyleableObjectProperty<Color> minorGuideBackgroundProperty () { return minorGuideBackground; }
    public void setMinorGuideBackground (Color nBackground) { minorGuideBackground.set (nBackground); }

    /* +------------------+ */
    /* | STANDARD MEMBERS | */
    /* +------------------+ */

    public GraphView () {

    }

    @Override
    public void dispose () {

    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
        return getClassCssMetaData ();
    }
}
