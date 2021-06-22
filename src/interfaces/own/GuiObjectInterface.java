package interfaces.own;

import interfaces.given.Vector3dInterface;

/**
 * Interface that sets the (partial) structure a data object should have if they want to be displayed in the GUI
 */
public interface GuiObjectInterface {
    /**
     * Gets the Gui displacement of the object
     *
     * @param size - the norm the displacement vector should have
     * @return A displacement vector of norm = size
     */
    Vector3dInterface guiDisplacement(double size);

    /**
     * Gets the name of the object
     *
     * @return String representing name
     */
    String getName();
}
