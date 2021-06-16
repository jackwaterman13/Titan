import interfaces.ODEFunctionInterface;
import interfaces.ODESolverInterface;
import interfaces.StateInterface;

public class ControllerSolver implements ODESolverInterface {

    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {

        return new StateInterface[0];
    }

    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {

        ControllerState[] states = new ControllerState[(int) (tf/h)];


        return new StateInterface[0];
    }

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {


        return null;
    }
}
