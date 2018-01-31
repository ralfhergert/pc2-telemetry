package de.ralfhergert.telemetry.property;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a map of {@link PropertyValue}.
 */
public class PropertyValues {

	private final Map<String, PropertyValue<?>> propertyValueMap = new HashMap<>();

	/**
	 * This method makes sure that a property with the given name does exist.
	 * If it exists, the listener is registered and the defaultValue is ignored.
	 * If if does not exist then a property with the given defaultValue is created
	 * and the listener is registered.
	 */
	public <Type> PropertyValues ensureProperty(String propertyName, Type defaultValue, PropertyValueListener<Type> listener) {
		PropertyValue<Type> propertyValue = (PropertyValue<Type>)propertyValueMap.get(propertyName);
		if (propertyValue != null) {
			propertyValue.addListener(listener);
		} else {
			propertyValue = new PropertyValue<>(defaultValue);
			if (listener != null) {
				propertyValue.addListener(listener);
			}
			propertyValueMap.put(propertyName, propertyValue);
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
		}
		return this;
	}
}
