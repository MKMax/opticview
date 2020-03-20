package io.github.mkmax.mathui;

public interface Function {

    double eval (double x);

    double[] getVerticalAsymptotes ();

    double[] getHorizontalAsymptotes ();

    Function getDerivative ();

    Function getIntegral ();

}
