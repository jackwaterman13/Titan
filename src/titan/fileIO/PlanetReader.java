package titan.fileIO;

import interfaces.given.Vector3dInterface;
import interfaces.own.DataInterface;
import interfaces.own.ReaderInterface;
import titan.utility.Planet;
import titan.utility.Rocket;
import titan.math.Vector3d;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlanetReader implements ReaderInterface<DataInterface> {
	List<DataInterface> objectsInSpace = null;

	/**
	 * Method to access the list containing the file data converted to objects.
	 *
	 * @return List with data if any read succeeded with the class instance.
	 */
	public List<DataInterface> getList() { return objectsInSpace; }

	String fileLocation = "Location of solar system data here";

	/**
	 * Gets the file path that is currently stored in the PlanetReader instance.
	 *
	 * @return A String containing the file path. Ex: "Directory/.../filename.filetype"
	 */
	public String getFilePath(){ return fileLocation; }

	/**
	 * Sets the entire file path that the PlanetReader instance should use when trying to perform a read() operation.
	 *
	 * @param fileLocation2Use - entire path to the location of where the file is stored at.
	 */
	public void setFilePath(String fileLocation2Use){ fileLocation = fileLocation2Use; }

	/**
	 * Tries to access and read data inside the file located at the file path remembered in the PlanetReader instance
	 */
	public void read() {
		objectsInSpace = new ArrayList<>();
		String
				massLabel 	= "mass=",
				radiusLabel = "radius=",
				xPosLabel 	= "x=",
				yPosLabel 	= "y=",
				zPosLabel 	= "z=",
				xVelLabel 	= "vx=",
				yVelLabel 	= "vy=",
				zVelLabel 	= "vz=";

		DataInterface data;
		Vector3dInterface pos, vel;

		StringBuilder
				massBuilder, radiusBuilder,
				pxBuilder, pyBuilder, pzBuilder,
				vxBuilder, vyBuilder, vzBuilder;


		try{
			File f = new File(fileLocation);
			if (!f.exists()){ System.out.println("File at specified location does not exist! | Location: " + fileLocation); return; }
			Scanner reader  = new Scanner(f);
			while(reader.hasNextLine()){
				String line = reader.nextLine();
				if (line.length() < 1 || line.contains("//") || line.startsWith(" ")){ continue; }

				massBuilder = new StringBuilder();
				radiusBuilder = new StringBuilder();
				pxBuilder = new StringBuilder();
				pyBuilder = new StringBuilder();
				pzBuilder = new StringBuilder();
				vxBuilder = new StringBuilder();
				vyBuilder = new StringBuilder();
				vzBuilder = new StringBuilder();

				int massIndex 	= line.indexOf(massLabel);
				int radiusIndex = line.indexOf(radiusLabel);
				int xPosIndex 	= line.indexOf(xPosLabel);
				int yPosIndex 	= line.indexOf(yPosLabel);
				int zPosIndex 	= line.indexOf(zPosLabel);
				int xVelIndex 	= line.indexOf(xVelLabel);
				int yVelIndex 	= line.indexOf(yVelLabel);
				int zVelIndex 	= line.indexOf(zVelLabel);

				char[] charInLine = line.toCharArray();
				for(int i = massIndex + massLabel.length(); i < xPosIndex; i++){
					if 		(charInLine[i] == ',') { break; }
					else if (charInLine[i] == ' ') { continue; }
					massBuilder.append(charInLine[i]);
				}

				if (radiusIndex > -1){
					for(int i = radiusIndex + radiusLabel.length(); i < xPosIndex; i++){
						if 		(charInLine[i] == ',') { 	break; }
						else if (charInLine[i] == ' ') { continue; }
						radiusBuilder.append(charInLine[i]);
					}
				}

				for(int i = xPosIndex + xPosLabel.length(); i < yPosIndex; i++){
					if 		(charInLine[i] == ',') { 	break; }
					else if (charInLine[i] == ' ') { continue; }
					pxBuilder.append(charInLine[i]);
				}

				for(int i = yPosIndex + yPosLabel.length(); i < zPosIndex; i++){
					if 		(charInLine[i] == ',') { 	break; }
					else if (charInLine[i] == ' ') { continue; }
					pyBuilder.append(charInLine[i]);
				}

				for(int i = zPosIndex + zPosLabel.length(); i < xVelIndex; i++){
					if 		(charInLine[i] == ',') {	break; }
					else if (charInLine[i] == ' ') { continue; }
					pzBuilder.append(charInLine[i]);
				}

				for(int i = xVelIndex + xVelLabel.length(); i < yVelIndex; i++){
					if 		(charInLine[i] == ',') {	break; }
					else if (charInLine[i] == ' ') { continue; }
					vxBuilder.append(charInLine[i]);
				}

				for(int i = yVelIndex + yVelLabel.length(); i < zVelIndex; i++){
					if 		(charInLine[i] == ',') {	break; }
					else if (charInLine[i] == ' ') { continue; }
					vyBuilder.append(charInLine[i]);
				}

				for(int i = zVelIndex + zVelLabel.length(); i < line.length(); i++){
					if 		(charInLine[i] == ',') { 	break; }
					else if (charInLine[i] == '}') { 	break; }
					else if (charInLine[i] == ' ') { continue; }
					vzBuilder.append(charInLine[i]);
				}

				double[] d = {
						Double.parseDouble(massBuilder.toString()),
						0.0,
						Double.parseDouble(pxBuilder.toString()),
						Double.parseDouble(pyBuilder.toString()),
						Double.parseDouble(pzBuilder.toString()),
						Double.parseDouble(vxBuilder.toString()),
						Double.parseDouble(vyBuilder.toString()),
						Double.parseDouble(vzBuilder.toString()),

				};

				if (!radiusBuilder.toString().equals("")){ d[1] = Double.parseDouble(radiusBuilder.toString()); }

				pos = new Vector3d(d[2], d[3], d[4]);
				vel = new Vector3d(d[5], d[6], d[7]);

				String name = line.substring(0, line.indexOf(":"));

				if (name.equals("Rocket")){ data = new Rocket(d[0], pos, vel); }
				else{ data = new Planet(name, d[0], d[1], pos, vel); }
				objectsInSpace.add(data);

				d = null;
			}
			reader.close();
		}
		catch(Exception e){ e.printStackTrace(); }
	}
}