package titan.lamberts;

import titan.math.Vector3d;

public class Lamberts {
    private static final double G = 6.67408 * Math.pow(10, -11); // gravitation constant
    private static final double Ms = 1.988500e30;
    double y = 1E10;


    public Vector3d lamberts(Vector3d probe, Vector3d saturn, double flightTime){

        double deltaT = flightTime;
        double Pi = Math.PI;
        double tolerance = 1E-3;
        double maxStep = 20000;
        double psi = 0;
        double psiU = y;
        double psiL = y;
        double tm = 1;
        double mu = G*Ms;
        double sqrt_mu = Math.sqrt(mu);

        double r0 = probe.norm();
        double r1 = saturn.norm();

        double gamma = (probe.getX()*saturn.getX() + probe.getY()*saturn.getY() + probe.getZ()*saturn.getZ())/(r0*r1);

        double beta = tm*Math.sqrt(1-gamma*gamma);

        double A = tm*Math.sqrt(r0*r1*(1+gamma));

        if (A==0){
            System.out.println("A is null");
            return null;
        }

        double c2 = C2(psi);
        double c3 = C3(psi);
        boolean solved = false;
        double B=0;
        double deltaT_;

        double chi3;

        //for (int i=1; i<maxStep; i++){
        while (!solved){

            B = r0+r1+A*(psi*c3-1)/Math.sqrt(c2);

            if (A>0 && B<0){
                psiL=Pi;
                B*=-1;
            }

            chi3 = Math.pow(Math.sqrt(B/c2),3);

            deltaT_ = (chi3*c3+A*Math.sqrt(B))/sqrt_mu;

            if (Math.abs(deltaT-deltaT_)<tolerance){
                solved=true;
                break;
            }

            if (deltaT_<=deltaT)
                psiL = psi;
            else psiU = psi;

            psi = (psiU+psiL)/2;

            c2 = C2(psi);
            c3 = C3(psi);
        }

        if (!solved){
            System.out.println("Lambert did NOT converge");
            return null;
        }

        double F = 1-B/r0;
        double g = A*Math.sqrt(B/mu);
        double gDot = 1-B/r1;

        double x = 1;

        double nX = x*(saturn.getX()-probe.getX()*F)/g;
        double nY = x*(saturn.getY()-probe.getY()*F)/g;
        double nZ = x*(saturn.getZ()-probe.getZ()*F)/g;

        Vector3d out = new Vector3d(nX,nY,nZ);
        System.out.println("lambert " + out.toString());
        return out;
    }

    private static double C2(double var){
        return (1-Math.cos(Math.sqrt(var)))/var;
    }

    private static double C3(double var){
        return (Math.sqrt(var) - Math.sin(Math.sqrt(var)))/(var*Math.sqrt(var));
    }
}