package de.ralfhergert.telemetry.persistence.csv;

import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;

import java.io.*;
import java.util.stream.Stream;

/**
 * Writes a stream of {@link CarPhysicsPacket} as CSV onto given {@link OutputStream}.
 */
public class CarPhysicsCsvWriter {

	public void write(Stream<CarPhysicsPacket> packets, OutputStream outStream) throws IOException {
		if (packets == null) {
			throw new IllegalArgumentException("packets can not be null");
		}
		if (outStream == null) {
			throw new IllegalArgumentException("outStream can not be null");
		}

		try (PrintStream printStream = new PrintStream(outStream, true, "UTF-8")){
			// write a version comment first
			printStream.println("#CarPhysics:v2 withColumnHeaders");
			// write the column headers.
			printStream.print("receivedDate,packetNumber,categoryPacketNumber,partialPacketIndex,partialPacketNumber,packetType,packetVersion,");
			printStream.print("viewedParticipantIndex,unfilteredThrottle,unfilteredBrake,unfilteredSteering,unfilteredClutch,");
			printStream.print("carFlags,oilTempCelsius,oilPressureKPa,waterTempCelsius,waterPressureKpa,fuelPressureKpa,fuelCapacity,");
			printStream.print("brake,throttle,clutch,fuelLevel,speed,rpm,maxRpm,steering,gearNumGears,boostAmount,crashState,odometerKM,");
			printStream.print("orientationX,orientationY,orientationZ,localVelocityX,localVelocityY,localVelocityZ,");
			printStream.print("worldVelocityX,worldVelocityY,worldVelocityZ,angularVelocityX,angularVelocityY,angularVelocityZ,");
			printStream.print("localAccelerationX,localAccelerationY,localAccelerationZ,worldAccelerationX,worldAccelerationY,worldAccelerationZ,");
			printStream.print("extentsCentreX,extentsCentreY,extentsCentreZ,tyreFlags1,tyreFlags2,tyreFlags3,tyreFlags4,");
			printStream.print("terrain1,terrain2,terrain3,terrain4,tyreY1,tyreY2,tyreY3,tyreY4,");
			printStream.print("tyreRPS1,tyreRPS2,tyreRPS3,tyreRPS4,tyreTemp1,tyreTemp2,tyreTemp3,tyreTemp4,");
			printStream.print("tyreHeightAboveGround1,tyreHeightAboveGround2,tyreHeightAboveGround3,tyreHeightAboveGround4,");
			printStream.print("tyreWear1,tyreWear2,tyreWear3,tyreWear4,");
			printStream.print("brakeDamage1,brakeDamage2,brakeDamage3,brakeDamage4,");
			printStream.print("suspensionDamage1,suspensionDamage2,suspensionDamage3,suspensionDamage4,");
			printStream.print("brakeTempCelsius1,brakeTempCelsius2,brakeTempCelsius3,brakeTempCelsius4,");
			printStream.print("tyreTreadTemp1,tyreTreadTemp2,tyreTreadTemp3,tyreTreadTemp4,");
			printStream.print("tyreLayerTemp1,tyreLayerTemp2,tyreLayerTemp3,tyreLayerTemp4,");
			printStream.print("tyreCarcassTemp1,tyreCarcassTemp2,tyreCarcassTemp3,tyreCarcassTemp4,");
			printStream.print("tyreRimTemp1,tyreRimTemp2,tyreRimTemp3,tyreRimTemp4,");
			printStream.print("tyreInternalAirTemp1,tyreInternalAirTemp2,tyreInternalAirTemp3,tyreInternalAirTemp4,");
			printStream.print("tyreTempLeft1,tyreTempLeft2,tyreTempLeft3,tyreTempLeft4,");
			printStream.print("tyreTempCenter1,tyreTempCenter2,tyreTempCenter3,tyreTempCenter4,");
			printStream.print("tyreTempRight1,tyreTempRight2,tyreTempRight3,tyreTempRight4,");
			printStream.print("wheelLocalPositionY1,wheelLocalPositionY2,wheelLocalPositionY3,wheelLocalPositionY4,");
			printStream.print("rideHeight1,rideHeight2,rideHeight3,rideHeight4,");
			printStream.print("suspensionTravel1,suspensionTravel2,suspensionTravel3,suspensionTravel4,");
			printStream.print("suspensionVelocity1,suspensionVelocity2,suspensionVelocity3,suspensionVelocity4,");
			printStream.print("suspensionRideHeight1,suspensionRideHeight2,suspensionRideHeight3,suspensionRideHeight4,");
			printStream.print("airPressure1,airPressure2,airPressure3,airPressure4,");
			printStream.print("engineSpeed,engineTorque,wings1,wings2,handBrake,");
			printStream.print("aeroDamage,engineDamage,joyPad0,dPad,");
			printStream.print("tyreCompound1,tyreCompound2,tyreCompound3,tyreCompound4,turboBoostPressure,");
			printStream.print("fullPositionX,fullPositionY,fullPositionZ,brakeBias");
			printStream.println();
			// write each single packet.
			packets.forEach((p) -> {
				printStream.print(p.getReceivedDate().getTime() + ",");
				printStream.print(p.getPacketNumber() + ",");
				printStream.print(p.getCategoryPacketNumber() + ",");
				printStream.print(p.getPartialPacketIndex() + ",");
				printStream.print(p.getPartialPacketNumber() + ",");
				printStream.print(p.getPacketType().name() + ",");
				printStream.print(p.getPacketVersion() + ",");
				printStream.print(p.viewedParticipantIndex + ",");
				printStream.print(p.unfilteredThrottle + ",");
				printStream.print(p.unfilteredBrake + ",");
				printStream.print(p.unfilteredSteering + ",");
				printStream.print(p.unfilteredClutch + ",");
				// Car state
				printStream.print(p.carFlags + ",");
				printStream.print(p.oilTempCelsius + ",");
				printStream.print(p.oilPressureKPa + ",");
				printStream.print(p.waterTempCelsius + ",");
				printStream.print(p.waterPressureKpa + ",");
				printStream.print(p.fuelPressureKpa + ",");
				printStream.print(p.fuelCapacity + ",");

				printStream.print(p.brake + ",");
				printStream.print(p.throttle + ",");
				printStream.print(p.clutch + ",");
				printStream.print(p.fuelLevel + ",");
				printStream.print(p.speed + ",");
				printStream.print(p.rpm + ",");
				printStream.print(p.maxRpm + ",");
				printStream.print(p.steering + ",");
				printStream.print(p.gearNumGears + ",");
				printStream.print(p.boostAmount + ",");
				printStream.print(p.crashState + ",");
				printStream.print(p.odometerKM + ",");

				printStream.print(p.orientation.get(0) + ",");
				printStream.print(p.orientation.get(1) + ",");
				printStream.print(p.orientation.get(2) + ",");
				printStream.print(p.localVelocity.get(0) + ",");
				printStream.print(p.localVelocity.get(1) + ",");
				printStream.print(p.localVelocity.get(2) + ",");

				printStream.print(p.worldVelocity.get(0) + ",");
				printStream.print(p.worldVelocity.get(1) + ",");
				printStream.print(p.worldVelocity.get(2) + ",");
				printStream.print(p.angularVelocity.get(0) + ",");
				printStream.print(p.angularVelocity.get(1) + ",");
				printStream.print(p.angularVelocity.get(2) + ",");

				printStream.print(p.localAcceleration.get(0) + ",");
				printStream.print(p.localAcceleration.get(1) + ",");
				printStream.print(p.localAcceleration.get(2) + ",");
				printStream.print(p.worldAcceleration.get(0) + ",");
				printStream.print(p.worldAcceleration.get(1) + ",");
				printStream.print(p.worldAcceleration.get(2) + ",");

				printStream.print(p.extentsCentre.get(0) + ",");
				printStream.print(p.extentsCentre.get(1) + ",");
				printStream.print(p.extentsCentre.get(2) + ",");
				printStream.print(p.tyreFlags[0] + ",");
				printStream.print(p.tyreFlags[1] + ",");
				printStream.print(p.tyreFlags[2] + ",");
				printStream.print(p.tyreFlags[3] + ",");

				printStream.print(p.terrain[0] + ",");
				printStream.print(p.terrain[1] + ",");
				printStream.print(p.terrain[2] + ",");
				printStream.print(p.terrain[3] + ",");
				printStream.print(p.tyreY[0] + ",");
				printStream.print(p.tyreY[1] + ",");
				printStream.print(p.tyreY[2] + ",");
				printStream.print(p.tyreY[3] + ",");

				printStream.print(p.tyreRPS[0] + ",");
				printStream.print(p.tyreRPS[1] + ",");
				printStream.print(p.tyreRPS[2] + ",");
				printStream.print(p.tyreRPS[3] + ",");
				printStream.print(p.tyreTemp[0] + ",");
				printStream.print(p.tyreTemp[1] + ",");
				printStream.print(p.tyreTemp[2] + ",");
				printStream.print(p.tyreTemp[3] + ",");

				printStream.print(p.tyreHeightAboveGround[0] + ",");
				printStream.print(p.tyreHeightAboveGround[1] + ",");
				printStream.print(p.tyreHeightAboveGround[2] + ",");
				printStream.print(p.tyreHeightAboveGround[3] + ",");
				printStream.print(p.tyreWear[0] + ",");
				printStream.print(p.tyreWear[1] + ",");
				printStream.print(p.tyreWear[2] + ",");
				printStream.print(p.tyreWear[3] + ",");
				printStream.print(p.brakeDamage[0] + ",");
				printStream.print(p.brakeDamage[1] + ",");
				printStream.print(p.brakeDamage[2] + ",");
				printStream.print(p.brakeDamage[3] + ",");
				printStream.print(p.suspensionDamage[0] + ",");
				printStream.print(p.suspensionDamage[1] + ",");
				printStream.print(p.suspensionDamage[2] + ",");
				printStream.print(p.suspensionDamage[3] + ",");
				printStream.print(p.brakeTempCelsius[0] + ",");
				printStream.print(p.brakeTempCelsius[1] + ",");
				printStream.print(p.brakeTempCelsius[2] + ",");
				printStream.print(p.brakeTempCelsius[3] + ",");
				printStream.print(p.tyreTreadTemp[0] + ",");
				printStream.print(p.tyreTreadTemp[1] + ",");
				printStream.print(p.tyreTreadTemp[2] + ",");
				printStream.print(p.tyreTreadTemp[3] + ",");
				printStream.print(p.tyreLayerTemp[0] + ",");
				printStream.print(p.tyreLayerTemp[1] + ",");
				printStream.print(p.tyreLayerTemp[2] + ",");
				printStream.print(p.tyreLayerTemp[3] + ",");
				printStream.print(p.tyreCarcassTemp[0] + ",");
				printStream.print(p.tyreCarcassTemp[1] + ",");
				printStream.print(p.tyreCarcassTemp[2] + ",");
				printStream.print(p.tyreCarcassTemp[3] + ",");
				printStream.print(p.tyreRimTemp[0] + ",");
				printStream.print(p.tyreRimTemp[1] + ",");
				printStream.print(p.tyreRimTemp[2] + ",");
				printStream.print(p.tyreRimTemp[3] + ",");
				printStream.print(p.tyreInternalAirTemp[0] + ",");
				printStream.print(p.tyreInternalAirTemp[1] + ",");
				printStream.print(p.tyreInternalAirTemp[2] + ",");
				printStream.print(p.tyreInternalAirTemp[3] + ",");
				printStream.print(p.tyreTempLeft[0] + ",");
				printStream.print(p.tyreTempLeft[1] + ",");
				printStream.print(p.tyreTempLeft[2] + ",");
				printStream.print(p.tyreTempLeft[3] + ",");
				printStream.print(p.tyreTempCenter[0] + ",");
				printStream.print(p.tyreTempCenter[1] + ",");
				printStream.print(p.tyreTempCenter[2] + ",");
				printStream.print(p.tyreTempCenter[3] + ",");
				printStream.print(p.tyreTempRight[0] + ",");
				printStream.print(p.tyreTempRight[1] + ",");
				printStream.print(p.tyreTempRight[2] + ",");
				printStream.print(p.tyreTempRight[3] + ",");
				printStream.print(p.wheelLocalPositionY[0] + ",");
				printStream.print(p.wheelLocalPositionY[1] + ",");
				printStream.print(p.wheelLocalPositionY[2] + ",");
				printStream.print(p.wheelLocalPositionY[3] + ",");
				printStream.print(p.rideHeight[0] + ",");
				printStream.print(p.rideHeight[1] + ",");
				printStream.print(p.rideHeight[2] + ",");
				printStream.print(p.rideHeight[3] + ",");
				printStream.print(p.suspensionTravel[0] + ",");
				printStream.print(p.suspensionTravel[1] + ",");
				printStream.print(p.suspensionTravel[2] + ",");
				printStream.print(p.suspensionTravel[3] + ",");
				printStream.print(p.suspensionVelocity[0] + ",");
				printStream.print(p.suspensionVelocity[1] + ",");
				printStream.print(p.suspensionVelocity[2] + ",");
				printStream.print(p.suspensionVelocity[3] + ",");
				printStream.print(p.suspensionRideHeight[0] + ",");
				printStream.print(p.suspensionRideHeight[1] + ",");
				printStream.print(p.suspensionRideHeight[2] + ",");
				printStream.print(p.suspensionRideHeight[3] + ",");
				printStream.print(p.airPressure[0] + ",");
				printStream.print(p.airPressure[1] + ",");
				printStream.print(p.airPressure[2] + ",");
				printStream.print(p.airPressure[3] + ",");

				printStream.print(p.engineSpeed + ",");
				printStream.print(p.engineTorque + ",");
				printStream.print(p.wings[0] + ",");
				printStream.print(p.wings[1] + ",");
				printStream.print(p.handBrake + ",");
				// Car damage
				printStream.print(p.aeroDamage + ",");
				printStream.print(p.engineDamage + ",");
				//  HW state
				printStream.print(p.joyPad0 + ",");
				printStream.print(p.dPad + ",");

				printStream.print("\"" + p.tyreCompound[0] + "\",");
				printStream.print("\"" + p.tyreCompound[1] + "\",");
				printStream.print("\"" + p.tyreCompound[2] + "\",");
				printStream.print("\"" + p.tyreCompound[3] + "\",");
				printStream.print(p.turboBoostPressure + ",");

				printStream.print(p.fullPosition.get(0) + ",");
				printStream.print(p.fullPosition.get(1) + ",");
				printStream.print(p.fullPosition.get(2) + ",");
				printStream.print(p.brakeBias);
				printStream.println();
			});
		} catch (IOException e) {
			throw new IOException("could not write CarPhysics data", e);
		}
	}
}
