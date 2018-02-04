package de.ralfhergert.telemetry.reflection;

/**
 * This accessor extracts the wanted index for the array retrieved by
 * the given {@link Accessor}.
 */
public class ShortArrayAccessor<Item> implements Accessor<Item, Short> {

	private final int index;
	private final Accessor<Item, short[]> accessor;

	public ShortArrayAccessor(int index, Accessor<Item, short[]> accessor) {
		if (accessor == null) {
			throw new IllegalArgumentException("accessor can not be null");
		}
		this.index = index;
		this.accessor = accessor;
	}

	@Override
	public Short getValue(Item o) {
		return short[].class.cast(accessor.getValue(o))[index];
	}
}
