package de.ralfhergert.telemetry.pc2.datagram.v2;

import java.net.DatagramPacket;

/**
 * This parser converts {@link }
 */
public class PacketParser {

	public BasePacket parse(DatagramPacket packet) {
		try {
			BasePacket basePacket = new BasePacket(packet.getData());
			if (basePacket.getPacketType() == PacketTypes.CarPhysics && basePacket.getPacketVersion() == 3) {
				return new CarPhysicsPacket(packet.getData());
			} else {
				return basePacket;
			}
		} catch (Exception e) {
			/* packet could not be parsed. */
		}
		return null;
	}
}
