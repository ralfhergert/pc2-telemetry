package de.ralfhergert.telemetry.pc2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Receives UDP packet from Project Cars 2.
 */
public class UDPReceiver implements Runnable {

	private int maxPacketSize = 2048;
	private DatagramSocket socket;
	private UDPListener listener;

	public UDPReceiver(DatagramSocket socket, UDPListener listener) {
		this.socket = socket;
		this.listener = listener;
	}

	@Override
	public void run() {
		for(;;) {
			byte buf[] = new byte[maxPacketSize];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				listener.received(packet);
			} catch (IOException e) {
				break;
			}
		}
	}
}
