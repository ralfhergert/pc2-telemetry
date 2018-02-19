package de.ralfhergert.telemetry.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

/**
 * This test ensures that {@link MapBuilder} is working as intended.
 */
public class MapBuilderTest {

	@Test
	public void testEmptyMapCreation() {
		final Map<String,Object> map = new MapBuilder<String,Object>().build();
		Assert.assertNotNull("map should not be null", map);
		Assert.assertTrue("map should be empty", map.isEmpty());
	}

	@Test
	public void testMapCreation() {
		final Map<Integer,String> map = new MapBuilder<Integer,String>().put(1, "1").put(2, "2").build();
		Assert.assertNotNull("map should not be null", map);
		Assert.assertFalse("map should not be empty", map.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullMapIsRejected() {
		new MapBuilder<String,Object>(null);
	}

	@Test(expected = Exception.class)
	public void testBuildingOnImmutableMapIsRejected() {
		new MapBuilder<String,Object>(Collections.unmodifiableMap(Collections.emptyMap())).put("foo", "bar");
	}
}
