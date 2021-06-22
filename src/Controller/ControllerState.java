package Controller;

import interfaces.given.RateInterface;
import interfaces.given.StateInterface;

public class ControllerState implements StateInterface {
    private Vector3d position;
    private Vector3d velocity;
    private double mass;
    private double radius = 2574730;
    private boolean thrustX = false;
    private boolean thrustY = false;

    public ControllerState(double[] pos, double[] vel) {
        this.position = new Vector3d(pos);
        this.velocity = new Vector3d(vel);
    }

    public ControllerState(double pX, double pY, double pZ, double vX, double vY, double vZ) {
        this.position = new Vector3d(pX, pY, pZ);
        this.velocity = new Vector3d(vX, vY, vZ);
    }

    public ControllerState(double pX, double pY, double pZ, double vX, double vY, double vZ, double mass) {
        this.position = new Vector3d(pX, pY, pZ);
        this.velocity = new Vector3d(vX, vY, vZ);
        this.mass = mass;
    }

    public void setThrustX(boolean thrust){
        this.thrustX = thrust;
    }

    public void setThrustY(boolean thrust){
        this.thrustY = thrust;
    }

    public boolean isThrustX(){
        return thrustX;
    }

    public boolean isThrustY(){
        return thrustY;
    }

    public Vector3d getPosition() {
        return this.position;
    }

    public Vector3d getVelocity() {
        return this.velocity;
    }

    public double getMass(){
        return this.mass;
    }

    public double getAltitude() {
        return Math.sqrt(position.getX()*position.getX() + position.getY()*position.getY()) - radius;
    }

    public double getR(){
        return Math.sqrt(position.getX()*position.getX() + position.getY()*position.getY());
    }

    public StateInterface addMul(double step, RateInterface rate) {
        return null;
    }
}
