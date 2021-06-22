package testing.blackbox;

import interfaces.given.ODEFunctionInterface;
import interfaces.given.ODESolverInterface;
import interfaces.own.DataInterface;
import org.junit.jupiter.api.Test;
import titan.math.Vector3d;
import titan.math.equations.Equation1;
import titan.physics.State;
import titan.solvers.Euler;
import titan.solvers.Kutta;
import titan.utility.Planet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KuttaTest {

    /**
     * Tests Classical Runge-Kutta method of 4th order for the differential equation dy/dt = y with y(0) = 1
     * Tested interval is [0, 0.2] is step size 0.2
     * Assumes that x value of the position vector of an object in the state is y for this function.
     * Therefore, initial position of the test object is (1, 0, 0) and everything else of the object is not relevant.
     */
    @Test public void testStep(){
        ODEFunctionInterface function = new Equation1();

        Planet p = new Planet();
        p.setPosition(new Vector3d(1, 0, 0));
        p.setVelocity(new Vector3d());

        DataInterface[] objects = { p };
        ODESolverInterface solver = new Kutta();

        State step = (State) solver.step(function, 0, new State(objects), 0.2);
        assertEquals(1.04428, step.getObjects()[0].getPosition().getX());
    }

    /**
     * Tests Classical Runge-Kutta method of 4th order for the differential equation dy/dt = y with y(0) = 1
     * Tested interval is [0, 0.8] where h is constant and equal to 0.2
     * Assumes that x value of the position vector of an object in the state is y for this function.
     * Therefore, initial position of the test object is (1, 0, 0) and everything else of the object is not relevant.
     */
    @Test public void testSolve(){
        ODEFunctionInterface function = new Equation1();

        Planet p = new Planet();
        p.setPosition(new Vector3d(1, 0, 0));
        p.setVelocity(new Vector3d());

        DataInterface[] objects = { p };
        ODESolverInterface solver = new Kutta();

        State[] results = (State[]) solver.solve(function, new State(objects), 0.4, 0.2);

        assertEquals(1,      results[0].getObjects()[0].getPosition().getX());   // t = 0
        assertEquals(1.04428,results[1].getObjects()[0].getPosition().getX());   // t = 0.2

        /* Can not hold true due to roundoff error */
        assertEquals(1.090520718, results[2].getObjects()[0].getPosition().getX());// t = 0.4
    }
}
