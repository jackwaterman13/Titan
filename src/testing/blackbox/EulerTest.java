package testing.blackbox;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import org.junit.jupiter.api.Test;
import titan.math.Function;
import titan.math.Vector3d;
import titan.physics.State;
import titan.solvers.Euler;
import titan.utility.Planet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests Euler's method in solving Ordinary Differential Equations
 * y[n+1] = y[n] + h f(t, y[n])
 *
 * Blackbox class ignores influence of gravity by setting mass of calculations to 0
 * Therefore, the only thing it will be testing is the the formula with the differential equation itself
 */
public class EulerTest {

    /**
     * Tests module integrity for y[n] > 0
     */
    @Test public void testPositive(){
        ODESolverInterface solver = new Euler();
        ODEFunctionInterface function = new Function(); // -> dy/dt = f(t, y) = [dx/dt, dv/dt] = [v, a]
        DataInterface data = new Planet();
        Vector3dInterface x = new Vector3d(1.0, 0.0, 0.0);
        Vector3dInterface v = new Vector3d(1.0, 0.0, 0.0);
        data.setPosition(x);
        data.setVelocity(v);

        DataInterface[] arr = { data };
        StateInterface y0 = new State(arr);
        State result = (State) solver.step(function, 10.0, y0, 2.0);
        data = result.getObjects()[0];
        x = data.getPosition();
        v = data.getVelocity();

        assertEquals(1.2, x.getX());
        assertEquals(0.0, x.getY());
        assertEquals(0.0, x.getZ());

        assertEquals(1.2, v.getX());
        assertEquals(0.0, v.getY());
        assertEquals(0.0, v.getZ());
    }

    /**
     * Tests module integrity for y[n] < 0
     */
    @Test public void testNegative(){
        ODESolverInterface solver = new Euler();
        ODEFunctionInterface function = new Function(); // -> dy/dt = f(t, y) = [dx/dt, dv/dt] = [v, a]
        DataInterface data = new Planet();
        Vector3dInterface x = new Vector3d(-1.0, 0.0, 0.0);
        Vector3dInterface v = new Vector3d(-1.0, 0.0, 0.0);
        data.setPosition(x);
        data.setVelocity(v);

        DataInterface[] arr = { data };
        StateInterface y0 = new State(arr);
        State result = (State) solver.step(function, 10.0, y0, 2.0);
        data = result.getObjects()[0];
        x = data.getPosition();
        v = data.getVelocity();

        assertEquals(-1.2, x.getX());
        assertEquals(0.0, x.getY());
        assertEquals(0.0, x.getZ());

        assertEquals(-1.2, v.getX());
        assertEquals(0.0, v.getY());
        assertEquals(0.0, v.getZ());
    }


    /**
     * Tests module integrity for y[n] = 0
     */
    @Test public void testZero(){
        ODESolverInterface solver = new Euler();
        ODEFunctionInterface function = new Function(); // -> dy/dt = f(t, y) = [dx/dt, dv/dt] = [v, a]
        DataInterface data = new Planet();
        Vector3dInterface x = new Vector3d(0.0, 0.0, 0.0);
        Vector3dInterface v = new Vector3d(0.0, 0.0, 0.0);
        data.setPosition(x);
        data.setVelocity(v);

        DataInterface[] arr = { data };
        StateInterface y0 = new State(arr);
        State result = (State) solver.step(function, 10.0, y0, 2.0);
        data = result.getObjects()[0];
        x = data.getPosition();
        v = data.getVelocity();

        assertEquals(0.0, x.getX());
        assertEquals(0.0, x.getY());
        assertEquals(0.0, x.getZ());

        assertEquals(0.0, v.getX());
        assertEquals(0.0, v.getY());
        assertEquals(0.0, v.getZ());
    }
}
