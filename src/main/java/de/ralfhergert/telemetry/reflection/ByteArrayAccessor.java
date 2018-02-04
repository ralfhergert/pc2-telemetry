package de.ralfhergert.telemetry.reflection;

/**
 * This accessor extracts the wanted index for the array retrieved by
 * the given {@link Accessor}.
 */
public class ByteArrayAccessor<Item> implements Accessor<Item, Byte> {

	private final int index;
	private final Accessor<Item, byte[]> accessor;

	public ByteArrayAccessor(int index, Accessor<Item, byte[]> accessor) {
		if (accessor == null) {
			throw new IllegalArgumentException("accessor can not be null");
		}
		this.index = index;
		this.accessor = accessor;
	}

	@Override
	public Byte getValue(Item o) {
		return byte[].class.cast(accessor.getValue(o))[index];
	}
}
