package de.ralfhergert.telemetry.pc2;

import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.PacketTypes;
import de.ralfhergert.telemetry.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Test;

import java.net.SocketException;
import java.util.Arrays;

/**
 * This test ensures that {@link UDPCaptureThread} is working correctly.
 */
public class UDPCaptureThreadTest {

	@Test(expected = IllegalArgumentException.class)
	public void testUDPCaptureThreadRejectsNullAsSocket() {
		new UDPCaptureThread(null, new ItemRepository<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUDPCaptureThreadRejectsNullAsRepository() throws SocketException {
		new UDPCaptureThread(new MockDatagramSocket(), null);
	}

	@Test
	public void testUDPCaptureThreadBeforeStarting() throws SocketException {
		final ItemRepository<CarPhysicsPacket> repository = new ItemRepository<>();
		UDPCaptureThread thread = new UDPCaptureThread(new MockDatagramSocket(), repository);
		Assert.assertFalse("thread is not yet started", thread.isRunning());
	}

	@Test
	public void testReceivingCarPhysicsPacketV3() throws SocketException {
		final ItemRepository<CarPhysicsPacket> repository = new ItemRepository<>();
		byte[] carPhysicsPacketV3Data = new byte[556];
		carPhysicsPacketV3Data[11] = 3; // packet version
		final MockDatagramSocket mockDatagramSocket = new MockDatagramSocket().addData(Arrays.asList(carPhysicsPacketV3Data));
		final UDPCaptureThread thread = new UDPCaptureThread(mockDatagramSocket, repository).start();
		Assert.assertTrue("thread should be running", thread.isRunning());
		try {
			thread.join();
		} catch (InterruptedException e) {
			Assert.fail("received an InterruptedException");
		}
		Assert.assertFalse("thread should no longer be running", thread.isRunning());
		Assert.assertEquals("number of call the socket received", 2, mockDatagramSocket.getReceiveCalled());
		Assert.assertEquals("number of CarPhysicsPackets received", 1, repository.getItemStream().count());
	}

	@Test
	public void testReceivingUnknownPacket() throws SocketException {
		final ItemRepository<CarPhysicsPacket> repository = new ItemRepository<>();
		byte[] carPhysicsPacketV3Data = new byte[2]; // this data is too short to even be a BasePacket
		final MockDatagramSocket mockDatagramSocket = new MockDatagramSocket().addData(Arrays.asList(carPhysicsPacketV3Data));
		final UDPCaptureThread thread = new UDPCaptureThread(mockDatagramSocket, repository).start();
		Assert.assertTrue("thread should be running", thread.isRunning());
		try {
			thread.join();
		} catch (InterruptedException e) {
			Assert.fail("received an InterruptedException");
		}
		Assert.assertFalse("thread should no longer be running", thread.isRunning());
		Assert.assertEquals("number of call the socket received", 2, mockDatagramSocket.getReceiveCalled());
		Assert.assertEquals("number of CarPhysicsPackets received", 0, repository.getItemStream().count());
	}

	@Test
	public void testReceivingUnknownBasePacket() throws SocketException {
		final ItemRepository<CarPhysicsPacket> repository = new ItemRepository<>();
		byte[] packet = new byte[12]; // weather packet are not send currently
		packet[10] = (byte)PacketTypes.WeatherState.ordinal();
		packet[11] = 3; // packet version
		final MockDatagramSocket mockDatagramSocket = new MockDatagramSocket().addData(Arrays.asList(packet));
		final UDPCaptureThread thread = new UDPCaptureThread(mockDatagramSocket, repository).start();
		Assert.assertTrue("thread should be running", thread.isRunning());
		try {
			thread.join();
		} catch (InterruptedException e) {
			Assert.fail("received an InterruptedException");
		}
		Assert.assertFalse("thread should no longer be running", thread.isRunning());
		Assert.assertEquals("number of call the socket received", 2, mockDatagramSocket.getReceiveCalled());
		Assert.assertEquals("number of CarPhysicsPackets received", 0, repository.getItemStream().count());
	}
}
