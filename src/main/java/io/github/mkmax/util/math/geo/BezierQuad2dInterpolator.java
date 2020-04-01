package io.github.mkmax.util.math.geo;

import org.joml.Vector2d;
import java.util.Objects;

public class BezierQuad2dInterpolator implements Quad2dInterpolator {

    private Quad2dc src;
    private Quad2dc dest;

    private UniBezierQuad2dInterpolator projection;
    private UniBezierQuad2dInterpolator inverse;

    public BezierQuad2dInterpolator (
        Quad2dc pSource,
        Quad2dc pDestination)
    {
        src  = Objects.requireNonNull (pSource);
        dest = Objects.requireNonNull (pDestination);

        projection = new UniBezierQuad2dInterpolator (src, dest);
        inverse    = new UniBezierQuad2dInterpolator (dest, src);
    }

    @Override
    public Quad2dc getSource () {
        return src;
    }

    @Override
    public void setSource (Quad2dc nSource) {
        src = Objects.requireNonNull (nSource);
        update ();
    }

    @Override
    public Quad2dc getDestination () {
        return dest;
    }

    @Override
    public void setDestination (Quad2dc nDestination) {
        dest = Objects.requireNonNull (nDestination);
        update ();
    }

    @Override
    public void set (Quad2dc nSource, Quad2dc nDestination) {
        src  = Objects.requireNonNull (nSource);
        dest = Objects.requireNonNull (nDestination);
        update ();
    }

    @Override
    public Vector2d unmap (Vector2d dest) {
        return inverse.map (dest);
    }

    @Override
    public Vector2d unmap (Vector2d dest, Vector2d src) {
        return inverse.map (dest, src);
    }

    @Override
    public Vector2d map (Vector2d src) {
        return projection.map (src);
    }

    @Override
    public Vector2d map (Vector2d src, Vector2d dest) {
        return projection.map (src, dest);
    }

    private void update () {
        projection.set (src, dest);
        inverse.set (dest, src);
    }
}
