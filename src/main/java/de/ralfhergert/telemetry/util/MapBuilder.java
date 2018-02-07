package de.ralfhergert.telemetry.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Helps with inlining map-creation
 */
public class MapBuilder<Key, Value> {

	private final Map<Key, Value> map;

	public MapBuilder() {
		this.map = new HashMap<>();
	}

	public MapBuilder(Map<Key, Value> map) {
		this.map = map;
	}

	public MapBuilder<Key, Value> put(Key key, Value value) {
		map.put(key, value);
		return this;
	}

	public Map<Key,Value> build() {
		return map;
	}
}
