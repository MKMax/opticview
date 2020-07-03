package io.github.mkmax.opticview.ui.layout;

import javafx.beans.property.ReadOnlyDoubleProperty;

/* component is an interface to allow extension of abstract/concrete classes */
public interface OrthoComponent {

    /* +---------------------+ */
    /* | LISTENER INTERFACES | */
    /* +---------------------+ */

    @FunctionalInterface
    public interface HorizontalRemapListener {
        void onRemap (OrthoComponent oc);
    }

    @FunctionalInterface
    public interface VerticalRemapListener {
        void onRemap (OrthoComponent oc);
    }

    @FunctionalInterface
    public interface TotalRemapListener {
        void onRemap (OrthoComponent oc);
    }

    /* +----------------------------+ */
    /* | PROPERTY QUERIES & SETTERS | */
    /* +----------------------------+ */

    /* WIDTH ("OC" suffix to avoid collision with JavaFX widthProperty()... */
    ReadOnlyDoubleProperty widthPropertyOC  ();
    double                 getWidthOC       ();
    void                   setWidthOC       (double nWidth);

    /* HEIGHT ("OC" suffix to avoid collision with JavaFX heightProperty()... */
    ReadOnlyDoubleProperty heightPropertyOC ();
    double                 getHeightOC      ();
    void                   setHeightOC      (double nHeight);

    /* LEFT (minimum X/Horizontal coordinate) */
    ReadOnlyDoubleProperty leftProperty     ();
    double                 getLeft          ();
    void                   setLeft          (double nLeft);

    /* RIGHT (maximum X/Horizontal coordinate) */
    ReadOnlyDoubleProperty rightProperty    ();
    double                 getRight         ();
    void                   setRight         (double nRight);

    /* BOTTOM (minimum Y/Vertical coordinate) */
    ReadOnlyDoubleProperty bottomProperty   ();
    double                 getBottom        ();
    void                   setBottom        (double nBottom);

    /* TOP (maximum Y/Vertical coordinate) */
    ReadOnlyDoubleProperty topProperty      ();
    double                 getTop           ();
    void                   setTop           (double nTop);

    /* FUSED VERTICAL & HORIZONTAL SETTER */
    void                   setHorizontal    (double nLeft, double nRight);
    void                   setVertical      (double nBottom, double nTop);

    /* FUSED WINDOW SETTER */
    void                   setWindow        (double nLeft, double nRight, double nBottom, double nTop);

    /* +---------------------+ */
    /* | LISTENER MANAGEMENT | */
    /* +---------------------+ */

    /* HORIZONTAL LISTENERS */
    void addRemapListener    (HorizontalRemapListener lis);
    void removeRemapListener (HorizontalRemapListener lis);

    /* VERTICAL LISTENERS */
    void addRemapListener    (VerticalRemapListener lis);
    void removeRemapListener (VerticalRemapListener lis);

    /* TOTAL LISTENERS */
    void addRemapListener    (TotalRemapListener lis);
    void removeRemapListener (TotalRemapListener lis);

    /* +--------------------+ */
    /* | MAPPING OPERATIONS | */
    /* +--------------------+ */

    double mapToComponentX (double x);
    double mapToComponentY (double y);

    double mapToVirtualX (double x);
    double mapToVirtualY (double y);

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
