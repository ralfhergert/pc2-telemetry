package de.ralfhergert.telemetry.reflection;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link FieldAccessor} is working correctly.
 */
public class FieldAccessorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsFieldIsRejected() {
		new FieldAccessor<>(null);
	}

	@Test
	public void testAccessingPublicField() throws NoSuchFieldException {
		class A {
			public int a;

			public A(int a) {
				this.a = a;
			}
		}
		FieldAccessor<A, Integer> accessor = new FieldAccessor<>(A.class.getField("a"));
		Assert.assertEquals("retrieved value should be", Integer.valueOf(84), accessor.getValue(new A(84)));
	}

	@Test
	public void testAccessingProtectedField() throws NoSuchFieldException {
		class A {
			protected int a;

			public A(int a) {
				this.a = a;
			}
		}
		FieldAccessor<A, Integer> accessor = new FieldAccessor<>(A.class.getDeclaredField("a"));
		Assert.assertEquals("retrieved value should be", Integer.valueOf(75), accessor.getValue(new A(75)));
	}

	@Test
	public void testAccessingPrivateField() throws NoSuchFieldException {
		class A {
			private int a;

			public A(int a) {
				this.a = a;
			}
		}
		FieldAccessor<A, Integer> accessor = new FieldAccessor<>(A.class.getDeclaredField("a"));
		Assert.assertEquals("retrieved value should be", null, accessor.getValue(new A(64)));
	}
}
