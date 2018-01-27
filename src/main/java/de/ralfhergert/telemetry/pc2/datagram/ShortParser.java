package de.ralfhergert.telemetry.pc2.datagram;

/**
 * This class parses two bytes into a short.
 */
public class ShortParser {

	public static short parse(byte[] data) {
		return parse(data, 0);
	}

	public static short parse(byte[] data, int offset) {
		if (data == null) {
			throw new IllegalArgumentException("data must not be null");
		}
		if (offset + 2 > data.length) {
			throw new IllegalArgumentException("given data array is too short to read an short from");
		}
		return (short)((short)data[offset] + (short)data[offset + 1] * 256);
	}
}
