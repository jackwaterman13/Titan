package testing.blackbox;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import org.junit.jupiter.api.Test;
import titan.math.Function;
import titan.math.Vector3d;
import titan.physics.State;
import titan.utility.Planet;
import titan.utility.Rate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the differential equation dy/dt = [dx/dt, dv/dt] = [v,a]
 * lets say dx = -10, dv = 6, dt =2
 * y0 = (x, v) = (10, 6)
 * y1 = f(t, y[0]) = f (2, (10, 6) = [dx/dt, dv/dt] = [10/2, 6/2] = [5, 3]
 */
public class FunctionTest {
    /**
     * Tests dy/dt = [dx/dt = 0/dt = 0, dv/dt = 0/dt = 0]
     */
    @Test public void testZero(){
        ODEFunctionInterface func = new Function();
        DataInterface data = new Planet();
        Vector3dInterface position = new Vector3d(0.0, 0.0, 0.0);
        Vector3dInterface velocity = new Vector3d(0.0, 0.0, 0.0);
        data.setPosition(position);
        data.setVelocity(velocity);

        DataInterface[] objectArray = { data };
        State y0 = new State(objectArray);
        Rate rateOfChange = (Rate) func.call(5, y0);
        Vector3dInterface[] xRateOfChange = rateOfChange.getPosRoc();
        Vector3dInterface[] vRateOfChange = rateOfChange.getVelRoc();
        Vector3dInterface v = xRateOfChange[0];
        Vector3dInterface a = vRateOfChange[0];

        assertEquals(0.0, v.getX());
        assertEquals(0.0, v.getY());
        assertEquals(0.0, v.getZ());

        assertEquals(0.0, a.getX());
        assertEquals(0.0, a.getY());
        assertEquals(0.0, a.getZ());
    }

    /**
     * Tests dy/dt = [dx/dt > 0, dv/dt > 0]
     */
    @Test public void testPositive(){
        ODEFunctionInterface func = new Function();
        DataInterface data = new Planet();
        Vector3dInterface pos = new Vector3d(1.0, 0.0, 0.0);
        Vector3dInterface vel = new Vector3d(1.0, 0.0, 0.0);
        data.setPosition(pos);
        data.setVelocity(vel);

        DataInterface[] arr = { data };
        Rate r = (Rate) func.call(2, new State(arr));

        Vector3dInterface[] xRateOfChange = r.getPosRoc();
        Vector3dInterface v = xRateOfChange[0];
        assertEquals(0.5, v.getX());
        assertEquals(0.0, v.getY());
        assertEquals(0.0, v.getZ());

        Vector3dInterface[] vRateOfChange = r.getVelRoc();
        Vector3dInterface a = vRateOfChange[0];
        assertEquals(0.5, a.getX());
        assertEquals(0.0, a.getY());
        assertEquals(0.0, a.getZ());
    }

    /**
     * Tests dy/dt = [dx/dt < 0, dv/dt < 0]
     */
    @Test public void testNegative(){
        ODEFunctionInterface func = new Function();
        DataInterface data = new Planet();
        Vector3dInterface pos = new Vector3d(-1.0, 0.0, 0.0);
        Vector3dInterface vel = new Vector3d(-1.0, 0.0, 0.0);
        data.setPosition(pos);
        data.setVelocity(vel);

        DataInterface[] arr = { data };
        Rate r = (Rate) func.call(2, new State(arr));

        Vector3dInterface[] xRateOfChange = r.getPosRoc();
        Vector3dInterface x1 = xRateOfChange[0];
        assertEquals(-0.5, x1.getX());
        assertEquals(0.0, x1.getY());
        assertEquals(0.0, x1.getZ());

        Vector3dInterface[] vRateOfChange = r.getVelRoc();
        Vector3dInterface v1 = vRateOfChange[0];
        assertEquals(-0.5, v1.getX());
        assertEquals(0.0, v1.getY());
        assertEquals(0.0, v1.getZ());
    }
}
