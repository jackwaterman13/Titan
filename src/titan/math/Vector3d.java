package titan.math;

import interfaces.given.Vector3dInterface;

/**
 * Class representing a 3-dimensional vector while implementing interface Vector3dInterface
 * This is an alternative version which should be more memory efficient
 */
public class Vector3d implements Vector3dInterface {
	private double[] values = { 0.0, 0.0, 0.0 };

	public double getX(){ return values[0]; }
	public void setX(double x){ values[0] = x; }

	public double getY(){ return values[1]; }
	public void setY(double y) { values[1] = y; }

	public double getZ() { return values[2]; }
	public void setZ(double z){ values[2] = z; }

	/**
	 * Empty constructor that creates a 3-dimensional zero vector
	 */
	public Vector3d(){}

	/**
	 * Creates a 3-dimensional vector
	 *
	 * @param x - 1st dimension value
	 * @param y - 2nd dimension value
	 * @param z - 3rd dimension value
	 */
	public Vector3d(double x, double y, double z){ values = new double[]{x, y, z}; }

	/**
	 * Creates a n-dimensional Vector, where n = values.length
	 *
	 * @param values - double array representing a multi-dimensional vector in order,
	 *                 where vector dimension i = values[i-1], with i = 1, 2, ... values.length
	 */
	public Vector3d(double[] values){ this.values = values; }

	/**
	 * Performs a vector addition for the first 3-dimensions (say x,y,z)
	 *
	 * @param other - addition vector consisting of at least 3 dimensions
	 * @return 3-dimensional vector representing the result of addition operation performed with other on the x,y,z dimensions
	 */
	public Vector3dInterface add(Vector3dInterface other){
		double[] result = { getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ() };
		return new Vector3d(result);
	}

	/**
	 * Performs a vector subtraction for the first 3-dimensions (say x,y,z)
	 *
	 * @param other - subtraction vector consisting of at least 3 dimensions
	 * @return 3-dimensional vector representing the result of subtraction operation performed with other on the x,y,z dimensions
	 */
	public Vector3dInterface sub(Vector3dInterface other){
		double[] result = { getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ() };
		return new Vector3d(result);
	}

	/**
	 * Performs a vector multiplication for the first 3-dimensions (say x,y,z)
	 *
	 * @param scalar - weight to use in multiplication
	 * @return 3-dimension vector representing the result of the dimensions x,y,z multiplied with the scalar
	 */
	public Vector3dInterface mul(double scalar){
		double[] result = { getX() * scalar, getY() * scalar, getZ() * scalar };
		return new Vector3d(result);
	}

	/**
	 * Scalar x vector multiplication, followed by an addition
	 *
	 * @param scalar the double used in the multiplication step
	 * @param other  the vector used in the multiplication step
	 * @return the result of the multiplication step added to this vector,
	 * for example:
	 *
	 *       Vector3d a = Vector();
	 *       double h = 2;
	 *       Vector3d b = Vector();
	 *       ahb = a.addMul(h, b);
	 *
	 * ahb should now contain the result of this mathematical operation:
	 *       a+h*b
	 */
	public Vector3dInterface addMul(double scalar, Vector3dInterface other){
		double[] result = { getX() + scalar * other.getX(), getY() + scalar * other.getY(), getZ() + scalar * other.getZ() };
		return new Vector3d(result);
	}

	/**
	 * @return the Euclidean norm of a vector
	 */
	public double norm(){
		return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2));
	}

	/**
	 * @return the Euclidean distance between two vectors
	 */
	public double dist(Vector3dInterface other){
		return Math.sqrt(Math.pow(other.getX() - getX(), 2) + Math.pow(other.getY() - getY(), 2) + Math.pow(other.getZ() - getZ(), 2));
	}

	/**
	 * @return A string in this format:
	 * Vector3d(-1.0, 2, -3.0) should print out (-1.0,2.0,-3.0)
	 */
	public String toString(){
		return "(" + getX() + "," + getY() + "," + getZ() + ")";
	}
}
