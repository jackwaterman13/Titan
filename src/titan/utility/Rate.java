package titan.utility;

import interfaces.given.RateInterface;
import interfaces.given.Vector3dInterface;

/**
 * Supportive class that holds the average dy/dt for each planet
 */
public class Rate implements RateInterface {
    private final Vector3dInterface[] xRoc;
    private final Vector3dInterface[] vRoc;

    public Rate(Vector3dInterface[] xRoc, Vector3dInterface[] vRoc){
        this.xRoc = xRoc;
        this.vRoc = vRoc;
    }

    /**
     * Accesses the average dx/dt for each planet
     * Array is ordered according to the order of the objects in the state class
     *
     * @return A 3-dimensional vector array containing the dx/dt for each planet.
     */
    public Vector3dInterface[] getPosRoc(){ return xRoc; }

    /**
     * Accesses the average dv/dt for each planet
     * Array is ordered according to the order of the objects in the state class
     *
     * @return A 3-dimensional vector array containing the dv/dt for each planet.
     */
    public Vector3dInterface[] getVelRoc(){ return vRoc; }

    public RateInterface add(RateInterface other){
        Rate r = (Rate) other;
        Vector3dInterface[] x = r.getPosRoc();
        Vector3dInterface[] v = r.getVelRoc();
        for(int i = 0; i < x.length; i++){
            x[i] = x[i].add(xRoc[i]);
            v[i] = v[i].add(vRoc[i]);
        }
        return new Rate(x, v);
    }
}
