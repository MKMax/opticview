package io.github.mkmax.util.math;

import org.joml.Matrix3d;
import org.joml.Matrix3dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class Quad6d {

    private final Vector3d qa; // lower-left
    private final Vector3d qb; // lower-right
    private final Vector3d qc; // upper-right

    private final Matrix3d tb = new Matrix3d (); // transform buffer

    public Quad6d (
        final Vector3dc pQa,
        final Vector3dc pQb,
        final Vector3dc pQc)
    {
        qa = new Vector3d (pQa);
        qb = new Vector3d (pQb);
        qc = new Vector3d (pQc);
    }

    public Quad6d () {
        qa = new Vector3d (-1d, -1d, 1d);
        qb = new Vector3d ( 1d, -1d, 1d);
        qc = new Vector3d ( 1d,  1d, 1d);
    }

    public Quad6d transform (Matrix3dc mat) {
        return transform (mat, this);
    }

    public Quad6d transform (Matrix3dc mat, Quad6d dest) {
        mat.transform (qa, dest.qa);
        mat.transform (qb, dest.qb);
        mat.transform (qc, dest.qc);
        return dest;
    }

    public Vector3d getCenter () {
        return getCenter (new Vector3d ());
    }

    public Vector3d getCenter (Vector3d dest) {
        return qc.add (
            0.5d * (qa.x - qc.x),
            0.5d * (qa.y - qc.y),
            0d,
            dest
        );
    }

    public double getAngle () {
        return Math.atan2 (qb.y - qa.y, qb.x - qa.x);
    }

    public double getWidth () {
        return qb.distance (qa);
    }

    public double getHeight () {
        return qb.distance (qc);
    }

    public Matrix3d map (Quad6d to) {
        // to.qa = D
        // to.qb = E
        // to.qc = F

        tb.setColumn (0, qa);
        tb.setColumn (0, qb);
        tb.setColumn (0, qc);

        final double detul = Matrix3ext.;

        final Vector3d V =
    }

}
