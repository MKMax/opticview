package io.github.mkmax.fx.math.cartesian;

public class CartesianAxisPoint {

    public final double windowSpace;
    public final double viewportSpace;

    public CartesianAxisPoint (
        double pWindowSpace,
        double pViewportSpace)
    {
        windowSpace   = pWindowSpace;
        viewportSpace = pViewportSpace;
    }

}
