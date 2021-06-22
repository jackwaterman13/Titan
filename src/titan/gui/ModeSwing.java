package titan.gui;

import interfaces.given.ODESolverInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.GuiInterface;
import interfaces.own.GuiObjectInterface;
import interfaces.own.SwingInterface;
import titan.math.Vector3d;
import titan.physics.State;
import titan.simulators.ProbeSimulator;
import titan.simulators.RocketSimulator;
import titan.simulators.StateSimulator;
import titan.solvers.Euler;
import titan.utility.Planet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;


/**
 * GUI implemented through Swing components
 *
 * Control instructions: Displayed states are controlled with left arrow (<-)
 * and right arrow(->) Left arrow: goes back to the previous state if any before
 * Right arrow: goes to the next state if any next
 */
public class ModeSwing extends Textures implements GuiInterface, SwingInterface {
    private static Dimension screenResolution;
    private final Dimension resolution;
    private final JFrame frame;

    private final StateSimulator stateSimulator;
    private final ProbeSimulator probeSimulator;
    private final RocketSimulator rocketSimulator;

    private enum Mode { Universe, Probe, Rocket };
    private enum PlayStyle { Static, Auto };

    private Mode mode = Mode.Universe;
    private PlayStyle style = PlayStyle.Auto;

    private State[] justUniverse, probeUniverse, rocketUniverse;

    private int index;
    private int playspeed = 1000;

    public static void main(String[] args) {
        ModeSwing gui = new ModeSwing("Swing V1.0", new Dimension(1920, 1080));
        gui.prepareApplication(new Euler(), 86400 * 365, 86400);
    }

    /**
     * Creates a GUI-Mode object that contains a JFrame.
     *
     * @param frameName - name of the frame to be displayed
     */
    public ModeSwing(String frameName, Dimension resolution) {
        frame = new JFrame(frameName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new SwingKeyListener());

        stateSimulator = new StateSimulator();
        probeSimulator = new ProbeSimulator();
        rocketSimulator = new RocketSimulator();

        if (screenResolution == null) {
            screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
        }
        this.resolution = resolution;
    }

    /**
     * Creates a JFrame that displays the results of a solver given a time frame and
     * step size. Automatically opens the Frame.
     *
     * @param solver - solver to use in calculating the states
     * @param tf     - final differential time
     * @param h      - the step size
     */
    public void prepareApplication(ODESolverInterface solver, double tf, double h) {
        collectStates(solver, tf, h);
        runSwing(resolution);
    }

