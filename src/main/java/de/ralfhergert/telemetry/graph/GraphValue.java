package de.ralfhergert.telemetry.graph;

/**
 * Stores a single value of a {@link Graph}.
 */
public class GraphValue<Key extends Number, Value extends Number> {

	private Key key;
	private Value value;

	public GraphValue(Key key, Value value) {
		if (key == null) {
			throw new IllegalArgumentException("key can not be null");
		}
		if (value == null) {
			throw new IllegalArgumentException("value can not be null");
		}
		this.key = key;
		this.value = value;
	}

	public Key getKey() {
		return key;
	}

	public Value getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "GraphValue{key=" + key + ", value=" + value + '}';
	}
}
