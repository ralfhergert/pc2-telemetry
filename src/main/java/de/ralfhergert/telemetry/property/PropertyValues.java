package de.ralfhergert.telemetry.property;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a map of {@link PropertyValue}.
 */
public class PropertyValues {

	private final Map<String, PropertyValue<?>> propertyValueMap = new HashMap<>();

	/**
	 * This method will add a listener for the property matching propertyName.
	 * If this property does not yet exist, it will be created.
	 */
	public <Type> PropertyValues addListener(String propertyName, PropertyValueListener<Type> listener) {
		if (propertyName == null) {
			throw new IllegalArgumentException("propertyName can not be null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		PropertyValue<Type> propertyValue = (PropertyValue<Type>)propertyValueMap.get(propertyName);
		if (propertyValue == null) {
			propertyValue = new PropertyValue<>(null);
			propertyValueMap.put(propertyName, propertyValue);
		}
		propertyValue.addListener(listener);
		return this;
	}

	/**
	 * This method will add a listener for the property matching propertyName.
	 * If this property does not yet exist, it will be created.
	 */
	public <Type> PropertyValues removeListener(String propertyName, PropertyValueListener<Type> listener) {
		PropertyValue<Type> propertyValue = (PropertyValue<Type>)propertyValueMap.get(propertyName);
		if (propertyValue != null) {
			propertyValue.removeListener(listener);
		}
		return this;
	}

	public <Type> Type getValue(final String propertyName, Type defaultValue) {
		PropertyValue propertyValue = propertyValueMap.get(propertyName);
		if (propertyValue != null) {
			return (Type)propertyValue.getValue();
		} else {
			return defaultValue;
		}
	}

	public <Type> PropertyValues setValue(final String propertyName, Type value) {
		PropertyValue<Type> propertyValue = (PropertyValue<Type>)propertyValueMap.get(propertyName);
		if (propertyValue != null) {
			propertyValue.setValue(value);
		} else { // create this property if it does not yet exist.
			propertyValueMap.put(propertyName, new PropertyValue<>(value));
		}
		return this;
	}
}
