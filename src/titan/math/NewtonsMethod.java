package titan.math;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.physics.Newton;
import titan.physics.State;
import titan.solvers.Euler;
import titan.utility.InitialState;
import titan.utility.Rocket;

public class NewtonsMethod {
    private final LinearAlgebra algebra;
    private final ODEFunctionInterface function;
    private final ODESolverInterface solver;
    private final State y0;

    /**
     * Constructs Newton's method where Euler's method will be used in the calculations of the complicated function g
     *
     * @param obj - the object (either the probe or rocket) for which we are trying to find initial velocity for
     */
    public NewtonsMethod(DataInterface obj){
        algebra = new LinearAlgebra();
        function = new Function();
        solver = new Euler();

        DataInterface[] initialPlanets = InitialState.getInitialState();
        DataInterface[] initialUniverse = new DataInterface[initialPlanets.length + 1];
        System.arraycopy(initialPlanets, 0, initialUniverse, 0, initialPlanets.length);
        initialUniverse[initialUniverse.length - 1] = obj;
        y0 = new State(initialUniverse);
    }

    /**
     * Constructs Newton's method with desired solver which will be used in the calculations of the complicated function g
     *
     * @param obj the object (either the probe or rocket) for which we are trying to find initial velocity for
     * @param odeSolver the solver that should be used in the complicated function g
     */
    public NewtonsMethod(DataInterface obj, ODESolverInterface odeSolver){
        algebra = new LinearAlgebra();
        function = new Function();
        solver = odeSolver;

        DataInterface[] initialPlanets = InitialState.getInitialState();
        DataInterface[] initialUniverse = new DataInterface[initialPlanets.length + 1];
        System.arraycopy(initialPlanets, 0, initialUniverse, 0, initialPlanets.length);
        initialUniverse[initialUniverse.length - 1] = obj;
        y0 = new State(initialUniverse);
    }

    /**
     * Iterator of Newton's method which tries to find a velocity v such that the distance between the target and
     * the object of interest is less or equal to the specified minimum distance
     *
     * @param v0 - the initial velocity this object has
     * @param tf - the final time of the simulation
     * @param h - the step size used to approach this final time
     * @param d - the minimum final distance
     * @return The initial velocity required to be at least distance d away from the target at the end of the simulation
     */
    public Vector3dInterface getImprovedVelocity(Vector3dInterface v0, double tf, double h, double d){
        Vector3dInterface g, v = v0;
        while(true){
            g = gFunction(v, tf, h);
            if (g.norm() <= d){
                System.out.println("Found velocity that fulfills condition!");
                System.out.println("Distance to Titan: " + g.norm() + " is less than " + d);
                System.out.println("Found velocity: " + v.norm());
                System.out.println("Vector distance: " + g.toString());
                System.out.println("Vector velocity: " + v.toString());
                break;
            }

            double[][] D = derivativeMatrix(v, tf, h);
            D = algebra.inverse(D);
            double[] x = { v.getX(), v.getY(), v.getZ() };
            double[] b = algebra.equate(D, x);
            v = v.sub(new Vector3d(b));

            System.out.println("Distance to Titan: " + g.norm());
            System.out.println("Found velocity: " + v.norm());

            System.out.println("Vector distance: " + g.toString());
            System.out.println("Vector velocity: " + v.toString());

       ;
            System.out.println();
        }
        return v;
    }

    /**
     * The complicated function g which calculates the final distance between the target and the object of interest
     * given the initial velocity
     *
     * @param v - the initial velocity the object of interest has
     * @param tf - the final time of the simulation
     * @param h - the step size used to approach the final time
     * @return the remaining distance at the end of the simulation between the target and the object of interest.
     */
    public Vector3dInterface gFunction(Vector3dInterface v, double tf, double h){
        DataInterface[] objects = y0.getObjects();
        objects[objects.length - 1].setVelocity(v);
        State[] simulation = (State[]) solver.solve(function, y0, tf, h);
        objects = simulation[simulation.length - 1].getObjects();
        return objects[objects.length - 1].distance3d(objects[8]);
    }

    /**
     *
     *
     * @param v
     * @param h
     * @param axis
     * @return
     */
    public Vector3dInterface addH(Vector3dInterface v, double h, int axis){
        if (axis == 0){ return new Vector3d(v.getX() + h, v.getY(), v.getZ()); }
        else if (axis == 1){ return new Vector3d(v.getX(), v.getY() + h, v.getZ()); }
        else if (axis == 2){ return new Vector3d(v.getX(), v.getY(), v.getZ() + h); }
        else{ throw new RuntimeException("Axis out of bound for interval [0, 2]"); }
    }

    /**
     *
     *
     * @param v
     * @param h
     * @param axis
     * @return
     */
    public Vector3dInterface subH(Vector3dInterface v, double h, int axis){
        if (axis == 0){ return new Vector3d(v.getX() - h, v.getY(), v.getZ()); }
        else if (axis == 1){ return new Vector3d(v.getX(), v.getY() - h, v.getZ()); }
        else if (axis == 2){ return new Vector3d(v.getX(), v.getY(), v.getZ() - h); }
        else{ throw new RuntimeException("Axis out of bound for interval [0, 2]"); }
    }

    /**
     *
     *
     * @param gAdd
     * @param gSub
     * @param h
     * @param axis
     * @return
     */
    public double getPartialDerivative(Vector3dInterface gAdd, Vector3dInterface gSub, double h, int axis){
        double numerator;
        double denominator = 2 * h;
        if (axis == 0){ numerator = gAdd.getX() - gSub.getX(); }
        else if (axis == 1){ numerator = gAdd.getY() - gSub.getY();  }
        else if (axis == 2){ numerator = gAdd.getZ() - gSub.getZ(); }
        else{ throw new RuntimeException("Axis out of bound for interval [0, 2]"); }

        return numerator / denominator;
    }

    /**
     *
     *
     * @param v
     * @param tf
     * @param h
     * @return
     */
    public double[][] derivativeMatrix(Vector3dInterface v, double tf, double h){
        double[][] matrix = new double[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Vector3dInterface vAdd = addH(v, h, j);
                Vector3dInterface vSub = subH(v, h, j);

                Vector3dInterface gAdd = gFunction(vAdd, tf, h);
                Vector3dInterface gSub = gFunction(vSub, tf, h);

                matrix[i][j] = getPartialDerivative(gAdd, gSub, h, i);
                //System.out.println("D" + i + j + ": " + matrix[i][j]);
            }
        }
        return matrix;
    }
}
