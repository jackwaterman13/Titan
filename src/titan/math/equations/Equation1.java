package titan.math.equations;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.math.Vector3d;
import titan.physics.State;
import titan.utility.Rate;

/**
 * Class representing the differential equation dy/dt = y where y(0) = 1
 * Exact answers can be obtained through the equation y = e^x
 */
public class Equation1 implements ODEFunctionInterface {
    /**
     * Represents differential equation dy/dt = y, where y is no complex variable but simply a single variable.
     *
     * @param t time to evaluate the function at
     * @param y the state to evaluate the function at where the x-axis of the position of the objects within represents y
     * @return Returns y encapsulated in a Rate class so it can be used by the solvers.
     */
    public RateInterface call(double t, StateInterface y) {
        State s = (State) y;
        DataInterface[] objects = s.getObjects();
        Vector3dInterface[] v = new Vector3dInterface[objects.length];
        Vector3dInterface[] a = new Vector3dInterface[objects.length];
        for(int i = 0; i < objects.length; i++){
            v[i] = objects[i].getPosition();
            a[i] = new Vector3d();
        }
        return new Rate(v, a);
    }
}