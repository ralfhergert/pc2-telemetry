package de.ralfhergert.telemetry.pc2.datagram.v2;

import java.net.DatagramPacket;

/**
 * This parser converts {@link }
 */
public class PackageParser {

	public BasePackage parse(DatagramPacket packet) {
		try {
			BasePackage basePackage = new BasePackage(packet.getData());
			if (basePackage.getPacketType() == PackageTypes.CarPhysics && basePackage.getPacketVersion() == 2) {
				return new CarPhysicsPackage(packet.getData());
			} else {
				return basePackage;
			}
		} catch (Exception e) {
			/* package could not be parsed. */
		}
		return null;
	}
}
