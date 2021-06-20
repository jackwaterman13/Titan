package titan.solvers;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.own.DataInterface;
import titan.math.Function;
import titan.physics.State;
import titan.utility.InitialState;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SolverExperiment {
    public static void main (String[] args){
        // The final time at which the last state will be evaluated at.
        double finalTime = 86400 * 365;

        // All the step sizes that will be used for time complexity
        // Member variables of this array do not necessarily need to fit within final time
        double[] stepSizes = {
            86400
        };

        // Variable that will hold all runtimes for each step size. By default these values are in nanoseconds
        long[] measuredRunTimes = new long[stepSizes.length];

        /*
        * Variable changes the time unit that will be saved.
        * By default the measured time is in nanoseconds.
        * 1e9 nanoseconds = 1 second
        * 1e6 nanoseconds = 1 millisecond
        *
        * If you want the time to be saved in nanoseconds, then timeScale = 1
        * Else if you want the time to be saved in milliseconds, then timeScale = (long) 1e9
        * Else if you want the time to be saved in seconds, then timeScale = (long) 1e6
        * */
        long timeScale = (long) 1e6;

        // Rate-of-change function for the universe
        ODEFunctionInterface function  = new Function();

        // Starting with Euler for the first  solver
        ODESolverInterface solver = new Euler();

        // Preparing the initial state
        DataInterface[] objects = InitialState.getInitialState();
        StateInterface y0 = new State(objects);

        int measuresTaken = 0;
        // For every step size,
        for(double h : stepSizes){
            // Get the current (nano) time in the computer
            long startTime = System.nanoTime();

            // Run the solver to figure out the time complexity for this step size
            solver.solve(function, y0, finalTime, h);

            // Get the current (nano) time in the computer after the solver's operation
            long endTime = System.nanoTime();

            // Get the difference in (nano) time between the end time and start time to figure out how much time has elasped.
            long elapsedTime = endTime - startTime;

            // Save the elapsed time into the array
            measuredRunTimes[measuresTaken] = elapsedTime;

            // Increment the array iterator with 1
            measuresTaken++;
        }

        /*
        * Current file format/design:
        * Solver
        * Step size:    h1  h2  h3  h4
        * Runtime:      t1  t2  t3  t4
        * */

        String columnSeparator = ",";
        String[] l = new String[5];

        StringBuilder sb = new StringBuilder();
        if (solver instanceof Euler){ sb.append("Euler"); }
        else if (solver instanceof Kutta){ sb.append("Kutta"); }
        else { sb.append("Verlet"); }

        l[0] = sb.toString();
        l[1] = "Step sizes are displayed in seconds";

        sb = new StringBuilder();
        sb.append("Measured runtimes are displayed in ");
        if (timeScale == (long) 1e6){ sb.append("milliseconds"); }
        else if (timeScale == (long) 1e9){ sb.append("seconds"); }
        else { sb.append("nanoseconds"); }
        l[2] = sb.toString();


        sb = new StringBuilder("Step size" + columnSeparator);
        for(int i = 0; i < stepSizes.length; i++){
            sb.append(stepSizes[i]).append(columnSeparator);
        }
        l[3] = sb.toString();

        sb = new StringBuilder("Runtime" + columnSeparator);
        for(int i = 0; i < measuredRunTimes.length; i++){
            long scaledTime = measuredRunTimes[i] / timeScale;
            sb.append(measuredRunTimes[i]).append(columnSeparator);
        }
        l[4] = sb.toString();


        // Create a date time formatter to display the current date and time in the desired format
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss");

        // Get the current (local) date time
        LocalDateTime now = LocalDateTime.now();

        // Assign the experiment file name eto be the current (local) date time
        String fileName =  dtf.format(now);

        // Assign a directory path to where the file should be created.
        String filePath = "src/experiments/";

        // Assign a file type to the experiment file | .csv = for excel/spreadsheets files
        String fileType = ".csv";

        try{
            File f = new File(filePath + fileName + fileType);

            PrintWriter writer = new PrintWriter(f);

            for(String s : l){
                writer.println(s);
            }

            writer.close();
        }
        catch (Exception e){ e.printStackTrace(); }
    }
}
