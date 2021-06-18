package testing.blackbox;

import interfaces.given.Vector3dInterface;
import org.junit.jupiter.api.Test;
import titan.math.Vector3d;
import titan.shapes.Box;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoxTest {

    @Test public void testDiagonalEqual() {

        Vector3dInterface titan = new Vector3d(0,0,5);
        Vector3dInterface rocket = new Vector3d(0,0,5);
        Vector3dInterface v = new Vector3d(0,0,5);

        Vector3dInterface[] diagonal = {rocket, titan};
        Box box = new Box();
        box.setPoints(diagonal);
        boolean check = box.withinShape(v.add(rocket));

        assertEquals(false, check);
    }

    @Test public void testDiagonalPosNeg() {

        Vector3dInterface titan = new Vector3d(0,0,9);
        Vector3dInterface rocket = new Vector3d(0,0,-2);
        Vector3dInterface v = new Vector3d(0,0,5);

        Vector3dInterface[] diagonal = {rocket, titan};
        Box box = new Box();
        box.setPoints(diagonal);
        boolean check = box.withinShape(v.add(rocket));

        assertEquals(true, check);
    }

    @Test public void testDiagonalNegPos() {

        Vector3dInterface titan = new Vector3d(0,0,-1);
        Vector3dInterface rocket = new Vector3d(0,0,2);

        Vector3dInterface v = new Vector3d(0,0,5);

        Vector3dInterface[] diagonal = {rocket, titan};
        Box box = new Box();
        box.setPoints(diagonal);
        boolean check = box.withinShape(v.add(rocket));

        assertEquals(false, check);
    }




}