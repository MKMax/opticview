package io.github.mkmax.opticview.ui.layout;

import io.github.mkmax.opticview.util.Disposable;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.util.List;

public class OrthoPane extends Pane implements OrthoComponent, Disposable {

    /* +-----------------+ */
    /* | JAVAFX CSS INFO | */
    /* +-----------------+ */

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
        return Region.getClassCssMetaData ();
    }

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */

    private final OrthoTrait orthoImpl = new OrthoTrait (
        widthProperty (),
        heightProperty (),
        this::setWidth,
        this::setHeight);

    public OrthoPane () {
        super ();
    }

    public OrthoPane (Node... children) {
        super (children);
    }

    /* +----------------------------+ */
    /* | PROPERTY QUERIES & SETTERS | */
    /* +----------------------------+ */

    /* WIDTH PROPERTY */
    @Override
    public ReadOnlyDoubleProperty widthPropertyOC () {
        return orthoImpl.widthPropertyOC ();
    }

    @Override
    public double getWidthOC () {
        return orthoImpl.getWidthOC ();
    }

    @Override
    public void setWidthOC (double nWidth) {
        orthoImpl.setWidthOC (nWidth);
    }

    /* HEIGHT PROPERTY */
    @Override
    public ReadOnlyDoubleProperty heightPropertyOC () {
        return orthoImpl.heightPropertyOC ();
    }

    @Override
    public double getHeightOC () {
        return orthoImpl.getHeightOC ();
    }

    @Override
    public void setHeightOC (double nHeight) {
        orthoImpl.setHeightOC (nHeight);
    }

    /* LEFT PROPERTY */
    @Override
    public ReadOnlyDoubleProperty leftProperty () {
        return orthoImpl.leftProperty ();
    }

    @Override
    public double getLeft () {
        return orthoImpl.getLeft ();
    }

    @Override
    public void setLeft (double nLeft) {
        orthoImpl.setLeft (nLeft);
    }

    /* RIGHT PROPERTY */
    @Override
    public ReadOnlyDoubleProperty rightProperty () {
        return orthoImpl.rightProperty ();
    }

    @Override
    public double getRight () {
        return orthoImpl.getRight ();
    }

    @Override
    public void setRight (double nRight) {
        orthoImpl.setRight (nRight);
    }

    /* BOTTOM PROPERTY */
    @Override
    public ReadOnlyDoubleProperty bottomProperty () {
        return orthoImpl.bottomProperty ();
    }

    @Override
    public double getBottom () {
        return orthoImpl.getBottom ();
    }

    @Override
    public void setBottom (double nBottom) {
        orthoImpl.setBottom (nBottom);
    }

    /* TOP PROPERTY */
    @Override
    public ReadOnlyDoubleProperty topProperty () {
        return orthoImpl.topProperty ();
    }

    @Override
    public double getTop () {
        return orthoImpl.getTop ();
    }

    @Override
    public void setTop (double nTop) {
        orthoImpl.setTop (nTop);
    }

    /* FUSED HORIZONTAL & VERTICAL SETTERS */
    @Override
    public void setHorizontal (double nLeft, double nRight) {
        orthoImpl.setHorizontal (nLeft, nRight);
    }

    @Override
    public void setVertical (double nBottom, double nTop) {
        orthoImpl.setVertical (nBottom, nTop);
    }

    /* FUSED WINDOW SETTER */
    @Override
    public void setWindow (double nLeft, double nRight, double nBottom, double nTop) {
        orthoImpl.setWindow (nLeft, nRight, nBottom, nTop);
    }

    /* +---------------------+ */
    /* | LISTENER MANAGEMENT | */
    /* +---------------------+ */

    /* HORIZONTAL REMAP LISTENER */
    @Override
    public void addRemapListener (HorizontalRemapListener lis) {
        orthoImpl.addRemapListener (lis);
    }

    @Override
    public void removeRemapListener (HorizontalRemapListener lis) {
        orthoImpl.removeRemapListener (lis);
    }

    /* VERTICAL REMAP LISTENER */
    @Override
    public void addRemapListener (VerticalRemapListener lis) {
        orthoImpl.addRemapListener (lis);
    }

    @Override
    public void removeRemapListener (VerticalRemapListener lis) {
        orthoImpl.removeRemapListener (lis);
    }

    /* TOTAL REMAP LISTENER */
    @Override
    public void addRemapListener (TotalRemapListener lis) {
        orthoImpl.addRemapListener (lis);
    }

    @Override
    public void removeRemapListener (TotalRemapListener lis) {
        orthoImpl.removeRemapListener (lis);
    }

    /* +--------------------+ */
    /* | MAPPING OPERATIONS | */
    /* +--------------------+ */

    @Override
    public double mapToComponentX (double x) {
        return orthoImpl.mapToComponentX (x);
    }

    @Override
    public double mapToComponentY (double y) {
        return orthoImpl.mapToComponentY (y);
    }

    @Override
    public double mapToVirtualX (double x) {
        return orthoImpl.mapToVirtualX (x);
    }

    @Override
    public double mapToVirtualY (double y) {
        return orthoImpl.mapToVirtualY (y);
    }

    @Override
    public void dispose () {
        orthoImpl.dispose ();
    }
}