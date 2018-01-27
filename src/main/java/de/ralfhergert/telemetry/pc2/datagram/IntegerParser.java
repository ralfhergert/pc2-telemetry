package de.ralfhergert.telemetry.pc2.datagram;

/**
 * This class parses the next 4 bytes as an integer.
 */
public class IntegerParser {

	public static int parse(byte[] data) {
		return parse(data, 0);
	}

	public static int parse(byte[] data, int offset) {
		if (data == null) {
			throw new IllegalArgumentException("data must not be null");
		}
		if (offset + 4 > data.length) {
			throw new IllegalArgumentException("given data array is too short to read an integer from");
		}
		int value = (int)data[offset + 3];
		value = (int)data[offset + 2] + value * 256;
		value = (int)data[offset + 1] + value * 256;
		return (int)data[offset] + value * 256;
	}
}
