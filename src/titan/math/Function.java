package titan.math;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.physics.Newton;
import titan.physics.State;
import titan.utility.Rate;

public class Function implements ODEFunctionInterface {
    Newton newton = new Newton();
    public RateInterface call(double t, StateInterface y){
        State s = (State) y;
        DataInterface[] objects = s.getObjects();
        Vector3dInterface[] v = new Vector3dInterface[objects.length];
        Vector3dInterface[] a = new Vector3dInterface[objects.length];
        for(int i = 0; i < objects.length; i++){
            a[i] = newton.acceleration(objects, i);
            v[i] = objects[i].getVelocity();
        }
        return new Rate(v, a);
    }
}
