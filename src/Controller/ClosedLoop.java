package Controller;

import java.util.ArrayList;

public class ClosedLoop {

    private double mass = 6000;                             // Starting mass of the Lander (kg)
    private double fuelMass = 8845;                         // Mass of fuel in the lander (kg)
    private double maxThrust = 42923;                       // Lander max thrust (N)
    private double RCS_Thrust = 2000;                       // Thrust of the lander's RCS thrusters (N)
    private double RCS_radius = 3.31/2;                     // Distance from the center of mass to the RCS thrusters (m)
    private double h = 1;                                   // Base time unit of simulation (s)
    private ControllerPhysics phy;


    public static void main(String[] args) {

        double posY = 50*1000+2574730;
        new ClosedLoop(3,posY,Math.PI,10,-1,8);
    }

    public ClosedLoop(double posX, double posY, double ang, double velX, double velY, double angV) {
        ControllerState start = new ControllerState(posX, posY, ang, velX, velY, angV, mass+fuelMass);
        controlLoop(start);
        phy = new ControllerPhysics();
    }


    public ArrayList<ControllerState> controlLoop(ControllerState y){

        double altitude = y.getAltitude();
        ControllerState current = y;
        ArrayList<ControllerState> results = new ArrayList<>();

        int step = 0;

        while (altitude > 0){
            results.addAll(controlX(current));
            current = results.get(results.size() - 1);
            altitude = current.getAltitude();
            System.out.println("Step = " + step++ + "\n" + "Altitude = " + altitude + "\n" + "Mass " + current.getMass());
        }


        return results;
    }


    /**
     * @param time          Time period over which calculations are taken (s)
     * @param y             Current state of the lander
     * @param accX          Acceleration in the X vector (m/s^2)
     * @param accY          Acceleration in the Y vector (m/s^2)
     * @return              Next states of the lander
     */
    private ArrayList<ControllerState> thrust(double time, ControllerState y, double accX, double accY){

        double posX = y.getPosition().getX();
        double posY = y.getPosition().getY();
        double velX = y.getVelocity().getX();
        double velY = y.getVelocity().getY();
        double ang  = y.getPosition().getZ();
        double angV = y.getVelocity().getZ();
        double mass = y.getMass();


        ArrayList<ControllerState> states = new ArrayList<>();
        double current_time = 0;
        while (current_time<time) {
             posX = phy.displacement(posX,velX, accX * h, h);
             velX = phy.nextVelocity(velX, accX * h, h);

             if (y.isThrustX())
                 mass = phy.updateFuelMassMT(h);

             posY = phy.displacement(posY, velY, accY * h, h);
             velY = phy.nextVelocity(velY,accY * h, h);

             if (y.isThrustY())
                 mass = phy.updateFuelMassMT(h);

             states.add(new ControllerState(posX, posY, ang, velX, velY, angV, mass));
             current_time += h;
        }
        if (time>current_time){
            double remainder = time - current_time;
            posX = phy.displacement(posX,velX, accX * remainder, remainder);
            velX = phy.nextVelocity(velX, accX * remainder, remainder);

            if (y.isThrustX())
                mass = phy.updateFuelMassMT(remainder);

            posY = phy.displacement(posY, velY, accY * remainder, remainder);
            velY = phy.nextVelocity(velY,accY * remainder, remainder);

            if (y.isThrustY())
                mass = phy.updateFuelMassMT(remainder);

            states.add(new ControllerState(posX, posY, ang, velX, velY, angV, mass));
        }

        return states;
    }

    private ArrayList<ControllerState> controlX(ControllerState y) {

        ControllerState current;
        ArrayList<ControllerState> out = new ArrayList<>();
        if (Math.abs(y.getPosition().getZ() - 2 * Math.PI) > 1.74) {
            out.addAll(adjustInclination(0, y));
            current = out.get(1);
        } else current = y;

        double[] forces = environmentalForces(current);

        double xThrust = forces[0];
        double yForce = forces[1];

        double time = Math.abs(xThrust/maxThrust);

        double accX = xThrust/mass;
        double accY = yForce/mass;

        System.out.println("Acceleration x " + accX + "\n" + "Acceleration y " + accY);
        out.addAll(thrust(time, current, accX, accY));

        return out;
    }

