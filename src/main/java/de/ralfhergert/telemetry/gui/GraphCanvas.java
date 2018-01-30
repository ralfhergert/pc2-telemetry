package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.Graph;
import de.ralfhergert.telemetry.graph.LineGraph;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * Canvas component rendering a single {@link Graph}.
 */
public class GraphCanvas<Item, Key extends Number,Value extends Number> extends JComponent implements ColoredLineGraphListener<Item,Key,Value>, Scrollable {

	private double xScale = 0.2;
	private DimensionChangeAdaptStyle xAdaptionStyle = DimensionChangeAdaptStyle.Size;
	private double yScale = 1;
	private DimensionChangeAdaptStyle yAdaptionStyle = DimensionChangeAdaptStyle.Scale;

	private final List<ColoredLineGraph<Item, Key,Value>> graphs = new ArrayList<>();

	private Color zeroLineColor = new Color(1f, 1f, 1f, 0.3f);

	public GraphCanvas<Item, Key, Value> addGraph(ColoredLineGraph<Item, Key, Value> graph) {
		if (graph == null) {
			throw new IllegalArgumentException("graph can not be null");
		}
		if (!graphs.contains(graph)) {
			graphs.add(graph);
			graph.addListener(this);
		}
		return this;
	}

	public GraphCanvas<Item, Key,Value> removeGraph(ColoredLineGraph<Item, Key,Value> graph) {
		if (graph == null) {
			throw new IllegalArgumentException("graph can not be null");
		}
		if (graphs.remove(graph)) {
			graph.removeListener(this);
		}
		return this;
	}

	public List<ColoredLineGraph<Item, Key, Value>> getGraphs() {
		return new ArrayList<>(graphs); // create a copy to avoid modification.
	}

	public GraphCanvas<Item, Key,Value> setZeroLineColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("zeroLineColor can not be null");
		}
		if (!this.zeroLineColor.equals(color)) {
			this.zeroLineColor = color;
			repaint();
		}
		return this;
	}

	@Override
	public void graphChanged(LineGraph graph) {
		invalidate();
		repaint();
	}

	@Override
	public void changedGraphColor(LineGraph graph, Color color) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());

		double minKey = Double.MAX_VALUE;
		double maxKey = Double.MIN_VALUE;
		double minValue = 0;
		double maxValue = 0;
		for (LineGraph graph : graphs) {
			Path2D path = graph.getPath();
			if (path == null) {
				continue;
			}
			minKey = Math.min(minKey, path.getBounds().getMinX());
			maxKey = Math.max(maxKey, path.getBounds().getMaxX());
			minValue = Math.min(minValue, path.getBounds().getMinY());
			maxValue = Math.max(maxValue, path.getBounds().getMaxY());
		}
		final double wantedXSize = maxKey - minKey;
		final double wantedYSize = maxValue - minValue;
		if (wantedXSize == 0 || wantedYSize == 0) {
			return; // nothing to draw
		}
		g.translate(0, getHeight());
		g.scale(getWidth() / wantedXSize, -getHeight() / wantedYSize);
		g.translate(0, -minValue);
		{ // render line at y = 0
			g.setColor(zeroLineColor);
			g.drawLine(0, 0, getWidth(), 0);
		}

		for (ColoredLineGraph<Item, Key,Value> graph : graphs) {
			Path2D path = graph.getPath();
			if (path == null) {
				continue;
			}
			g.setColor(graph.getColor());
			g.draw(path);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		double minKey = Double.MAX_VALUE;
		double maxKey = Double.MIN_VALUE;
		double minValue = 0;
		double maxValue = 0;
		for (LineGraph graph : graphs) {
			Path2D path = graph.getPath();
			if (path == null) {
				continue;
			}
			minKey = Math.min(minKey, path.getBounds().getMinX());
			maxKey = Math.max(maxKey, path.getBounds().getMaxX());
			minValue = Math.min(minValue, path.getBounds().getMinY());
			maxValue = Math.max(maxValue, path.getBounds().getMaxY());
		}
		final double wantedXSize = (maxKey - minKey) * xScale;
		final double wantedYSize = (maxValue - minValue) * yScale;
		return new Dimension((int)wantedXSize, (int)wantedYSize);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return null;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return xAdaptionStyle == DimensionChangeAdaptStyle.Scale;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return yAdaptionStyle == DimensionChangeAdaptStyle.Scale;
	}
}
