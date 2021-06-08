package interfaces.own;

import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.own.DataInterface;
import interfaces.given.Vector3dInterface;

public interface NewtonInterface {
    /**
     * Gravitational constant G
     * Current value : 6.67430 * 10^-11
     */
    public static double G = 6.67430 * Math.pow(10, -11);

    /**
     * Calculates the gravity between 2 data objects whose position and mass is known
     *
     * @param victim - the data object that experiences a gravitational influence
     * @param exhibitor - the data object that exhibits a gravitational influence
     * @return The gravity applied on body 1 whose position and mass are both the 1st index in the arrays
     */
    public Vector3dInterface gravity(DataInterface victim, DataInterface exhibitor);

    /**
     * Calculates the net gravity applied on a body in the presence of multiple bodies
     *
     * @param environment - a data array representing all data objects currently in the simulation
     * @param i - the index of the data object for which we want to calculate the net gravity for
     * @return The net gravity which is the result of the summation of all gravities applying on the target object at index i in the environment
     */
    public Vector3dInterface netGravity(DataInterface[] environment, int i);

    /**
     * Calculates the acceleration after having calculated the net gravity F[net]
     *
     * @param environment - a data array representing all data objects currently in the simulation
     * @param i - the index of the data object for which we want to calculate the net gravity for
     * @return The acceleration obtained by dividing (calculated) net gravity with the target object's mass
     */
    public Vector3dInterface acceleration(DataInterface[] environment, int i);


    /**
     * Calculates the dy/dt influenced by the net gravity for each object in the state
     *
     * @param y - state to calculate the influence for
     * @param t - differential time
     * @return Array containing dy/dt for each object due to gravitational influence
     */
    public RateInterface getRoc(StateInterface y, double t);
}
