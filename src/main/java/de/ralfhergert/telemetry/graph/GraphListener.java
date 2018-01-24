package de.ralfhergert.telemetry.graph;

/**
 * Interface for receiving graph changes.
 */
public interface GraphListener<Key extends Number, Value extends Number> {

	void addedGraphValue(Graph<Key,Value> graph, GraphValue<Key,Value> graphValue, boolean keyDimensionChanged, boolean valueDimensionChanged);
}
