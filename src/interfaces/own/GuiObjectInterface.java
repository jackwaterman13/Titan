package interfaces.own;

import interfaces.given.Vector3dInterface;

/**
 * Interface that sets the (partial) structure a data object should have if they want to be displayed in the GUI
 */
public interface GuiObjectInterface {
    /**
     * Resizes the position vector to the desired length.
     *
     * @param l - the length the vector should have
     * @return A rescaled vector to the length l
     */
    public Vector3dInterface resizePosition(double l);

    /**
     * Gets the name of the object
     *
     * @return String representing name
     */
    public String getName();
}
