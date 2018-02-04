package de.ralfhergert.telemetry.reflection;

/**
 * Interface for accessor implementations.
 */
public interface Accessor<Item, Value> {

	Value getValue(Item item);
}
