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
 * Source: Equation and function from Numerical assignment 2 answers.
 *
 * Class representing the differential equation dy/dt = e^-t - y^2 where y(0) = 0
 * Complicated function... so no function for this xD
 * Exact answers for the following time evaluations:
 * y(0) = 0
 * y(1) = 0.503346658225,
 * y(2) = 0.478421766451,
 * y(5) = 0.237813428537,
 * y(10) = 0.110790590981
 */
public class Equation2 implements ODEFunctionInterface {
    /**
     * Represents differential equation dy/dt = e^-t - y^2; y(0) = 0, where y is no complex variable but simply a single variable.
     *
     * @param t time to evaluate the function at
     * @param y the state to evaluate the function at where the x-axis of the position of the objects within represents y
     * @return Returns dy(t)/dt encapsulated in a Rate class so it can be used by the solvers.
     */
    public RateInterface call(double t, StateInterface y) {
        State s = (State) y;
        DataInterface[] data = s.getObjects();
        Vector3dInterface[] v = new Vector3d[data.length];
        Vector3dInterface[] a = new Vector3d[data.length];
        for(int i = 0; i < data.length; i++){
            Vector3dInterface x = data[i].getPosition();
            double d = Math.exp(-t) - Math.pow(x.getX(), 2);
            v[i] = new Vector3d(d, 0, 0);
            a[i] = new Vector3d();
        }
        return new Rate(v, a);
    }
}
