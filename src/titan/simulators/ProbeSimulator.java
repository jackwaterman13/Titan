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
     * Descriptions from interface:
     * @param   p0      the starting position of the probe, relative to the earth's position.
     * @param   v0      the starting velocity of the probe, relative to the earth's velocity.
     * @param   tf      the final time of the evolution.
     * @param   h       the size of step to be taken
     * But different return value
     * @return State array containing entire simulation journey
     */
    public StateInterface[] simulate(ODESolverInterface solver, Vector3dInterface p0, Vector3dInterface v0, double tf, double h){
        DataInterface[] objects = InitialState.getInitialState();
        Vector3dInterface xEarth = objects[3].getPosition();
        Vector3dInterface vEarth = objects[3].getVelocity();
        Vector3dInterface vProbe = new Vector3d(5431.785297590439,-29335.365587332988,0.6580991559410286);

        DataInterface probe = new Planet(
                "Probe",
                probeMass,
                0.0,
                p0.add(xEarth),
                vProbe
        );

        System.out.println("Earth vel norm: " + vEarth.norm());

        NewtonsMethod newton = new NewtonsMethod(probe);
        vProbe = newton.getImprovedVelocity(vProbe, tf, h, 1e8);
        probe.setVelocity(vProbe);

        DataInterface[] included = new DataInterface[objects.length + 1];
        System.arraycopy(objects, 0, included, 0, objects.length);
        included[included.length - 1] = probe;

        return solver.solve(function, new State(included), tf, h);
    }


    public static void main(String[] args){
        Vector3dInterface p0 = new Vector3d();
        Vector3dInterface v0 = new Vector3d();

        double tf = 365 * 86400;
        double h = 86400;


        ProbeSimulator simulator = new ProbeSimulator();
        State[] course = (State[]) simulator.simulate(new Euler(), p0, v0, tf, h);

        for(int i = 0; i < course.length; i++){
            DataInterface[] objects = course[i].getObjects();
            System.out.println("Time: " + h * i);
            System.out.println("Probe position: " + objects[objects.length - 1].getPosition().toString());
            System.out.println("Distance to Titan: " + objects[objects.length - 1].distance(objects[8]));
            System.out.println();
        }
    }
}
