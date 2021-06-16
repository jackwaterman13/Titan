import interfaces.ODEFunctionInterface;
import interfaces.RateInterface;
import interfaces.StateInterface;
import wind.WindFunction;

public class ClosedLoop {
    public double t;
    public double mass = 15200; // Lander Mass (kg)
    public double maxThrust = 42923; // Lander max thrust (N)
    double radius = 2574730; // radius of Titan (m)

    public static void main(String[] args) {
    }

    public ClosedLoop() {
    }

    /**
     * Equation for calculating Titan's air density at a given height
     * @param alt           altitude of lander (m)
     * @return              air density (kg/m^3)
     */
    private double airDensity(double alt) {
        return Math.pow(1267000.0 * Math.pow(2.718281828459045, -0.0532 * alt / 1000.0) * 10.0, -17.0);
    }

    /**
     * Calculates the drag force acting on the lander
     * @param velocity      velocity of the lander (m/s)
     * @param alt           altitude of the lander (m)
     * @param x             boolean for calculating drag force in x or y direction
     * @return
     */
    public double drag(double velocity, double alt, boolean x) {
        double dragCoe = 0.8;
        double areaX = 3.31*4.22;
        if (x) {
            return dragCoe * this.airDensity(alt) * velocity * velocity / 2 * areaX;
        } else {
            double areaY = 1.29;
            return dragCoe * this.airDensity(alt) * velocity * velocity / 2 * areaY;
        }
    }

    public class ControlFunction implements ODEFunctionInterface {

        WindFunction wf = new WindFunction();
        private double  posX, velX, posY,
                        velY, angP, angV, nVelX;

        public ControlFunction(ControllerState y){
            double tolerance = 1.0;
            ControllerState lander = y;

            posX = lander.getPosition().getX();
            posY = lander.getPosition().getY();
            angP = lander.getPosition().getY();
            velX = lander.getVelocity().getX();
            velY = lander.getVelocity().getY();
            angV = lander.getVelocity().getZ();


            double windF = wf.windForce(posX - radius);
            nVelX = velX + windF - drag(velX, posX, true);
            double reqThrust = this.uT(nVelX, posX, angP);
            double time = reqThrust/maxThrust;




        }

        public RateInterface call(double t, StateInterface y) {

            return null;
        }

        public double uT(double vel, double pos, double ang) {
            return Math.pow(vel, 2) / (2 * pos * Math.sin(ang));
        }

        /**
         * Calculates the angular velocity of the lander
         * @param vel       angular velocity of the lander (rad/s)
         * @param time      time step (s)
         * @return angular velocity of the lander (rad/s^2)
         */
        public double angularAcc(double vel, double time){
            return vel/time;
        }
    }
}