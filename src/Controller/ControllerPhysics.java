package Controller;

public class ControllerPhysics {
    private double TitanRadius = 2574730;                   // radius of Titan (m)
    private double G = 6.67408 * Math.pow(10, -11);         // Gravitational Constant
    private double titanMass = 1.3452 * Math.pow(10, 23);   // Mass of Titan (kg)
    private double windAngle = 1;
    private double RCS_radius = 3.31/2;                     // Distance from the center of mass to the RCS thrusters (m)

    public ControllerPhysics(){
        windAngle = getRandomDoubleBetweenRange(0, 2*Math.PI);
    }

    /**
     * @return          Random angle through which the wind force is applied (rad)
     */
    public double windAngle(){
        int changeAngle = getRandomIntBetweenRange(0,100);

        if (changeAngle < 33){
            windAngle += getRandomDoubleBetweenRange(0,0.5);
            if (windAngle > 2*Math.PI)
                windAngle -= 2*Math.PI;
        }
        else if (changeAngle == 69)
            windAngle = Math.abs(Math.PI - windAngle);
        return windAngle;
    }

    /**
     *
     * @param alt           Current altitude of the lander (m)
     * @return              Force of wind acting on the lander (N)
     */
    public double windForce(double alt){
        double windSpeed = windSpeed(alt);

        return 0.5 * airDensity(alt) * (3.31*4.22) * windSpeed * windSpeed;
    }

    /**
     * Equation for calculating Titan's air density at a given height about the surface.
     * The data ranged from 0 to 200 km above the surface. Numbers outside this range are
     * therefore inaccurate.
     * @param alt           altitude of lander (m)
     * @return              air density (kg/m^3)
     */
    public double airDensity(double alt) {
        return Math.pow(1267000.0 * Math.pow(2.718281828459045, -0.0532 * alt / 1000.0) * 10.0, -17.0);
    }

    /**
     * @param alt           Current altitude of the lander (m)
     * @return              Wind speed at this altitude (m/s)
     */
    public double windSpeed(double alt) {
        alt /= 1000;
        return (0.0773 + -8.69*alt + 8.69*pow(alt,2) + -2.91*pow(alt,3) + 0.482*pow(alt,4) + -0.0443*pow(alt,5) + 2.42E-03*pow(alt,6)
                + -8.1E-05*pow(alt,7) + 1.62E-06*pow(alt,8) + -1.8E-08*pow(alt, 9) + 8.46E-11*pow(alt,10));
    }

    public double pow(double x,double y){
        return Math.pow(x,y);
    }

    public double getRandomDoubleBetweenRange(double min, double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }

    public int getRandomIntBetweenRange(int min, int max){
        return (int) (Math.random()*((max-min)+1))+min;
    }

    /**
     * Calculates the drag force acting on the lander
     * @param velocity      velocity of the lander (m/s)
     * @param alt           altitude of the lander (m)
     * @param x             boolean for calculating drag force in x or y direction
     * @return              Drag force acting on the lander (X or Y axis) (N)
     */
    public double drag(double velocity, double alt, boolean x) {
        double dragCoe = 0.8;
        if (x) {
            double areaX = 3.31*4.22;
            return dragCoe * airDensity(alt) * velocity * velocity / 2 * areaX;
        } else {
            double areaY = Math.PI*RCS_radius*RCS_radius;
            return dragCoe * airDensity(alt) * velocity * velocity / 2 * areaY;
        }
    }

    /**
     * @param time      Time period the RCS thrusters are burning for (s)
     * @return          Loss of mass through exhausting gases (kg)
     */
    public double updateFuelMassRCS(double time){
        return time * 5.3523856;
    }

    /**
     * @param time      Time period the main thrusters burn for (s)
     * @return          Loss of mass to combustion (kg)
     */
    public double  updateFuelMassMT(double time){
        double O2 = time * 0.0904;
        double H2 = time * 0.0331;
        return O2 + H2;
    }

    /**
     *
     * @param force     Force acting on the lander (N)
     * @param radius    Distance between the centre of mass of the lander and the point the force is acting through (m)
     * @return          Torque applied to the lander (Nm)
     */
    public double torque(double force, double radius){
        return force * radius;
    }

    public double inertiaMoment(double mass, double radius){
        return mass*radius*radius;
    }

    public double displacement(double position, double velocity, double acceleration, double time){
        return position + (velocity * time) + (0.5 * acceleration * time * time);
    }

    public double nextVelocity(double velocity, double acceleration, double time){
        return velocity + acceleration * time;
    }

    public double finalVelocitySquared(double initialV, double acceleration, double displacement){
        return initialV * initialV + 2 * acceleration * displacement;
    }

    public double angleOfAction(double y, double x){
        return Math.atan(y/x);
    }

    public double xComponent(double force, double angle){
        return force * Math.cos(angle);
    }

    public double yComponent(double force, double angle){
        return force * Math.sin(angle);
    }

    /**
     *
     * @param force     Rotational force acting on the body (N)
     * @param radius    Distance from center of mass to the point of application (m)
     * @param mass      Mass of the rotating body (kg)
     * @return          Angular velocity of the body rad/s
     */
    public double angularVelocity(double force, double radius, double mass){
        return Math.sqrt(force * radius / mass);
    }

    public double rotationTime(double initial_velocity, double final_velocity, double acceleration){
        return (final_velocity - initial_velocity)/acceleration;
    }

    /**
     * @param angV      Angular velocity the body (rad/s)
     * @param radius    Distance from the center of mass to the thruster (m)
     * @param mass      Mass of the body (kg)
     * @return          Centripetal force acting on the rotating body (N)
     */
    public double centripetalForce(double angV, double radius, double mass){
        return (mass * angV * angV) / radius;
    }

    /**
     * @param angV      Angular velocity the body (rad/s)
     * @param radius    Distance from the center of mass to the thruster (m)
     * @param mass      Mass of the body (kg)
     * @return          Centrifugal force acting on a rotating body (N)
     */
    public double centrifugalForce(double angV, double radius, double mass){
        return mass * radius * angV * angV;
    }

    /**
     * @param R         Distance between center of mass of the two bodies (m)
     * @param landerM   Mass of the lander (kg)
     * @return          Gravitational force acting on the two bodies (N)
     */
    public double gravity(double R, double landerM) {
        return G * titanMass * landerM / (R*R);
    }
}
