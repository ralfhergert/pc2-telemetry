package de.ralfhergert.telemetry.reflection;

/**
 * This accessor extracts the wanted index for the array retrieved by
 * the given {@link Accessor}.
 */
public class FloatArrayAccessor<Item> implements Accessor<Item, Float> {

	private final int index;
	private final Accessor<Item, float[]> accessor;

	public FloatArrayAccessor(int index, Accessor<Item, float[]> accessor) {
		if (accessor == null) {
			throw new IllegalArgumentException("accessor can not be null");
		}
		this.index = index;
		this.accessor = accessor;
	}

	@Override
	public Float getValue(Item o) {
		return float[].class.cast(accessor.getValue(o))[index];
	}
}
