package de.ralfhergert.telemetry.gui;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures the {@link GraphCanvas} is working correctly.
 */
public class GraphCanvasTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsGraphIsRejected() {
		new GraphCanvas<>().addGraph(null);
		Assert.fail("GraphCanvas should not accept null as graph");
	}

	@Test
	public void testOnAddingGraphTheGraphCanvasRegistersItselfAsListener() {
		ColoredGraph<Integer,Integer> graph = new ColoredGraph<>(Integer::compareTo, Integer::compareTo);
		GraphCanvas<Integer,Integer> canvas = new GraphCanvas<Integer,Integer>().addGraph(graph);
		Assert.assertEquals("number of listeners on the graph", 1, graph.getListeners().size());
		/* This assertion is ok for now, but technically the canvas can also use an internally created listener.
		 * The correct check would be to confirm that the canvas is informed when changes on the graph happen. */
		Assert.assertEquals("canvas should be the listener", canvas, graph.getListeners().get(0));
	}

	@Test
	public void testOnRemovingGraphTheGraphCanvasUnregistersItselfAsListener() {
		ColoredGraph<Integer,Integer> graph = new ColoredGraph<>(Integer::compareTo, Integer::compareTo);
		GraphCanvas<Integer,Integer> canvas = new GraphCanvas<Integer,Integer>().addGraph(graph);
		/**
		 * This test does not retest whether the canvas registers itself correctly as a listener,
		 * because this is already tested by {@link testOnAddingGraphTheGraphCanvasRegistersItselfAsListener()}
		 */
		canvas.removeGraph(graph);
		Assert.assertEquals("number of graphs in the canvas", 0, canvas.getGraphs().size());
		Assert.assertEquals("number of listeners on the graph", 0, graph.getListeners().size());
	}
}