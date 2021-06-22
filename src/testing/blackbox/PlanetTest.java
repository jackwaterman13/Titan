package testing.blackbox;

import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.GuiObjectInterface;
import org.junit.jupiter.api.Test;
import titan.math.Vector3d;
import titan.utility.Planet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanetTest {
    /**
     * Tests whether setName() and getName() are functioning properly
     */
    @Test public void testName(){
        DataInterface planet = new Planet();
        planet.setName("P");
        assertEquals("P", planet.getName());
    }

    /**
     * Tests whether setMass() and getMass() are functioning properly
     */
    @Test public void testMass(){
        DataInterface planet = new Planet();
        planet.setMass(1.0);
        assertEquals(1.0, planet.getMass());
    }

    /**
     * Tests whether getRadius() and radius initializer in the constructor are working properly together
     */
    @Test public void testRadius(){
        Planet planet = new Planet(null, 0.0, 6.123e5, null, null);
        assertEquals(6.123e5, planet.getRadius());
    }

    /**
     * Tests whether setPosition() and getPosition() are functioning properly
     */
    @Test public void testPosition(){
        DataInterface planet = new Planet();
        Vector3dInterface vector = new Vector3d(1.0, 0.0, 0.0);
        planet.setPosition(vector);

        Vector3dInterface position = planet.getPosition();
        assertEquals(1.0, position.getX());
        assertEquals(0.0, position.getY());
        assertEquals(0.0, position.getZ());
    }

    /**
     * Tests whether setVelocity() and getVelocity() are functioning properly
     */
    @Test public void testVelocity(){
        DataInterface planet = new Planet();
        Vector3dInterface vector = new Vector3d(1.0, 0.0, 0.0);
        planet.setVelocity(vector);

        Vector3dInterface velocity = planet.getVelocity();
        assertEquals(1.0, velocity.getX());
        assertEquals(0.0, velocity.getY());
        assertEquals(0.0, velocity.getZ());
    }

    /**
     * Tests whether setGravity() and getGravity() are functioning properly
     */
    @Test public void testGravity(){
        DataInterface planet = new Planet();
        Vector3dInterface vector = new Vector3d(1.0, 0.0, 0.0);
        planet.setGravity(vector);

        Vector3dInterface gravity = planet.getGravity();
        assertEquals(1.0, gravity.getX());
        assertEquals(0.0, gravity.getY());
        assertEquals(0.0, gravity.getZ());
    }


    /**
     * Tests integrity of the distance method
     */
    @Test public void testDist(){
        DataInterface planet1 = new Planet();
        planet1.setPosition(new Vector3d(6, 9, 7));

        DataInterface planet2 = new Planet();
        planet2.setPosition(new Vector3d(8, 12, 13));

        assertEquals(Math.sqrt(49), planet1.distance(planet2));
    }

    /**
     * Tests whether the distance vector is calculated properly
     */
    @Test public void testDistance3d(){
        DataInterface planet1 = new Planet();
        planet1.setPosition(new Vector3d(10, 90, 72));

        DataInterface planet2 = new Planet();
        planet2.setPosition(new Vector3d(-8, -12, 135));

        Vector3dInterface distance = planet1.distance3d(planet2);
        assertEquals(-18, distance.getX());
        assertEquals(-102, distance.getY());
        assertEquals(63, distance.getZ());
    }


    /**
     * Tests the integrity of the toString() method
     */
    @Test public void testToString(){
        DataInterface planet = new Planet();
        planet.setName("P");
        planet.setPosition(new Vector3d(1.0, 2.0, 3.0));
        planet.setVelocity(new Vector3d(4.0, 5.0, 6.0));

        String s = "P: { mass=0.0, r=0.0, x=1.0, y=2.0, z=3.0, vx=4.0, vy=5.0, vz=6.0 }";
        assertEquals(s, planet.toString());
    }

    /**
     * Tests the integrity of the update() method
     */
    @Test public void testUpdate(){
        DataInterface planet = new Planet(null, 0.0, 0.0, new Vector3d(), new Vector3d());
        DataInterface updated = planet.update(new Vector3d(1.0, 0.0, 0.0), new Vector3d(1.0, 0.0, 0.0));
        Vector3dInterface x = updated.getPosition();
        Vector3dInterface v = updated.getVelocity();

        assertEquals(1.0, x.getX());
        assertEquals(0.0, x.getY());
        assertEquals(0.0, x.getZ());

        assertEquals(1.0, v.getX());
        assertEquals(0.0, v.getY());
        assertEquals(0.0, v.getZ());
    }

    /**
     * Tests the integrity of resizePosition()
     */
    @Test public void testResizePosition(){
        GuiObjectInterface planet = new Planet(null, 0, 0, new Vector3d(1.0, 0.0, 0.0), null);
        Vector3dInterface x = planet.guiDisplacement(5.0);
        assertEquals(5.0, x.getX());
        assertEquals(0.0, x.getY());
        assertEquals(0.0, x.getZ());
    }
}
