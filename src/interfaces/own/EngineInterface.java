package interfaces.own;

import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;

/**
 * Interface that sets the structure of the Physics Engine
 */
public interface EngineInterface {
    /**
     * Runs the solver for multiple steps.
     * Initial state will be the state from 'solar_system_data-2020_04_01.txt' which is the provided initial state
     *
     * @param solver
     * @param ts - array containing all steps to take in order with ts[0] = 0
     * @return All states from initial till last state. Array size = ts.length
     */
    public StateInterface[] runSolver(ODESolverInterface solver, double[] ts);
    /**
     * Runs the solver for multiple steps with the desired final time and step size.
     * Initial state will be the state from 'solar_system_data-2020_04_01.txt' which is the provided initial state
     *
     * @param solver - the solver the Engine should use to compute the solution
     * @param tf - the final simulation time
     * @param h - the step size used to approach the final time
     * @return All states from initial till last state
     */
    public StateInterface[] runSolver(ODESolverInterface solver, double tf, double h);

    /**
     * Runs the solver for a single time step
     * Initial state will be the state from 'solar_system_data-2020_04_01.txt' which is the provided initial state
     *
     * @param solver - the solver the Engine should use to compute the solution
     * @param t - the time we are approximating for
     * @param h - the step to make
     * @return The state after the single step h
     */
    public StateInterface performStep(ODESolverInterface solver, double t, double h);
}
