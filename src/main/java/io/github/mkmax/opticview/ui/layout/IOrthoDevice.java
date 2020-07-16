package io.github.mkmax.opticview.ui.layout;

import io.github.mkmax.opticview.util.IDisposable;

import javafx.beans.property.ReadOnlyObjectProperty;

/* component is an interface to allow extension of abstract/concrete classes */
public interface IOrthoDevice extends IDisposable {

    @FunctionalInterface
    interface IRemapListener {
        void onRemap (IOrthoDevice source);
    }

    /* +------------+ */
    /* | DIMENSIONS | */
    /* +------------+ */
    ReadOnlyObjectProperty<? extends Number> deviceWidthProperty  ();
    ReadOnlyObjectProperty<? extends Number> deviceHeightProperty ();

    Number getDeviceWidth  ();
    void   setDeviceWidth  (Number nWidth);

    Number getDeviceHeight ();
    void   setDeviceHeight (Number nHeight);

    /* +--------+ */
    /* | WINDOW | */
    /* +--------+ */
    ReadOnlyObjectProperty<? extends Number> leftProperty ();
    Number getLeft   ();
    void   setLeft   (Number nLeft);

    ReadOnlyObjectProperty<? extends Number> rightProperty ();
    Number getRight  ();
    void   setRight  (Number nRight);

    ReadOnlyObjectProperty<? extends Number> bottomProperty ();
    Number getBottom ();
    void   setBottom (Number nBottom);

    ReadOnlyObjectProperty<? extends Number> topProperty ();
    Number getTop    ();
    void   setTop    (Number nTop);

    /* +----------------------+ */
    /* | WINDOW FUSED SETTERS | */
    /* +----------------------+ */
    void setHorizontal (Number nLeft,
                        Number nRight);

    void setVertical   (Number nBottom,
                        Number nTop);

    void setWindow     (Number nLeft,
                        Number nRight,
                        Number nBottom,
                        Number nTop);

    /* +-----------------------+ */
    /* | OTHER MAPPING QUERIES | */
    /* +-----------------------+ */

    /* Math.abs(right - left) and Math.abs(top - bottom) */
    Number getHorizontalSpan ();
    Number getVerticalSpan   ();

    /* getHorizontalSpan() / 2 and getVerticalSpan() / 2 */
    Number getHorizontalZoom ();
    Number getVerticalZoom   ();

    /* +----------+ */
    /* | LISTENER | */
    /* +----------+ */
    void registerHorizontalRemapListener (IRemapListener lis);
    void removeHorizontalRemapListener   (IRemapListener lis);

    void registerVerticalRemapListener   (IRemapListener lis);
    void removeVerticalRemapListener     (IRemapListener lis);

    void registerWindowRemapListener     (IRemapListener lis);
    void removeWindowRemapListener       (IRemapListener lis);

    /* +---------+ */
    /* | MAPPING | */
    /* +---------+ */
    Number mapToDeviceX (Number virtualX);
    Number mapToDeviceY (Number virtualY);

    Number mapToVirtualX (Number devX);
    Number mapToVirtualY (Number devY);

    /* +---------+ */
    /* | BINDING | */
    /* +---------+ */
    void bindOrtho   (IOrthoDevice dev);
    void unbindOrtho ();
}
