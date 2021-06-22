package titan.solvers;

import interfaces.given.*;
import interfaces.own.DataInterface;
import titan.math.Vector3d;
import titan.physics.State;
import titan.utility.Rate;

/**
 * Class that is coding representation of Runge-Kutta 4th order.
 * The local truncation error of RK4 is of order Ot(h^5), giving a global truncation error of order O(h^4).
 * There are 2 variations of the fourth order method: Classical and Modern
 *
 * Classical Kutta's formula for computing next point:
 * y[n+1]=y[n]+1/6(k[1]+2k[2]+2k[3]+k[4])
 * t[n+1]=t[n]+h
 *
 * where h is step size and
 * k[1]=hf(t[n],y[n])
 * k[2]=hf(t[n]+h/2, y[n]+k[1]/2)
 * k[3]=hf(t[n]+h/2, y[n]+k[2]/2)
 * k[4]=hf(t[n]+h, y[n]+k[3])
 *
 * Modern Kutta's formula for computing next point:
 * y[n+1]=y[n]+1/8(k[1]+3k[2]+3k[3]+k[4])
 * t[n+1]=t[n]+h
 *
 * where h is step size and
 * k[1]=hf(t[n],y[n])
 * k[2]=hf(t[n]+h 1/3, y[n]+k[1] 1/3)
 * k[3]=hf(t[n]+h 2/3, y[n]+k[2] 1/3)
 * k[4]=hf(t[n]+h, y[n]+k[1]-k[2]+k[3])
 *
 */
