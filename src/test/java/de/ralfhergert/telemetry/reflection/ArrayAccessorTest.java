package de.ralfhergert.telemetry.reflection;

import org.junit.Assert;
import org.junit.Test;

/**
 * Ensures the {@link ArrayAccessor} is working correctly.
 */
public class ArrayAccessorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAccessorIsRejected() {
		new ArrayAccessor<>(0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeIndexIsRejected() {
		new ArrayAccessor<Object, Byte, byte[]>(-1, new Accessor<Object, byte[]>() {
			@Override
			public byte[] getValue(Object o) {
				return new byte[0];
			}
		});
	}

	@Test
	public void testByteRetrievalPrimitive() {
		Accessor<Object,Byte> accessor = new ArrayAccessor<>(1, new Accessor<Object, byte[]>() {
			@Override
			public byte[] getValue(Object o) {
				return new byte[]{0,1,2};
			}
		});
		Assert.assertEquals("retrieved value should be", Byte.valueOf((byte)1), accessor.getValue(null));
	}

	@Test
	public void testByteRetrieval() {
		Accessor<Object,Byte> accessor = new ArrayAccessor<>(1, new Accessor<Object, Byte[]>() {
			@Override
			public Byte[] getValue(Object o) {
				return new Byte[]{0,1,2};
			}
		});
		Assert.assertEquals("retrieved value should be", Byte.valueOf((byte)1), accessor.getValue(null));
	}

	@Test
	public void testShortRetrieval() {
		Accessor<Object,Short> accessor = new ArrayAccessor<>(1, new Accessor<Object, short[]>() {
			@Override
			public short[] getValue(Object o) {
				return new short[]{71,72,73};
			}
		});
		Assert.assertEquals("retrieved value should be", Short.valueOf((short)72), accessor.getValue(null));
	}
}
