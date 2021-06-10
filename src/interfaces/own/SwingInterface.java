package interfaces.own;

import java.awt.Dimension;

/**
 * Basic swing structure
 */
public interface SwingInterface {
    /**
     * Runs the Swing GUI (i.e. displays the frame);
     *
     * @param resolution - the resolution the frame should have
     */
    public void runSwing(Dimension resolution);
}
