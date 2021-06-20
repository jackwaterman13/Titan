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

public class GiacomoClass {
    private StateInterface y0;
    private StateInterface y;

    private double h = 1;

    private Vector3dInterface Vk = new Vector3d();
    private Vector3dInterface titan = new Vector3d(7,7,7);
    private Vector3dInterface currentPos = new Vector3d(7,7,7);
    private Vector3dInterface dummy = new Vector3d(0,0,0);

    public static void main(String[] args){
        long startTime = System.nanoTime();
        GiacomoClass gClass = new GiacomoClass(new Vector3d());
        gClass.getImprovedVelocity();

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("Runtime required to find Vk: " + (elapsedTime / 1e9) + " seconds");
    }


    public GiacomoClass (Vector3dInterface Vk){
        this.Vk = Vk;

        DataInterface[] planets = InitialState.getInitialState();   // -> getting the planets
        // -> index 3 in planets is Earth
        DataInterface rocket = new Rocket(
                0, // fuel mass, can be 0 for now
                planets[3].getPosition(),
                Vk
        );

        DataInterface[] initialUniverse = new DataInterface[planets.length + 1];
        System.arraycopy(planets, 0, initialUniverse, 0, planets.length); // Copies the elements from one array to another array, meaning this copies all planets
        initialUniverse[initialUniverse.length - 1] = rocket; // last element of the array is empty, this is where the rocket should be in

        y0 = new State(initialUniverse);
        y = y0;
    }

    public Vector3dInterface getImprovedVelocity(){
        boolean stop = false;
        Vector3dInterface gValue = new Vector3d (0,0,0);

        int x = 0;
        while (!stop){

            if (x == 0){ gValue = GFunction(y0); x++; }
            else { gValue = GFunction(Vk);}

            double[][] matrix = matrixCreator(Vk, h);
            double[][] InvMatrix = InverseMatrix(matrix);
            Vector3dInterface nextVk = findNextVk(InvMatrix, gValue, Vk);

            if (checkDistance(gValue)){
                stop = true;
            }

            Vk = nextVk;
            System.out.println("Current distance: " + gValue.norm());
            System.out.println("Found next Vk: " + nextVk.norm());
            System.out.println();
        }
        return Vk;
    }

    public Vector3dInterface GFunction(Vector3dInterface Vk){
        ODESolverInterface solver = new Euler();
        ODEFunctionInterface function = new Function();

        State s = (State) y;
        DataInterface[] objects = s.getObjects();
        objects[objects.length -1].setVelocity(Vk); // Sets the velocity for the rocket

        State[] entireSimulation = (State[]) solver.solve(function, y, 86400 * 120, 86400); // Calculate 'next' state where the rocket has Vk as velocity
        objects = entireSimulation[entireSimulation.length - 1].getObjects();  // Get last state of the simulation

        return objects[8].getPosition().sub(objects[objects.length - 1].getPosition()); // Distance between Titan and Rocket
    }

    public Vector3dInterface GFunction(StateInterface y){
        State s = (State) y;
        DataInterface[] objects = s.getObjects(); // Entry order of array: Sun, Mercury, Venus, Earth, Moon, Mars, Jupiter, Saturn, Titan, Neptune, Uranus, Rocket
        return objects[8].getPosition().sub(objects[objects.length - 1].getPosition()); // G = Pt - Pk
    }

    public boolean checkDistance (Vector3dInterface distance) {
        return distance.norm() <= 1000;
    }

