package titan.simulators;

import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.own.DataInterface;
import titan.math.Function;
import titan.physics.State;
import titan.solvers.Euler;
import titan.solvers.Kutta;
import titan.solvers.Verlet;
import titan.utility.InitialState;
import titan.utility.Rocket;

/**
 * Class that simulates the universe state, where the probe/rocket is excluded
 */
public class StateSimulator {
    final static Function f = new Function();
    private static StateInterface y0; // Singleton variable

    public StateSimulator(){
        if (y0 == null){ y0 = new State(InitialState.getInitialState()); }
    }

    /**
     * Runs the solver for multiple steps.
     * Initial state will be the state from 'solar_system_data-2020_04_01.txt' which is the provided initial state
     *
     * @param solver - the solver the Engine should use to compute the solution
     * @param ts - array containing all steps to take in order with ts[0] = 0
     * @return All states from initial till last state. Array size = ts.length
     */

    public StateInterface[] runSolver(ODESolverInterface solver, double[] ts){
        return solver.solve(f, y0, ts);
    }
    /**
     * Runs the solver for multiple steps with the desired final time and step size.
     * Initial state will be the state from 'solar_system_data-2020_04_01.txt' which is the provided initial state
     *
     * @param solver - the solver the Engine should use to compute the solution
     * @param tf - the final simulation time
     * @param h - the step size used to approach the final time
     * @return All states from initial till last state
     */
    public StateInterface[] runSolver(ODESolverInterface solver, double tf, double h){ return solver.solve(f, y0, tf, h); }

    /**
     * Runs the solver for a single time step
     * Initial state will be the state from 'solar_system_data-2020_04_01.txt' which is the provided initial state
     *
     * @param solver - the solver the Engine should use to compute the solution
     * @param t - the time we are approximating for
     * @param h - the step to make
     * @return The state after the single step h
     */
    public StateInterface performStep(ODESolverInterface solver, double t, double h){
        return solver.step(f, t, y0, h);
    }

    public static void main (String[] args){
        ODESolverInterface solver = new Euler();
        double tf = 60 * 60 * 24 * 365;
        double h = 30;

        StateSimulator simulator = new StateSimulator();
        State[] states = (State[]) simulator.runSolver(solver, tf, h);
        for(int i = 0; i < states.length; i++){
            DataInterface target = states[i].getObjects()[3];
            System.out.println(target.getName());
            System.out.println("Pos: " + target.getPosition().toString());
            System.out.println("Vel: " + target.getVelocity().toString());
            System.out.println();
        }
    }
}