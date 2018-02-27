package de.ralfhergert.telemetry.reflection;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link MethodAccessor} is working correctly.
 */
public class MethodAccessorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsMethodIsRejected() {
		new MethodAccessor<>(null);
	}

	@Test
	public void testAccessingPublicMethod() throws NoSuchMethodException {
		class A {
			private int a;

			public A(int a) {
				this.a = a;
			}

			public int getA() {
				return a;
			}
		}
		MethodAccessor<A, Integer> accessor = new MethodAccessor<>(A.class.getMethod("getA"));
		Assert.assertEquals("retrieved value should be", Integer.valueOf(93), accessor.getValue(new A(93)));
	}

	@Test
	public void testAccessingProtectedMethod() throws NoSuchMethodException {
		class A {
			private int a;

			public A(int a) {
				this.a = a;
			}

			protected int getA() {
				return a;
			}
		}
		MethodAccessor<A, Integer> accessor = new MethodAccessor<>(A.class.getDeclaredMethod("getA"));
		Assert.assertEquals("retrieved value should be", Integer.valueOf(57), accessor.getValue(new A(57)));
	}

	@Test
	public void testAccessingPrivateMethod() throws NoSuchMethodException {
		class A {
			private int a;

			public A(int a) {
				this.a = a;
			}

			private int getA() {
				return a;
			}
		}
		MethodAccessor<A, Integer> accessor = new MethodAccessor<>(A.class.getDeclaredMethod("getA"));
		Assert.assertEquals("retrieved value should be", null, accessor.getValue(new A(38)));
	}
}