    public double[][] InverseMatrix (double[][] matrix){
        ///// ***** copied code from https://www.thejavaprogrammer.com/java-program-find-inverse-matrix/   ***/////
        double[][] inverse = new double[3][3];
        double det = 0;

        for (int i = 0; i < 3; i++){
            det = det + (matrix[0][i] * (matrix[1][(i + 1) % 3] * matrix[2][(i + 2) % 3] - matrix[1][(i + 2) % 3] * matrix[2][(i + 1) % 3]));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j){
                inverse[i][j] = (((matrix[(j+1)%3][(i+1)%3] * matrix[(j+2)%3][(i+2)%3]) - (matrix[(j+1)%3][(i+2)%3] * matrix[(j+2)%3][(i+1)%3]))/ det);
            }
        }
        return inverse;
    }

    public Vector3dInterface findNextVk (double[][] matrix, Vector3dInterface gValue, Vector3dInterface Vk){

        double firstRow = matrix[0][0]* gValue.getX() + matrix[0][1]* gValue.getY() + matrix[0][2]* gValue.getZ();
        double secondRow= matrix[1][0]* gValue.getX() + matrix[1][1]* gValue.getY() + matrix[1][2]* gValue.getZ();
        double thirdRow = matrix[2][0]* gValue.getX() + matrix[2][1]* gValue.getY() + matrix[2][2]* gValue.getZ();
        Vector3dInterface VecXMat = new Vector3d(firstRow, secondRow, thirdRow);

        return Vk.sub(VecXMat);
    }

    public double[][] matrixCreator(Vector3dInterface Vk, double h){
        double[][] matrix = new double[3][3];

        Vector3dInterface g00a =  vectorAddition(Vk, 1, h);
        Vector3dInterface g00b =  vectorSubtraction(Vk, 1, h);
        matrix[0][0] = (GFunction(g00a).getX() - GFunction(g00b).getX()) / 2 * h;


        Vector3dInterface g01a =  vectorAddition(Vk, 2, h);
        Vector3dInterface g01b =  vectorSubtraction(Vk, 2, h);
        matrix[0][1] = (GFunction(g01a).getX() - GFunction(g01b).getX()) / 2 * h;

        Vector3dInterface g02a =  vectorAddition(Vk, 3, h);
        Vector3dInterface g02b =  vectorSubtraction(Vk, 3, h);
        matrix[0][2] = (GFunction(g02a).getX() - GFunction(g02b).getX()) / 2 * h;

        //_____________________________________________________________
        Vector3dInterface g10a =  vectorAddition(Vk, 1, h);
        Vector3dInterface g10b =  vectorSubtraction(Vk, 1, h);
        matrix[1][0] = (GFunction(g10a).getY() - GFunction(g10b).getY()) / 2 * h;

        Vector3dInterface g11a =  vectorAddition(Vk, 2, h);
        Vector3dInterface g11b =  vectorSubtraction(Vk, 2, h);
        matrix[1][1] = (GFunction(g11a).getY() - GFunction(g11b).getY()) / 2 * h;

        Vector3dInterface g12a =  vectorAddition(Vk, 3, h);
        Vector3dInterface g12b =  vectorSubtraction(Vk, 3, h);
        matrix[1][2] = (GFunction(g12a).getY() - GFunction(g12b).getY()) / 2 * h;

        //---------------------------------------------------------------
        Vector3dInterface g20a =  vectorAddition(Vk, 1, h);
        Vector3dInterface g20b =  vectorSubtraction(Vk, 1, h);
        matrix[2][0] = (GFunction(g20a).getZ() - GFunction(g20b).getZ()) / 2 * h;

        Vector3dInterface g21a =  vectorAddition(Vk, 2, h);
        Vector3dInterface g21b =  vectorSubtraction(Vk, 2, h);
        matrix[2][1] = (GFunction(g21a).getZ() - GFunction(g21b).getZ()) / 2 * h;

        Vector3dInterface g22a =  vectorAddition(Vk, 3, h);
        Vector3dInterface g22b =  vectorSubtraction(Vk, 3, h);
        matrix[2][2] = (GFunction(g22a).getZ() - GFunction(g22b).getZ()) / 2 * h;

        return matrix;
    }

    public Vector3dInterface vectorAddition(Vector3dInterface v, int n, double h){
        if (n == 1){
            double add = v.getX() + h;
            return new Vector3d(add, v.getY(), v.getZ());
        }
        if (n == 2){
            double add = v.getY() + h;
            return new Vector3d(v.getX(), add, v.getZ());
        }
        if (n == 3){
            double add = v.getZ() + h;
            return new Vector3d(v.getX(), v.getY(), add);
        }
        else{
            return null;
        }

    }

    public Vector3dInterface vectorSubtraction(Vector3dInterface v, int n, double h){
        if (n == 1){
            double add = v.getX() - h;
            return new Vector3d(add, v.getY(), v.getZ());
        }
        if (n == 2){
            double add = v.getY() - h;
            return new Vector3d(v.getX(), add, v.getZ());
        }
        if (n == 3){
            double add = v.getZ() - h;
            return new Vector3d(v.getX(), v.getY(), add);
        }
        else{ return null; }
    }
}