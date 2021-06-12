package titan.Wind;

import titan.fileIO.*;

import java.util.ArrayList;

public class WindFunction {


    LinearRegression slr;
    WindDataReader windData;
    /*
    The following method converts the wind speed to a force that will be applied on the rocket's X axis during the landing
    Using Linear Regression we obtain a function that gives us the wind speed of a given altitude. The model is stochastic,
    therefore the wind speed is multiplied by a random number between 0.8 and 1.2. It also has a 1/100 chance of changing direction
    @param altitude : The current altitude of the rocket
    @return force : The resulting force based on the wind speed (To be applied only on the X axis)
     */
    public double windForce(double altitude)
    {
        windData = new WindDataReader();
        windData.read();
        ArrayList<Double> aX = windData.getwindSpeed(); // Wind Speed
        ArrayList<Double> aY = windData.getAltitude(); // Altitude

        slr = new LinearRegression(aX, aY);
        double slope = slr.getSlope();
        double constant = slr.getIntercept();

        // In order to make our wind stochastic
        // We create a random factor between 0.8 and 1.2 and factor the wind speed with it
        double random  = getRandomDoubleBetweenRange(0.8,1.2);

            // There is a chance of 1/100 that the wind direction changes ( sign )
            if(getRandomDoubleBetweenRange(0,100) == 50)
            {
                random = - random;
            }

            // The following is the formula for the force which is the Drag Equation F = 1/2 * P * v^2 * A
            // P is the air density of Titan
            // A is the area of the landing module exposed. We suppose that it is constant and it has a value of 10 m^2
        double force = 0.5 * 1.92 * (random * (slope * altitude + constant) * (random * (slope * altitude + constant))) * 10;

            //The following force only applies on the X axis, assuming the wind direction is only horizontal
            return force;


       // System.out.println("Slope = "+slr.getSlope() + "  Intercept = "+slr.getIntercept());
        // System.out.println("y = "+slr.getSlope()+"x + ("+slr.getIntercept()+")");


    }


    public static double getRandomDoubleBetweenRange(double min, double max){
        double x = (Math.random()*((max-min)+1))+min;
        return x;
    }



}
