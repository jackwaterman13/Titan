package titan.simulators;

import interfaces.given.*;
import interfaces.own.DataInterface;
import titan.lamberts.LambertsProblem;
import titan.math.Function;
import titan.math.NewtonsMethod;
import titan.math.Vector3d;
import titan.physics.State;
import titan.solvers.Euler;
import titan.utility.InitialState;
import titan.utility.Planet;

public class ProbeSimulator implements ProbeSimulatorInterface {
    private static final double probeMass = 1.5e5;
    private static final ODESolverInterface solver = new Euler();
    private static final ODEFunctionInterface function = new Function();
    /**
     * Documentation from interface:
     * Simulate the solar system, including a probe fired from Earth at 00:00h on 1 April 2020.
     *
     * @param   p0      the starting position of the probe, relative to the earth's position.
     * @param   v0      the starting velocity of the probe, relative to the earth's velocity.
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time.
     * @return  an array of size ts.length giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre.
     */
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts){
        DataInterface[] objects = InitialState.getInitialState();
        Vector3dInterface xEarth = objects[3].getPosition();
        Vector3dInterface vEarth = objects[3].getVelocity();
        DataInterface probe = new Planet(
                "Probe",
                probeMass,
                0.0,
                p0.sub(xEarth),
                v0.sub(vEarth)
        );

        DataInterface[] included = new DataInterface[objects.length + 1];
        System.arraycopy(objects, 0, included, 0, objects.length);
        included[included.length - 1] = probe;

        State[] states = (State[]) solver.solve(function, new State(included), ts);
        Vector3dInterface[] course = new Vector3dInterface[states.length];
        for(int i = 0; i < states.length; i++){
            course[i] = states[i].getObjects()[included.length - 1].getPosition();
        }
        return course;
    }

    /**
     * Documentation from interface:
     * Simulate the solar system with steps of an equal size.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range.
     *
     * @param   p0      the starting position of the probe, relative to the earth's position.
     * @param   v0      the starting velocity of the probe, relative to the earth's velocity.
     * @param   tf      the final time of the evolution.
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre
     */
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h){
        DataInterface[] objects = InitialState.getInitialState();
        Vector3dInterface xEarth = objects[3].getPosition();
        Vector3dInterface vEarth = objects[3].getVelocity();
        DataInterface probe = new Planet(
                "Probe",
                probeMass,
                0.0,
                p0.sub(xEarth),
                v0.sub(vEarth)
        );

        DataInterface[] included = new DataInterface[objects.length + 1];
        System.arraycopy(objects, 0, included, 0, objects.length);
        included[included.length - 1] = probe;

        State[] states = (State[]) solver.solve(function, new State(included), tf, h);
        Vector3dInterface[] course = new Vector3dInterface[states.length];
        for(int i = 0; i < states.length; i++){
            course[i] = states[i].getObjects()[included.length - 1].getPosition();
        }
        return course;
    }

    /**
     * Simulates the universe including the probe
     *
     * @param solver -the solver that should be used to simulate the universe with the probe
     * @param   tf      the final time of the evolution.
     * @param   h       the size of step to be taken
     * But different return value
     * @return State array containing entire simulation journey
     */
    public StateInterface[] simulate(ODESolverInterface solver, double tf, double h){
        DataInterface[] objects = InitialState.getInitialState();
        Vector3dInterface xEarth = objects[3].getPosition();
        Vector3dInterface vProbe = new Vector3d (27805.720809264523,-36002.925093637044,-1020.3072795635245);

        /* Velocity of probe gotten from Newton's method using Euler's solver
         * ==================================================================
         * Probe setting:
         * mass = 1.5e5
         * p0 = earth pos
         *
         * Time frame setting:
         * final time Tf = 1 year = 365 * 86400
         * step size h = 86400
         *
         * Console output:
         * Found velocity that fulfills condition!
         * Distance to Titan: 93949.14425396464 is less than 100000.0
         * Found velocity: 45501.755482239765
         * Vector distance: (14105.201171875,-22886.297607421875,-90020.56647109985)
         * Vector velocity: (27805.720809264523,-36002.925093637044,-1020.3072795635245)
         */

        DataInterface probe = new Planet(
                "Probe",
                probeMass,
                0.0,
                xEarth,
                vProbe
        );

        /* Uncomment lines below if you would like to retry finding a velocity such that the probe can reach Titan in the given time frame
         * NewtonsMethod newton = new NewtonsMethod(probe);
         * vProbe = newton.getImprovedVelocity(vProbe, tf, h, 1e5);
         */
        probe.setVelocity(vProbe);

        DataInterface[] included = new DataInterface[objects.length + 1];
        System.arraycopy(objects, 0, included, 0, objects.length);
        included[included.length - 1] = probe;

        return solver.solve(function, new State(included), tf, h);
    }


    public static void main(String[] args){
        double tf = 365 * 86400;
        double h = 86400;

        ProbeSimulator simulator = new ProbeSimulator();
        State[] course = (State[]) simulator.simulate(new Euler(), tf, h);

        for(int i = 0; i < course.length; i++){
            DataInterface[] objects = course[i].getObjects();
            System.out.println("Time: " + h * i);
            System.out.println("Probe position: " + objects[objects.length - 1].getPosition().toString());
            System.out.println("Distance to Titan: " + objects[objects.length - 1].distance(objects[8]));
            System.out.println();
        }
    }
}