    /**
     * Runs the Swing GUI (i.e. displays the frame);
     *
     * @param resolution - the resolution the frame should have
     */
    public void runSwing(Dimension resolution) {
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
    private void setFrameSize(Dimension resolution) {
        frame.setSize(resolution);
    }

    /**
     * Uses the engine to gather/get the states simulated with the solver, time
     * frame and step size
     *
     * @param solver - solver to use in calculating the states
     * @param tf     - final differential time
     * @param h      - the step size
     */
    private void collectStates(ODESolverInterface solver, double tf, double h) {
        justUniverse = (State[]) stateSimulator.runSolver(solver, tf, h);
        probeUniverse = (State[]) probeSimulator.simulate(solver, tf, h);
        rocketUniverse = (State[]) rocketSimulator.simulate(solver, tf, h);
        index = -1;
    }

    private void autoPlay(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
               if (style == PlayStyle.Auto){
                   updateFrame(false);
                   autoPlay();
               }
            }
        };
        timer.schedule(timerTask, playspeed);
    }

    /**
     * Updates the frame to display the 'next or previous' state
     *
     * @param reverse - reversion toggle; if true -> display previous state if any
     *                else -> display next state if any
     */

    private void updateFrame(boolean reverse) {
        if (index - 1 < 0 && reverse || index + 1 >= justUniverse.length) { return; }

        if (reverse) { index--; }
        else { index++; }

        if (mode == Mode.Universe){ drawState(justUniverse, index); }
        else if (mode == Mode.Probe){ drawState(probeUniverse, index); }
        else if (mode == Mode.Rocket){ drawState(rocketUniverse, index); }
    }

    /**
     * Draws the state on a emptied frame
     *
     * @param s - the state that needs to be displayed
     */

    private void drawState(State[] s, int index) {
        Graphics2D g2 = (Graphics2D) frame.getGraphics();
        Dimension resolution = frame.getSize();

        g2.clearRect(0, 0, resolution.width, resolution.height);

        DataInterface[] objects;
        GuiObjectInterface obj;
        Vector3dInterface displacement;

        Point2D p1 = null, p2 = null;
        Shape line = null;

        BasicStroke trajectoryThickness = new BasicStroke(2);
        Color trajectoryColor = Color.black;

        BasicStroke planetThickness = new BasicStroke(3);
        Color planetColor = Color.blue;

        BasicStroke nameThickness = new BasicStroke(1);
        Color nameColor = Color.black;

        double constant = 60;
//        for (int i = 0; i < s.length; i++) {
//            objects = s[i].getObjects();
//            for (int j = 0; j < objects.length; j++) {
//                obj = (Planet) objects[j];
//                displacement = obj.guiDisplacement(constant);
//                double x = displacement.getX() + (double) resolution.width / 2;
//                double y = displacement.getY() + (double) resolution.height / 2;
//                p1 = new Point2D.Double(x, y);
//
//                if (i == s.length - 1) {
//                    obj = (Planet) s[0].getObjects()[j];
//                    displacement = obj.guiDisplacement(j * constant);
//                    double x2 = displacement.getX() + (double) resolution.width / 2;
//                    double y2 = displacement.getY() + (double) resolution.height / 2;
//                    p2 = new Point2D.Double(x2, y2);
//                } else {
//                    obj = (Planet) s[i + 1].getObjects()[j];
//                    displacement = obj.guiDisplacement(j * constant);
//                    double x2 = displacement.getX() + (double) resolution.width / 2;
//                    double y2 = displacement.getY() + (double) resolution.height / 2;
//                    p2 = new Point2D.Double(x2, y2);
//                }
//                line = new Line2D.Double(p1, p2);
//
//                g2.setColor(trajectoryColor);
//                g2.setStroke(trajectoryThickness);
//                g2.draw(line);
//            }
//        }

        objects = s[index].getObjects();
        int radius = 30;
        for (int i = 0; i < objects.length; i++) {
            obj = (Planet) objects[i];
            displacement = obj.guiDisplacement(3e9);

            System.out.println(obj.getName() + " | "+ displacement.toString());

            double xTopLeft = displacement.getX() + (double) resolution.width / 2 - radius;
            double yTopLeft = displacement.getY() + (double) resolution.height / 2 - radius;
            Shape shape = new Ellipse2D.Double(xTopLeft, yTopLeft, radius, radius);

            g2.setStroke(planetThickness);
            g2.setColor(planetColor);
            g2.draw(shape);
            g2.fill(shape);

            // DRAW NAMES ABOVE PLANETS
            Font f = new Font("T", Font.ITALIC, 18);
            Shape name = generateShapeFromText(f, objects[i].getName(), xTopLeft - 5, yTopLeft - 5);
            g2.setStroke(nameThickness);
            g2.setColor(nameColor);
            g2.draw(name);
            g2.fill(name);
        }
    }


    public static Shape generateShapeFromText(Font font, String string, double x, double y) {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        try {
            GlyphVector vect = font.createGlyphVector(g2.getFontRenderContext(), string);
            Shape shape = vect.getOutline((float) x, (float) y);
            return shape;
        } finally { g2.dispose(); }
    }

    /**
     * Centers the frame to the middle of the screen
     *
     * @param f - frame that needs to be centered
     */
    private static void centerFrame(Frame f) {
        Dimension frameResolution = f.getSize();
        f.setLocation(screenResolution.width / 2 - frameResolution.width / 2, screenResolution.height / 2 - frameResolution.height / 2);
    }

    /**
     * Class to handle keyboard input in the GUI Updated the panel state when the
     * right arrow key has been released. Reverts the panel state when the left
     * arrow key has been released.
     */

    class SwingKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_P) {
                if (style == PlayStyle.Auto){
                    style = PlayStyle.Static;
                }
                else if (style == PlayStyle.Static){
                    style = PlayStyle.Auto;
                    autoPlay();
                }
            }
            else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE){
                updateFrame(false);
            }
            else if (key == KeyEvent.VK_R) {
                updateFrame(true);
            }
            else if (key == KeyEvent.VK_UP){
                if (mode == Mode.Universe){ mode = Mode.Probe; }
                else if (mode == Mode.Probe){ mode = Mode.Rocket; }
                else if (mode == Mode.Rocket){ mode = Mode.Universe; }
            }
            else if (key == KeyEvent.VK_DOWN){
                if (mode == Mode.Universe){ mode = Mode.Rocket; }
                else if (mode == Mode.Probe){ mode = Mode.Universe; }
                else if (mode == Mode.Rocket){ mode = Mode.Probe; }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) { // Do nothing
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}