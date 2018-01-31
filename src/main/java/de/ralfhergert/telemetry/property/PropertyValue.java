package de.ralfhergert.telemetry.property;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Encapsulate a single value. Provide property change support.
 */
public class PropertyValue<Type> {

	private final List<PropertyValueListener<Type>> listeners = new ArrayList<>();
	private final Comparator<Type> comparator;

	private Type value;

	public PropertyValue(Type value) {
		this(value, null);
	}

	public PropertyValue(Type value, Comparator<Type> comparator) {
		this.value = value;
		this.comparator = comparator;
	}

	public PropertyValue<Type> addListener(PropertyValueListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		return this;
	}

	public PropertyValue<Type> removeListener(PropertyValueListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		listeners.remove(listener);
		return this;
	}

	public Type getValue() {
		return value;
	}

	public void setValue(Type value) {
		if (comparator == null || comparator.compare(this.value, value) != 0) {
			Type oldValue = this.value;
			this.value = value;
			listeners.forEach(listener -> listener.valueChanged(this, oldValue, value));
		}
	}
}
