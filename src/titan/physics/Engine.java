package titan.physics;

import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.own.DataInterface;
import interfaces.own.EngineInterface;
import titan.fileIO.PlanetReader;
import titan.math.Function;
import titan.solvers.Euler;
import titan.solvers.Verlet;

import java.util.List;

/**
 *  Main class for pure Physics
 *  Supportive class for GUI
 *
 *  Main function at the bottom
 */
public class Engine implements EngineInterface {
    final static String pathInitialState = "D:\\JProjects\\Y1P2\\src\\titan\\solar_system_data-2020_04_01.txt";
    final static Function f = new Function();
    private static StateInterface y0; // Singleton variable

    public Engine(){
        if (y0 == null){
            PlanetReader reader = new PlanetReader();
            reader.setFilePath(pathInitialState);
            reader.read();
            List<DataInterface> objects = reader.getList();
            /* Add rocket,probe etc here before the array construction to the list */

            DataInterface[] objectArr = new DataInterface[objects.size()];
            for(int i = 0; i < objects.size(); i++){ objectArr[i] = objects.get(i); }
            y0 = new State(objectArr);
        }
    }

    /**
     * Runs the solver for multiple steps.
     * Initial state will be the state from 'solar_system_data-2020_04_01.txt' which is the provided initial state
     *
     * @param solver
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
    public StateInterface[] runSolver(ODESolverInterface solver, double tf, double h){
        return solver.solve(f, y0, tf, h);
    }

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

    public static void main(String[] args){
        Engine engine = new Engine();
        ODESolverInterface solver = new Euler();
        StateInterface[] y = engine.runSolver(solver, 86400.0, 3600.0);
        State s = (State) y[2];
        System.out.println(s.toString());
    }
}