public class Kutta implements ODESolverInterface {
    /**
     * Documentation given from the interface:
     * Solve the differential equation by taking multiple steps.
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time
     * @return  an array of size ts.length with all intermediate states along the path
     */

    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts){
        StateInterface[] states = new State[ts.length];
        states[0] = y0;

        for(int i = 1; i < ts.length; i++){
            states[i] = step(f, ts[i], states[i-1], ts[i]-ts[i-1]);
        }
        return states;
    }

    /**
     * Documentation given from the interface:
     * Solve the differential equation by taking multiple steps of equal size, starting at time 0.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   tf      the final time
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 including all intermediate states along the path
     */

    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h){
        int fit = (int) (tf / h) + 1;

        StateInterface[] states;
        if (tf % h != 0){ states = new State[fit + 1]; }
        else { states = new State[fit]; }
        states[0] = y0;

        for(int i = 1; i < fit; i++){
            states[i] = step(f, i * h, states[i-1], h);
        }
        if (fit < states.length){
            double remainingTime = tf % h;
            states[states.length - 1] = step(f, tf, states[states.length - 2], remainingTime);
        }
        return states;
    }


    /**
     * Kutta's formula for computing next point:
     * y[n+1]=y[n]+1/6(k[1]+2k[2]+2k[3]+k[4])
     * t[n+1]=t[n]+h
     *
     * where h is step size and
     *
     * k[1]=hf(t[n],y[n])
     * k[2]=hf(t[n]+h/2, y[n]+k[1]/2)
     * k[3]=hf(t[n]+h/2, y[n]+k[2]/2)
     * k[4]=hf(t[n]+h, y[n]+k[3])
     *
     * Documentation given from the interface:
     * Update rule for one step.
     *
     * @param   f   the function defining the differential equation dy/dt=f(t,y)
     * @param   t   the time
     * @param   y   the state
     * @param   h   the step size
     * @return  the new state after taking one step
     */

    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h){
        Rate r = (Rate) f.call(t, y);
        Rate k1 = r.mul(h);
        System.out.println("k1 = " + k1.getPosRoc()[0].getX());

        State s = (State) y;
        DataInterface[] objects = s.getObjects();
        DataInterface[] assist = new DataInterface[objects.length];

        Vector3dInterface[] v = k1.getPosRoc();
        Vector3dInterface[] a = k1.getVelRoc();
        for(int i = 0; i < objects.length; i++){
            Vector3dInterface pos = objects[i].getPosition();
            Vector3dInterface vel = objects[i].getVelocity();
            double[] displacement = { pos.getX() + v[i].getX() / 2, pos.getY() + v[i].getY() / 2, pos.getZ() + v[i].getZ() / 2 };
            double[] velocity = { vel.getX() + a[i].getX() / 2, vel.getY() + a[i].getY() / 2, vel.getZ() + a[i].getZ() / 2 };
            assist[i] = objects[i].update(
                    new Vector3d(displacement),
                    new Vector3d(velocity)
            );
        }
        s = new State(assist);

        r = (Rate) f.call(t + h/2.0, s);
        Rate k2 = r.mul(h);
        System.out.println("k2 = " + k2.getPosRoc()[0].getX());

        v = k2.getPosRoc();
        a = k2.getVelRoc();
        assist = new DataInterface[objects.length];
        for(int i = 0; i < objects.length; i++){
            Vector3dInterface pos = objects[i].getPosition();
            Vector3dInterface vel = objects[i].getVelocity();
            double[] displacement = { pos.getX() + v[i].getX() / 2, pos.getY() + v[i].getY() / 2, pos.getZ() + v[i].getZ() / 2 };
            double[] velocity = { vel.getX() + a[i].getX() / 2, vel.getY() + a[i].getY() / 2, vel.getZ() + a[i].getZ() / 2 };
            assist[i] = objects[i].update(
                    new Vector3d(displacement),
                    new Vector3d(velocity)
            );
        }
        s = new State(assist);

        r = (Rate) f.call(t + h/2.0, s);
        Rate k3 = r.mul(h);
        System.out.println("k3 = " + k3.getPosRoc()[0].getX());

        v = k3.getPosRoc();
        a = k3.getVelRoc();
        assist = new DataInterface[objects.length];
        for(int i = 0; i < objects.length; i++){
            Vector3dInterface pos = objects[i].getPosition();
            Vector3dInterface vel = objects[i].getVelocity();
            double[] displacement = { pos.getX() + v[i].getX(), pos.getY() + v[i].getY(), pos.getZ() + v[i].getZ() };
            double[] velocity = { vel.getX() + a[i].getX(), vel.getY() + a[i].getY(), vel.getZ() + a[i].getZ() };
            assist[i] = objects[i].update(
                    new Vector3d(displacement),
                    new Vector3d(velocity)
            );
        }
        s = new State(assist);

        r = (Rate) f.call(t + h, s);
        Rate k4 = r.mul(h);
        System.out.println("k4 = " + k4.getPosRoc()[0].getX());

        Vector3dInterface[] vK1 = k1.getPosRoc();
        Vector3dInterface[] vK2 = k2.getPosRoc();
        Vector3dInterface[] vK3 = k3.getPosRoc();
        Vector3dInterface[] vK4 = k4.getPosRoc();

        Vector3dInterface[] aK1 = k1.getVelRoc();
        Vector3dInterface[] aK2 = k2.getVelRoc();
        Vector3dInterface[] aK3 = k3.getVelRoc();
        Vector3dInterface[] aK4 = k4.getVelRoc();

        assist = new DataInterface[objects.length];
        for(int i = 0; i < objects.length; i++){
            Vector3dInterface pos = objects[i].getPosition();
            Vector3dInterface vel = objects[i].getVelocity();
            double[] displacement = {
                    pos.getX() + (h / 6) * (vK1[i].getX() + 2 * vK2[i].getX() + 2 * vK3[i].getX() + vK4[i].getX()),
                    pos.getY() + (h / 6) * (vK1[i].getY() + 2 * vK2[i].getY() + 2 * vK3[i].getY() + vK4[i].getY()),
                    pos.getY() + (h / 6) * (vK1[i].getZ() + 2 * vK2[i].getZ() + 2 * vK3[i].getZ() + vK4[i].getZ())
            };
            double[] velocity = {
                    vel.getX() + (h / 6) * (aK1[i].getX() + 2 * aK2[i].getX() + 2 * aK3[i].getX() + aK4[i].getX()),
                    vel.getY() + (h / 6) * (aK1[i].getY() + 2 * aK2[i].getY() + 2 * aK3[i].getY() + aK4[i].getY()),
                    vel.getY() + (h / 6) * (aK1[i].getZ() + 2 * aK2[i].getZ() + 2 * aK3[i].getZ() + aK4[i].getZ())
            };

            assist[i] = objects[i].update(
                    new Vector3d(displacement),
                    new Vector3d(velocity)
            );
        }
        System.out.println("y[n+1] = " + assist[0].getPosition().getX());
        System.out.println();

        State y0 = (State) y;
        s = new State(assist);
        s.setPrevious(y0);
        s.setPeriod(y0.getPeriod() + h);
        return s;
    }
}
