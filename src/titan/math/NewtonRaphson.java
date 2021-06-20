package titan.math;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.physics.State;
import titan.solvers.Euler;
import titan.utility.InitialState;
import titan.utility.Rocket;

public class NewtonRaphson {
    static ODESolverInterface solver;
    static ODEFunctionInterface function;
    static LinearAlgebra algebra;

    State y0;
    double tf, h;

    public NewtonRaphson(){
        if (solver == null){ solver = new Euler(); }
        if (function == null){ function = new Function(); }
        if (algebra == null){ algebra = new LinearAlgebra(); }

        DataInterface[] planets = InitialState.getInitialState();
        DataInterface rocket = new Rocket(
                0,
                planets[3].getPosition(),
                new Vector3d()
        );

        DataInterface[] initialUniverse = new DataInterface[planets.length + 1];
        System.arraycopy(planets, 0, initialUniverse, 0, planets.length);
        initialUniverse[initialUniverse.length - 1] = rocket;

        y0 = new State(initialUniverse);
    }

    public Vector3dInterface solve(Vector3dInterface v0, double tf, double h){
        this.tf = tf;
        this.h = h;
        Vector3dInterface v = v0;
        while(true){
            v = step(v);
            if (function(v).norm() <= 1000){ break; }
        }
        return  v;
    }

    public Vector3dInterface step(Vector3dInterface v){
        Double[][] D = derivativeMatrix(v);
        D = algebra.invertedMatrix(D);

        Vector3dInterface g = function(v);
        Double[] x = { g.getX(), g.getY(), g.getZ() };

        Double[] b = algebra.equate(D, x);
        return v.sub(new Vector3d(b[0], b[1], b[2]));
    }


    public Vector3dInterface function(Vector3dInterface v){
        DataInterface[] objects = y0.getObjects();
        objects[objects.length - 1].setVelocity(v);

        State[] simulation = (State[]) solver.solve(function, y0, tf, h);
        objects = simulation[simulation.length - 1].getObjects();
        return objects[8].getPosition().sub(objects[objects.length - 1].getPosition());
    }

    public double primeFunction(Vector3dInterface v, int i, int j){
        Vector3dInterface a, b;
        if (j == 0) {
            a = new Vector3d(v.getX() + h, v.getY(), v.getZ());
            b = new Vector3d(v.getX() - h, v.getY(), v.getZ());
        }
        else if (j == 1){
            a = new Vector3d(v.getX(), v.getY() + h, v.getZ());
            b = new Vector3d(v.getX(), v.getY() - h, v.getZ());
        }
        else{
            a = new Vector3d(v.getX(), v.getY(), v.getZ() + h);
            b = new Vector3d(v.getX(), v.getY(), v.getZ() - h);
        }

        Vector3dInterface dg = function(a).sub(function(b));
        double numerator;
        if (i == 0){
            numerator = dg.getX();
        }
        else if (i == 1){
            numerator = dg.getY();
        }
        else{
            numerator = dg.getZ();
        }
        System.out.println( (numerator / (h * 2)));
        return numerator / (2 * h);
    }

    public Double[][] derivativeMatrix(Vector3dInterface v){
        Double[][] D = new Double[3][3];
        for(int i = 0; i < D.length; i++){
            for(int j = 0; j < D[i].length; j++){
                D[i][j] = primeFunction(v, i, j);
                System.out.println("D" + i + j + ": " + D[i][j]);
            }
        }
        return D;
    }

    public static void main(String[] args){
        double h = 86400;
        double tf = h * 365;
        NewtonRaphson newton = new NewtonRaphson();
        newton.solve(new Vector3d(), tf, h);
    }
}