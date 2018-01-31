package de.ralfhergert.telemetry.persistence.csv;

import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPackage;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * This test ensures that {@link CarPhysicsCsvWriter} is working correctly.
 */
public class CarPhysicsCsvWriterTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsPacketStreamIsRejected() throws IOException {
		new CarPhysicsCsvWriter().write(null, new ByteArrayOutputStream());
		Assert.fail("CarPhysicCsvWriter should have not been accepting null as packet stream");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullOutputStreamIsRejected() throws IOException {
		new CarPhysicsCsvWriter().write(new ArrayList<CarPhysicsPackage>().stream(), null);
		Assert.fail("CarPhysicCsvWriter should have not been accepting null as output stream");
	}

	@Test
	public void testExportOfDefaultCarPhysicsPackage() throws IOException {
		byte[] emptyCarPacketBytes = new byte[556];
		emptyCarPacketBytes[11] = 2;
		final CarPhysicsPackage packet = new CarPhysicsPackage(emptyCarPacketBytes);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		new CarPhysicsCsvWriter().write(Stream.of(packet), stream);
		Assert.assertFalse("something was written", stream.toString("UTF-8").isEmpty());
		String[] lines = stream.toString("UTF-8").replaceAll("\r","").split("\n");
		Assert.assertNotNull("lines should not be null", lines);
		Assert.assertEquals("number of lines", 3, lines.length);
		Assert.assertEquals("first line is expected to be", "#CarPhysics:v2 withColumnHeaders", lines[0]);
		Assert.assertEquals("second line is expected to be", "receivedDate,packetNumber,categoryPacketNumber," +
			"partialPacketIndex,partialPacketNumber,packetType,packetVersion,viewedParticipantIndex," +
			"unfilteredThrottle,unfilteredBrake,unfilteredSteering,unfilteredClutch,carFlags,oilTempCelsius," +
			"oilPressureKPa,waterTempCelsius,waterPressureKpa,fuelPressureKpa,fuelCapacity,brake,throttle,clutch," +
			"fuelLevel,speed,rpm,maxRpm,steering,gearNumGears,boostAmount,crashState,odometerKM,orientationX," +
			"orientationY,orientationZ,localVelocityX,localVelocityY,localVelocityZ,worldVelocityX,worldVelocityY," +
			"worldVelocityZ,angularVelocityX,angularVelocityY,angularVelocityZ,localAccelerationX,localAccelerationY," +
			"localAccelerationZ,worldAccelerationX,worldAccelerationY,worldAccelerationZ,extentsCentreX," +
			"extentsCentreY,extentsCentreZ,tyreFlags1,tyreFlags2,tyreFlags3,tyreFlags4,terrain1,terrain2,terrain3," +
			"terrain4,tyreY1,tyreY2,tyreY3,tyreY4,tyreRPS1,tyreRPS2,tyreRPS3,tyreRPS4,tyreTemp1,tyreTemp2,tyreTemp3," +
			"tyreTemp4,tyreHeightAboveGround1,tyreHeightAboveGround2,tyreHeightAboveGround3,tyreHeightAboveGround4," +
			"tyreWear1,tyreWear2,tyreWear3,tyreWear4,brakeDamage1,brakeDamage2,brakeDamage3,brakeDamage4," +
			"suspensionDamage1,suspensionDamage2,suspensionDamage3,suspensionDamage4,brakeTempCelsius1," +
			"brakeTempCelsius2,brakeTempCelsius3,brakeTempCelsius4,tyreTreadTemp1,tyreTreadTemp2,tyreTreadTemp3," +
			"tyreTreadTemp4,tyreLayerTemp1,tyreLayerTemp2,tyreLayerTemp3,tyreLayerTemp4,tyreCarcassTemp1," +
			"tyreCarcassTemp2,tyreCarcassTemp3,tyreCarcassTemp4,tyreRimTemp1,tyreRimTemp2,tyreRimTemp3,tyreRimTemp4," +
			"tyreInternalAirTemp1,tyreInternalAirTemp2,tyreInternalAirTemp3,tyreInternalAirTemp4,tyreTempLeft1," +
			"tyreTempLeft2,tyreTempLeft3,tyreTempLeft4,tyreTempCenter1,tyreTempCenter2,tyreTempCenter3," +
			"tyreTempCenter4,tyreTempRight1,tyreTempRight2,tyreTempRight3,tyreTempRight4,wheelLocalPositionY1," +
			"wheelLocalPositionY2,wheelLocalPositionY3,wheelLocalPositionY4,rideHeight1,rideHeight2,rideHeight3," +
			"rideHeight4,suspensionTravel1,suspensionTravel2,suspensionTravel3,suspensionTravel4,suspensionVelocity1," +
			"suspensionVelocity2,suspensionVelocity3,suspensionVelocity4,suspensionRideHeight1,suspensionRideHeight2," +
			"suspensionRideHeight3,suspensionRideHeight4,airPressure1,airPressure2,airPressure3,airPressure4," +
			"engineSpeed,engineTorque,wings1,wings2,handBrake,aeroDamage,engineDamage,joyPad0,dPad,tyreCompound1," +
			"tyreCompound2,tyreCompound3,tyreCompound4,turboBoostPressure,fullPositionX,fullPositionY,fullPositionZ," +
			"brakeBias", lines[1]);
		Assert.assertEquals("third line is expected to be", packet.getReceivedDate().getTime() + ",0,0,0,0," +
			"CarPhysics,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0.0,0.0,0,0,0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0," +
			"0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,0,0,0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,0,0," +
			"0.0,0.0,0.0,0.0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
			"0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0," +
			"0.0,0.0,0,0,0,0,0,0,0,\"0000000000000000000000000000000000000000\"," +
			"\"0000000000000000000000000000000000000000\",\"0000000000000000000000000000000000000000\"," +
			"\"0000000000000000000000000000000000000000\",0.0,0.0,0.0,0.0,0", lines[2]);
	}
}
