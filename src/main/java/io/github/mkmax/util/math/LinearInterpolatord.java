package io.github.mkmax.util.math;

import org.joml.Rectangled;

public class LinearInterpolatord implements LinearInterpolatordc {

    public static LinearInterpolatord mapX (
        Rectangled from,
        Rectangled to)
    {
        return new LinearInterpolatord (
            from.minX, to.minX,
            from.maxX, to.maxX
        );
    }

    public static LinearInterpolatord redefineX (
        LinearInterpolatord of,
        Rectangled          from,
        Rectangled          to)
    {
        return of.redefine (
            from.minX, to.minX,
            from.maxX, to.maxX
        );
    }

    public static LinearInterpolatord mapY (
        Rectangled from,
        Rectangled to)
    {
        return new LinearInterpolatord (
            from.minY, to.minY,
            from.maxY, to.maxY
        );
    }

    public static LinearInterpolatord redefineY (
        LinearInterpolatord of,
        Rectangled          from,
        Rectangled          to)
    {
        return of.redefine (
            from.minY, to.minY,
            from.maxY, to.maxY
        );
    }

    private double M;
    private double iM;
    private double B;

    public LinearInterpolatord (
        double Pa, double Qa,
        double Pb, double Qb)
    {
        redefine (
            Pa, Qa,
            Pb, Qb
        );
    }

    public LinearInterpolatord () {
        redefine (
            -1d, -1d,
             1d,  1d
        );
    }

    @Override
    public double map (double p) {
        return M * p + B;
    }

    @Override
    public double unmap (double q) {
        return (q - B) * iM;
    }

    public LinearInterpolatord redefine (
        double Pa, double Qa,
        double Pb, double Qb)
    {
        M  = (Qb - Qa) / (Pb - Pa);
        iM = 1d / M;
        B  = Qa - M * Pa;
        return this;
    }

}
