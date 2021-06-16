import interfaces.RateInterface;
import interfaces.StateInterface;

public class ControllerState implements StateInterface {
    private Vector3d position;
    private Vector3d velocity;

    public ControllerState(double[] pos, double[] vel) {
        this.position = new Vector3d(pos);
        this.velocity = new Vector3d(vel);
    }

    public ControllerState(double pX, double pY, double pZ, double vX, double vY, double vZ) {
        this.position = new Vector3d(pX, pY, pZ);
        this.velocity = new Vector3d(vX, vY, vZ);
    }

    public Vector3d getPosition() {
        return this.position;
    }

    public Vector3d getVelocity() {
        return this.velocity;
    }

    public StateInterface addMul(double step, RateInterface rate) {
        return null;
    }
}
