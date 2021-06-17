package titan.utility;

import interfaces.given.StateInterface;
import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import titan.math.Vector3d;
import titan.physics.State;

public class InitialState {
    private static final String[] s = {
            "Sun: { mass=1.988500e30,   x=-6.806783239281648e+08,  y= 1.080005533878725e+09,  z= 6.564012751690170e+06,  vx=-1.420511669610689e+01, vy=-4.954714716629277e+00, vz= 3.994237625449041e-01 }",
            "Mercury: { mass=3.302e23,   x= 6.047855986424127e+06,  y=-6.801800047868888e+10,  z=-5.702742359714534e+09,  vx= 3.892585189044652e+04, vy= 2.978342247012996e+03, vz=-3.327964151414740e+03 }",
            "Venus: { mass=4.8685e24,   x=-9.435345478592035e+10,  y= 5.350359551033670e+10,  z= 6.131453014410347e+09,  vx=-1.726404287724406e+04, vy=-3.073432518238123e+04, vz= 5.741783385280979e-04 }",
            "Earth: { mass=5.97219e24,   radius=6371e3,   x=-1.471922101663588e+11,  y=-2.860995816266412e+10,  z= 8.278183193596080e+06,  vx= 5.427193405797901e+03, vy=-2.931056622265021e+04, vz= 6.575428158157592e-01 }",
            "Moon: { mass=7.349e22,   x=-1.472343904597218e+11,  y=-2.822578361503422e+10,  z= 1.052790970065631e+07,  vx= 4.433121605215677e+03, vy=-2.948453614110320e+04, vz= 8.896598225322805e+01 }",
            "Mars: { mass=6.4171e23,   x=-3.615638921529161e+10,  y=-2.167633037046744e+11,  z=-3.687670305939779e+09,  vx= 2.481551975121696e+04, vy=-1.816368005464070e+03, vz=-6.467321619018108e+02 }",
            "Jupiter: { mass=1.89813e27,   x= 1.781303138592153e+11,  y=-7.551118436250277e+11,  z=-8.532838524802327e+08,  vx= 1.255852555185220e+04, vy= 3.622680192790968e+03, vz=-2.958620380112444e+02 }",
            "Saturn: { mass=5.6834e26,   x= 6.328646641500651e+11,  y=-1.358172804527507e+12,  z=-1.578520137930810e+09,  vx= 8.220842186554890e+03, vy= 4.052137378979608e+03, vz=-3.976224719266916e+02 }",
            "Titan: { mass=1.34553e23,   radius=2575.5e3,   x= 6.332873118527889e+11,  y=-1.357175556995868e+12,  z=-2.134637041453660e+09,  vx= 3.056877965721629e+03, vy= 6.125612956428791e+03, vz=-9.523587380845593e+02 }",
            "Uranus: { mass=8.6813e25,   x= 2.395195786685187e+12,  y= 1.744450959214586e+12,  z=-2.455116324031639e+10,  vx=-4.059468635313243e+03, vy= 5.187467354884825e+03, vz= 7.182516236837899e+01 }",
            "Neptune: { mass=1.02413e26,   x= 4.382692942729203e+12,  y=-9.093501655486243e+11,  z=-8.227728929479486e+10,  vx= 1.068410720964204e+03, vy= 5.354959501569486e+03, vz=-1.343918199987533e+02 }" };

