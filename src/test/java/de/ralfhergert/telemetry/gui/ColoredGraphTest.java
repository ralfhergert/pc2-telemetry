package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.Graph;
import de.ralfhergert.telemetry.graph.GraphValue;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * This test ensures that {@link ColoredGraph} is working correctly.
 */
public class ColoredGraphTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullColorIsRejected() {
		new ColoredGraph<>(Integer::compareTo, Integer::compareTo).setColor(null);
		Assert.fail("graph should not have been accepting null as color");
	}

	@Test
	public void testAlteringColorTriggersAllListeners() {
		final Color expectedColor = Color.RED;
		// create a specific listener.
		class TestColoredGraphListener implements ColoredGraphListener<Integer,Integer> {
			public boolean triggered = false;
			@Override
			public void changedGraphColor(Graph<Integer,Integer> graph, Color color) {
				Assert.assertEquals("color should be", expectedColor, color);
				triggered = true;
			}
			@Override
			public void addedGraphValue(Graph<Integer, Integer> graph, GraphValue<Integer, Integer> graphValue, boolean keyDimensionChanged, boolean valueDimensionChanged) {
				Assert.fail("this method should not be triggered in this test");
			}
			public boolean wasTriggered() {
				return triggered;
			}
		};
		// create two listeners
		TestColoredGraphListener listener1 = new TestColoredGraphListener();
		TestColoredGraphListener listener2 = new TestColoredGraphListener();
		// test the ColoredGraph
		new ColoredGraph<>(Integer::compareTo, Integer::compareTo)
			.addListener(listener1)
			.addListener(listener2)
			.setColor(expectedColor);
		// confirm both listeners have been called.
		Assert.assertTrue("listener1 should have been triggered", listener1.wasTriggered());
		Assert.assertTrue("listener2 should have been triggered", listener2.wasTriggered());
	}
}
