package titan.math;


import interfaces.given.Vector3dInterface;
import interfaces.own.FunctionInterface;

public class DefaultFunction implements FunctionInterface {
    /**
     * Function representing generic differential equation = du/dt
     *
     * @param t - differential time t
     * @param u - differential variable u
     * @return Vector representing derivative du/dt
     */
    public double call(double t, double u){ return u/t; }

    /**
     * Prime function -du/dt^2  of the function representing du/dt
     *
     * @param t - differential time t
     * @param u - differential variable u
     * @return The result of the prime function
     */
    public double primeCall(double t, double u){ return -u/Math.pow(t, 2); }
}