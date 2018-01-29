package de.ralfhergert.telemetry.graph;

/**
 * Interface for accessor implementations.
 */
public interface Accessor<Item, Value extends Number> {

	Value getValue(Item item);
}
