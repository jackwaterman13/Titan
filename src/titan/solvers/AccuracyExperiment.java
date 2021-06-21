package titan.solvers;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.own.DataInterface;
import titan.math.AccuracyFunction;
import titan.math.Function;
import titan.math.Vector3d;
import titan.physics.State;
import titan.utility.InitialState;
import titan.utility.Planet;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccuracyExperiment {
    public static void main(String[] args) {
        // Accuracy function that will test the accuracy of our solvers
        ODEFunctionInterface function = new AccuracyFunction();

        /* Exact solutions of the function
         * y(0) = 0
         * y(1) = 0.503346658225,
         * y(2) = 0.478421766451,
         * y(5) = 0.237813428537,
         * y(10) = 0.110790590981
         * */
        double[] es = {0, 0.503346658225, 0.478421766451, 0.237813428537, 0.110790590981};

        // The times at which function will be evaluated. These should be equal to the times of the exact solution
        double[] ts = { 0, 1, 2, 5, 10 };

        // Starting with Euler for the first  solver
        ODESolverInterface solver = new Euler();

        // Preparing the initial state which should be an empty planets whose initial position and velocity will be a zero vector
        // Because the function works with a single variable, so will be using the x-axis of the position vector for our comparisons.
        // But as a matter of fact, the axes of the position should have the same values as the axes of velocity at the end of the calculation
        // Due to all of the axes starting at 0 and the function evaluating each axis independently of the other axis
        // Furthermore Verlet actually shouldn't be tested for accuracy as it is NOT an ODE Solver, but a numerical method.
        DataInterface object = new Planet();
        object.setPosition(new Vector3d());
        object.setVelocity(new Vector3d());

        DataInterface[] objects = new DataInterface[]{
                object
        };
        StateInterface y0 = new State(objects);

        // Get the results of the function
        State[] results = (State[]) solver.solve(function, y0, ts);

        /*
         * Current file format/design:
         * Solver
         * Evaluation time: t1  t2  t3  t4
         * Step size:       h1  h2  h3  h4
         * Exact value:     es1 es2 es3 es4
         * Measured value:  ms1 ms2 ms3 ms4
         * Absolute error:  ae1 ae2 ae3 ae4
         * Accuracy:        a1% a2% a3% a4%
         * */

        String columnSeparator = ",";
        String[] l = new String[7];

        StringBuilder sb = new StringBuilder();
        if (solver instanceof Euler) {
            sb.append("Euler");
        } else if (solver instanceof Kutta) {
            sb.append("Kutta");
        } else {
            sb.append("Verlet");
        }

        l[0] = sb.toString();

        sb = new StringBuilder("Evaluation time:" + columnSeparator);
        for (double t : ts) {
            sb.append(t).append(columnSeparator);
        }
        l[1] = sb.toString();


        sb = new StringBuilder("Step size:" + columnSeparator);
        for(int i = 0; i < ts.length; i++){
            double h = 0;
            if (i > 0){
                h = ts[i] - ts[i-1];
            }
            sb.append(h).append(columnSeparator);
        }
        l[2] = sb.toString();

        sb = new StringBuilder("Exact value:" + columnSeparator);
        for (double e : es) {
            sb.append(e).append(columnSeparator);
        }
        l[3] = sb.toString();

        sb = new StringBuilder("Measured value:" + columnSeparator);
        for (State s : results) {
            sb.append(s.getObjects()[0].getPosition().getX()).append(columnSeparator);
        }
        l[4] = sb.toString();

        sb = new StringBuilder("Absolute error:" + columnSeparator);
        for (int i = 0; i < es.length; i++) {
            double error = es[i] - results[i].getObjects()[0].getPosition().getX();
            sb.append(error).append(columnSeparator);
        }
        l[5] = sb.toString();

        sb = new StringBuilder("Accuracy:" + columnSeparator);
        for (int i = 0; i < es.length; i++) {
            double error = es[i] - results[i].getObjects()[0].getPosition().getX();
            double accuracy = error / es[i] * 100;
            if (error == 0) {
                 accuracy = 100;
            }
            sb.append(accuracy).append("%").append(columnSeparator);
        }
        l[6] = sb.toString();

        // Create a date time formatter to display the current date and time in the desired format
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss");

        // Get the current (local) date time
        LocalDateTime now = LocalDateTime.now();

        // Assign the experiment file name eto be the current (local) date time
        String fileName = dtf.format(now);

        // Assign a directory path to where the file should be created.
        String filePath = "src/experiments/";

        // Assign a file type to the experiment file | .csv = for excel/spreadsheets files
        String fileType = ".csv";

        try {
            File f = new File(filePath + fileName + fileType);

            PrintWriter writer = new PrintWriter(f);

            for (String s : l) {
                writer.println(s);
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
