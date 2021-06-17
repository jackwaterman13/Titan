package titan.math;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.physics.State;
import titan.solvers.Euler;

public class NewtonRaphson {
    static ODESolverInterface solver;
    static ODEFunctionInterface function;
    static LinearAlgebra algebra;

    public NewtonRaphson(){
        if (solver == null){ solver = new Euler(); }
        if (function == null){ function = new Function(); }
        if (algebra == null){ algebra = new LinearAlgebra(); }
    }

    public Vector3dInterface step(double h, StateInterface y){
        State s = (State) y;
        s.check4Rocket = false;

        State partition;

        DataInterface[] objects = s.getObjects();
        Vector3dInterface v = objects[objects.length - 1].getVelocity();
        Vector3dInterface d1, d2, ans;

        Double[] vars = { v.getX(), v.getY(), v.getZ() };

        /* Derivative matrix */
        Double[][] D = new Double[3][3];
        for(int i = 0; i  < 3; i++){
            for(int j = 0; j < 3; j++){
                if (j == 0){ v.setX(v.getX() + h); }            // 1st entry of row -> x
                else if (j == 1){ v.setY(v.getY() + h); }       // 2nd entry of row -> y
                else { v.setZ(v.getZ() + h); }                  // 3rd entry of row -> z

                partition = (State) solver.step(function, 0, s, h);

                objects = partition.getObjects();

                d1 = objects[objects.length - 1].distance3d(objects[8]);

                partition = (State) solver.step(function, 0, y, h);
                objects = partition.getObjects();

                d2 = objects[objects.length - 1].distance3d(objects[8]);

                ans = d1.sub(d2);

                double numerator;
                if (i == 0){ numerator = ans.getX(); }      // 1st row -> use x
                else if (i == 1){ numerator = ans.getY(); } // 2nd row -> use y
                else { numerator = ans.getZ(); }            // 3rd row -> use z

                D[i][j] = numerator / (2 * h);
            }
        }

        /* Inverted section */
        D = algebra.invertedMatrix(D);

        /* Matrix equation Ax = b section */
       Double[] b = algebra.equate(D, vars);

        /* Subtraction part */
        double[] finalVars = new double[3];
        for(int i = 0; i < 3; i++){ finalVars[i] = vars[i] - b[i]; }

        s.check4Rocket = true;
        return new Vector3d(finalVars);
    }
}