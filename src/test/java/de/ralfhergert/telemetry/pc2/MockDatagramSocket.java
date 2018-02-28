package de.ralfhergert.telemetry.pc2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This mock {@link DatagramSocket} will deliver the given data.
 * If it has no more data it will throw a SocketException.
 */
public class MockDatagramSocket extends DatagramSocket {

	protected int receiveCalled = 0;
	private List<byte[]> data = new ArrayList<>();

	public MockDatagramSocket() throws SocketException {}

	@Override
	public synchronized void receive(DatagramPacket p) throws IOException {
		receiveCalled++;
		if (data.size() >= receiveCalled) {
			p.setData(data.get(receiveCalled - 1));
		} else {
			throw new SocketException("no more data");
		}
	}

	public MockDatagramSocket addData(Collection<byte[]> data) {
		this.data.addAll(data);
		return this;
	}

	public int getReceiveCalled() {
		return receiveCalled;
	}

	public void clear() {
		receiveCalled = 0;
	}
}
