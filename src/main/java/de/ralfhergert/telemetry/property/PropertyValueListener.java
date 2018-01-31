package de.ralfhergert.telemetry.property;

/**
 * Interface for listeners observing a {@link PropertyValue}.
 */
public interface PropertyValueListener<Type> {

	void valueChanged(PropertyValue<Type> propertyValue, Type oldValue, Type newValue);
}
