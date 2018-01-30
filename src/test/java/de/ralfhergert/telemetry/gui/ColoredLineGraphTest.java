package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * This test ensures that {@link ColoredLineGraph} is working correctly.
 */
public class ColoredLineGraphTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullRepositoryIsRejected() {
		new ColoredLineGraph<>(null, (t,o) -> null, (t) -> null);
		Assert.fail("graph should not have been accepting null as repository");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullColorIsRejected() {
		new ColoredLineGraph<>(new IndexedRepository<>((o1,o2) -> 0), (t, o) -> null, (t) -> null).setColor(null);
		Assert.fail("graph should not have been accepting null as color");
	}

	@Test
	public void testAlteringColorTriggersAllListeners() {
		final Color expectedColor = Color.RED;
		// create a specific listener.
		class TestColoredLineGraphListener implements ColoredLineGraphListener<Object,Integer,Integer> {
			public boolean triggered = false;

			@Override
			public void changedGraphColor(LineGraph<Object, Integer, Integer> graph, Color color) {
				Assert.assertEquals("color should be", expectedColor, color);
				triggered = true;
			}
			@Override
			public void graphChanged(LineGraph<Object, Integer, Integer> graph) {
				Assert.fail("this method should not be triggered in this test");
			}
			public boolean wasTriggered() {
				return triggered;
			}
		}
		// create two listeners
		TestColoredLineGraphListener listener1 = new TestColoredLineGraphListener();
		TestColoredLineGraphListener listener2 = new TestColoredLineGraphListener();
		// test the ColoredLineGraph
		new ColoredLineGraph<>(new IndexedRepository<>((o1,o2) -> 0), (t, o) -> null, (t) -> null)
			.addListener((ColoredLineGraphListener)listener1)
			.addListener((ColoredLineGraphListener)listener2)
			.setColor(expectedColor);
		// confirm both listeners have been called.
		Assert.assertTrue("listener1 should have been triggered", listener1.wasTriggered());
		Assert.assertTrue("listener2 should have been triggered", listener2.wasTriggered());
	}
}
