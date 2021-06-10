package interfaces.own;

import interfaces.given.ODESolverInterface;

import java.awt.Dimension;

/**
 * Basic structure for any GUI implementation
 */
public interface GuiInterface {
    /**
     * Prepares the GUI to display the result(s) of the solver
     *
     * @param solver - solver to use in calculating the states
     * @param tf - final differential time
     * @param h - the step size
     */
    public void prepareApplication(ODESolverInterface solver, double tf, double h);
}
