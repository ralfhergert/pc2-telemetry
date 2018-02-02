package de.ralfhergert.telemetry.persistence.csv;

import de.ralfhergert.telemetry.pc2.datagram.Vector;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.PacketTypes;
import de.ralfhergert.telemetry.repository.Repository;

import java.io.*;
import java.util.Date;

/**
 * Writes a stream of {@link CarPhysicsPacket} as CSV onto given {@link OutputStream}.
 */
public class CarPhysicsCsvReader {

	public void read(Repository<CarPhysicsPacket> repository, InputStream inStream) throws IOException {
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
			CarPhysicsPacket carPhysicsPacket = new CarPhysicsPacket();
			carPhysicsPacket.setReceivedDate(new Date(Long.valueOf(part[0])));
			carPhysicsPacket.setPacketNumber(Integer.valueOf(part[1]));
			carPhysicsPacket.setCategoryPacketNumber(Integer.valueOf(part[2]));
			carPhysicsPacket.setPartialPacketIndex(Short.valueOf(part[3]));
			carPhysicsPacket.setPartialPacketNumber(Short.valueOf(part[4]));
			carPhysicsPacket.setPacketType(PacketTypes.valueOf(part[5]));
			carPhysicsPacket.setPacketVersion(Short.valueOf(part[6]));
			carPhysicsPacket.viewedParticipantIndex = Short.valueOf(part[7]);
			carPhysicsPacket.unfilteredThrottle = Short.valueOf(part[8]);
			carPhysicsPacket.unfilteredBrake = Short.valueOf(part[9]);
			carPhysicsPacket.unfilteredSteering = Short.valueOf(part[10]);
			carPhysicsPacket.unfilteredClutch = Short.valueOf(part[11]);
			// Car state
			carPhysicsPacket.carFlags = Byte.valueOf(part[12]);
			carPhysicsPacket.oilTempCelsius = Short.valueOf(part[13]);
			carPhysicsPacket.oilPressureKPa = Short.valueOf(part[14]);
			carPhysicsPacket.waterTempCelsius = Short.valueOf(part[15]);
			carPhysicsPacket.waterPressureKpa = Short.valueOf(part[16]);
			carPhysicsPacket.fuelPressureKpa = Short.valueOf(part[17]);
			carPhysicsPacket.fuelCapacity = Short.valueOf(part[18]);

			carPhysicsPacket.brake = Short.valueOf(part[19]);
			carPhysicsPacket.throttle = Short.valueOf(part[20]);
			carPhysicsPacket.clutch = Short.valueOf(part[21]);
			carPhysicsPacket.fuelLevel = Float.valueOf(part[22]);
			carPhysicsPacket.speed = Float.valueOf(part[23]);
			carPhysicsPacket.rpm = Short.valueOf(part[24]);
			carPhysicsPacket.maxRpm = Short.valueOf(part[25]);
			carPhysicsPacket.steering = Short.valueOf(part[26]);
			carPhysicsPacket.gearNumGears = Short.valueOf(part[27]);
			carPhysicsPacket.boostAmount = Short.valueOf(part[28]);
			carPhysicsPacket.crashState = Short.valueOf(part[29]);
			carPhysicsPacket.odometerKM = Float.valueOf(part[30]);

			carPhysicsPacket.orientation = new Vector(Float.valueOf(part[31]), Float.valueOf(part[32]), Float.valueOf(part[33]));
			carPhysicsPacket.localVelocity = new Vector(Float.valueOf(part[34]), Float.valueOf(part[35]), Float.valueOf(part[36]));
			carPhysicsPacket.worldVelocity = new Vector(Float.valueOf(part[37]), Float.valueOf(part[38]), Float.valueOf(part[39]));
			carPhysicsPacket.angularVelocity = new Vector(Float.valueOf(part[40]), Float.valueOf(part[41]), Float.valueOf(part[42]));
			carPhysicsPacket.localAcceleration = new Vector(Float.valueOf(part[43]), Float.valueOf(part[44]), Float.valueOf(part[45]));
			carPhysicsPacket.worldAcceleration = new Vector(Float.valueOf(part[46]), Float.valueOf(part[47]), Float.valueOf(part[48]));
			carPhysicsPacket.extentsCentre = new Vector(Float.valueOf(part[49]), Float.valueOf(part[50]), Float.valueOf(part[51]));
			carPhysicsPacket.tyreFlags = new byte[]{Byte.valueOf(part[52]), Byte.valueOf(part[53]), Byte.valueOf(part[54]), Byte.valueOf(part[55])};

			carPhysicsPacket.terrain = new byte[]{Byte.valueOf(part[56]), Byte.valueOf(part[57]), Byte.valueOf(part[58]), Byte.valueOf(part[59])};
			carPhysicsPacket.tyreY = new float[]{Float.valueOf(part[60]), Float.valueOf(part[61]), Float.valueOf(part[62]), Float.valueOf(part[63])};

			carPhysicsPacket.tyreRPS = new float[]{Float.valueOf(part[64]), Float.valueOf(part[65]), Float.valueOf(part[66]), Float.valueOf(part[67])};
			carPhysicsPacket.tyreTemp = new short[]{Short.valueOf(part[68]), Short.valueOf(part[69]), Short.valueOf(part[70]), Short.valueOf(part[71])};

			carPhysicsPacket.tyreHeightAboveGround = new float[]{Float.valueOf(part[72]), Float.valueOf(part[73]), Float.valueOf(part[74]), Float.valueOf(part[75])};
			carPhysicsPacket.tyreWear = new short[]{Short.valueOf(part[76]), Short.valueOf(part[77]), Short.valueOf(part[78]), Short.valueOf(part[79])};
			carPhysicsPacket.brakeDamage = new short[]{Short.valueOf(part[80]), Short.valueOf(part[81]), Short.valueOf(part[82]), Short.valueOf(part[83])};
			carPhysicsPacket.suspensionDamage = new short[]{Short.valueOf(part[84]), Short.valueOf(part[85]), Short.valueOf(part[86]), Short.valueOf(part[87])};
			carPhysicsPacket.brakeTempCelsius = new short[]{Short.valueOf(part[88]), Short.valueOf(part[89]), Short.valueOf(part[90]), Short.valueOf(part[91])};
			carPhysicsPacket.tyreTreadTemp = new short[]{Short.valueOf(part[92]), Short.valueOf(part[93]), Short.valueOf(part[94]), Short.valueOf(part[95])};
			carPhysicsPacket.tyreLayerTemp = new short[]{Short.valueOf(part[96]), Short.valueOf(part[97]), Short.valueOf(part[98]), Short.valueOf(part[99])};
			carPhysicsPacket.tyreCarcassTemp = new short[]{Short.valueOf(part[100]), Short.valueOf(part[101]), Short.valueOf(part[102]), Short.valueOf(part[103])};
			carPhysicsPacket.tyreRimTemp = new short[]{Short.valueOf(part[104]), Short.valueOf(part[105]), Short.valueOf(part[106]), Short.valueOf(part[107])};
			carPhysicsPacket.tyreInternalAirTemp = new short[]{Short.valueOf(part[108]), Short.valueOf(part[109]), Short.valueOf(part[110]), Short.valueOf(part[111])};
			carPhysicsPacket.tyreTempLeft = new short[]{Short.valueOf(part[112]), Short.valueOf(part[113]), Short.valueOf(part[114]), Short.valueOf(part[115])};
			carPhysicsPacket.tyreTempCenter = new short[]{Short.valueOf(part[116]), Short.valueOf(part[117]), Short.valueOf(part[118]), Short.valueOf(part[119])};
			carPhysicsPacket.tyreTempRight = new short[]{Short.valueOf(part[120]), Short.valueOf(part[121]), Short.valueOf(part[122]), Short.valueOf(part[123])};
			carPhysicsPacket.wheelLocalPositionY = new float[]{Float.valueOf(part[124]), Float.valueOf(part[125]), Float.valueOf(part[126]), Float.valueOf(part[127])};
			carPhysicsPacket.rideHeight = new float[]{Float.valueOf(part[128]), Float.valueOf(part[129]), Float.valueOf(part[130]), Float.valueOf(part[131])};
			carPhysicsPacket.suspensionTravel = new float[]{Float.valueOf(part[132]), Float.valueOf(part[133]), Float.valueOf(part[134]), Float.valueOf(part[135])};
			carPhysicsPacket.suspensionVelocity = new float[]{Float.valueOf(part[136]), Float.valueOf(part[137]), Float.valueOf(part[138]), Float.valueOf(part[139])};
			carPhysicsPacket.suspensionRideHeight = new float[]{Float.valueOf(part[140]), Float.valueOf(part[141]), Float.valueOf(part[142]), Float.valueOf(part[143])};
			carPhysicsPacket.airPressure = new float[]{Float.valueOf(part[144]), Float.valueOf(part[145]), Float.valueOf(part[146]), Float.valueOf(part[147])};

			carPhysicsPacket.engineSpeed = Float.valueOf(part[148]);
			carPhysicsPacket.engineTorque = Float.valueOf(part[149]);
			carPhysicsPacket.wings = new short[]{Short.valueOf(part[150]), Short.valueOf(part[151])};
			carPhysicsPacket.handBrake = Short.valueOf(part[152]);
			// Car damage
			carPhysicsPacket.aeroDamage = Short.valueOf(part[153]);
			carPhysicsPacket.engineDamage = Short.valueOf(part[154]);
			//  HW state
			carPhysicsPacket.joyPad0 = Integer.valueOf(part[155]);
			carPhysicsPacket.dPad = Short.valueOf(part[156]);

			carPhysicsPacket.tyreCompound = new String[]{extractString(part[157]), extractString(part[158]), extractString(part[159]), extractString(part[160])};
			carPhysicsPacket.turboBoostPressure = Float.valueOf(part[161]);

			carPhysicsPacket.fullPosition = new Vector(Float.valueOf(part[162]), Float.valueOf(part[163]), Float.valueOf(part[164]));
			carPhysicsPacket.brakeBias = Short.valueOf(part[165]);
			repository.addItem(carPhysicsPacket);
		});
	}

	public static String extractString(String csvString) {
		if (csvString.startsWith("\"")) {
			csvString = csvString.substring(1);
		}
		if (csvString.endsWith("\"")) {
			csvString = csvString.substring(0, csvString.length() - 1);
		}
		return csvString.replaceAll("\"\"", "\"");
	}
}
