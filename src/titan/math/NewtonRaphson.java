package titan.math;

import interfaces.given.Vector3dInterface;
import interfaces.own.FunctionInterface;
import interfaces.own.RaphsonInterface;

public class NewtonRaphson implements RaphsonInterface {

    /**
     * Performs a single step of a multi-var Newton's method.
     *
     * @param u - 3-dimensional vector
     * @param f - the involved function
     * @return Returns a 3-dimensional vector u[n+1] obtained through u[n+1] = u[n] - D[f'(x)]^-1 f(x)
     */
    public Vector3dInterface step(Vector3dInterface u, FunctionInterface f){
        Double[] vars = { u.getX(), u.getY(), u.getZ() };

        /* Derivative matrix */
        Double[][] D = new Double[3][3];
        for(int i = 0; i  < 3; i++){
            for(int j = 0; j < 3; j++){
                D[i][j] = f.primeCall(vars[j], vars[i]); // -> f'(x)
            }
        }

        /* Inverted section */
        LinearAlgebra algebra = new LinearAlgebra(); // -> class to use Linear Algebra methods
        D = algebra.invertedMatrix(D);

        /* Matrix equation Ax = b section */
       Double[] b = algebra.equate(D, vars);

        /* Subtraction part */
        double[] finalVars = new double[3];
        for(int i = 0; i < 3; i++){
            finalVars[i] = vars[i] - b[i];
            System.out.println(vars[i] + " | " + b[i] + " | " + finalVars[i]);
        }
        System.out.println();
        return new Vector3d(finalVars);
    }
}

/*
    So what is the difference between a weak specification and strong specification



 */