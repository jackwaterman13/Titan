package titan.solvers;

import interfaces.given.*;
import interfaces.own.DataInterface;
import titan.physics.Newton;
import titan.physics.State;

/**
 * Class that implements Verlet integration
 * Formula:
 * x[n+1] = 2 * x[n] - x[n-1] + a * t^2
 * v[n+1] = v[n] + a * t
 *
 * Uses Euler for the very 1st calculation
 * Example:
 *      x[0] = given
 *      x[1] = Euler
 *      x[2] = 2 * x[1] (<- calculated by Euler) - x[0] + a * t^2
 */
public class Verlet implements ODESolverInterface {
    Newton newton = new Newton();

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

        for(int i = 1; i < ts.length; i++){
            states[i] = step(f, ts[i], states[i-1], ts[i]-ts[i-1]);
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
        State s = (State) y;
        if (s.getPrevious() == null){
            ODESolverInterface solver = new Euler();
            return solver.step(f, t, y, h);
        }

        State previous = s.getPrevious();
        DataInterface[] before = previous.getObjects();
        DataInterface[] current = s.getObjects();
        DataInterface[] result = new DataInterface[current.length];
        for(int i = 0; i < current.length; i++){
            Vector3dInterface acc = newton.acceleration(current, i);
            Vector3dInterface v = current[i].getVelocity();
            Vector3dInterface x = current[i].getPosition();
            Vector3dInterface xOld = before[i].getPosition();
            x = x.mul(2).sub(xOld).addMul(h * h, acc); // => new pos = 2 * current pos - old pos + acc * t^2
            v = v.addMul(h, acc);
            result[i] = current[i].update(x, v);
        }
        State nextState = new State(result);
        nextState.setPrevious(s);
        nextState.setPeriod(s.getPeriod() + h);
        return nextState;
    }
}
