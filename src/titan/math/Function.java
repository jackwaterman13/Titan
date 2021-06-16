package titan.math;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.FunctionInterface;
import titan.physics.State;
import titan.utility.Rate;

public class Function implements ODEFunctionInterface {
    FunctionInterface functionInterface;

    /**
     * Constructs a function with the default differential equation dy/dt
     */
    public Function(){ functionInterface = new DefaultFunction(); }

    /**
     * Constructs the function with a specified differential equation
     *
     * @param functionInterface - the specified differential equation
     */
    public Function(FunctionInterface functionInterface){ this.functionInterface = functionInterface; }

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
        Vector3dInterface[] xRateOfChange = new Vector3d[objects.length];
        Vector3dInterface[] vRateOfChange = new Vector3d[objects.length];
        for(int i = 0; i < objects.length; i++){
            xRateOfChange[i] = differentiate(t, objects[i].getPosition());
            vRateOfChange[i] = differentiate(t, objects[i].getVelocity());
        }
        return new Rate(xRateOfChange, vRateOfChange);
    }

    /**
     * Uses a differential equation to calculate the derivative of a given vector.
     * By default the differential equation is du/dt
     * To use another equation construct a new Class and pass the equation you want.
     *
     * @param t - differential time t
     * @param u - differential vector u
     *
     * @return Vector representing result of the differential equation
     */
    public Vector3dInterface differentiate(double t, Vector3dInterface u){
        double x = functionInterface.call(t, u.getX());
        double y = functionInterface.call(t, u.getY());
        double z = functionInterface.call(t, u.getZ());
        return new Vector3d(x, y, z);
    }
}
