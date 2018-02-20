package de.ralfhergert.telemetry.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This listener stores event it receives.
 * It is mainly used for testing and debugging purposes.
 */
public class StoringPropertyValueListener<Type> implements PropertyValueListener<Type> {

	private final List<PropertyChangeEvent<Type>> events = new ArrayList<>();

	@Override
	public void valueChanged(PropertyValue<Type> propertyValue, Type oldValue, Type newValue) {
		events.add(new PropertyChangeEvent<>(propertyValue, oldValue, newValue));
	}

	public List<PropertyChangeEvent<Type>> getEvents() {
		return Collections.unmodifiableList(events);
	}

	public static class PropertyChangeEvent<Type> {

		private final PropertyValue<Type> propertyValue;
		private final Type oldValue;
		private final Type newValue;

		public PropertyChangeEvent(PropertyValue<Type> propertyValue, Type oldValue, Type newValue) {
			this.propertyValue = propertyValue;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public PropertyValue<Type> getPropertyValue() {
			return propertyValue;
		}

		public Type getOldValue() {
			return oldValue;
		}

		public Type getNewValue() {
			return newValue;
		}
	}
}
