package de.ralfhergert.telemetry.property;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a map of {@link PropertyValue}.
 */
public class PropertyValues {

	private final Map<String, PropertyValue<?>> propertyValueMap = new HashMap<>();

	/**
	 * This method makes sure that a property with that name and value does exist.
	 * If a listener is given it will be registered at the property.
	 * @param propertyName name of the property
	 */
	public <Type> PropertyValues ensureProperty(String propertyName, Type value, PropertyValueListener<Type> listener) {
		PropertyValue<Type> propertyValue = (PropertyValue<Type>)propertyValueMap.get(propertyName);
		if (propertyValue == null) { // create this value if not yet defined
			propertyValue = new PropertyValue<>(value);
			propertyValueMap.put(propertyName, propertyValue);
		} else {
			propertyValue.setValue(value);
		}
		if (listener != null) {
			propertyValue.addListener(listener);
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
