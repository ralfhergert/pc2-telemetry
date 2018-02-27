package de.ralfhergert.telemetry.reflection;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensure that {@link PropertyInfo} is working correctly.
 */
public class PropertyInfoTest {

	@Test
	public void testToStringStartsWithClassname() {
		Assert.assertTrue("toString should start with the simple classname",
			new PropertyInfo<>("foo", null, null).toString().startsWith(PropertyInfo.class.getSimpleName()));
	}
}
