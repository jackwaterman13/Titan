package titan.solvers;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import titan.physics.State;

public class Kutta implements ODESolverInterface {
    /**
     * Documentation given from the interface:
     * Solve the differential equation by taking multiple steps.
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time
     * @return  an array of size ts.length with all intermediate states along the path
     */

    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts){
        double tSum = 0.0;
        for(double t : ts){ tSum = tSum + t; }

        StateInterface[] states = new State[ts.length];
        states[0] = y0;

        for(int i = 1; i < ts.length; i++){ states[i] = step(f, tSum, states[i-1], ts[i]); }
        return states;
    }

    /**
     * Documentation given from the interface:
     * Solve the differential equation by taking multiple steps of equal size, starting at time 0.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   tf      the final time
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 including all intermediate states along the path
     */

    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h){
        int fit = (int) (tf / h) + 1;

        StateInterface[] states;
        if (tf % h != 0){ states = new State[fit + 1]; }
        else { states = new State[fit]; }
        states[0] = y0;

        for(int i = 1; i < fit; i++){ states[i] = step(f, tf, states[i-1], h); }
        if (fit < states.length){
            double remainingTime = tf % h;
            states[states.length - 1] = step(f, tf, states[states.length - 2], remainingTime);
        }
        return states;
    }


    /**
     * Documentation given from the interface:
     * Update rule for one step.
     *
     * @param   f   the function defining the differential equation dy/dt=f(t,y)
     * @param   t   the time
     * @param   y   the state
     * @param   h   the step size
     * @return  the new state after taking one step
     */

    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h){


        return null;
    }
}
