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
    Vector3dInterface resizePosition(double l);

    /**
     * Resizes the position relative to another position to the desired length.
     *
     * @param l - the length the final vector needs to have
     * @param relativeTo - the position that our position should relative to be
     * @return A relative position to relativeTo scaled to length l
     *
     * Example:
     *      Vector3dInterface myPos = (10, 0, 0)
     *      Vector3dInterface relativeTo = (0, 5, 3)
     *      double l = 9
     *
     *      myPos.relativeTo(l, relativeTo) = 1/9 * ( (10+0), (0+5), (0+3) ) = (10/9, 5/9, 3/9)
     */
    Vector3dInterface resizeRelativePosition(double l, Vector3dInterface relativeTo);
    /**
     * Gets the name of the object
     *
     * @return String representing name
     */
    String getName();
}
