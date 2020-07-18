package io.github.mkmax.opticview.ui.layout;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.List;

public class OrthoStackPane extends StackPane implements IOrthoDevice {

    /* +-----------------+ */
    /* | JAVAFX CSS INFO | */
    /* +-----------------+ */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData () {
        return Region.getClassCssMetaData ();
    }

    /* +--------------------------+ */
    /* | INITIALIZATION & MEMBERS | */
    /* +--------------------------+ */
    private final OrthoMixin orthoImpl = new OrthoMixin (
        widthProperty (),
        heightProperty (),
        this::setWidth,
        this::setHeight);

    public OrthoStackPane () {
        super ();
    }

    public OrthoStackPane (Node... children) {
        super (children);
    }

    /* +----------------------------+ */
    /* | PROPERTY QUERIES & SETTERS | */
    /* +----------------------------+ */

    /* WIDTH PROPERTY */
    @Override
    public ReadOnlyDoubleProperty deviceWidthProperty () {
        return orthoImpl.deviceWidthProperty ();
    }

    @Override
    public double getDeviceWidth () {
        return orthoImpl.getDeviceWidth ();
    }

    @Override
    public void setDeviceWidth (double nWidth) {
        orthoImpl.setDeviceWidth (nWidth);
    }

    /* HEIGHT PROPERTY */
    @Override
    public ReadOnlyDoubleProperty deviceHeightProperty () {
        return orthoImpl.deviceHeightProperty ();
    }

    @Override
    public double getDeviceHeight () {
        return orthoImpl.getDeviceHeight ();
    }

    @Override
    public void setDeviceHeight (double nHeight) {
        orthoImpl.setDeviceHeight (nHeight);
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

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */

    /* HORIZONTAL REMAP LISTENERS */
    @Override
    public void registerHorizontalRemapListener (RemapListener lis) {
        orthoImpl.registerHorizontalRemapListener (lis);
    }

    @Override
    public void removeHorizontalRemapListener (RemapListener lis) {
        orthoImpl.removeHorizontalRemapListener (lis);
    }

    /* VERTICAL REMAP LISTENERS */
    @Override
    public void registerVerticalRemapListener (RemapListener lis) {
        orthoImpl.registerVerticalRemapListener (lis);
    }

    @Override
    public void removeVerticalRemapListener (RemapListener lis) {
        orthoImpl.removeVerticalRemapListener (lis);
    }

    /* WINDOW REMAP LISTENERS */
    @Override
    public void registerWindowRemapListener (RemapListener lis) {
        orthoImpl.registerWindowRemapListener (lis);
    }

    @Override
    public void removeWindowRemapListener (RemapListener lis) {
        orthoImpl.removeWindowRemapListener (lis);
    }

    /* +----------------------+ */
    /* | TRANSFORM OPERATIONS | */
    /* +----------------------+ */
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
    public void zoomHorizontal (double vx, double mult) {
        orthoImpl.zoomHorizontal (vx, mult);
    }

    @Override
    public void zoomVertical (double vy, double mult) {
        orthoImpl.zoomVertical (vy, mult);
    }

    @Override
    public void zoomWindow (double vx, double vy, double mult) {
        orthoImpl.zoomWindow (vx, vy, mult);
    }

    /* +---------+ */
    /* | BINDING | */
    /* +---------+ */
    @Override
    public void bindOrtho (IOrthoDevice to) {
        orthoImpl.bindOrtho (to);
    }

    @Override
    public void unbindOrtho () {
        orthoImpl.unbindOrtho ();
    }

    /* +-------------+ */
    /* | IDisposable | */
    /* +-------------+ */
    @Override
    public void dispose () {
        orthoImpl.dispose ();
    }
}