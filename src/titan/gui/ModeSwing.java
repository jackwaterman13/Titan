package titan.gui;

import interfaces.given.ODESolverInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.GuiInterface;
import interfaces.own.GuiObjectInterface;
import interfaces.own.SwingInterface;
import titan.math.Vector3d;
import titan.physics.Engine;
import titan.physics.State;
import titan.solvers.Euler;
import titan.solvers.Verlet;
import titan.utility.Planet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;

/**
 * GUI implemented through Swing components
 *
 * Control instructions:
 * Displayed states are controlled with left arrow (<-) and right arrow(->)
 * Left arrow: goes back to the previous state if any before
 * Right arrow: goes to the next state if any next
 */
public class ModeSwing implements GuiInterface, SwingInterface {
    private static Dimension screenResolution;
    private final Dimension resolution;
    private final JFrame frame;

    private final Engine engine;
    private State[] states;
    private Vector3dInterface[] displacements;
    private int index;

    public static void main(String[] args){
        ModeSwing gui = new ModeSwing("Swing V1.0", new Dimension(1200, 720));
        gui.prepareApplication(new Euler(), 86400 * 120, 86400);
    }

    /**
     * Creates a GUI-Mode object that contains a JFrame.
     *
     * @param frameName - name of the frame to be displayed
     */
    public ModeSwing(String frameName, Dimension resolution){
        frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addKeyListener(new SwingKeyListener());
        engine = new Engine();

        if (screenResolution == null){ screenResolution = Toolkit.getDefaultToolkit().getScreenSize(); }
        this.resolution = resolution;
    }

    /**
     * Creates a JFrame that displays the results of a solver given a time frame and step size.
     * Automatically opens the Frame.
     *
     * @param solver - solver to use in calculating the states
     * @param tf - final differential time
     * @param h - the step size
     */
    public void prepareApplication(ODESolverInterface solver, double tf, double h){
        collectStates(solver, tf, h);
        runSwing(resolution);
    }

    /**
     * Runs the Swing GUI (i.e. displays the frame);
     *
     * @param resolution - the resolution the frame should have
     */
    public void runSwing(Dimension resolution){
        setFrameSize(resolution);
        frame.setVisible(true);
        centerFrame(frame);
        updateFrame(false);
    }

    /**
     * Sets the resolution of the frame
     *
     * @param resolution - width and height the frame should have
     */
    private void setFrameSize(Dimension resolution){ frame.setSize(resolution); }

    /**
     * Uses the engine to gather/get the states simulated with the solver, time frame and step size
     *
     * @param solver - solver to use in calculating the states
     * @param tf - final differential time
     * @param h - the step size
     */
    private void collectStates(ODESolverInterface solver, double tf, double h){
        states = (State[]) engine.runSolver(solver, tf, h);
        index = -1;
    }

    /**
     * Updates the frame to display the 'next or previous' state
     *
     * @param reverse - reversion toggle; if true -> display previous state if any
     *                                    else -> display next state if any
     */
    private void updateFrame(boolean reverse){
        if (index - 1 < 0 && reverse || index + 1 >= states.length){ return; }

        if (reverse){ index--; }
        else{ index++; }

        drawState(states[index]);
    }

    /**
     * Draws the state on a emptied frame
     *
     * @param s - the state that needs to be displayed
     */
    private void drawState(State s){

        Graphics2D g2 = (Graphics2D) frame.getGraphics();
        Dimension resolution = frame.getSize();
        //ModeSwing.clear(g2, resolution);

        DataInterface[] objects = s.getObjects();
        if (displacements == null){ displacements = new Vector3d[objects.length]; }

        int radius = 30;
        for(int i = 0; i < objects.length; i++){
            GuiObjectInterface d = (Planet) objects[i];
            displacements[i] = d.resizePosition(60 * i);
            System.out.println(d.getName() + " | position: " + displacements[i].toString());

            int xTopLeft = (int) displacements[i].getX() + resolution.width / 2 - radius;
            int yTopLeft = (int) displacements[i].getY() + resolution.height / 2 - radius;
            Shape shape = new Ellipse2D.Double(xTopLeft, yTopLeft, radius, radius);

            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(3));
            g2.draw(shape);
        }
    }

    /**
     * Clears the drawing on a JComponent
     *
     * @param g2 - the graphics2d object of the component/object we want to clear
     * @param d - the dimension/resolution this object has
     */
    private static void clear(Graphics2D g2, Dimension d){
        g2.clearRect(0, 0, d.width, d.height);
    }

    /**
     * Centers the frame to the middle of the screen
     *
     * @param f - frame that needs to be centered
     */
    private static void centerFrame(Frame f){
        Dimension frameResolution = f.getSize();
        f.setLocation(screenResolution.width / 2 - frameResolution.width / 2, screenResolution.height / 2 - frameResolution.height / 2);
    }


    /**
     * Class to handle keyboard input in the GUI
     * Updated the panel state when the right arrow key has been released.
     * Reverts the panel state when the left arrow key has been released.
     */
    class SwingKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            // Do nothing
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_RIGHT){ updateFrame(false); }
            else if (key == KeyEvent.VK_LEFT){ updateFrame(true); }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // Do nothing
        }
    }
}