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
 * Class that represents differential equation dy/dt = cos(t)−y/3 where y(0) = 0
 * Exact answers can be obtained by function y(t) = 9/10 sin(t) + 3/10 (cos(t)−exp(−t/3))
 */
public class Equation3 implements ODEFunctionInterface {
    /**
     * Represents differential equation dy/dt = cos(t)−y/3 where y(0) = 0
     *
     * @param t time to evaluate the function at
     * @param y the state to evaluate the function at where the x-axis of the position of the objects within represents y
     * @return Returns dy(t)/dt encapsulated in a Rate class so it can be used by the solvers.
     */
    public RateInterface call(double t, StateInterface y) {
        State s = (State) y;
        DataInterface[] objects = s.getObjects();
        Vector3dInterface[] v = new Vector3dInterface[objects.length];
        Vector3dInterface[] a = new Vector3dInterface[objects.length];
        for(int i = 0; i < objects.length; i++){
            double d = Math.cos(t) - objects[i].getPosition().getX() / 3;
            v[i] = new Vector3d(d, 0, 0);
            a[i] = new Vector3d();
        }
        return new Rate(v, a);
    }
}
