import interfaces.Vector3dInterface;

public class Vector3d implements Vector3dInterface {
    private double[] values = new double[]{0.0D, 0.0D, 0.0D};

    public double getX() {
        return this.values[0];
    }

    public void setX(double x) {
        this.values[0] = x;
    }

    public double getY() {
        return this.values[1];
    }

    public void setY(double y) {
        this.values[1] = y;
    }

    public double getZ() {
        return this.values[2];
    }

    public void setZ(double z) {
        this.values[2] = z;
    }

    public Vector3d() {
    }

    public Vector3d(double x, double y, double z) {
        this.values = new double[]{x, y, z};
    }

    public Vector3d(double[] values) {
        this.values = values;
    }

    public Vector3dInterface add(Vector3dInterface other) {
        double[] result = new double[]{this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ()};
        return new Vector3d(result);
    }

    public Vector3dInterface sub(Vector3dInterface other) {
        double[] result = new double[]{this.getX() - other.getX(), this.getY() - other.getY(), this.getZ() - other.getZ()};
        return new Vector3d(result);
    }

    public Vector3dInterface mul(double scalar) {
        double[] result = new double[]{this.getX() * scalar, this.getY() * scalar, this.getZ() * scalar};
        return new Vector3d(result);
    }

    public Vector3dInterface addMul(double scalar, Vector3dInterface other) {
        double[] result = new double[]{this.getX() + scalar * other.getX(), this.getY() + scalar * other.getY(), this.getZ() + scalar * other.getZ()};
        return new Vector3d(result);
    }

    public double norm() {
        return Math.sqrt(Math.pow(this.getX(), 2.0D) + Math.pow(this.getY(), 2.0D) + Math.pow(this.getZ(), 2.0D));
    }

    public double dist(Vector3dInterface other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2.0D) + Math.pow(other.getY() - this.getY(), 2.0D) + Math.pow(other.getZ() - this.getZ(), 2.0D));
    }

    public String toString() {
        double var10000 = this.getX();
        return "(" + var10000 + "," + this.getY() + "," + this.getZ() + ")";
    }
}
