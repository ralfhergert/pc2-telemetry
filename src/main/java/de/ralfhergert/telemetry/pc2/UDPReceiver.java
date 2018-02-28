package de.ralfhergert.telemetry.pc2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Receives UDP packets and pushes them to connected listeners.
 */
public class UDPReceiver implements Runnable {

	private int maxPacketSize = 2048;
	private DatagramSocket socket;
	private UDPListener listener;
	private boolean running = true;

	public UDPReceiver(DatagramSocket socket, UDPListener listener) {
		if (socket == null) {
			throw new IllegalArgumentException("socket can not be null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		this.socket = socket;
		this.listener = listener;
	}

	@Override
	public void run() {
		while (running) {
			byte buf[] = new byte[maxPacketSize];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				listener.received(packet);
			} catch (SocketException se) {
				// receiving a SocketException means that the socket has been closed.
				running = false;
				return;
			} catch (IOException e) {
				running = false;
				break;
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public UDPReceiver stop() {
		running = false;
		return this;
	}
}
