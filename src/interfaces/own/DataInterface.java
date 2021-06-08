package interfaces.own;

import interfaces.given.Vector3dInterface;

/**
 * Base structure to represent planets, rockets, probe etc.
 */
public interface DataInterface {
    /**
     * Sets the name of a data object such as a planet, rocket, probe etc.
     * Name label acts a String identifier of the object
     *
     * @param s - String representing the name/identity of the data object
     */
    public void setName(String s);

    /**
     * Accesses the object's name
     *
     * @return String representing the name/identity of the object
     */
    public String getName();

    /**
     * Sets the mass of a object such as a planet, rocket, probe etc
     *
     * @param m - double representing the mass of the object
     */
    public void setMass(double m);

    /**
     * Accesses the object's mass value
     *
     * @return Double representing the current mass value of the object
     */
    public double getMass();

    /**
     * Sets the position/displacement of the object according to the SSB coordinate system
     *
     * @param x - a 3-dimensional vector representing the position/displacement
     */
    public void setPosition(Vector3dInterface x);

    /**
     * Accesses the object's position/displacement according to the SSB coordinate system
     *
     * @return A 3-dimensional vector representing the position/displacement
     */
    public Vector3dInterface getPosition();

    /**
     * Sets the velocity of the object according to the SSB coordinate system
     *
     * @param v - a 3-dimensional vector representing the velocity
     */
    public void setVelocity(Vector3dInterface v);

    /**
     * Accesses the object's velocity according to the SSB coordinate system
     *
     * @return A 3-dimensional vector representing the object's velocity
     */
    public Vector3dInterface getVelocity();

    /**
     * Sets the net universal gravity that is applied on a data object
     *
     * @param g - a 3-dimensional vector representing the net universal gravity
     */
    public void setGravity(Vector3dInterface g);

    /**
     * Accesses the object's net universal gravity
     *
     * @return A 3-dimensional vector representing the object's net universal gravity
     */
    public Vector3dInterface getGravity();

    /**
     * Calculates the distance to another data object according to their position in the SSB coordinate system
     *
     * @param other - the object we are trying to measure distance to
     * @return A double representing the Euclidean distance between other object's position and this object's position
     */
    public double distance(DataInterface other);

    /**
     * Calculates the distance to another data object according to their position in the SSB coordinate system
     *
     * @param other - the object we are trying to measure distance to
     * @return A 3-dimensional vector representing the distance between the other's position and this object's position
     */
    public Vector3dInterface distance3d(DataInterface other);

    /**
     * Constructs a copy of the object with the updated position and velocity
     *
     * @return A data interface representing the updated state of the object
     */
    public DataInterface update(Vector3dInterface x, Vector3dInterface v);

    /**
     * Method that returns a string containing data of a planet in the format = name: { mass, radius, position, velocity }
     * Where each property in the curly bracket { } has its own label/identifier
     *
     * @return A String in the same format a planet in the provided text file.
     */
    public String toString();
}
