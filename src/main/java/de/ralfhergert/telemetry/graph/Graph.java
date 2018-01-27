package de.ralfhergert.telemetry.graph;

import java.util.*;

/**
 * This class holds the values of a single graph.
 */
public class Graph<Key extends Number, Value extends Number> {

	private final Comparator<Key> keyComparator;
	private final Comparator<Value> valueComparator;

	protected final List<GraphListener<Key,Value>> listeners = new ArrayList<>();
	private final List<GraphValue<Key,Value>> values = new ArrayList<>();

	private Key minKey = null;
	private Key maxKey = null;
	private Value minValue = null;
	private Value maxValue = null;

	public Graph(Comparator<Key> keyComparator, Comparator<Value> valueComparator) {
		this.keyComparator = keyComparator;
		this.valueComparator = valueComparator;
	}

	public Graph<Key,Value> addListener(GraphListener<Key,Value> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		return this;
	}

	public Graph<Key,Value> removeListener(GraphListener<Key,Value> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		listeners.remove(listener);
		return this;
	}

	public List<GraphListener<Key, Value>> getListeners() {
		return new ArrayList<>(listeners); // create a copy to deny external manipulations.
	}

	public Graph<Key,Value> addValue(final GraphValue<Key,Value> value) {
		if (value == null) {
			throw new IllegalArgumentException("value can not be null");
		}
		values.add(value);
		boolean keyDimensionChanged = false;
		if (minKey == null || keyComparator.compare(value.getKey(), minKey) < 0) {
			minKey = value.getKey();
			keyDimensionChanged = true;
		}
		if (maxKey == null || keyComparator.compare(value.getKey(), maxKey) > 0) {
			maxKey = value.getKey();
			keyDimensionChanged = true;
		}
		boolean valueDimensionChanged = false;
		if (minValue == null || valueComparator.compare(value.getValue(), minValue) < 0) {
			minValue = value.getValue();
			valueDimensionChanged = true;
		}
		if (maxValue == null || valueComparator.compare(value.getValue(), maxValue) > 0) {
			maxValue = value.getValue();
			valueDimensionChanged = true;
		}
		for (GraphListener<Key,Value> listener : listeners) {
			listener.addedGraphValue(this, value, keyDimensionChanged, valueDimensionChanged);
		}
		return this;
	}

	public Key getMinKey() {
		return minKey;
	}

	public Key getMaxKey() {
		return maxKey;
	}

	public Value getMinValue() {
		return minValue;
	}

	public Value getMaxValue() {
		return maxValue;
	}

	public List<GraphValue<Key, Value>> getValues() {
		return new ArrayList<>(values); // create a copy to deny modifications.
	}

	public boolean isDrawable() {
		return minKey != null
			&& maxKey != null
			&& keyComparator.compare(minKey, maxKey) < 0
			&& minValue != null
			&& maxValue != null
			&& valueComparator.compare(minValue, maxValue) < 0
			&& values.size() > 1;
	}
}
