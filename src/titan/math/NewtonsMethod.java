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


    public static void main(String[] args){
        long startTime = System.nanoTime();
        double h = 86400;
        double tf = 86400 * 365;


        DataInterface rocket = new Rocket(
                5e6,
                new Vector3d(-1.471922101663588e+11,  -2.860995816266412e+10, 8.278183193596080e+06),
                new Vector3d()
        );

        NewtonsMethod newton = new NewtonsMethod(rocket);
        newton.getImprovedVelocity(new Vector3d(), tf, h, 1000);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("Runtime required to find Vk: " + (elapsedTime / 1e9) + " seconds");
    }


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

    public Vector3dInterface getImprovedVelocity(Vector3dInterface v0, double tf, double h, double d){
        Vector3dInterface g, v = v0;
        while(true){
            g = gFunction(v, tf, h);
            if (g.norm() <= d){ break; }

            double[][] D = derivativeMatrix(v, tf, h);
            D = algebra.inverse(D);
            double[] x = { v.getX(), v.getY(), v.getZ() };
            double[] b = algebra.equate(D, x);
            v = v.sub(new Vector3d(b));

            System.out.println("Distance to Titan: " + g.norm());
            System.out.println("Found velocity: " + v.norm());
            System.out.println("Vector velocity: " + v.toString());
       ;
            System.out.println();
        }
        return v;
    }


    public Vector3dInterface gFunction(Vector3dInterface v, double tf, double h){
        DataInterface[] objects = y0.getObjects();
        objects[objects.length - 1].setVelocity(v);
        State[] simulation = (State[]) solver.solve(function, y0, tf, h);
        objects = simulation[simulation.length - 1].getObjects();
        return objects[objects.length - 1].distance3d(objects[8]);
    }


    public Vector3dInterface addH(Vector3dInterface v, double h, int axis){
        if (axis == 0){ return new Vector3d(v.getX() + h, v.getY(), v.getZ()); }
        else if (axis == 1){ return new Vector3d(v.getX(), v.getY() + h, v.getZ()); }
        else if (axis == 2){ return new Vector3d(v.getX(), v.getY(), v.getZ() + h); }
        else{ throw new RuntimeException("Axis out of bound for interval [0, 2]"); }
    }

    public Vector3dInterface subH(Vector3dInterface v, double h, int axis){
        if (axis == 0){ return new Vector3d(v.getX() - h, v.getY(), v.getZ()); }
        else if (axis == 1){ return new Vector3d(v.getX(), v.getY() - h, v.getZ()); }
        else if (axis == 2){ return new Vector3d(v.getX(), v.getY(), v.getZ() - h); }
        else{ throw new RuntimeException("Axis out of bound for interval [0, 2]"); }
    }

    public double getPartialDerivative(Vector3dInterface gAdd, Vector3dInterface gSub, double h, int axis){
        double numerator;
        double denominator = 2 * h;
        if (axis == 0){ numerator = gAdd.getX() - gSub.getX(); }
        else if (axis == 1){ numerator = gAdd.getY() - gSub.getY();  }
        else if (axis == 2){ numerator = gAdd.getZ() - gSub.getZ(); }
        else{ throw new RuntimeException("Axis out of bound for interval [0, 2]"); }

        return numerator / denominator;
    }

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
