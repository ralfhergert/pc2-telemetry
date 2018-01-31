package de.ralfhergert.telemetry.graph;

/**
 * Interface for accessor implementations.
 */
public interface NegativeOffsetAccessor<Item, Value extends Number> {

	Value getValueWithNegOffset(Item item, Value offset);
}
