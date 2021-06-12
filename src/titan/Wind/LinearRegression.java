package titan.Wind;

import java.util.ArrayList;

import static java.lang.Math.pow;

/**
 * Simple Linear Regression: Find a linear function that represents a set of data points.
 * @author Xuan Ngo
 */
public class LinearRegression {


        private ArrayList<Double> m_aX = new ArrayList<Double>();
        private ArrayList<Double> m_aY = new ArrayList<Double>();

        private ArrayList<Double> m_aXX = null;
        private ArrayList<Double> m_aXY = null;

        private double m_dSumOfXs = 0;
        private double m_dSumOfYs = 0;
        private double m_dSumOfXXs = 0;
        private double m_dSumOfXYs = 0;

        private double m_dSlope = 0;


        public LinearRegression(final ArrayList<Double> aX, final ArrayList<Double> aY)
        {
            this.m_aX = aX;
            this.m_aY = aY;

            // Prepare sigma values.
            this.m_dSumOfXs = this.sum(aX);
            this.m_dSumOfYs = this.sum(aY);

            this.calculateXX();
            this.m_dSumOfXXs = this.sum(this.m_aXX);

            this.calculateXY();
            this.m_dSumOfXYs = this.sum(this.m_aXY);
        }

        /**
         * Slope = (NΣXY - (ΣX)(ΣY)) / (NΣ(X^2) - (ΣX)^2)
         * where, N = number of values.
         * @return
         */
        public double getSlope()
        {
            final int iNumOfValues = this.m_aX.size();
            final double dSlope = ((iNumOfValues*this.m_dSumOfXYs) - (this.m_dSumOfXs*this.m_dSumOfYs)) / ((iNumOfValues*this.m_dSumOfXXs) - pow(this.m_dSumOfXs, 2.0));

            this.m_dSlope = dSlope;
            return this.m_dSlope;
        }
        /**
         * Intercept = (ΣY - b(ΣX)) / N
         * where, N = number of values.
         * @return
         */
        public double getIntercept()
        {
            // If slope is 0 throw an exception.
            if(this.m_dSlope==0)
                throw new RuntimeException("Run this.getSlope() to calculate the slope first.");

            final int iNumOfValues = this.m_aX.size();
            final double dConstant = (this.m_dSumOfYs - (this.m_dSlope*this.m_dSumOfXs)) / iNumOfValues;

            return dConstant;
        }
        /**
         * Sum all values in the array list.
         * @param aD
         * @return
         */
        private double sum(ArrayList<Double> aD)
        {
            double dSum=0;
            for(int i=0; i<aD.size(); i++)
            {
                dSum+=aD.get(i).doubleValue();
            }
            return dSum;
        }

        /**
         * Calculate X*Y for all values.
         */
        private void calculateXY()
        {
            this.m_aXY = new ArrayList<Double>();

            for(int i=0; i<this.m_aX.size(); i++)
            {
                double x = this.m_aX.get(i).doubleValue();
                double y = this.m_aY.get(i).doubleValue();

                this.m_aXY.add(new Double(x*y));
            }
        }
        /**
         * Calculate X*X for all values.
         */
        private void calculateXX()
        {
            this.m_aXX = new ArrayList<Double>();

            for(int i=0; i<this.m_aX.size(); i++)
            {
                double x = this.m_aX.get(i).doubleValue();
                this.m_aXX.add(new Double(x*x));
            }
        }
    }


