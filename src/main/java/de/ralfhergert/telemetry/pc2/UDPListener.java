package de.ralfhergert.telemetry.pc2;

import java.net.DatagramPacket;

/**
 * A general listener interface for classes.
 */
public interface UDPListener {

	void received(DatagramPacket packet);
}
