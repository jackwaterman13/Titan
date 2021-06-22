package titan.solvers;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.own.DataInterface;
import titan.math.equations.Equation1;
import titan.math.equations.Equation2;
import titan.math.Vector3d;
import titan.physics.State;
import titan.utility.Planet;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccuracyExperiment {
    public static void main(String[] args){
        ODESolverInterface solver = new Kutta();

        // Accuracy function that will test the accuracy of our solvers
        ODEFunctionInterface function = new Equation1();

        double h = 1;

        /*
         Equation 1 => Differential equation: dy/dt = y and its exact function : y = e^t
         Solution set 1:
         y(0) = 1
         y(0.2) = 1.221402758
         y(0.4) = 1.491824698
         y(0.6) = 1.8221188
         y(0.8) = 2.22540928
         y(1.0) = 2.718281828
         y(1.2) = 3.320116923
         y(1.4) = 4.055199967
         y(1.6) = 4.953032424
         y(1.8) = 6.049647464
         y(2.0) = 7.389056099

         double[] es = {
                 1, 1.2214022758, 1.41824698, 1.8221188, 2.22540928,
                 2.718281828, 3.320116923,  4.055199967, 4.953032424,
                 6.049647464, 7.389056099
         };
         double[] ts = { 0, 0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0 };


         Solution set 2:
         y(0) = 1
         y(2) = 7.389056099
         y(4) = 5.459815003e1
         y(6) = 4.034287935e2
         y(8) = 2.980957987e3
         y(10) = 2.202646579e4
         y(12) = 1.627547914e5
         y(14) = 1.202604284e6
         y(16) = 8.88611052e6
         y(18) = 6.565996914e7
         y(20) = 4.851651954e8

         double[] es = {
                 1,7.389056099, 5.459815003e1, 4.034287935e2, 2.980957987e3,
                 2.202646579e4,1.627547914e5, 1.202604284e6,8.88611052e6,
                 6.565996914e7, 4.851651954e8
         };
         double[] ts = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };
         */

        double[] es = {
                1,7.389056099, 5.459815003e1, 4.034287935e2, 2.980957987e3,
                2.202646579e4,1.627547914e5, 1.202604284e6,8.88611052e6,
                6.565996914e7, 4.851651954e8
        };
        double[] ts = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };

        // Preparing the initial state which should be an empty planets whose initial position and velocity will be a zero vector
        // Because the function works with a single variable, so will be using the x-axis of the position vector for our comparisons.
        // But as a matter of fact, the axes of the position should have the same values as the axes of velocity at the end of the calculation
        // Due to all of the axes starting at 0 and the function evaluating each axis independently of the other axis
        // Furthermore Verlet actually shouldn't be tested for accuracy as it is NOT an ODE Solver, but a numerical method.
        DataInterface object = new Planet();
        object.setPosition(new Vector3d(1.0, 0, 0));
        object.setVelocity(new Vector3d());

        DataInterface[] objects = new DataInterface[]{
                object
        };
        StateInterface y0 = new State(objects);


        State[] results = new State[ts.length];

        for(int i = 0; i < ts.length; i++){
            State[] states = (State[]) solver.solve(function, y0, ts[i], h);
            results[i] = states[states.length - 1];
        }

        /*
         * Current file format/design:
         * Solver
         * Constant step size h
         * Evaluation time: t1  t2  t3  t4
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

        l[1] = "Constant step size: h = " + h + columnSeparator;

        sb = new StringBuilder("Evaluation time:" + columnSeparator);
        for (double t : ts) {
            sb.append(t).append(columnSeparator);
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
            double absoluteError = es[i] - results[i].getObjects()[0].getPosition().getX();
            absoluteError = Math.abs(absoluteError);
            sb.append(absoluteError).append(columnSeparator);
        }
        l[5] = sb.toString();

        sb = new StringBuilder("Relative error:" + columnSeparator);
        for (int i = 0; i < es.length; i++) {
            double absoluteError = es[i] - results[i].getObjects()[0].getPosition().getX();
            absoluteError = Math.abs(absoluteError);
            double relativeError = absoluteError / Math.abs(results[i].getObjects()[0].getPosition().getX());
            if (absoluteError == 0){ relativeError = 0; }
            sb.append(relativeError).append(columnSeparator);
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
