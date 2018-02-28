package de.ralfhergert.telemetry.pc2;

import org.junit.Ignore;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Ignore
public class StoringUDPListener implements UDPListener {

	private final List<DatagramPacket> packets = new ArrayList<>();

	@Override
	public void received(DatagramPacket packet) {
		packets.add(packet);
	}

	public List<DatagramPacket> getPackets() {
		return Collections.unmodifiableList(packets);
	}

	public void clear() {
		packets.clear();
	}
}
