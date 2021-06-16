public class NewtonLander {

    double tMass = 1.3452 * Math.pow(10, 23);
    double G = 6.67408 * Math.pow(10, -11);

    public NewtonLander() {
    }

    public double gravity(double height, double landerM) {
        return this.G * this.tMass * landerM / (height * height);
    }

    public double acceleration(double landerM, double force) {
        return force / landerM;
    }
}