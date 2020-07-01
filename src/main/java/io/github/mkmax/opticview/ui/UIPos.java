package io.github.mkmax.opticview.ui;

public enum UIPos {
    TOP_LEFT      (-1.0d, -1.0d),
    TOP_CENTER    (-0.5d, -1.0d),
    TOP_RIGHT     ( 0.0d, -1.0d),
    CENTER_LEFT   (-1.0d, -0.5d),
    CENTER_CENTER (-0.5d, -0.5d),
    CENTER_RIGHT  ( 0.0d, -0.5d),
    BOTTOM_LEFT   (-1.0d,  0.0d),
    BOTTOM_CENTER (-0.5d,  0.0d),
    BOTTOM_RIGHT  ( 0.0d,  0.0d);

    public final double XMUL;
    public final double YMUL;

    UIPos (double pxmul, double pymul) {
        XMUL = pxmul;
        YMUL = pymul;
    }
}