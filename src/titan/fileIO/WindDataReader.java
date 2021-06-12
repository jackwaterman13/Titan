package titan.fileIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
public class WindDataReader {


    String fileLocation = "src/titan/fileIO/WindData.txt";
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<Double> altitude = new ArrayList<Double>();
    private ArrayList<Double> windSpeed = new ArrayList<Double>();
    private ArrayList<Double> windError = new ArrayList<Double>();


    /**
     * Reads and saves data from the file whose location is stored inside the class.
     */
    public void read() {
        try {
            File f = new File(fileLocation);
            if (!f.exists()) {
                System.out.println("File at specified location does not exist! | Location: " + fileLocation);
                return;
            }
            Scanner reader = new Scanner(f);
            while (reader.hasNextLine()) {
                date.add(reader.next());
                altitude.add(Double.valueOf(reader.next()));
                windSpeed.add(Double.valueOf(reader.next()));
                windError.add(Double.valueOf(reader.next()));
            }
            reader.close();
        }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }

    }


            /**
             * Stores the file path inside the class
             *
             * @param fileLocation2Use - the entire file path where our target data file is at.
             */
            public void setFilePath (String fileLocation2Use)
            {
                fileLocation = fileLocation2Use;
            }


            /**
             * Gets the file data stored in a list
             *
             * @return The list containing data objects extracted from the file
             */
            public ArrayList<Double> getAltitude ()
            {
               return altitude;
            }

           public ArrayList<Double> getwindSpeed ()
           {
               return windSpeed;
           }


            /**
             * Gets the file path that is remembered inside the class
             *
             * @return A String containing the entire path to where the data file is located at
             */
            public String getFilePath ()
            {
                return fileLocation;
            }

    public static double getRandomDoubleBetweenRange(double min, double max){
        double x = (Math.random()*((max-min)+1))+min;
        return x;
    }



        }


