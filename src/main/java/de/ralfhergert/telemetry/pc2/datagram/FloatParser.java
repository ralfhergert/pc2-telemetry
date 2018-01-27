package de.ralfhergert.telemetry.pc2.datagram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class parses the next 4 bytes as a float.
 */
public class FloatParser {

	public static float parse(byte[] data) {
		return parse(data, 0);
	}

	public static float parse(byte[] data, int offset) {
		if (data == null) {
			throw new IllegalArgumentException("data must not be null");
		}
		if (offset + 4 > data.length) {
			throw new IllegalArgumentException("given data array is too short to read an float from");
		}
		return ByteBuffer.wrap(data, offset, 4).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get();
	}
}
