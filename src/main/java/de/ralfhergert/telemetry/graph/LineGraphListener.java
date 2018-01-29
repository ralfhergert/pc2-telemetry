package de.ralfhergert.telemetry.graph;

/**
 * Interface for receiving graph changes.
 */
public interface LineGraphListener<Item, Key extends Number, Value extends Number> {

	void graphChanged(LineGraph<Item, Key, Value> graph, boolean boundariesChanged);
}
