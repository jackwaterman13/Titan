package titan.solvers;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import titan.physics.State;
import titan.utility.Rate;

/**
 * Class that is coding representation of Runge-Kutta 4th order
 * Kutta's formula for computing next point:
 * y[n+1]=y[n]+1/6(k[1]+2k[2]+2k[3]+k[4])
 * t[n+1]=t[n]+h
 *
 * where h is step size and
 *
 * k[1]=hf(t[n],y[n])
 * k[2]=hf(t[n]+h/2, y[n]+k[1]/2)
 * k[3]=hf(t[n]+h/2, y[n]+k[2]/2)
 * k[4]=hf(t[n]+h, y[n]+k[3])
 *
 * The local truncation error of RK4 is of order Ot(h^5), giving a global truncation error of order O(h^4).
 */
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
        StateInterface[] states = new State[ts.length];
        states[0] = y0;

        double tSum = 0.0;
        for(int i = 1; i < ts.length; i++){
            tSum += ts[i];
            states[i] = step(f, tSum, states[i-1], ts[i]);
        }
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

        for(int i = 1; i < fit; i++){
            states[i] = step(f, i * h, states[i-1], h);
        }
        if (fit < states.length){
            double remainingTime = tf % h;
            states[states.length - 1] = step(f, tf, states[states.length - 2], remainingTime);
        }
        return states;
    }


    /**
     * Kutta's formula for computing next point:
     * y[n+1]=y[n]+1/6(k[1]+2k[2]+2k[3]+k[4])
     * t[n+1]=t[n]+h
     *
     * where h is step size and
     *
     * k[1]=hf(t[n],y[n])
     * k[2]=hf(t[n]+h/2, y[n]+k[1]/2)
     * k[3]=hf(t[n]+h/2, y[n]+k[2]/2)
     * k[4]=hf(t[n]+h, y[n]+k[3])
     *
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
        Rate r = (Rate) f.call(t, y);
        Rate k1 = r.mul(h);

        r = (Rate) f.call(t + h/2.0, y.addMul(1.0/2.0, k1));
        Rate k2 = r.mul(h);

        r = (Rate) f.call(t + h/2.0, y.addMul(1.0/2.0, k2));
        Rate k3 = r.mul(h);

        r = (Rate) f.call(t + h, y.addMul(1, k3));
        Rate k4 = r.mul(h);

        Rate finalRoc = k1.addMul(2, k2).addMul(2, k3).add(k4).mul(1.0/6.0);

        State nextState = (State) y.addMul(1, finalRoc); // -> this adds +step(=1) to period
        nextState.setPeriod(nextState.getPeriod() - 1 + h);   // hence, new period = current period -step + h
        return nextState;
    }
}
