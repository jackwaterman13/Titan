package titan.math;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.physics.State;
import titan.utility.Rate;

public class AccuracyFunction implements ODEFunctionInterface {

    /**
     * Equation for accuracy: dy/dt = e^-t - y^2; y(0) = 0
     * Exact solutions for the function:
     * y(0) = 0
     * y(1) = 0.503346658225,
     * y(2) = 0.478421766451,
     * y(5) = 0.237813428537,
     * y(10) = 0.110790590981
     */
    public RateInterface call(double t, StateInterface y) {
        State s = (State) y;
        DataInterface[] data = s.getObjects();
        Vector3dInterface[] xRateOfChange = new Vector3d[data.length];
        Vector3dInterface[] vRateOfChange = new Vector3d[data.length];
        for(int i = 0; i < data.length; i++){
            Vector3dInterface x = data[i].getPosition();
            double e1 = Math.exp(-t) - Math.pow(x.getX(), 2);
            double e2 = Math.exp(-t) - Math.pow(x.getY(), 2);
            double e3 = Math.exp(-t) - Math.pow(x.getZ(), 2);
            xRateOfChange[i] = new Vector3d(e1, e2, e3);

            Vector3dInterface v = data[i].getVelocity();
            e1 = Math.exp(-t) - Math.pow(v.getX(), 2);
            e2 = Math.exp(-t) - Math.pow(v.getY(), 2);
            e3 = Math.exp(-t) - Math.pow(v.getZ(), 2);
            vRateOfChange[i] = new Vector3d(e1, e2, e3);
        }
        return new Rate(xRateOfChange, vRateOfChange);
    }
}
