package interfaces.own;

import interfaces.given.Vector3dInterface;

/**
 * Interface representing the base structure of Newton-Raphson's method
 */
public interface RaphsonInterface {

    /**
     * Performs a single step of a multi-var Newton's method.
     *
     * @param u - 3-dimensional vector
     * @param f - the involved function
     * @return Returns a 3-dimensional vector u[n+1] obtained through u[n+1] = u[n] - D[f'(x)]^-1 f(x)
     */
    public Vector3dInterface step(Vector3dInterface u, FunctionInterface f);
}
