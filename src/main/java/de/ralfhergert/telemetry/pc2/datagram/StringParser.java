package de.ralfhergert.telemetry.pc2.datagram;

/**
 * This class reads the next bytes into a string.
 */
public class StringParser {

	public static String parse(byte[] data, int length) {
		return parse(data, 0, length);
	}

	public static String parse(byte[] data, int offset, int length) {
		String value = "";
		for (int i = 0; i < length && i + offset < data.length; i++) {
			value += data[i + offset];
		}
		return value;
	}
}
