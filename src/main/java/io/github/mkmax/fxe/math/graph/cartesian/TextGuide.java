package io.github.mkmax.fxe.math.graph.cartesian;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class TextGuide extends Guide {

    public enum Justify {
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

        Justify (double xOffsetMultiplier, double yOffsetMultiplier) {
            X_OFFSET_MULTIPLIER = xOffsetMultiplier;
            Y_OFFSET_MULTIPLIER = yOffsetMultiplier;
        }
    }

    public class Appearance extends Guide.Appearance {

        private static final double MIN_PADDING = 1d;

        private double textPadding    = MIN_PADDING;
        private Font   textFont       = Font.getDefault ();
        private Color  textForeground = Color.WHITE;
        private Color  textBackground = Color.BLACK;

        public double getTextPadding () {
            return textPadding;
        }

        public void setTextPadding (double nTextPadding) {
            TextGuide.this.onTextPaddingChanged (
                textPadding,
                textPadding = Math.max (MIN_PADDING, nTextPadding));
        }

        public Font getTextFont () {
            return textFont;
        }

        public void setTextFont (Font nFont) {
            TextGuide.this.onTextFontChanged (
                textFont,
                textFont = nFont == null ? Font.getDefault () : nFont);
        }

        public Color getTextForeground () {
            return textForeground;
        }

        public void setTextForeground (Color nTextForeground) {
            TextGuide.this.onTextForegroundChanged (
                textForeground,
                textForeground = nTextForeground == null ? Color.WHITE : nTextForeground);
        }

        public Color getTextBackground () {
            return textBackground;
        }

        public void setTextBackground (Color nTextBackground) {
            TextGuide.this.onTextBackgroundChanged (
                textBackground,
                textBackground = nTextBackground == null ? Color.BLACK : nTextBackground);
        }

    }

    private String  text;
    private Justify justify;

    /* +-------------------+ */
    /* | COMMON PROPERTIES | */
    /* +-------------------+ */

    /* TEXT */

    public String getText () {
        return text;
    }

    public void setText (String nText) {
        onTextChanged (text, text = nText);
    }

    /* JUSTIFY */

    public Justify getJustify () {
        return justify;
    }

    public void setJustify (Justify nJustify) {
        onTextJustifyChanged (justify, justify = nJustify);
    }

    /* +--------------------+ */
    /* | OPTIONAL CALLBACKS | */
    /* +--------------------+ */

    protected abstract void onTextChanged (String old, String now);

    protected abstract void onTextJustifyChanged (Justify old, Justify now);

    /* APPEARANCE */

    protected abstract void onTextPaddingChanged (double old, double now);

    protected abstract void onTextFontChanged (Font old, Font now);

    protected abstract void onTextBackgroundChanged (Color old, Color now);

    protected abstract void onTextForegroundChanged (Color old, Color now);

}
