package de.ralfhergert.telemetry.pc2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Receives UDP packet from Project Cars 2.
 */
public class UDPReceiver implements Runnable {

	private int maxPacketSize = 2048;
	private DatagramSocket socket;
	private UDPListener listener;
	private boolean running = true;

	public UDPReceiver(DatagramSocket socket, UDPListener listener) {
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
