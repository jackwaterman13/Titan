package titan.math;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.physics.State;
import titan.utility.Rate;

public class Function implements ODEFunctionInterface {
    /**
     * Calculates the average dy/dt = [v, a] for each planet
     *
     * Full comment citation from interface:
     * "This is an interface for the function f that represents the
     * differential equation ý = dy/dt = f(t,y).
     * You need to implement this function to represent to the laws of physics.
     *
     * For example, consider the differential equation
     *  dy[0]/dt = y[1];  dy[1]/dt=cos(t)-sin(y[0])
     * Then this function would be
     *   f(t,y) = (y[1],cos(t)-sin(y[0])).
     *
     * @param   t   the time at which to evaluate the function
     * @param   y   the state at which to evaluate the function
     * @return  The average rate-of-change over the time-step. Has dimensions of [state]/[time]."
     */


    public RateInterface call(double t, StateInterface y){
        State s = (State) y;
        DataInterface[] objects = s.getObjects();
        Vector3dInterface[] dxdt = new Vector3d[objects.length];
        Vector3dInterface[] dvdt = new Vector3d[objects.length];
        for(int i = 0; i < objects.length; i++){
            dxdt[i] = objects[i].getPosition().mul(1.0 / t); // => dx/dt = v
            dvdt[i] = objects[i].getVelocity().mul(1.0 / t); // => dv/dt = a
        }
        return new Rate(dxdt, dvdt);
    }
}
