package io.github.mkmax.opticview.material;

import java.util.Objects;

public enum Glass {

    /* +-------------+ */
    /* | GLASS TYPES | */
    /* +-------------+ */
    UVFS ("Fused Silica") {
        @Override
        public double calcRefractiveIndex (double lambda) {
            final double lambdaSq = lambda * lambda;
            final double
                K1 = 0.6961663d,
                K2 = 0.00467914825849d,
                K3 = 0.4079426d,
                K4 = 0.013512063073959999d,
                K5 = 0.8974794d,
                K6 = 97.93400253792099d;
            final double
                T1 = 1d,
                T2 = (K1 * lambdaSq) / (lambdaSq - K2),
                T3 = (K3 * lambdaSq) / (lambdaSq - K4),
                T4 = (K5 * lambdaSq) / (lambdaSq - K6);
            return Math.sqrt (T1 + T2 + T3 + T4);
        }
    },

    N_BK7 ("N-BK7") {
        @Override
        public double calcRefractiveIndex (double lambda) {
            final double lambdaSq = lambda * lambda;
            final double
                K1 = 1.03961212d,
                K2 = 0.00600069867d,
                K3 = 0.231792344d,
                K4 = 0.0200179144d,
                K5 = 1.01046945d,
                K6 = 103.560653d;
            final double
                T1 = 1d,
                T2 = (K1 * lambdaSq) / (lambdaSq - K2),
                T3 = (K3 * lambdaSq) / (lambdaSq - K4),
                T4 = (K5 * lambdaSq) / (lambdaSq - K6);
            return Math.sqrt (T1 + T2 + T3 + T4);
        }
    },

    CaF2 ("CaF2") {
        @Override
        public double calcRefractiveIndex (double lambda) {
            final double lambdaSq = lambda * lambda;
            final double
                K1 = 0.5675888d,
                K2 = 0.002526429987596025d,
                K3 = 0.4710914d,
                K4 = 0.010078332802810001d,
                K5 = 3.8484723d,
                K6 = 1200.5559729216d;
            final double
                T1 = 1d,
                T2 = (K1 * lambdaSq) / (lambdaSq - K2),
                T3 = (K3 * lambdaSq) / (lambdaSq - K4),
                T4 = (K5 * lambdaSq) / (lambdaSq - K6);
            return Math.sqrt (T1 + T2 + T3 + T4);
        }
    },

    ZnSe ("ZnSe") {
        @Override
        public double calcRefractiveIndex (double lambda) {
            final double lambdaSq = lambda * lambda;
            final double
                K1 = 4d,
                K2 = 1.9d,
                K3 = 0.113d;
            return Math.sqrt (K1 + K2 * lambdaSq / (lambdaSq - K3));
        }
    };

    /* +------------------+ */
    /* | GLASS DEFINITION | */
    /* +------------------+ */
    public final String NAME;

    Glass (String pName) {
        NAME = Objects.requireNonNull (pName, "The name of a glass is required");
    }

    /**
     * Compute the refractive index <i>n</i> given some wavelength
     * in micro-meters.
     *
     * @param lambda The wavelength in micro-meters.
     */
    public abstract double calcRefractiveIndex (double lambda);
}