    public static DataInterface[] getInitialState(){
        DataInterface[] objects = new DataInterface[s.length];

        String
                massLabel 	= "mass=",
                radiusLabel = "radius=",
                xPosLabel 	= "x=",
                yPosLabel 	= "y=",
                zPosLabel 	= "z=",
                xVelLabel 	= "vx=",
                yVelLabel 	= "vy=",
                zVelLabel 	= "vz=";

        Vector3dInterface x, v;

        StringBuilder sb = new StringBuilder();

        try{
            for(int i = 0; i < s.length; i++){
                double[] vars = new double[8];
                int startIndex 	= s[i].indexOf(massLabel);
                int endIndex = s[i].indexOf(xPosLabel);

                for(int j = startIndex + massLabel.length(); j < endIndex; j++){
                    if (s[i].charAt(j) == ','){ break; }
                    else if (s[i].charAt(j) == ' '){ continue; }
                    sb.append(s[i].charAt(j));
                }

                vars[0] = Double.parseDouble(sb.toString());

                sb = new StringBuilder();

                startIndex = s[i].indexOf(radiusLabel);
                if (startIndex > -1){
                    for(int j = startIndex + radiusLabel.length(); j < endIndex; j++){
                        if (s[i].charAt(j) == ','){ break; }
                        else if (s[i].charAt(j) == ' '){ continue; }
                        sb.append(s[i].charAt(j));
                    }

                    vars[1] = Double.parseDouble(sb.toString());

                    sb = new StringBuilder();
                }
                else{ vars[1] = 0.0; }

                startIndex = s[i].indexOf(xPosLabel);
                endIndex = s[i].indexOf(yPosLabel);
                for(int j = startIndex + xPosLabel.length(); j < endIndex; j++){
                    if (s[i].charAt(j) == ','){ break; }
                    else if (s[i].charAt(j) == ' '){ continue; }
                    sb.append(s[i].charAt(j));
                }

                vars[2] = Double.parseDouble(sb.toString());

                sb = new StringBuilder();

                startIndex = s[i].indexOf(yPosLabel);
                endIndex = s[i].indexOf(zPosLabel);
                for(int j = startIndex + yPosLabel.length(); j < endIndex; j++){
                    if (s[i].charAt(j) == ','){ break; }
                    else if (s[i].charAt(j) == ' '){ continue; }
                    sb.append(s[i].charAt(j));
                }

                vars[3] = Double.parseDouble(sb.toString());

                sb = new StringBuilder();

                startIndex = s[i].indexOf(zPosLabel);
                endIndex = s[i].indexOf(xVelLabel);
                for(int j = startIndex + zPosLabel.length(); j < endIndex; j++){
                    if (s[i].charAt(j) == ','){ break; }
                    else if (s[i].charAt(j) == ' '){ continue; }
                    sb.append(s[i].charAt(j));
                }

                vars[4] = Double.parseDouble(sb.toString());

                sb = new StringBuilder();

                startIndex = s[i].indexOf(xVelLabel);
                endIndex = s[i].indexOf(yVelLabel);
                for(int j = startIndex + xVelLabel.length(); j < endIndex; j++){
                    if (s[i].charAt(j) == ','){ break; }
                    else if (s[i].charAt(j) == ' '){ continue; }
                    sb.append(s[i].charAt(j));
                }

                vars[5] = Double.parseDouble(sb.toString());

                sb = new StringBuilder();

                startIndex = s[i].indexOf(yVelLabel);
                endIndex = s[i].indexOf(zVelLabel);
                for(int j = startIndex + yVelLabel.length(); j < endIndex; j++){
                    if (s[i].charAt(j) == ','){ break; }
                    else if (s[i].charAt(j) == ' '){ continue; }
                    sb.append(s[i].charAt(j));
                }

                vars[6] = Double.parseDouble(sb.toString());

                sb = new StringBuilder();

                startIndex = s[i].indexOf(zVelLabel);
                endIndex = s[i].length();
                for(int j = startIndex + xVelLabel.length(); j < endIndex; j++){
                    if (s[i].charAt(j) == ','){ break; }
                    else if (s[i].charAt(j) == ' '){ continue; }
                    else if (s[i].charAt(j) == '}'){ break; }
                    sb.append(s[i].charAt(j));
                }

                vars[7] = Double.parseDouble(sb.toString());

                sb = new StringBuilder();
                
                x = new Vector3d(vars[2], vars[3], vars[4]);
                v = new Vector3d(vars[5], vars[6], vars[7]);
                objects[i] = new Planet(
                        s[i].substring(0, s[i].indexOf(":")),
                        vars[0],
                        vars[1],
                        x,
                        v
                );
            }
        }
        catch(Exception e){ e.printStackTrace(); }
        return objects;
    }
}