package titan.simulators;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.lamberts.LambertsProblem;
import titan.math.Function;
import titan.math.Vector3d;
import titan.physics.State;
import titan.solvers.Euler;
import titan.utility.InitialState;
import titan.utility.Planet;
import titan.utility.Rocket;

public class RocketSimulator {
     private static final ODEFunctionInterface function = new Function();

    /**
     * Simulates the universe including the rocket
     *
     * @param solver -the solver that should be used to simulate the universe with the probe
     * Descriptions from interface:
     * @param   p0      the starting position of the rocket, relative to the earth's position.
     * @param   v0      the launch velocity of the rocket should accelerate to, relative to the earth's velocity
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time.
     *
     * But different return value
     * @return State array containing entire simulation journey
     */
    public StateInterface[] simulate(ODESolverInterface solver, Vector3dInterface p0, Vector3dInterface v0, double ts[]){
        DataInterface[] objects = InitialState.getInitialState();
        Vector3dInterface xEarth = objects[3].getPosition();
        Vector3dInterface vEarth = objects[3].getVelocity();
        DataInterface rocket = new Rocket(
                6e5,
                p0.sub(xEarth),
                v0.sub(vEarth)
        );

        DataInterface[] included = new DataInterface[objects.length + 1];
        System.arraycopy(objects, 0, included, 0, objects.length);
        included[included.length - 1] = rocket;
        State y0 = new State(included);
        return solver.solve(function, y0, ts);
    }

    /**
     * Simulates the universe with the rocket included
     *
     * @param solver -the solver that should be used to simulate the universe with the probe
     * Same param descriptions as interface methods:
     * @param   p0      the starting position of the rocket, relative to the earth's position.
     * @param   v0      the launch velocity of the rocket should accelerate to, relative to the earth's velocity
     * @param   tf      the final time of the evolution.
     * @param   h       the size of step to be taken
     *
     * But different return value
     * @return State array representing the entire simulation
     */
    public StateInterface[] simulate(ODESolverInterface solver, Vector3dInterface p0, Vector3dInterface v0, double tf, double h){
        DataInterface[] objects = InitialState.getInitialState();
        Vector3dInterface xEarth = objects[3].getPosition();
        Vector3dInterface vEarth = objects[3].getVelocity();

        DataInterface rocket = new Rocket(
                6e5,
                p0.sub(xEarth),
                v0.sub(vEarth)
        );

        DataInterface[] included = new DataInterface[objects.length + 1];
        System.arraycopy(objects, 0, included, 0, objects.length);
        included[included.length - 1] = rocket;
        State y0 = new State(included);
        return solver.solve(function, y0, tf, h);
    }

    public static void main (String[] args){
        RocketSimulator simulator = new RocketSimulator();
        Vector3dInterface p0 = new Vector3d();
        Vector3dInterface v0 = new Vector3d();

        double tf = 86400 * 365;
        double h = 3600;
        State[] states = (State[]) simulator.simulate(new Euler(), p0, v0, tf, h);
        for(int i = 0; i < states.length; i++){
            DataInterface[] objects = states[i].getObjects();
            System.out.println("Time: " + h * i);
            System.out.println("Rocket position: " + objects[objects.length - 1].getPosition().toString());
            System.out.println("Distance to Titan: " + objects[objects.length - 1].distance(objects[8]));
            System.out.println();
        }
    }
}
