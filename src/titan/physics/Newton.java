package titan.physics;

import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.NewtonInterface;
import titan.math.Vector3d;
import titan.utility.Rate;

public class Newton implements NewtonInterface {
    /**
     * Calculates the gravity between 2 data objects whose position and mass is known
     *
     * @param victim - the data object that experiences a gravitational influence
     * @param exhibitor - the data object that exhibits a gravitational influence
     * @return The gravity applied on body 1 whose position and mass are both the 1st index in the arrays
     */
    public Vector3dInterface gravity(DataInterface victim, DataInterface exhibitor){
        Vector3dInterface v = victim.distance3d(exhibitor);
        double d = v.norm();
        double numerator = (victim.getMass() * exhibitor.getMass()) * G;
        double denominator = Math.pow(d, 2) * d;
        return v.mul(numerator / denominator);
    }

    /**
     * Calculates the net gravity applied on a body in the presence of multiple bodies
     * Furthermore, saves the net gravity of the object inside the object
     *
     * @param environment - a data array representing all data objects currently in the simulation
     * @param i - the index of the data object for which we want to calculate the net gravity for
     * @return The net gravity which is the result of the summation of all gravities applying on the target object at index i in the environment
     */
    public Vector3dInterface netGravity(DataInterface[] environment, int i){
        Vector3dInterface v = new Vector3d();
        DataInterface target = environment[i];
        for(int n = 0; n < environment.length; n++){
            if (n == i){ continue; }
            v.add(gravity(target, environment[n]));
        }
        target.setGravity(v);
        return v;
    }

    /**
     * Calculates the acceleration after having calculated the net gravity F[net]
     *
     * @param environment - a data array representing all data objects currently in the simulation
     * @param i - the index of the data object for which we want to calculate the net gravity for
     * @return The acceleration obtained by dividing (calculated) net gravity with the target object's mass
     */
    public Vector3dInterface acceleration(DataInterface[] environment, int i){
        double m = environment[i].getMass();
        if (m == 0.0){ return new Vector3d(); } // No mass -> no need to waste resources to compute final result 0
                                                // Last but not least, Java double result for: 1.0 / 0.0 -> NaN because "... an operation was not defined or not representable as a real number"
        Vector3dInterface net = netGravity(environment, i);
        return net.mul(1.0 / m);
    }
}
