package titan.physics;

import interfaces.given.RateInterface;
import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.utility.Rate;

public class State implements StateInterface, RateInterface {
	private DataInterface[] objects;
	private State previous;
	private double period;

	/**
	 * Constructs a state given a set of data objects.
	 *
	 * @param objects - the objects representing the state. Each object should at least contain a position and velocity
	 */
	public State(DataInterface[] objects){ this.objects = objects; }

	/**
	 * Accesses the objects in the state
	 *
	 * @return A data array representing the state of all objects in the simulation
	 */
	public DataInterface[] getObjects(){ return objects; }

	/**
	 * Sets the previous state used to calculate the current state
	 *
	 * @param previous - the state used to calculate the current state
	 */
	public void setPrevious(State previous){ this.previous = previous; }
	/**
	 * Accesses the previous state used to calculate this state
	 *
	 * @return A state that represents the previous state to have calculated the current state
	 */
	public State getPrevious(){ return previous; }

	/**
	 * Sets the period, time interval t at which this state is calculated.
	 * So if t0 = 0, t1 = 2, t2 = 4 and this state is at t3, then t3 = 3 * 2 = 6
	 *
	 * @param t - the time at which state gets evaluated
	 */
	public void setPeriod(double t){ this.period = t; }

	/**
	 * Accesses the time that the state has been evaluated at.
	 *
	 * @return Double representing the time this state has been evaluated in the in simulation
	 */
	public double getPeriod(){ return period; }

	/**
	 * Documentation from the interface:
	 * Update a state to a new state computed by: this + step * rate
	 *
	 * @param step   The time-step of the update
	 * @param rate   The average rate-of-change over the time-step. Has dimensions of [state]/[time].
	// State[] index 0 is respective
	// Time[] index 0
	 * @return The new state after the update. Required to have the same class as 'this'.
	 */
	public StateInterface addMul(double step, RateInterface rate){
		Rate r  = (Rate) rate;
		Vector3dInterface[] xRoc = r.getPosRoc();
		Vector3dInterface[] vRoc = r.getVelRoc();
		DataInterface[] result = new DataInterface[objects.length];
		for(int i = 0; i < objects.length; i++){
			Vector3dInterface xFinal = objects[i].getPosition().addMul(step, xRoc[i]);
			Vector3dInterface vFinal = objects[i].getVelocity().addMul(step, vRoc[i]);
			result[i] = objects[i].update(xFinal, vFinal);
		}
		State nextState = new State(result);
		nextState.setPrevious(this);
		nextState.setPeriod(getPeriod() + step);
		return nextState;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(DataInterface d: objects){
			sb.append(d.getName()).append("\n");
			sb.append("Position: ").append(d.getPosition().toString()).append("\n");
			sb.append("Velocity: ").append(d.getVelocity().toString()).append("\n");
			sb.append("\n");
		}
		return sb.toString();
	}
}
