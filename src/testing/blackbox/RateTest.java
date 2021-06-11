package testing.blackbox;

import interfaces.given.Vector3dInterface;
import org.junit.jupiter.api.Test;
import titan.math.Vector3d;
import titan.utility.Rate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class to tests the integrity of the Rate class containing the rate-of-changes for both
 * position and velocity
 */
public class RateTest {

    /**
     * Tests integrity of rate-of-change additions for positive values
     */
    @Test public void testAddPositive(){
        Vector3dInterface[] xRateOfChange = { new Vector3d() };
        Vector3dInterface[] vRateOfChange = { new Vector3d() };
        Rate r = new Rate(xRateOfChange, vRateOfChange);

        xRateOfChange = new Vector3dInterface[]{ new Vector3d(1.0, 0.0, 0.0) };
        vRateOfChange = new Vector3dInterface[]{ new Vector3d(1.0, 0.0, 0.0) };

        r = r.add(new Rate(xRateOfChange, vRateOfChange));

        xRateOfChange = r.getPosRoc();
        vRateOfChange = r.getVelRoc();

        assertEquals(1.0, xRateOfChange[0].getX());
        assertEquals(0.0, xRateOfChange[0].getY());
        assertEquals(0.0, xRateOfChange[0].getZ());

        assertEquals(1.0, vRateOfChange[0].getX());
        assertEquals(0.0, vRateOfChange[0].getY());
        assertEquals(0.0, vRateOfChange[0].getZ());
    }

    /**
     * Tests integrity of rate-of-change addition for negative values
     */
    @Test public void testAddNegative(){
        Vector3dInterface[] xRateOfChange = { new Vector3d() };
        Vector3dInterface[] vRateOfChange = { new Vector3d() };
        Rate r = new Rate(xRateOfChange, vRateOfChange);

        xRateOfChange = new Vector3dInterface[]{ new Vector3d(-1.0, 0.0, 0.0) };
        vRateOfChange = new Vector3dInterface[]{ new Vector3d(-1.0, 0.0, 0.0) };

        r = r.add(new Rate(xRateOfChange, vRateOfChange));

        xRateOfChange = r.getPosRoc();
        vRateOfChange = r.getVelRoc();

        assertEquals(-1.0, xRateOfChange[0].getX());
        assertEquals(0.0, xRateOfChange[0].getY());
        assertEquals(0.0, xRateOfChange[0].getZ());

        assertEquals(-1.0, vRateOfChange[0].getX());
        assertEquals(0.0, vRateOfChange[0].getY());
        assertEquals(0.0, vRateOfChange[0].getZ());
    }

    /**
     * Tests integrity of rate-of-change addition for zero
     */
    @Test public void testAddZero(){
        Vector3dInterface[] xRateOfChange = { new Vector3d() };
        Vector3dInterface[] vRateOfChange = { new Vector3d() };
        Rate r = new Rate(xRateOfChange, vRateOfChange);

        xRateOfChange = new Vector3dInterface[]{ new Vector3d() };
        vRateOfChange = new Vector3dInterface[]{ new Vector3d() };

        r = r.add(new Rate(xRateOfChange, vRateOfChange));

        xRateOfChange = r.getPosRoc();
        vRateOfChange = r.getVelRoc();

        assertEquals(0.0, xRateOfChange[0].getX());
        assertEquals(0.0, xRateOfChange[0].getY());
        assertEquals(0.0, xRateOfChange[0].getZ());

        assertEquals(0.0, vRateOfChange[0].getX());
        assertEquals(0.0, vRateOfChange[0].getY());
        assertEquals(0.0, vRateOfChange[0].getZ());
    }

    /**
     * Tests integrity of rate-of-change multiplication with positive values
     */
    @Test public void testMulPositive(){
        Vector3dInterface[] xRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Vector3dInterface[] vRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Rate r = new Rate(xRateOfChange, vRateOfChange);

        r = r.mul(3.0);

        xRateOfChange = r.getPosRoc();
        vRateOfChange = r.getVelRoc();

        assertEquals(3.0, xRateOfChange[0].getX());
        assertEquals(0.0, xRateOfChange[0].getY());
        assertEquals(0.0, xRateOfChange[0].getZ());

        assertEquals(3.0, vRateOfChange[0].getX());
        assertEquals(0.0, vRateOfChange[0].getY());
        assertEquals(0.0, vRateOfChange[0].getZ());
    }

    /**
     * Tests integrity of rate-of-change multiplication with negative values
     */
    @Test public void testMulNegative(){
        Vector3dInterface[] xRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Vector3dInterface[] vRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Rate r = new Rate(xRateOfChange, vRateOfChange);

        r = r.mul(-1.0);

        xRateOfChange = r.getPosRoc();
        vRateOfChange = r.getVelRoc();

        assertEquals(-1.0, xRateOfChange[0].getX());
        assertEquals(-0.0, xRateOfChange[0].getY());
        assertEquals(-0.0, xRateOfChange[0].getZ());

        assertEquals(-1.0, vRateOfChange[0].getX());
        assertEquals(-0.0, vRateOfChange[0].getY());
        assertEquals(-0.0, vRateOfChange[0].getZ());
    }

    /**
     * Tests integrity of rate-of-change multiplication with zero
     */
    @Test public void testMulZero(){
        Vector3dInterface[] xRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Vector3dInterface[] vRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Rate r = new Rate(xRateOfChange, vRateOfChange);

        r = r.mul(0.0);

        xRateOfChange = r.getPosRoc();
        vRateOfChange = r.getVelRoc();

        assertEquals(0.0, xRateOfChange[0].getX());
        assertEquals(0.0, xRateOfChange[0].getY());
        assertEquals(0.0, xRateOfChange[0].getZ());

        assertEquals(0.0, vRateOfChange[0].getX());
        assertEquals(0.0, vRateOfChange[0].getY());
        assertEquals(0.0, vRateOfChange[0].getZ());
    }

    /**
     * Tests integrity of rate-of-change addMul operation
     * Same addMul principle as the VectorInterface one
     */
    @Test public void testAddMul(){

    }

    /**
     * Tests the correctness of the constructor and get method for the rate-of-changes
     */
    @Test public void testGetRateOfChanges(){
        Vector3dInterface[] xRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Vector3dInterface[] vRateOfChange = { new Vector3d(1.0, 0.0, 0.0) };
        Rate r = new Rate(xRateOfChange, vRateOfChange);

        xRateOfChange = r.getPosRoc();
        vRateOfChange = r.getVelRoc();

        assertEquals(1.0, xRateOfChange[0].getX());
        assertEquals(0.0, xRateOfChange[0].getY());
        assertEquals(0.0, xRateOfChange[0].getZ());

        assertEquals(1.0, vRateOfChange[0].getX());
        assertEquals(0.0, vRateOfChange[0].getY());
        assertEquals(0.0, vRateOfChange[0].getZ());
    }
}
