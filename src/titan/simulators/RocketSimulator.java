package titan.simulators;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.math.Function;
import titan.math.NewtonsMethod;
import titan.math.Vector3d;
import titan.physics.State;
import titan.solvers.Euler;
import titan.utility.InitialState;
import titan.utility.Rocket;

public class RocketSimulator {
     private static final ODEFunctionInterface function = new Function();

    /**
     * Simulates the universe with the rocket included
     *
     * @param solver the solver that should be used to simulate the universe with the probe
     * @param tf     the final time of the evolution.
     * @param h      the size of step to be taken
     *
     * But different return value
     * @return State array representing the entire simulation
     */
    public StateInterface[] simulate(ODESolverInterface solver, double tf, double h){
        DataInterface[] objects = InitialState.getInitialState();
        Vector3dInterface xEarth = objects[3].getPosition();
        Vector3dInterface vRocket = new Vector3d (5119.28161058284,2614.901310729494,-958.0211065946626);

        Rocket rocket = new Rocket(
                7e5,
                xEarth,
                vRocket
        );


        /* Velocity found from Newton's method using Euler's solver
         * ========================================================
         * Rocket setting:
         * Fuel mass = 7e5
         * Major mass loss at beginning: 6.0965e5
         *
         * Time frame setting:
         * final time tf = 12 years = 86400 * 365 * 12
         * step size h = 86400
         *
         * Console output from Newton's method confirming final distance:
         * Found velocity that fulfills condition!
         * Distance to Titan: 99677.0166946462 is less than 100000.0
         * Found velocity: 5827.740343734209
         * Vector distance: (-14112.88134765625,30671.999267578125,-93784.66131591797)
         * Vector velocity: (5119.28161058284,2614.901310729494,-958.0211065946626)
         */

        /* Uncomment lines below if you would like to retry finding a velocity such that the rocket can reach Titan in the given time frame
         * NewtonsMethod newton = new NewtonsMethod(rocket);
         * vRocket = newton.getImprovedVelocity(vRocket, tf, h, 1e5);
         * rocket.setVelocity(vRocket);
         */


        DataInterface[] included = new DataInterface[objects.length + 1];
        System.arraycopy(objects, 0, included, 0, objects.length);
        included[included.length - 1] = rocket;
        State y0 = new State(included);
        return solver.solve(function, y0, tf, h);
    }

    public static void main (String[] args){
        double tf = 86400 * 365 * 12;
        double h = 1800;
        RocketSimulator simulator = new RocketSimulator();
        simulator.simulate(new Euler(), tf, h);
    }
}
