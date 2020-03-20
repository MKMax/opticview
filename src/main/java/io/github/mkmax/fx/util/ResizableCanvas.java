package io.github.mkmax.fx.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;

public class ResizableCanvas extends Canvas {

    private static final double DEF_MIN_WIDTH  = 128;
    private static final double DEF_MIN_HEIGHT = 128;

    private static final double DEF_WIDTH = 512;
    private static final double DEF_HEIGHT = 512;

    private static final double DEF_MAX_WIDTH  = Double.MAX_VALUE;
    private static final double DEF_MAX_HEIGHT = Double.MAX_VALUE;

    private final DoubleProperty minWidth  = new SimpleDoubleProperty ();
    private final DoubleProperty minHeight = new SimpleDoubleProperty ();

    private final DoubleProperty maxWidth  = new SimpleDoubleProperty ();
    private final DoubleProperty maxHeight = new SimpleDoubleProperty ();

    public ResizableCanvas (
        double minWidth,
        double minHeight,
        double width,
        double height,
        double maxWidth,
        double maxHeight)
    {
        super (width, height);
        this.minWidth.set (minWidth);
        this.minHeight.set (minHeight);
        this.maxWidth.set (maxWidth);
        this.maxHeight.set (maxHeight);
    }

    public ResizableCanvas (
        double width,
        double height)
    {
        this (
            DEF_MIN_WIDTH,
            DEF_MIN_HEIGHT,
            width,
            height,
            DEF_MAX_WIDTH,
            DEF_MAX_HEIGHT
        );
    }

    public ResizableCanvas (
        Dimension2D minSize,
        Dimension2D size,
        Dimension2D maxSize)
    {
        /* Assume that '*size' are not null; if they are, that's the client's problem */
        this (
            minSize.getWidth (),
            minSize.getHeight (),
            size.getWidth (),
            size.getHeight (),
            maxSize.getWidth (),
            maxSize.getHeight ()
        );
    }

    public ResizableCanvas (Dimension2D size) {
        /* Assume that 'size' is not null; if it is, that's the client's problem */
        this (
            size.getWidth (),
            size.getHeight ()
        );
    }

    public ResizableCanvas () {
        this (
            DEF_WIDTH,
            DEF_HEIGHT
        );
    }

    /* +---------------------+ */
    /* | Min Width Property  | */
    /* +---------------------+ */

    public final double getMinWidth () {
        return minWidth.get ();
    }

    public final void setMinWidth (double minWidth) {
        this.minWidth.set (minWidth);
    }

    public DoubleProperty minWidthProperty () {
        return minWidth;
    }

    /* +---------------------+ */
    /* | Min Height Property | */
    /* +---------------------+ */

    public final double getMinHeight () {
        return minHeight.get ();
    }

    public final void setMinHeight (double minHeight) {
        this.minHeight.set (minHeight);
    }

    public DoubleProperty minHeightProperty () {
        return minHeight;
    }

    /* +---------------------+ */
    /* | Max Width Property  | */
    /* +---------------------+ */

    public final double getMaxWidth () {
        return maxWidth.get ();
    }

    public final void setMaxWidth (double maxWidth) {
        this.maxWidth.set (maxWidth);
    }

    public DoubleProperty maxWidthProperty () {
        return maxWidth;
    }

    /* +---------------------+ */
    /* | Max Height Property | */
    /* +---------------------+ */

    public final double getMaxHeight () {
        return maxHeight.get ();
    }

    public final void setMaxHeight (double maxHeight) {
        this.maxHeight.set (maxHeight);
    }

    public DoubleProperty maxHeightProperty () {
        return maxHeight;
    }

    /* +------------------------------------------+ */
    /* | Node overrides to enable canvas resizing | */
    /* +------------------------------------------+ */

    @Override
    public void resize (double width, double height) {
        super.setWidth (width);
        super.setHeight (height);
    }

    @Override
    public boolean isResizable () {
        return true;
    }

    @Override
    public double minWidth (double height) {
        return minWidth.get ();
    }

    @Override
    public double minHeight (double width) {
        return minHeight.get ();
    }

    @Override
    public double prefWidth (double height) {
        return getWidth ();
    }

    @Override
    public double prefHeight (double width) {
        return getHeight ();
    }

    @Override
    public double maxWidth (double height) {
        return maxWidth.get ();
    }

    @Override
    public double maxHeight (double width) {
        return maxHeight.get ();
    }
}
