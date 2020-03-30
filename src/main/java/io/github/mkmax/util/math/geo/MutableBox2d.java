package io.github.mkmax.util.math.geo;

public class MutableBox2d implements ReadBox2d, WriteBox2d {

    private double left, right, bottom, top;

    public MutableBox2d () {
        /* Zero box */
    }

    public MutableBox2d (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        left   = pLeft;
        right  = pRight;
        bottom = pBottom;
        top    = pTop;
    }

    @Override
    public double getLeft () {
        return left;
    }

    @Override
    public double getRight () {
        return right;
    }

    @Override
    public double getBottom () {
        return bottom;
    }

    @Override
    public double getTop () {
        return top;
    }

    @Override
    public void setLeft (double nLeft) {
        left = nLeft;
    }

    @Override
    public void setRight (double nRight) {
        right = nRight;
    }

    @Override
    public void setBottom (double nBottom) {
        bottom = nBottom;
    }

    @Override
    public void setTop (double nTop) {
        top = nTop;
    }

    @Override
    public void set (
        double pLeft,
        double pRight,
        double pBottom,
        double pTop)
    {
        left   = pLeft;
        right  = pRight;
        bottom = pBottom;
        top    = pTop;
    }
}
