package titan.utility;
/* Package import statements here */

import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.GuiObjectInterface;

public class Planet implements DataInterface, GuiObjectInterface {
    String name;
    double mass = 0.0, radius = 0.0;
    Vector3dInterface position, velocity, gravity;

    /**
     * Constructs an empty Planet object, where no variables are initialized except the doubles.
     */
    public Planet(){ }

    /**
     * Constructs the desired Planet object
     *
     * @param name - the name identifying this planet
     * @param mass - the mass the planet has
     * @param radius - the radius this planet has
     * @param x - the position this planet has
     * @param v - the velocity this planet has
     */
    public Planet(String name, double mass, double radius, Vector3dInterface x, Vector3dInterface v){
        setName(name);
        setMass(mass);
        setPosition(x);
        setVelocity(v);
        this.radius = radius;
    }

    /**
     * Sets the name of a data object such as a planet, rocket, probe etc.
     * Name label acts a String identifier of the object
     *
     * @param s - String representing the name/identity of the data object
     */
    public void setName(String s){ name = s; }

    /**
     * Accesses the object's name
     *
     * @return String representing the name/identity of the object
     */
    public String getName(){ return name; }

    /**
     * Sets the mass of a object such as a planet, rocket, probe etc
     *
     * @param m - double representing the mass of the object
     */
    public void setMass(double m){ mass = m; }

    /**
     * Accesses the object's mass value
     *
     * @return Double representing the current mass value of the object
     */
    public double getMass(){ return mass; }

    /**
     * Sets the position/displacement of the object according to the SSB coordinate system
     *
     * @param x - a 3-dimensional vector representing the position/displacement
     */
    public void setPosition(Vector3dInterface x){ position = x; }

    /**
     * Accesses the object's position/displacement according to the SSB coordinate system
     *
     * @return A 3-dimensional vector representing the position/displacement
     */
    public Vector3dInterface getPosition(){ return position; }

    /**
     * Sets the velocity of the object according to the SSB coordinate system
     *
     * @param v - a 3-dimensional vector representing the velocity
     */
    public void setVelocity(Vector3dInterface v){ velocity = v; }

    /**
     * Accesses the object's velocity according to the SSB coordinate system
     *
     * @return A 3-dimensional vector representing the object's velocity
     */
    public Vector3dInterface getVelocity(){ return velocity; }

    /**
     * Sets the net universal gravity that is applied on a data object
     *
     * @param g - a 3-dimensional vector representing the net universal gravity
     */
    public void setGravity(Vector3dInterface g){ gravity = g; }

    /**
     * Accesses the object's net universal gravity
     *
     * @return A 3-dimensional vector representing the object's net universal gravity
     */
    public Vector3dInterface getGravity(){ return gravity; }

    /**
     * Calculates the distance to another data object according to their position in the SSB coordinate system
     *
     * @param other - the object we are trying to measure distance to
     * @return A double representing the Euclidean distance between other object's position and this object's position
     */
    public double distance(DataInterface other){ return position.dist(other.getPosition()); }

    /**
     * Calculates the distance to another data object according to their position in the SSB coordinate system
     *
     * @param other - the object we are trying to measure distance to
     * @return A 3-dimensional vector representing the distance between the other's position and this object's position
     */
    public Vector3dInterface distance3d(DataInterface other){ return other.getPosition().sub(position); }

    /**
     * Constructs a copy of the object with the updated position and velocity
     *
     * @return A data interface representing the updated state of the object
     */
    public DataInterface update(Vector3dInterface x, Vector3dInterface v){
        return new Planet(name, mass, radius, x, v);
    }

    /**
     * Method that returns a string containing data of a planet in the format = name: { mass, radius, position, velocity }
     * Where each property in the curly bracket { } has its own label/identifier
     *
     * @return A String in the same format a planet in the provided text file.
     */
    public String toString(){
        return name +
                ": { mass=" + mass +
                ", r=" + radius +
                ", x=" + position.getX() +
                ", y=" + position.getY() +
                ", z=" + position.getZ() +
                ", vx=" + velocity.getX() +
                ", vy=" + velocity.getY() +
                ", vz=" + velocity.getZ() +
                " }"
                ;
    }

    /**
     * Resizes the position vector to the desired length.
     *
     * @param size - the length the vector should have
     * @return A rescaled vector to the length size
     */
    public Vector3dInterface guiDisplacement(double size){
        return position.mul(1/ size);
    }


    /**
     * Accesses the radius of the planets.
     *
     * @return A double != 0.0 if a radius was specified in the initial data file.
     */
    public double getRadius(){ return radius; }
}