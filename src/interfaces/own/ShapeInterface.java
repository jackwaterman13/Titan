package interfaces.own;

import interfaces.given.Vector3dInterface;

public interface ShapeInterface {
    /**
     * Sets corner, mid and other points of the shape.
     * What's get set is entirely dependent on the class implementing the interface
     *
     * @param points - the main points required to form the shape
     */
    public void setPoints(Vector3dInterface[] points);

    /**
     * Gets the important corner, mid and other points of the shape.
     *
     * @return A vectory array containing the main points that form the shape together
     */
    public Vector3dInterface[] getPoints();

    /**
     * Checks whether a vector falls inside a shape that is constructed using 2 starting points
     *
     * @param v - the vector that we are trying to check
     * @return True if and ony if v falls inside the shape
     */
    public boolean withinShape(Vector3dInterface v);
}
