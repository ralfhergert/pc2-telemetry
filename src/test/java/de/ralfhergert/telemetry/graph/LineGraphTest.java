package de.ralfhergert.telemetry.graph;

import de.ralfhergert.telemetry.repository.IndexedRepository;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * This test ensures that {@link LineGraph} is working correctly.
 */
public class LineGraphTest {

	@Test
	public void testSettingColor() {
		LineGraph<Integer,Integer,Integer> lineGraph = new LineGraph<>(new IndexedRepository<>((i1,i2) -> i2 - i1), (Integer i, Integer o) -> i - o, Integer::intValue);
		Assert.assertNull("line has no color set", lineGraph.getProperty("color", null));
		lineGraph.setProperty("color", Color.RED);
		Assert.assertEquals("line has red color set", Color.RED, lineGraph.getProperty("color", null));
	}

	@Test
	public void testChangingColor() {
		LineGraph<Integer,Integer,Integer> lineGraph = new LineGraph<>(new IndexedRepository<>((i1,i2) -> i2 - i1), (Integer i, Integer o) -> i - o, Integer::intValue);
		lineGraph.setProperty("color", Color.BLUE);
		Assert.assertEquals("line has red color set", Color.BLUE, lineGraph.getProperty("color", null));
		lineGraph.setProperty("color", Color.GREEN);
		Assert.assertEquals("line has green color set", Color.GREEN, lineGraph.getProperty("color", null));
	}
}
