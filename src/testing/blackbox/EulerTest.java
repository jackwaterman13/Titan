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

public class EulerTest {
    @Test public void testStep(){
        ODESolverInterface solver = new Euler();
        ODEFunctionInterface function = new Function();
        DataInterface[] objects = { new Planet(null, 0.0, 0.0, new Vector3d(), new Vector3d(1.0, 0.0, 0.0)) };
        StateInterface y = new State(objects);
        State s = (State) solver.step(function, 1.0, y, 1.0);
        objects = s.getObjects();
        Vector3dInterface x = objects[0].getPosition();
        assertEquals(1.0, x.getX());
        assertEquals(0.0, x.getY());
        assertEquals(0.0, x.getZ());

        Vector3dInterface v = objects[0].getVelocity();
        assertEquals(1.0, v.getX());
        assertEquals(0.0, v.getY());
        assertEquals(0.0, v.getZ());
    }
}
