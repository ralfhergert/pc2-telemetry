package de.ralfhergert.telemetry.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * This test ensure that {@link PropertyInspector} is working correctly.
 */
public class PropertyInspectorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullClassIsRejected() {
		PropertyInspector.inspect(null);
	}

	@Test
	public void testInspectPrivateAttributes() {
		class A {
			private int a; // private attributes should not be accessible.
		}
		final List<PropertyInfo<A,?>> info = PropertyInspector.inspect(A.class);
		Assert.assertNotNull("info should not be null", info);
		Assert.assertEquals("number of properties", 1, info.size());
		Assert.assertEquals("property name", "class", info.get(0).getPropertyName());
		// test the found accessor.
		Assert.assertEquals("value should be correctly retrieved", A.class, info.get(0).getPropertyAccessor().getValue(new A()));
	}

	@Test
	public void testInspectPublicAttributes() {
		class A {
			public int a;
			public A(int a) {
				this.a = a;
			}
		}
		final List<PropertyInfo<A,?>> info = PropertyInspector.inspect(A.class);
		Assert.assertNotNull("info should not be null", info);
		Assert.assertEquals("number of properties", 2, info.size());
		Assert.assertEquals("property name", "a", info.get(0).getPropertyName());
		// test the found accessor.
		Assert.assertEquals("value should be correctly retrieved", 42, info.get(0).getPropertyAccessor().getValue(new A(42)));
	}

	@Test
	public void testInspectPrivateMethod() {
		class A {
			private int getA() { return 0; } // private methods should not be accessible.
		}
		final List<PropertyInfo<A,?>> info = PropertyInspector.inspect(A.class);
		Assert.assertNotNull("info should not be null", info);
		Assert.assertEquals("number of properties", 1, info.size());
		Assert.assertEquals("property name", "class", info.get(0).getPropertyName());
		// test the found accessor.
		Assert.assertEquals("value should be correctly retrieved", A.class, info.get(0).getPropertyAccessor().getValue(new A()));
	}

	@Test
	public void testInspectPublicGetMethod() {
		class A {
			public int getA() { return 42; }
		}
		final List<PropertyInfo<A,?>> info = PropertyInspector.inspect(A.class);
		Assert.assertNotNull("info should not be null", info);
		Assert.assertEquals("number of properties", 2, info.size());
		Assert.assertEquals("property name", "a", info.get(0).getPropertyName());
		// test the found accessor.
		Assert.assertEquals("value should be correctly retrieved", 42, info.get(0).getPropertyAccessor().getValue(new A()));
	}

	@Test
	public void testConvertingNullString() {
		Assert.assertEquals("resulting string should be", null, PropertyInspector.convertFirstCharacterToLowerCase(null));
	}

	@Test
	public void testConvertingEmptyString() {
		Assert.assertEquals("resulting string should be", "", PropertyInspector.convertFirstCharacterToLowerCase(""));
	}

	@Test
	public void testConvertingExampleString() {
		Assert.assertEquals("resulting string should be", "aBC", PropertyInspector.convertFirstCharacterToLowerCase("ABC"));
	}
}
