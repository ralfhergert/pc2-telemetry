package de.ralfhergert.telemetry.reflection;

import java.lang.reflect.Array;

/**
 * This accessor extracts the wanted index for the array retrieved by
 * the given {@link Accessor}.
 * @param <Item> from which the value should be read
 */
public class ArrayAccessor<Item, Type, ArrayType> implements Accessor<Item, Type> {

	private final int index;
	private final Accessor<Item, ArrayType> accessor;

	public ArrayAccessor(int index, Accessor<Item, ArrayType> accessor) {
		if (accessor == null) {
			throw new IllegalArgumentException("accessor can not be null");
		}
		if (index < 0) {
			throw new IllegalArgumentException("index can not be negative");
		}
		this.index = index;
		this.accessor = accessor;
	}

	@Override
	public Type getValue(Item o) {
		return (Type)Array.get(accessor.getValue(o), index);
	}
}
