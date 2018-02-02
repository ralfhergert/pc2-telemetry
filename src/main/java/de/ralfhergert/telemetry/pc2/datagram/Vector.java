package de.ralfhergert.telemetry.pc2.datagram;

import java.util.Arrays;

/**
 * A vector of float.
 */
public class Vector {

	final float[] values;

	public Vector(float... values) {
		if (values == null) {
			throw new IllegalArgumentException("values can not be null");
		}
		this.values = values;
	}

	public float get(int index) {
		return values[index];
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Vector vector = (Vector) o;

		return Arrays.equals(values, vector.values);

	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(values);
	}
}
