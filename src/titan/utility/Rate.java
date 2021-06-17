package titan.utility;

import interfaces.given.RateInterface;
import interfaces.given.Vector3dInterface;
import titan.math.Vector3d;

/**
 * Supportive class that holds the average dy/dt for each planet
 */
public class Rate implements RateInterface {
    private final Vector3dInterface[] xRateOfChange;
    private final Vector3dInterface[] vRateOfChange;

    /**
     * Constructs a new rate-of-change dy/dt = [v, a]
     *
     * @param xRateOfChange - dx/dt = v
     * @param vRateOfChange - dv/dt = a
     */
    public Rate(Vector3dInterface[] xRateOfChange, Vector3dInterface[] vRateOfChange){
        this.xRateOfChange = xRateOfChange;
        this.vRateOfChange = vRateOfChange;
    }

    /**
     * Addition of 2 rate-of-changes to get 1 final rate-of-change
     *
     * @param other - rate-of-change that needs to be added
     * @return Result of this rate-of-change + other rate-of-change
     */
    public Rate add(Rate other){
        Vector3dInterface[] xRateOfChange = other.getPosRoc();
        Vector3dInterface[] vRateOfChange = other.getVelRoc();

        Vector3dInterface[] xRocFinal = new Vector3d[this.xRateOfChange.length];
        Vector3dInterface[] vRocFinal = new Vector3d[this.vRateOfChange.length];
        for(int i = 0; i < xRateOfChange.length; i++){
            xRocFinal[i] = xRateOfChange[i].add(this.xRateOfChange[i]);
            vRocFinal[i] = vRateOfChange[i].add(this.vRateOfChange[i]);
        }
        return new Rate(xRocFinal, vRocFinal);
    }

    /**
     * Scalar multiplication of rate-of-change
     *
     * @param scalar - weight used in multiplication
     */
    public Rate mul(double scalar){
        Vector3dInterface[] xRateOfChange = new Vector3d[this.xRateOfChange.length];
        Vector3dInterface[] vRateOfChange = new Vector3d[this.vRateOfChange.length];
        for(int i = 0; i < xRateOfChange.length; i++){
            xRateOfChange[i] = this.xRateOfChange[i].mul(scalar);
            vRateOfChange[i] = this.vRateOfChange[i].mul(scalar);
        }
        return new Rate(xRateOfChange, vRateOfChange);
    }

    /**
     * Scalar multiplication of rate-of-change
     *
     * @param scalar the double used in the multiplication step
     * @param other  the rate-of-change used in the multiplication step
     * @return the result of the multiplication step added to this rate-of-change,
     */
    public Rate addMul(double scalar, Rate other){
        Vector3dInterface[] xRateOfChange = other.getPosRoc();
        Vector3dInterface[] vRateOfChange = other.getVelRoc();

        Vector3dInterface[] xRocFinal = new Vector3d[this.xRateOfChange.length];
        Vector3dInterface[] vRocFinal = new Vector3d[this.vRateOfChange.length];

        for(int i = 0; i < xRateOfChange.length; i++){
            xRocFinal[i] = this.xRateOfChange[i].addMul(scalar, xRateOfChange[i]);
            vRocFinal[i] = this.vRateOfChange[i].addMul(scalar, vRateOfChange[i]);
        }
        return new Rate(xRocFinal, vRocFinal);
    }

    /**
     * Accesses the average dx/dt for each planet
     * Array is ordered according to the order of the objects in the state class
     *
     * @return A 3-dimensional vector array containing the dx/dt = v for each planet.
     */
    public Vector3dInterface[] getPosRoc(){ return xRateOfChange; }

    /**
     * Accesses the average dv/dt for each planet
     * Array is ordered according to the order of the objects in the state class
     *
     * @return A 3-dimensional vector array containing the dv/dt = a for each planet.
     */
    public Vector3dInterface[] getVelRoc(){ return vRateOfChange; }
}
