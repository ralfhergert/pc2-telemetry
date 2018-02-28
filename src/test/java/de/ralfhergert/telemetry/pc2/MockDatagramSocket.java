package de.ralfhergert.telemetry.pc2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MockDatagramSocket extends DatagramSocket {

	private int receiveCalled = 0;

	public MockDatagramSocket() throws SocketException {}

	@Override
	public synchronized void receive(DatagramPacket p) throws IOException {
		receiveCalled++;
	}

	public int getReceiveCalled() {
		return receiveCalled;
	}

	public void clear() {
		receiveCalled = 0;
	}
}
