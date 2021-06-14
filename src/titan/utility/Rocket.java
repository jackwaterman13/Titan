package titan.utility;
/*
Citation from mail:
    "Dear all,

    the examiners want you to use the following space ship specifics:
    Mass of craft (dry mass of Space Shuttle)
    78000kg = 7.8e4 kg
    Mass of lander (roughly mass of Apollo landers)
    6000kg = 6e3 kg
    Effective exhaust velocity (current ion thrusters)
    20km/s=2e4 m/s
    Maximum thrust (full-burn space shuttle engine)
    30MN=3e7 N

    As an extra (not mandatory), you could try a two-fuel solution.
    For take-off use combustion fuel thrusters, and for in-flight corrections, use ion-fuel or magnetoplasmadynamic thrusters.

    Combustion fuel:
    Effective exhaust velocity 4 km/s = 4e3 m/s
    Maximum thrust 30 MN = 3e7 N

    Ion fuel:
    Effective exhaust velocity 20km/s = 2e4 m/s
    Maximum thrust 0.1N = 1e-1 N

    Magnetoplasmadynamic
    Effective exhaust velocity 15-60 km/s
    Maximum thrust 2.5-25 N

    Kind regards,
    Katharina"
 */

/* Remark: Mission is to enter Titan's orbit between 1e5 and 3e5 km */

import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.ShapeInterface;
import titan.math.Vector3d;
import titan.shapes.Box;

public class Rocket extends Planet {
    public static boolean unlimitedFuel = true;
    public static boolean return2Earth = false;
    final static double mass_shuttle = 7.8e4;
    final static double mass_lander  = 6e3;
    static double mass_spent;
    double mass_fuel;

    final double thrust_max = 3e7;
    final double v_exhaust = 4e3;

    ShapeInterface shape = new Box();

    public Rocket(){ }
    public Rocket(double fuelMass, Vector3dInterface pos, Vector3dInterface vel){
        mass_fuel = fuelMass;
        setPosition(pos);
        setVelocity(vel);
        setName("Rocket");
    }

    /**
     * Checks whether we have hit the target based on the permitted orbit
     *
     * @param target - the destination our rocket has in mind
     * @param deviance - the maximal distance the rocket should be away from the target
     * @return True if and only if the distance to the target is less or equal to deviance
     */
    public boolean arrivedAt(DataInterface target, double deviance){ return distance(target) <= deviance; }


    public Vector3dInterface correctTrajectory(DataInterface target, double dt){

        return new Vector3d();
    }

    /**
     * Function that needs to be called whenever we update the state, so the rocket will be able to update itself as well.
     * Purpose: To find acceleration correction vector that will negate the effects of the gravitational acceleration while
     *          thrusting/moving towards our target.
     *
     * @param target - the target we want our rocket to reach
     * @param t - the delta time for which we have to find the acceleration correction vector for
     * @return The acceleration correction vector containing the negation of gravitational acceleration and the possible thrust to the target
     */
    public Vector3dInterface accelerateTo(DataInterface target, double t){
        Vector3dInterface fg = getGravity();
        double dm_max = calculateMassLoss(thrust_max, v_exhaust, t);
        double dm_negation = calculateMassLoss(fg.norm(), v_exhaust, t);
        double dm_left = dm_max - dm_negation;
        double f_thrust = calculateThrust(v_exhaust, dm_left, t);
        double a_thrust = f_thrust / getMass();
        double dm = dm_left;
        double dx = a_thrust * t * t;
        Vector3dInterface distance = distance3d(target);
        if (dx > distance.norm()){
            a_thrust = distance.norm() / t / t;
            f_thrust = a_thrust * getMass();
            dm = calculateMassLoss(f_thrust, v_exhaust, t);
        }

        if(!unlimitedFuel){
            mass_fuel += dm;
            mass_spent+= dm;
            System.out.println("Fuel mass spent so far: " + mass_spent);
        }

        Vector3dInterface a = distance.mul(1.0 / distance.norm());
        a = a.mul(a_thrust);
        return a;
    }

    /**
     * Calculates the thrust given the effective exhaustive velocity, delta mass and - time
     *
     * @param v_ex - effective exhaustive velocity; v[exhaust]
     * @param dm - delta mass; the mass part of the fuel burn rate
     * @param dt - delta time; the time part of the fuel burn rate
     * @return A double representing the thrust the engine will exhibit under these circumstances
     */
    public static double calculateThrust(double v_ex, double dm, double dt){ return -v_ex * dm / dt; }

    /**
     * Calculates the velocity of the rocket through v[final] = v0 + v[exhaust] * ln(m0 / dm)
     * Futhermore, assumes v0 = 0 to only return the dv
     *
     * @param v_ex - effective exhaustive velocity
     * @param m0 - initial mass
     * @param dm - change in mass
     * @return The velocity change that will be added to v0 to get v[final]
     */
    public static double calculateVelocity(double v_ex, double m0, double dm){
        return v_ex * Math.log(m0 / (m0+dm));
    }

    /**
     * Calculates the change in mass required to achieve the thrust
     *
     * @param F_thrust - the thrust we want
     * @param v_ex - the effective exhaustive velocity of the fuel used
     * @param dt - the delta time we want to apply this thrust at
     * @return The minimum change in mass required to achieve the thrusts
     */
    public static double calculateMassLoss(double F_thrust, double v_ex, double dt){
        return F_thrust * dt / -v_ex;
    }

    @Override
    public double getMass() { return mass_shuttle + mass_lander + mass_fuel; }

    @Override
    public void setMass(double fuelMass){ mass_fuel = fuelMass; }

    @Override
    public Rocket update(Vector3dInterface x, Vector3dInterface v){
        return new Rocket(mass_fuel, x, v);
    }
}
