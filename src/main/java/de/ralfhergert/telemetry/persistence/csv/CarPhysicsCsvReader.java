package de.ralfhergert.telemetry.persistence.csv;

import de.ralfhergert.telemetry.pc2.datagram.Vector;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPackage;
import de.ralfhergert.telemetry.pc2.datagram.v2.PackageTypes;
import de.ralfhergert.telemetry.repository.Repository;

import java.io.*;
import java.util.Date;

/**
 * Writes a stream of {@link CarPhysicsPackage} as CSV onto given {@link OutputStream}.
 */
public class CarPhysicsCsvReader {

	public void read(Repository<CarPhysicsPackage> repository, InputStream inStream) throws IOException {
		if (inStream == null) {
			throw new IllegalArgumentException("inStream can not be null");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));

		String firstLine = reader.readLine();
		if (!firstLine.startsWith("#CarPhysics:v2")) {
			return; // this stream can not be rad by this parser.
		}
		if (firstLine.contains("withColumnHeaders")) {
			reader.readLine(); // this line is supposed to be the column headers.
		}
		reader.lines().forEach((line) -> {
			String[] part = line.split(",");
			CarPhysicsPackage carPhysicsPackage = new CarPhysicsPackage();
			carPhysicsPackage.setReceivedDate(new Date(Integer.valueOf(part[0])));
			carPhysicsPackage.setPacketNumber(Integer.valueOf(part[1]));
			carPhysicsPackage.setCategoryPacketNumber(Integer.valueOf(part[2]));
			carPhysicsPackage.setPartialPacketIndex(Short.valueOf(part[3]));
			carPhysicsPackage.setPartialPacketNumber(Short.valueOf(part[4]));
			carPhysicsPackage.setPacketType(PackageTypes.valueOf(part[5]));
			carPhysicsPackage.setPacketVersion(Short.valueOf(part[6]));
			carPhysicsPackage.viewedParticipantIndex = Short.valueOf(part[7]);
			carPhysicsPackage.unfilteredThrottle = Short.valueOf(part[8]);
			carPhysicsPackage.unfilteredBrake = Short.valueOf(part[9]);
			carPhysicsPackage.unfilteredSteering = Short.valueOf(part[10]);
			carPhysicsPackage.unfilteredClutch = Short.valueOf(part[11]);
			// Car state
			carPhysicsPackage.carFlags = Byte.valueOf(part[12]);
			carPhysicsPackage.oilTempCelsius = Short.valueOf(part[13]);
			carPhysicsPackage.oilPressureKPa = Short.valueOf(part[14]);
			carPhysicsPackage.waterTempCelsius = Short.valueOf(part[15]);
			carPhysicsPackage.waterPressureKpa = Short.valueOf(part[16]);
			carPhysicsPackage.fuelPressureKpa = Short.valueOf(part[17]);
			carPhysicsPackage.fuelCapacity = Short.valueOf(part[18]);

			carPhysicsPackage.brake = Short.valueOf(part[19]);
			carPhysicsPackage.throttle = Short.valueOf(part[20]);
			carPhysicsPackage.clutch = Short.valueOf(part[21]);
			carPhysicsPackage.fuelLevel = Float.valueOf(part[22]);
			carPhysicsPackage.speed = Float.valueOf(part[23]);
			carPhysicsPackage.rpm = Short.valueOf(part[24]);
			carPhysicsPackage.maxRpm = Short.valueOf(part[25]);
			carPhysicsPackage.steering = Short.valueOf(part[26]);
			carPhysicsPackage.gearNumGears = Short.valueOf(part[27]);
			carPhysicsPackage.boostAmount = Short.valueOf(part[28]);
			carPhysicsPackage.crashState = Short.valueOf(part[29]);
			carPhysicsPackage.odometerKM = Float.valueOf(part[30]);

			carPhysicsPackage.orientation = new Vector(Float.valueOf(part[31]), Float.valueOf(part[32]), Float.valueOf(part[33]));
			carPhysicsPackage.localVelocity = new Vector(Float.valueOf(part[34]), Float.valueOf(part[35]), Float.valueOf(part[36]));
			carPhysicsPackage.worldVelocity = new Vector(Float.valueOf(part[37]), Float.valueOf(part[38]), Float.valueOf(part[39]));
			carPhysicsPackage.angularVelocity = new Vector(Float.valueOf(part[40]), Float.valueOf(part[41]), Float.valueOf(part[42]));
			carPhysicsPackage.localAcceleration = new Vector(Float.valueOf(part[43]), Float.valueOf(part[44]), Float.valueOf(part[45]));
			carPhysicsPackage.worldAcceleration = new Vector(Float.valueOf(part[46]), Float.valueOf(part[47]), Float.valueOf(part[48]));
			carPhysicsPackage.extentsCentre = new Vector(Float.valueOf(part[49]), Float.valueOf(part[50]), Float.valueOf(part[51]));
			carPhysicsPackage.tyreFlags = new byte[]{Byte.valueOf(part[52]), Byte.valueOf(part[53]), Byte.valueOf(part[54]), Byte.valueOf(part[55])};

			carPhysicsPackage.terrain = new byte[]{Byte.valueOf(part[56]), Byte.valueOf(part[57]), Byte.valueOf(part[58]), Byte.valueOf(part[59])};
			carPhysicsPackage.tyreY = new float[]{Float.valueOf(part[60]), Float.valueOf(part[61]), Float.valueOf(part[62]), Float.valueOf(part[63])};

			carPhysicsPackage.tyreRPS = new float[]{Float.valueOf(part[64]), Float.valueOf(part[65]), Float.valueOf(part[66]), Float.valueOf(part[67])};
			carPhysicsPackage.tyreTemp = new short[]{Short.valueOf(part[68]), Short.valueOf(part[69]), Short.valueOf(part[70]), Short.valueOf(part[71])};

			carPhysicsPackage.tyreHeightAboveGround = new float[]{Float.valueOf(part[72]), Float.valueOf(part[73]), Float.valueOf(part[74]), Float.valueOf(part[75])};
			carPhysicsPackage.tyreWear = new short[]{Short.valueOf(part[76]), Short.valueOf(part[77]), Short.valueOf(part[78]), Short.valueOf(part[79])};
			carPhysicsPackage.brakeDamage = new short[]{Short.valueOf(part[80]), Short.valueOf(part[81]), Short.valueOf(part[82]), Short.valueOf(part[83])};
			carPhysicsPackage.suspensionDamage = new short[]{Short.valueOf(part[84]), Short.valueOf(part[85]), Short.valueOf(part[86]), Short.valueOf(part[87])};
			carPhysicsPackage.brakeTempCelsius = new short[]{Short.valueOf(part[88]), Short.valueOf(part[89]), Short.valueOf(part[90]), Short.valueOf(part[91])};
			carPhysicsPackage.tyreTreadTemp = new short[]{Short.valueOf(part[92]), Short.valueOf(part[93]), Short.valueOf(part[94]), Short.valueOf(part[95])};
			carPhysicsPackage.tyreLayerTemp = new short[]{Short.valueOf(part[96]), Short.valueOf(part[97]), Short.valueOf(part[98]), Short.valueOf(part[99])};
			carPhysicsPackage.tyreCarcassTemp = new short[]{Short.valueOf(part[100]), Short.valueOf(part[101]), Short.valueOf(part[102]), Short.valueOf(part[103])};
			carPhysicsPackage.tyreRimTemp = new short[]{Short.valueOf(part[104]), Short.valueOf(part[105]), Short.valueOf(part[106]), Short.valueOf(part[107])};
			carPhysicsPackage.tyreInternalAirTemp = new short[]{Short.valueOf(part[108]), Short.valueOf(part[109]), Short.valueOf(part[110]), Short.valueOf(part[111])};
			carPhysicsPackage.tyreTempLeft = new short[]{Short.valueOf(part[112]), Short.valueOf(part[113]), Short.valueOf(part[114]), Short.valueOf(part[115])};
			carPhysicsPackage.tyreTempCenter = new short[]{Short.valueOf(part[116]), Short.valueOf(part[117]), Short.valueOf(part[118]), Short.valueOf(part[119])};
			carPhysicsPackage.tyreTempRight = new short[]{Short.valueOf(part[120]), Short.valueOf(part[121]), Short.valueOf(part[122]), Short.valueOf(part[123])};
			carPhysicsPackage.wheelLocalPositionY = new float[]{Float.valueOf(part[124]), Float.valueOf(part[125]), Float.valueOf(part[126]), Float.valueOf(part[127])};
			carPhysicsPackage.rideHeight = new float[]{Float.valueOf(part[128]), Float.valueOf(part[129]), Float.valueOf(part[130]), Float.valueOf(part[131])};
			carPhysicsPackage.suspensionTravel = new float[]{Float.valueOf(part[132]), Float.valueOf(part[133]), Float.valueOf(part[134]), Float.valueOf(part[135])};
			carPhysicsPackage.suspensionVelocity = new float[]{Float.valueOf(part[136]), Float.valueOf(part[137]), Float.valueOf(part[138]), Float.valueOf(part[139])};
			carPhysicsPackage.suspensionRideHeight = new float[]{Float.valueOf(part[140]), Float.valueOf(part[141]), Float.valueOf(part[142]), Float.valueOf(part[143])};
			carPhysicsPackage.airPressure = new float[]{Float.valueOf(part[144]), Float.valueOf(part[145]), Float.valueOf(part[146]), Float.valueOf(part[147])};

			carPhysicsPackage.engineSpeed = Float.valueOf(part[148]);
			carPhysicsPackage.engineTorque = Float.valueOf(part[149]);
			carPhysicsPackage.wings = new short[]{Short.valueOf(part[150]), Short.valueOf(part[151])};
			carPhysicsPackage.handBrake = Short.valueOf(part[152]);
			// Car damage
			carPhysicsPackage.aeroDamage = Short.valueOf(part[153]);
			carPhysicsPackage.engineDamage = Short.valueOf(part[154]);
			//  HW state
			carPhysicsPackage.joyPad0 = Integer.valueOf(part[155]);
			carPhysicsPackage.dPad = Short.valueOf(part[156]);

			carPhysicsPackage.tyreCompound = new String[]{part[157], part[158], part[159], part[160]};
			carPhysicsPackage.turboBoostPressure = Float.valueOf(part[161]);

			carPhysicsPackage.fullPosition = new Vector(Float.valueOf(part[162]), Float.valueOf(part[163]), Float.valueOf(part[164]));
			carPhysicsPackage.brakeBias = Short.valueOf(part[165]);
			repository.addItem(carPhysicsPackage);
		});
	}
}