    /**
     * @param targetAngle       Angle which the lander is to point towards (rad)
     * @param y                 Current state of the lander
     * @return                  States involved in adjust inclination of the lander
     */
    private ArrayList<ControllerState> adjustInclination(double targetAngle, ControllerState y){

        ArrayList<ControllerState> out = new ArrayList<>();
        int direction = 1;
        double angle  = y.getPosition().getZ();
        double initial_velocity = y.getVelocity().getZ();
        double mass = y.getMass();

        double deltaAng = Math.abs(targetAngle - angle);

        if (deltaAng > 2*Math.PI)
            System.out.println("Angle change greater than 1 revolution");

        if (deltaAng > Math.PI) {
            deltaAng = 2 * Math.PI - deltaAng;
            direction = -1;
        }

        deltaAng = deltaAng/2;
        double target_angle = angle + direction * deltaAng;

        if (target_angle < 0)
            target_angle += 2*Math.PI;
        else if (target_angle > 2*Math.PI)
            target_angle -= 2*Math.PI;

        double acceleration = direction * phy.torque(2*RCS_Thrust, RCS_radius)/ phy.inertiaMoment(mass, RCS_radius);
        double final_velocity = phy.finalVelocitySquared(initial_velocity, acceleration, deltaAng);

        if (final_velocity < 0)
            final_velocity = -Math.sqrt(Math.abs(final_velocity));
        else final_velocity = Math.sqrt(final_velocity);

        double RCS_time = phy.rotationTime(initial_velocity, final_velocity, acceleration);

        double posX = y.getPosition().getX();
        double posY = y.getPosition().getY();
        double velX = y.getVelocity().getX();
        double velY = y.getVelocity().getY();

        mass -= phy.updateFuelMassRCS(RCS_time);
        ControllerState step1 = new ControllerState(posX, posY, target_angle, velX, velY, final_velocity, mass);

        double[] forces = environmentalForces(step1);
        double xForce = forces[0];
        double yForce = forces[1];

        double accX = xForce/mass;
        double accY = yForce/mass;
        System.out.println("RCS Time " + RCS_time);
        out.addAll(thrust(RCS_time, step1, accX, accY));
        step1 = out.get(out.size()-1);

        angle = step1.getPosition().getZ();
        target_angle = angle + direction * deltaAng;

        if (target_angle > 2*Math.PI)
            target_angle -= 2*Math.PI;

        if (direction == 1)
            direction = -1;
        else direction = 1;

        posX = step1.getPosition().getX();
        posY = step1.getPosition().getY();
        velX = step1.getVelocity().getX();
        velY = step1.getVelocity().getY();
        initial_velocity = step1.getVelocity().getZ();

        acceleration = direction * phy.torque(2*RCS_Thrust, RCS_radius)/phy.inertiaMoment(mass, RCS_radius);
        RCS_time = phy.rotationTime(initial_velocity, 0, acceleration);
        mass -= phy.updateFuelMassRCS(RCS_time);

        forces = environmentalForces(step1);
        xForce = forces[0];
        yForce = forces[1];

        accX = xForce/mass;
        accY = yForce/mass;
        ControllerState step2 = new ControllerState(posX, posY, target_angle, velX, velY, 0, mass);
        out.addAll(thrust(RCS_time,step2,accX,accY));

        return out;
    }

    /**
     * @param y         Current state of the lander
     * @return          The total natural forces acting on the lander (N)
     */
    private double[] environmentalForces(ControllerState y){

        double[] out = new double[2];

        double posX = y.getPosition().getX();
        double posY = y.getPosition().getY();
        double velX = y.getVelocity().getX();
        double velY = y.getVelocity().getY();
        double alt = y.getAltitude();
        double mass = y.getMass();

        double xForce = 0;
        double yForce = 0;

        double xDrag = phy.drag(velX, alt, true);
        double yDrag = phy.drag(velY, alt, false);

        if (velX < 0)
            xForce += xDrag;
        else xForce -= xDrag;

        if (velY < 0)
            yForce += yDrag;
        else yForce -= yDrag;

        double gravity = phy.gravity(y.getR(), mass);
        double gravity_Angle = phy.angleOfAction(Math.abs(posY), Math.abs(posX));

        if (posX > 0 && posY > 0) {
            xForce += phy.xComponent(-gravity, gravity_Angle);
            yForce += phy.yComponent(-gravity, gravity_Angle);
        }
        else if (posX < 0 && posY > 0) {
            xForce += phy.xComponent(gravity, gravity_Angle);
            yForce += phy.yComponent(-gravity, gravity_Angle);
        }
        else if (posX < 0 && posY < 0) {
            xForce += phy.xComponent(gravity, gravity_Angle);
            yForce += phy.yComponent(gravity, gravity_Angle);
        }
        else if (posX > 0 && posY < 0) {
            xForce += phy.xComponent(gravity, gravity_Angle);
            yForce += phy.yComponent(gravity, gravity_Angle);
        }

        double windForce = phy.windForce(alt);
        double windAngle = phy.windAngle();

        if (windAngle > Math.PI && windAngle < 1.5*Math.PI) {
            xForce += phy.xComponent(windForce, windAngle - Math.PI);
            yForce += phy.yComponent(windForce, windAngle - Math.PI);
        }
        else if (windAngle < 2*Math.PI && windAngle > 1.5*Math.PI) {
            xForce += phy.xComponent(-windForce, 2*Math.PI - windAngle);
            yForce += phy.yComponent(-windForce, 2*Math.PI - windAngle);
        }
        else if (windAngle < Math.PI && windAngle > 0.5*Math.PI) {
            xForce += phy.xComponent(windForce, Math.PI - windAngle);
            yForce += phy.yComponent(-windForce, Math.PI - windAngle);
        }
        else if (windAngle < 0.5*Math.PI && windAngle > 0) {
            xForce += phy.xComponent(-windForce, windAngle);
            yForce += phy.yComponent(-windForce, windAngle);
        } else if (windAngle < 0)
            System.out.println("Negative Wind Angle");

        return new double[]{xForce, yForce};
    }
}
