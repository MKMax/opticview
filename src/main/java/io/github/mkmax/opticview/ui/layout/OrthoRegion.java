package io.github.mkmax.opticview.ui.layout;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.layout.Region;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.List;

public class OrthoRegion extends Region implements IOrthoDevice {

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
        (w) -> setWidth (w.doubleValue ()),
        (h) -> setHeight (h.doubleValue ()));

    public OrthoRegion () {
        super ();
    }

    /* +------------+ */
    /* | DIMENSIONS | */
    /* +------------+ */
    @Override
    public ReadOnlyObjectProperty<? extends Number> deviceWidthProperty () {
        return orthoImpl.deviceWidthProperty ();
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> deviceHeightProperty () {
        return orthoImpl.deviceHeightProperty ();
    }

    @Override
    public Number getDeviceWidth () {
        return orthoImpl.getDeviceWidth ();
    }

    @Override
    public void setDeviceWidth (Number nWidth) {
        orthoImpl.setDeviceWidth (nWidth);
    }

    @Override
    public Number getDeviceHeight () {
        return orthoImpl.getDeviceHeight ();
    }

    @Override
    public void setDeviceHeight (Number nHeight) {
        orthoImpl.setDeviceHeight (nHeight);
    }

    /* +--------+ */
    /* | WINDOW | */
    /* +--------+ */
    @Override
    public ReadOnlyObjectProperty<? extends Number> leftProperty () {
        return orthoImpl.leftProperty ();
    }

    @Override
    public Number getLeft () {
        return orthoImpl.getLeft ();
    }

    @Override
    public void setLeft (Number nLeft) {
        orthoImpl.setLeft (nLeft);
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> rightProperty () {
        return orthoImpl.rightProperty ();
    }

    @Override
    public Number getRight () {
        return orthoImpl.getRight ();
    }

    @Override
    public void setRight (Number nRight) {
        orthoImpl.setRight (nRight);
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> bottomProperty () {
        return orthoImpl.bottomProperty ();
    }

    @Override
    public Number getBottom () {
        return orthoImpl.getBottom ();
    }

    @Override
    public void setBottom (Number nBottom) {
        orthoImpl.setBottom (nBottom);
    }

    @Override
    public ReadOnlyObjectProperty<? extends Number> topProperty () {
        return orthoImpl.topProperty ();
    }

    @Override
    public Number getTop () {
        return orthoImpl.getTop ();
    }

    @Override
    public void setTop (Number nTop) {
        orthoImpl.setTop (nTop);
    }

    /* +----------------------+ */
    /* | WINDOW FUSED SETTERS | */
    /* +----------------------+ */
    @Override
    public void setHorizontal (Number nLeft, Number nRight) {
        orthoImpl.setHorizontal (nLeft, nRight);
    }

    @Override
    public void setVertical (Number nBottom, Number nTop) {
        orthoImpl.setVertical (nBottom, nTop);
    }

    @Override
    public void setWindow (Number nLeft, Number nRight, Number nBottom, Number nTop) {
        orthoImpl.setWindow (nLeft, nRight, nBottom, nTop);
    }

    /* +-----------------------+ */
    /* | OTHER MAPPING QUERIES | */
    /* +-----------------------+ */

    /* Math.abs(right - left) and Math.abs(top - bottom) */
    @Override
    public Number getHorizontalSpan () {
        return orthoImpl.getHorizontalSpan ();
    }

    @Override
    public Number getVerticalSpan () {
        return orthoImpl.getVerticalSpan ();
    }

    /* getHorizontalSpan() / 2 and getVerticalSpan() / 2 */
    @Override
    public Number getHorizontalZoom () {
        return orthoImpl.getHorizontalZoom ();
    }

    @Override
    public Number getVerticalZoom () {
        return orthoImpl.getVerticalZoom ();
    }

    /* +----------+ */
    /* | LISTENER | */
    /* +----------+ */

    /* HORIZONTAL */
    @Override
    public void registerHorizontalRemapListener (IRemapListener lis) {
        orthoImpl.registerHorizontalRemapListener (lis);
    }

    @Override
    public void removeHorizontalRemapListener (IRemapListener lis) {
        orthoImpl.removeHorizontalRemapListener (lis);
    }

    /* VERTICAL */
    @Override
    public void registerVerticalRemapListener (IRemapListener lis) {
        orthoImpl.registerVerticalRemapListener (lis);
    }

    @Override
    public void removeVerticalRemapListener (IRemapListener lis) {
        orthoImpl.removeVerticalRemapListener (lis);
    }

    /* WINDOW */
    @Override
    public void registerWindowRemapListener (IRemapListener lis) {
        orthoImpl.registerWindowRemapListener (lis);
    }

    @Override
    public void removeWindowRemapListener (IRemapListener lis) {
        orthoImpl.removeWindowRemapListener (lis);
    }

    /* +---------+ */
    /* | MAPPING | */
    /* +---------+ */
    @Override
    public Number mapToDeviceX (Number virtualX) {
        return orthoImpl.mapToDeviceX (virtualX);
    }

    @Override
    public Number mapToDeviceY (Number virtualY) {
        return orthoImpl.mapToDeviceY (virtualY);
    }

    @Override
    public Number mapToVirtualX (Number devX) {
        return orthoImpl.mapToVirtualX (devX);
    }

    @Override
    public Number mapToVirtualY (Number devY) {
        return orthoImpl.mapToVirtualY (devY);
    }

    /* +---------+ */
    /* | BINDING | */
    /* +---------+ */
    @Override
    public void bindOrtho (IOrthoDevice dev) {
        orthoImpl.bindOrtho (dev);
    }

    @Override
    public void unbindOrtho () {
        orthoImpl.unbindOrtho ();
    }

    /* +------------+ */
    /* | DISPOSABLE | */
    /* +------------+ */
    @Override
    public void dispose () {
        orthoImpl.dispose ();
    }
}