package titan.math;


import interfaces.given.Vector3dInterface;
import interfaces.own.Equation;

public class DefaultEquation implements Equation {
    /**
     * Generic differential equation = du/dt
     *
     * @param t - differential time t
     * @param u - differential vector u
     * @return Vector representing derivative du/dt
     */
    public Vector3dInterface derive(double t, Vector3dInterface u){
        return u.mul(Math.pow(t, -1));
    }
}
