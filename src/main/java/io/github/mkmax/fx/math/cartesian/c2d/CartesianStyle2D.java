package io.github.mkmax.fx.math.cartesian.c2d;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CartesianStyle2D {

    private final ObjectProperty<Font>  primaryFont    = new SimpleObjectProperty<> ();
    private final ObjectProperty<Color> background     = new SimpleObjectProperty<> ();
    private final ObjectProperty<Color> majorAxisColor = new SimpleObjectProperty<> ();
    private final ObjectProperty<Color> minorAxisColor = new SimpleObjectProperty<> ();

    public CartesianStyle2D () {
        primaryFont.set    (Font.loadFont ("jar://font/opensans-regular.ttf", 0d));
        background.set     (Color.rgb (255, 255, 255));
        majorAxisColor.set (Color.rgb (225, 225, 225));
        majorAxisColor.set (Color.rgb (240, 240, 240));
    }
    /* +------------+ */
    /* | Background | */
    /* +------------+ */

    public Color getBackground () {
        return background.get ();
    }

    public void setBackground (Color background) {
        this.background.set (background);
    }

    public ObjectProperty<Color> backgroundProperty () {
        return background;
    }

    /* +------------------+ */
    /* | MAJOR Axis Color | */
    /* +------------------+ */

    public Color getMajorAxisColor () {
        return majorAxisColor.get ();
    }

    public void setMajorAxisColor (Color majorAxisColor) {
        this.majorAxisColor.set (majorAxisColor);
    }

    public ObjectProperty<Color> majorAxisColorProperty () {
        return majorAxisColor;
    }

    /* +------------------+ */
    /* | MINOR Axis Color | */
    /* +------------------+ */

    public Color getMinorAxisColor () {
        return minorAxisColor.get ();
    }

    public void setMinorAxisColor (Color minorAxisColor) {
        this.minorAxisColor.set (minorAxisColor);
    }

    public ObjectProperty<Color> minorAxisColorProperty () {
        return minorAxisColor;
    }
}
