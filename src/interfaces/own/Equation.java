package interfaces.own;

import interfaces.given.Vector3dInterface;

/**
 * Structure that represents a differential equation
 */
public interface Equation {
    /**
     * Derives the vector according to whatever equation you have specified in the class implementing this.
     *
     * @param t - the differential time
     * @param u - the vector that will be derived
     * @return The result of the derivation through the equation
     */
    public Vector3dInterface derive(double t, Vector3dInterface u);
}
