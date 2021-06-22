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
    final static double mass_shuttle = 7.8e4;
    final static double mass_lander  = 6e3;
    double mass_fuel = 0;

    final double thrust_max = 3e7;
    final double v_exhaust = 4e3;

    public Rocket(Vector3dInterface pos, Vector3dInterface vel){
        setPosition(pos);
        setVelocity(vel);
        setName("Rocket");
    }

    public Rocket(double fuelMass, Vector3dInterface pos, Vector3dInterface vel){
        mass_fuel = fuelMass;
        setPosition(pos);
        setVelocity(vel);
        setName("Rocket");
        double dm = calculateMassLoss(vel.norm(), 0, v_exhaust, getMass());
        mass_fuel += dm;

        if (mass_fuel < 0){ throw new RuntimeException("Initial fuel mass wasn't enough for desired launch velocity!"); }
    }

    public static double calculateMassLoss(double vFinal, double v0, double v_exhaust, double m0){
        // vFinal = v0 + vEx ln (m0 / mFinal)
        // vFinal - v0 = vEx ln (m0 / mFinal)
        // (vFinal - v0) / vEx = ln (m0 / mFinal)
        // e^( (vFinal - v0) / vEx ) = m0 / mFinal
        // mFinal = m0 / e^( (vFinal - v0) / vEx )
        // dm = m0 - mFinal
        double mFinal = m0 / Math.exp((vFinal - v0) / v_exhaust);
        return mFinal - m0;
    }

    @Override
    public double getMass() { return mass_shuttle + mass_lander + mass_fuel; }

    @Override
    public void setMass(double fuelMass){ mass_fuel = fuelMass; }

    @Override
    public Rocket update(Vector3dInterface x, Vector3dInterface v){
        Rocket r = new Rocket(x, v);
        r.mass_fuel = mass_fuel;
        return r;
    }
}
