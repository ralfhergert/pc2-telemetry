package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Color;

/**
 * This test ensures the {@link GraphCanvas} is working correctly.
 */
public class GraphCanvasTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsGraphIsRejected() {
		new GraphCanvas<>().addGraph(null);
		Assert.fail("GraphCanvas should not accept null as graph");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGraphCanvasRejectsRemovingNullGraph() {
		new GraphCanvas<>().removeGraph(null);
		Assert.fail("GraphCanvas should not reject removing null graph");
	}

	@Test
	public void testOnAddingGraphTheGraphCanvasRegistersItselfAsListener() {
		LineGraph<Object,Integer,Integer> graph = new LineGraph<>(new IndexedRepository<>(null), (t, o) -> null, (t) -> null);
		GraphCanvas<Object,Integer,Integer> canvas = new GraphCanvas<Object,Integer,Integer>().addGraph(graph);
		Assert.assertEquals("number of listeners on the graph", 1, graph.getListeners().size());
		/* This assertion is ok for now, but technically the canvas can also use an internally created listener.
		 * The correct check would be to confirm that the canvas is informed when changes on the graph happen. */
		Assert.assertEquals("canvas should be the listener", canvas, graph.getListeners().get(0));
	}

	/**
	 * This test does not retest whether the canvas registers itself correctly as a listener,
	 * because this is already tested by {@link #testOnAddingGraphTheGraphCanvasRegistersItselfAsListener()}
	 */
	@Test
	public void testOnRemovingGraphTheGraphCanvasUnregistersItselfAsListener() {
		LineGraph<Object,Integer,Integer> graph = new LineGraph<>(new IndexedRepository<>(null), (t,o) -> null, (o) -> null);
		GraphCanvas<Object,Integer,Integer> canvas = new GraphCanvas<Object,Integer,Integer>().addGraph(graph);
		canvas.removeGraph(graph);
		Assert.assertEquals("number of graphs in the canvas", 0, canvas.getGraphs().size());
		Assert.assertEquals("number of listeners on the graph", 0, graph.getListeners().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSettingNullAsZeroLineColorIsRejected() {
		new GraphCanvas<>().setZeroLineColor(null);
		Assert.fail("GraphCanvas should not allow null to be set as zero-Line-color");
	}

	@Test
	public void testSettingNewZeroLineColorTriggersRepaint() {
		final CountingGraphCanvas canvas = new CountingGraphCanvas();
		// set the color white.
		canvas.setZeroLineColor(Color.WHITE);
		canvas.clear();
		Assert.assertEquals("repaint should not have been triggered", 0, canvas.getRepaintsTriggered());
		canvas.setZeroLineColor(Color.BLACK);
		Assert.assertEquals("repaint should have been triggered once", 1, canvas.getRepaintsTriggered());
	}

	@Test
	public void testSettingTheSameZeroLineColorDoesNotTriggerRepaint() {
		final CountingGraphCanvas canvas = new CountingGraphCanvas();
		// set the color white.
		canvas.setZeroLineColor(Color.WHITE);
		canvas.clear();
		Assert.assertEquals("repaint should not have been triggered", 0, canvas.getRepaintsTriggered());
		canvas.setZeroLineColor(Color.WHITE);
		Assert.assertEquals("repaint should not have been triggered", 0, canvas.getRepaintsTriggered());
	}

	@Ignore
	public class CountingGraphCanvas extends GraphCanvas {

		private int repaintsTriggered = 0;

		@Override
		public void repaint(long tm, int x, int y, int width, int height) {
			repaintsTriggered++;
			super.repaint(tm, x, y, width, height);
		}

		public int getRepaintsTriggered() {
			return repaintsTriggered;
		}

		public void clear() {
			repaintsTriggered = 0;
		}
	}
}
