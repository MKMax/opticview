package io.github.mkmax.util.math;

import org.joml.Matrix3d;
import org.joml.Matrix3dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class Quad6d {

    private final Vector3d qa; // lower-left
    private final Vector3d qb; // lower-right
    private final Vector3d qc; // upper-right

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
        return map (to, new Matrix3d ());
    }

    public Matrix3d map (Quad6d to, Matrix3d dest) {
        // this.qa = A
        // this.qb = B
        // this.qc = C

        // to.qa = D
        // to.qb = E
        // to.qc = F

        final double detAxyBxy = qa.x * qb.y - qb.x * qa.y;
        final double detAxzBxz = qa.x * qb.z - qb.x * qa.z;
        final double detBxyCxy = qb.x * qc.y - qc.x * qb.y;
        final double detBxzCxz = qb.x * qc.z - qc.x * qb.z;

        /* TUV is analogous to XYZ and are the vectors of the transformation matrix */
        final Vector3d T = new Vector3d (1d, 0d, 0d);
        final Vector3d U = new Vector3d (0d, 1d, 0d);
        final Vector3d V = new Vector3d (0d, 0d, 1d);

        final double Tdiv = qa.x;
        final double Udiv = detAxyBxy;
        final double Vdiv = detBxzCxz * detAxyBxy - detBxyCxy * detAxzBxz;

        /* The reason we can completely ignore the computation for
         * some vectors if their "Xdiv" == 0 is because this is actually
         * a linear equation we've solved and it turns out the if the
         * coefficient == 0, then we know that it is a "free variable" (or
         * vector in our case) and we can just use the respective unit
         * vector.
         */

        if (!FloatingPoint.strictEq (Vdiv, 0d)) {
            V.x = (to.qc.x * qb.x - to.qb.x * qc.x) * detAxyBxy - (to.qb.x * qa.x - to.qa.x * qb.x) * detBxyCxy;
            V.y = (to.qc.y * qb.x - to.qb.y * qc.x) * detAxyBxy - (to.qb.y * qa.x - to.qa.y * qb.x) * detBxyCxy;
            V.z = (to.qc.z * qb.x - to.qb.z * qc.x) * detAxyBxy - (to.qb.z * qa.x - to.qa.z * qb.x) * detBxyCxy;
            V.div (Vdiv);
        }

        if (!FloatingPoint.strictEq (Udiv, 0d)) {
            U.x = to.qb.x * qa.x - to.qa.x * qb.x - detAxzBxz * V.x;
            U.y = to.qb.y * qa.x - to.qa.y * qb.x - detAxzBxz * V.y;
            U.z = to.qb.z * qa.x - to.qa.z * qb.x - detAxzBxz * V.z;
            U.div (Udiv);
        }

        if (!FloatingPoint.strictEq (Tdiv, 0d)) {
            T.x = to.qa.x - U.x * qa.y - V.x - qa.z;
            T.y = to.qa.y - U.y * qa.y - V.y - qa.z;
            T.z = to.qa.z - U.z * qa.y - V.z - qa.z;
            T.div (Tdiv);
        }

        return dest.set (T, U, V);
    }

}
