package titan.fileIO;

import java.util.ArrayList;

public class ReadingTheWindData {


    public static void main(String[]args)
    {
       WindDataReader windData = new WindDataReader();
        windData.read();


        ArrayList<Double> aX = windData.getwindSpeed(); // Wind Speed
        ArrayList<Double> aY = windData.getAltitude(); // Altitude

        LinearRegression slr = new LinearRegression(aX, aY);

        System.out.println("Slope = "+slr.getSlope()+"  Intercept = "+slr.getIntercept());
        System.out.println("y = "+slr.getSlope()+"x + ("+slr.getIntercept()+")");;

    }

}
