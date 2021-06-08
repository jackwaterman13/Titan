package titan.shapes;

import interfaces.own.ShapeInterface;
import interfaces.given.Vector3dInterface;

/**
 * A box representing the maximum deviance the velocity of the rocket may have while moving closer to the target
 */
public class Box implements ShapeInterface {
    Vector3dInterface[] diagonal = null;
    /**
     * Sets corner points of 1 diagonal of the box
     *
     * @param points - Required { Rocket pos, Target pos }
     */
    public void setPoints(Vector3dInterface[] points){ diagonal = points; }


    /**
     * Gets the corner points of the main diagonal that represents the distance between Target and Rocket
     *
     * @return The 2 points { Rocket pos, Target pos } forming the diagonal of the shape
     */
    public Vector3dInterface[] getPoints(){ return diagonal; }

    /**
     * Checks whether the vector is within the box formed by the main diagonal { Rocket pos, Target pos }
     *
     * @param v - the vector that we are trying to check
     * @return True if and ony if v is within the box
     */
    public boolean withinShape(Vector3dInterface v){
        boolean xInBounds = false, yInBounds = false, zInBounds = false;

        if (diagonal[0].getX() < v.getX() && v.getX() < diagonal[1].getX()){ xInBounds = true; }
        else if (diagonal[0].getX() > v.getX() && v.getX() > diagonal[1].getX()){ xInBounds = true; }

        if (diagonal[0].getY() < v.getY() && v.getY() < diagonal[1].getY()){ yInBounds = true; }
        else if (diagonal[0].getY() > v.getY() && v.getY() > diagonal[1].getY()){ yInBounds = true; }

        if (diagonal[0].getZ() < v.getZ() && v.getZ() < diagonal[1].getZ()){ zInBounds = true; }
        else if (diagonal[0].getZ() > v.getZ() && v.getZ() > diagonal[1].getZ()){ zInBounds = true; }

        return xInBounds && yInBounds && zInBounds;
    }
}
