package interfaces.own;

import interfaces.given.Vector3dInterface;

/**
 * Structure that represents a function f(x)
 */
public interface FunctionInterface {
    /**
     * Calls the specified body within f(x) | i.e. calculates the mapping/result given the function and initial vector
     *
     * @param t - differential time t
     * @param u - differential variable u
     * @return Vector representing derivative du/dt
     */
    public double call(double t, double u);


    /**
     * Calls the specified body within f'(x) | i.e. calculates the mapping/result given the prime function and initial vector
     *
     * @param t - differential time t
     * @param u - differential variable u
     * @return The result of the prime function
     */
    public double primeCall(double t, double u);
}
