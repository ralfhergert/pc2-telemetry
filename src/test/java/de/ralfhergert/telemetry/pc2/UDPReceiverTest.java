package de.ralfhergert.telemetry.pc2;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

/**
 * This test ensures that the {@link UDPReceiver} is working correctly.
 */
public class UDPReceiverTest {

	@Test(expected = IllegalArgumentException.class)
	public void testUDPReceiverRejectsNullAsSocket() {
		new UDPReceiver(null, packet -> {});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUDPReceiverRejectsNullAsListener() throws SocketException {
		new UDPReceiver(new DatagramSocket(), null);
	}

	@Test
	public void testStoppingReceiverBeforeItIsStartedWillNeverReadFromSocket() throws SocketException {
		final StoringUDPListener listener = new StoringUDPListener();
		final MockDatagramSocket mockDatagramSocket = new MockDatagramSocket();
		// create a 'stopped' UDPReceiver.
		final UDPReceiver receiver = new UDPReceiver(mockDatagramSocket, listener).stop();
		Assert.assertFalse("receiver should be stopped", receiver.isRunning());
		// try to run the receiver in a thread.
		Thread thread = new Thread(receiver);
		thread.start();
		try {
			thread.join(); // wait for the thread to die.
		} catch (InterruptedException e) {
			Assert.fail("an InterruptedException occurred");
		}
		// verify that socket had never been read from.
		Assert.assertEquals("socket should have never been read from", 0, mockDatagramSocket.getReceiveCalled());
		Assert.assertEquals("listener should have never been triggered", 0, listener.getPackets().size());
	}

	@Test
	public void testReceiverStopsWhenSocketSendsASocketException() throws SocketException {
		final StoringUDPListener listener = new StoringUDPListener();
		// create a socket which fires a SocketException when trying to read from.
		final MockDatagramSocket mockDatagramSocket = new MockDatagramSocket();
		final UDPReceiver receiver = new UDPReceiver(mockDatagramSocket, listener);
		Assert.assertTrue("receiver should be runnable", receiver.isRunning());
		// try to run the receiver in a thread.
		Thread thread = new Thread(receiver);
		thread.start();
		try {
			thread.join(); // wait for the thread to die.
		} catch (InterruptedException e) {
			Assert.fail("an InterruptedException occurred");
		}
		// verify that socket had never been read from.
		Assert.assertFalse("receiver should no longer be runnable", receiver.isRunning());
		Assert.assertEquals("socket should have been read from", 1, mockDatagramSocket.getReceiveCalled());
		Assert.assertEquals("listener should have never been triggered", 0, listener.getPackets().size());
	}

	@Test
	public void testReceiverForwardsPacketsToListener() throws SocketException {
		final StoringUDPListener listener = new StoringUDPListener();
		// create a socket which fires a SocketException when trying to read from.
		final MockDatagramSocket mockDatagramSocket = new MockDatagramSocket()
			.addData(Arrays.asList(new byte[1], new byte[1], new byte[1]));
		final UDPReceiver receiver = new UDPReceiver(mockDatagramSocket, listener);
		Assert.assertTrue("receiver should be runnable", receiver.isRunning());
		// try to run the receiver in a thread.
		Thread thread = new Thread(receiver);
		thread.start();
		try {
			thread.join(); // wait for the thread to die.
		} catch (InterruptedException e) {
			Assert.fail("an InterruptedException occurred");
		}
		// verify that socket had never been read from.
		Assert.assertFalse("receiver should no longer be runnable", receiver.isRunning());
		Assert.assertEquals("socket should have been read from", 4, mockDatagramSocket.getReceiveCalled());
		Assert.assertEquals("listener should have been triggered", 3, listener.getPackets().size());
	}

	@Test
	public void testReceiverStopsOnAnyIOException() throws SocketException {
		final StoringUDPListener listener = new StoringUDPListener();
		// create a socket which fires a SocketException when trying to read from.
		final MockDatagramSocket mockDatagramSocket = new MockDatagramSocket() {
			@Override
			public synchronized void receive(DatagramPacket p) throws IOException {
				receiveCalled++;
				throw new IOException(); // throw always an IOException
			}
		};
		final UDPReceiver receiver = new UDPReceiver(mockDatagramSocket, listener);
		Assert.assertTrue("receiver should be runnable", receiver.isRunning());
		// try to run the receiver in a thread.
		Thread thread = new Thread(receiver);
		thread.start();
		try {
			thread.join(); // wait for the thread to die.
		} catch (InterruptedException e) {
			Assert.fail("an InterruptedException occurred");
		}
		// verify that socket had never been read from.
		Assert.assertFalse("receiver should no longer be runnable", receiver.isRunning());
		Assert.assertEquals("socket should have been read from", 1, mockDatagramSocket.getReceiveCalled());
		// since every attempt to read throws an IOException, the listener should never be triggered.
		Assert.assertEquals("listener should have never been triggered", 0, listener.getPackets().size());
	}
}
