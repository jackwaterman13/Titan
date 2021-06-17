package interfaces.own;

import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.own.DataInterface;
import interfaces.given.Vector3dInterface;
import titan.math.Vector3d;
import titan.physics.State;
import titan.utility.Rate;

public interface NewtonInterface {
    /**
     * Gravitational constant G
     * Current value : 6.67430 * 10^-11
     */
    double G = 6.67430e-11;

    /**
     * Calculates the gravity between 2 data objects whose position and mass is known
     *
     * @param victim - the data object that experiences a gravitational influence
     * @param exhibitor - the data object that exhibits a gravitational influence
     * @return The gravity applied on body 1 whose position and mass are both the 1st index in the arrays
     */
    Vector3dInterface gravity(DataInterface victim, DataInterface exhibitor);

    /**
     * Calculates the net gravity applied on a body in the presence of multiple bodies
     *
     * @param environment - a data array representing all data objects currently in the simulation
     * @param i - the index of the data object for which we want to calculate the net gravity for
     * @return The net gravity which is the result of the summation of all gravities applying on the target object at index i in the environment
     */
    Vector3dInterface netGravity(DataInterface[] environment, int i);

    /**
     * Calculates the acceleration after having calculated the net gravity F[net]
     *
     * @param environment - a data array representing all data objects currently in the simulation
     * @param i - the index of the data object for which we want to calculate the net gravity for
     * @return The acceleration obtained by dividing (calculated) net gravity with the target object's mass
     */
    Vector3dInterface acceleration(DataInterface[] environment, int i);
}
