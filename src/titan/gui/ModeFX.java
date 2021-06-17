package titan.gui;

import interfaces.given.ODESolverInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.GuiInterface;
import interfaces.own.GuiObjectInterface;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;

import javafx.scene.shape.Sphere;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import titan.simulators.StateSimulator;
import titan.physics.State;
import titan.solvers.Euler;
import titan.utility.Planet;

public class ModeFX extends Application implements GuiInterface {
    private final Dimension resolution = new Dimension(1200, 720);

    private static StateSimulator engine; // Singleton
    private State[] states;
    private int index;

    static final int radius = 100;
    static final int scale = 500;

    List<FxObject> fxObjects = new ArrayList<>();
    Transform focus = new Translate(0.0, 0.0, 0.0);
    int focusIndex = 0;

    double anchorX, anchorY;


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage){
        prepareApplication(new Euler(), 86400, 3600);

        Scene scene = createScene(resolution);
        setBackground(scene);

        addObjectNodes(scene, states[0]);

        Camera cam = create3dCam();
        setCamera(scene, cam, focus);

        EventHandler<MouseEvent> mousePressHandler = onMousePressed();
        scene.setOnMousePressed(mousePressHandler);

        EventHandler<MouseEvent> mouseDragHandler = onMouseDrag(cam);
        scene.setOnMouseDragged(mouseDragHandler);

        EventHandler<ScrollEvent> zoomHandler = getZoomHandler(cam);
        scene.setOnScroll(zoomHandler);

        EventHandler<KeyEvent> keyPressHandler = onKeyPress(scene);
        scene.setOnKeyPressed(keyPressHandler);

        EventHandler<KeyEvent> keyReleaseHandler = onKeyRelease(cam);
        scene.setOnKeyReleased(keyReleaseHandler);


        stage.setTitle("FX Version 1.0");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a fx stage (window) that will display the states
     *
     * @param solver - solver to use in calculating the states
     * @param tf - final differential time
     * @param h - the step size
     */
    public void prepareApplication(ODESolverInterface solver, double tf, double h){
        if (engine == null){ engine = new StateSimulator(); }
        collectStates(solver, tf, h);
    }

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
     * Creates the initial nodes representing the objects in the state
     *
     * @param scene - the scene that needs to have object initialized
     * @param state - the state that this scene needs to be displayed
     */
    private void addObjectNodes(Scene scene, State state){
        String[] textureUrls = Textures.getTextureUrls();

        Group root = (Group) scene.getRoot();
        ObservableList<Node> children = root.getChildren();
        DataInterface[] objects = state.getObjects();
        for(int i = 0; i < objects.length; i++){
            int length = i * scale;
            GuiObjectInterface obj = (Planet) objects[i];
            Vector3dInterface position = obj.resizePosition(length);

            Shape3D shape = new Sphere(radius);
            shape.setTranslateX(position.getX());
            shape.setTranslateY(position.getY());
            shape.setTranslateZ(position.getZ());

            PhongMaterial mat;
            if (i < textureUrls.length){
                Image image = new Image(textureUrls[i]);
                mat = new PhongMaterial();
                mat.setDiffuseMap(image);
            }
            else { mat = new PhongMaterial(Color.RED); }
            shape.setMaterial(mat);

            fxObjects.add(new FxObject(shape, obj.getName()));
            children.add(shape);
            if (i == 0){ focus = new Translate(shape.getTranslateX(), shape.getTranslateY(), shape.getTranslateZ()); }
        }
    }
    /**
     * Updates the scene to display either the next or previous state
     *
     * @param scene - the scene that needs to be updated
     * @param reverse - reversion toggle; if true -> display previous state if any
     *                                    else -> display next state if any
     */
    private void changeDisplayedState(Scene scene, boolean reverse){
        if (index - 1 < 0 && reverse || index + 1 >= states.length){ return; }

        if (reverse){ index--; }
        else{ index++; }

        updateScene(scene, states[index]);
    }

    /**
     * Updates the scene to display the state
     *
     * @param scene - the scene that needs to be updated
     * @param state - the state that needs to be displayed
     */
    private void updateScene(Scene scene, State state){
        Group root = (Group) scene.getRoot();
        ObservableList<Node> children = root.getChildren();
        DataInterface[] objects = state.getObjects();

        for(int i = 0; i < objects.length; i++){
            int length =  i * scale;
            GuiObjectInterface obj = (Planet) objects[i];
            for(Node n : children){
                if (!(n instanceof Shape3D)){ continue; }
                for(int j = 0; j < objects.length; j++){
                    FxObject fxObj = fxObjects.get(j);
                    if (fxObj.getShape().equals(n) && fxObj.getID().equals(obj.getName())){
                        Vector3dInterface position = obj.resizePosition(length);
                        fxObj.update(position);
                    }
                }
            }
        }
    }

    /**
     * Re-centers the focus from the cam from one planet to another according to the other of the FxObjects list
     *
     * @param cam - the camera that needs to (re-)focus to a planet
     * @param reverse - if true -> switch back to previous planetary focus if any
     *                  else -> switch to next planetary focus if any
     */
    private void updatePlanetFocus(Camera cam, boolean reverse){
        if (focusIndex - 1 < 0 && reverse || focusIndex + 1 >= fxObjects.size()){ return; }

        if (reverse){ focusIndex--; }
        else{ focusIndex++; }

        Shape3D obj = fxObjects.get(focusIndex).getShape();
        changeCamFocus(cam, new Translate(obj.getTranslateX(), obj.getTranslateY(), obj.getTranslateZ()));
    }

    /**
     * Sets the background of the scene
     *
     * @param s - scene whose background needs to be set
     */
    private void setBackground(Scene s){
        s.setFill(Color.rgb(100, 100, 100));
    }

    /**
     * Sets the camera in the scene focused at the Sun
     *
     * @param s - scene whose camera needs to be set
     * @param cam - camera that shall be set to the scene
     * @param focus - the point/object the camera should center its vision at
     */
    private void setCamera(Scene s, Camera cam, Transform focus){
        s.setCamera(cam);
        cam.setTranslateX(focus.getTx() - (double) resolution.width / 2);
        cam.setTranslateY(focus.getTy() - (double) resolution.height / 2);
        cam.setTranslateZ(-5 * 1e3);
    }

    /**
     * Re-focuses the camera to a location/point
     *
     * @param cam - the camera that needs to re-focus
     * @param focus - the location/point the camera will have to focus to
     */
    private void changeCamFocus(Camera cam, Transform focus){
        cam.setTranslateX(focus.getTx() - (double) resolution.width / 2);
        cam.setTranslateY(focus.getTy() - (double) resolution.height / 2);
        cam.setTranslateZ(cam.getTranslateZ());
    }

    /**
     * Creates an empty scene
     *
     * @param resolution - the resolution (width, height) the scene should have
     * @return A scene with an empty root node and passed resolution as size
     */
    private Scene createScene(Dimension resolution){
        return new Scene(new Group(), resolution.width, resolution.height);
    }

    /**
     * Creates a Perspective camera to display 3d graphics
     *
     * @return A new perspective camera
     */
    private Camera create3dCam(){
        return new PerspectiveCamera();
    }

    /**
     * Constructs an EventHandler that manages on mouse press event(s)
     * Currently, it only sets anchor x,y positions in case of a drag event.
     *
     * @return EventHandler that manages on mouse press event(s)
     */
    private EventHandler<MouseEvent> onMousePressed(){
        return event ->{
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        };
    }

    /**
     * Constructs an EventHandler that manages mouse drag movements in the stage (=window)
     * The drag repositions the camera.
     *
     * @param cam - the camera affected by the drag
     * @return EventHandler that repositions the camera to create the drag illusion of moving content within the stage
     */
    private EventHandler<MouseEvent> onMouseDrag(Camera cam){
        return event ->{
            cam.setTranslateX(anchorX - event.getSceneX() + cam.getTranslateX());
            cam.setTranslateY(anchorY - event.getSceneY() + cam.getTranslateY());
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        };
    }


    /**
     * Constructs an EventHandler that uses the vertical scrolling to determine the camera position on its z-axis
     * (i.e. method handles zooming in and out)
     *
     * @param cam - the camera that requires a zoom handler
     * @return EventHandler that updates the camera translate z of the scene
     */
    private EventHandler<ScrollEvent> getZoomHandler(Camera cam){
        return event -> cam.setTranslateZ(cam.getTranslateZ() + event.getDeltaY());
    }


    /**
     * Constructs a EventHandler that handles keyboard presses
     * For now this only handles state displays.
     *
     * @param scene - the scene that needs to be updated to display the objects/state
     * @return EventHandler that changes position of objects in the stage
     */
    private EventHandler<KeyEvent> onKeyPress(Scene scene){
        return event -> {
            KeyCode key = event.getCode();
            if (key == KeyCode.RIGHT){ changeDisplayedState(scene, false); }
            else if (key == KeyCode.LEFT){ changeDisplayedState(scene, true); }
        };
    }

    /**
     * Constructs an EventHandler that handles keyboard press releases.
     * For now this only affects the planetary focus
     *
     * @param cam - camera that needs to (re-)focus on a plnet
     * @return EventHandler that can change Camera's transform
     */
    private EventHandler<KeyEvent> onKeyRelease(Camera cam){
        return event ->{
            KeyCode key = event.getCode();
            if (key == KeyCode.UP){ updatePlanetFocus(cam, false); }
            else if (key == KeyCode.DOWN){ updatePlanetFocus(cam, true); }
        };
    }

    static class FxObject {
        private final Shape3D shape;
        private final String identifier;

        /**
         * Constructs a FxObject to keep track of the object's shape, location etc
         *
         * @param shape - the shape representing the object
         * @param id - the name of the object
         */
        public FxObject(Shape3D shape, String id){
            this.shape = shape;
            this.identifier = id;
        }

        public Shape3D getShape(){ return shape; }

        /**
         * Accesses the String identifier for this object. (i.e. name which is way different than Node ID)
         * Warning: This method is not the same as getId()!
         *
         * @return String representing identifier of the object that matches with the objects in the state class
         */
        public String getID(){ return identifier; }

        /**
         * Updates the position of the shape
         *
         * @param pos - shape location in the stage (window)
         */
        public void update(Vector3dInterface pos){
            shape.setTranslateX(pos.getX());
            shape.setTranslateX(pos.getY());
            shape.setTranslateZ(pos.getZ());
        }
    }
}