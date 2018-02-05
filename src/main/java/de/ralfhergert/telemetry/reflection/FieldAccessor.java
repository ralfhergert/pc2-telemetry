package de.ralfhergert.telemetry.reflection;

import java.lang.reflect.Field;

/**
 * This accessor uses the given {@link Field} to access an objects value.
 */
public class FieldAccessor<Item, Value> implements Accessor<Item, Value> {

	private final Field field;

	public FieldAccessor(Field field) {
		if (field == null) {
			throw new IllegalArgumentException("field can not be null");
		}
		this.field = field;
	}

	@Override
	public Value getValue(Item o) {
		try {
			return (Value)field.get(o);
		} catch (IllegalAccessException e) {
			return null;
		}
	}
}
