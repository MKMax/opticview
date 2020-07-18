package io.github.mkmax.opticview.ui.layout;

import io.github.mkmax.opticview.util.IDisposable;

import javafx.beans.property.ReadOnlyDoubleProperty;

/* component is an interface to allow extension of abstract/concrete classes */
public interface IOrthoDevice extends IDisposable {

    /* +---------------------+ */
    /* | LISTENER INTERFACES | */
    /* +---------------------+ */
    @FunctionalInterface
    interface RemapListener {
        void onRemap (
            IOrthoDevice oc,
            boolean isWidth,  double nWidth,
            boolean isHeight, double nHeight,
            boolean isLeft,   double nLeft,
            boolean isRight,  double nRight,
            boolean isBottom, double nBottom,
            boolean isTop,    double nTop);
    }

    /* +----------------------------+ */
    /* | PROPERTY QUERIES & SETTERS | */
    /* +----------------------------+ */

    /* WIDTH ("OC" suffix to avoid collision with JavaFX widthProperty()... */
    ReadOnlyDoubleProperty deviceWidthProperty ();
    double                 getDeviceWidth      ();
    void                   setDeviceWidth      (double nWidth);

    /* HEIGHT ("OC" suffix to avoid collision with JavaFX heightProperty()... */
    ReadOnlyDoubleProperty deviceHeightProperty ();
    double                 getDeviceHeight      ();
    void                   setDeviceHeight      (double nHeight);

    /* LEFT (minimum X/Horizontal coordinate) */
    ReadOnlyDoubleProperty leftProperty         ();
    double                 getLeft              ();
    void                   setLeft              (double nLeft);

    /* RIGHT (maximum X/Horizontal coordinate) */
    ReadOnlyDoubleProperty rightProperty        ();
    double                 getRight             ();
    void                   setRight             (double nRight);

    /* BOTTOM (minimum Y/Vertical coordinate) */
    ReadOnlyDoubleProperty bottomProperty       ();
    double                 getBottom            ();
    void                   setBottom            (double nBottom);

    /* TOP (maximum Y/Vertical coordinate) */
    ReadOnlyDoubleProperty topProperty          ();
    double                 getTop               ();
    void                   setTop               (double nTop);

    /* FUSED VERTICAL & HORIZONTAL SETTER */
    void                   setHorizontal        (double nLeft, double nRight);
    void                   setVertical          (double nBottom, double nTop);

    /* FUSED WINDOW SETTER */
    void                   setWindow            (double nLeft, double nRight, double nBottom, double nTop);

    /* +---------------------+ */
    /* | LISTENER MANAGEMENT | */
    /* +---------------------+ */

    /* HORIZONTAL LISTENERS (fired if and only if the horizontal mapping solely changes) */
    void registerHorizontalRemapListener (RemapListener lis);
    void removeHorizontalRemapListener   (RemapListener lis);

    /* VERTICAL LISTENERS (fired if an only if the vertical mapping solely changes) */
    void registerVerticalRemapListener   (RemapListener lis);
    void removeVerticalRemapListener     (RemapListener lis);

    /* WINDOW LISTENERS (fired if and only if both the horizontal and vertical mappings both change) */
    void registerWindowRemapListener     (RemapListener lis);
    void removeWindowRemapListener       (RemapListener lis);

    /* +----------------------+ */
    /* | TRANSFORM OPERATIONS | */
    /* +----------------------+ */
    double mapToComponentX (double x);
    double mapToComponentY (double y);

    double mapToVirtualX (double x);
    double mapToVirtualY (double y);

    void zoomHorizontal (double vx, double mult);
    void zoomVertical   (double vy, double mult);
    void zoomWindow     (double vx, double vy, double mult);

    /* +---------+ */
    /* | BINDING | */
    /* +---------+ */
    void bindOrtho (IOrthoDevice to);
    void unbindOrtho ();

    /* +-----------------------+ */
    /* | DEPRECATED & DEFAULTS | */
    /* +-----------------------+ */

    @Deprecated
    default double mapx (double x) {
        return mapToComponentX (x);
    }

    @Deprecated
    default double mapy (double y) {
        return mapToComponentY (y);
    }

    @Deprecated
    default double unmapx (double x) {
        return mapToVirtualX (x);
    }

    @Deprecated
    default double unmapy (double y) {
        return mapToVirtualY (y);
    }
}
