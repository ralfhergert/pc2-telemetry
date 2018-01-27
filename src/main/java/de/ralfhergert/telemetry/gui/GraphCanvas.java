package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.Graph;
import de.ralfhergert.telemetry.graph.GraphValue;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * Canvas component rendering a single {@link Graph}.
 */
public class GraphCanvas<Key extends Number,Value extends Number> extends JComponent implements ColoredGraphListener<Key,Value>, Scrollable {

	private double xScale = 1;
	private int xSize = 100;
	private DimensionChangeAdaptStyle xAdaptionStyle = DimensionChangeAdaptStyle.Size;
	private double yScale = 1;
	private int ySize = 100;
	private DimensionChangeAdaptStyle yAdaptionStyle = DimensionChangeAdaptStyle.Scale;

	private final List<ColoredGraph<Key,Value>> graphs = new ArrayList<>();

	private Color zeroLineColor = new Color(1f, 1f, 1f, 0.3f);

	public GraphCanvas<Key,Value> addGraph(ColoredGraph<Key,Value> graph) {
		if (graph == null) {
			throw new IllegalArgumentException("graph can not be null");
		}
		if (!graphs.contains(graph)) {
			graphs.add(graph);
			graph.addListener(this);
		}
		return this;
	}

	public GraphCanvas<Key,Value> removeGraph(ColoredGraph<Key,Value> graph) {
		if (graph == null) {
			throw new IllegalArgumentException("graph can not be null");
		}
		if (graphs.remove(graph)) {
			graph.removeListener(this);
		}
		return this;
	}

	public List<ColoredGraph<Key, Value>> getGraphs() {
		return new ArrayList<>(graphs); // create a copy to avoid modification.
	}

	public GraphCanvas<Key,Value> setZeroLineColor(Color color) {
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
	public void addedGraphValue(Graph<Key, Value> graph, GraphValue<Key, Value> graphValue, final boolean keyDimensionChanged, final boolean valueDimensionChanged) {
		if (graph.isDrawable()) {
			if (keyDimensionChanged) { // recalculation of x dimension
				final double wantedXSize = graph.getMaxKey().doubleValue() - graph.getMinKey().doubleValue();
				if (xAdaptionStyle == DimensionChangeAdaptStyle.Size) {
					xSize = (int) Math.ceil(xScale * wantedXSize);
				} else {
					xScale = xSize / wantedXSize;
				}
				invalidate();
			}
			if (valueDimensionChanged) { // recalculation of y dimension
				final double wantedYSize = graph.getMaxValue().doubleValue() - graph.getMinValue().doubleValue();
				if (yAdaptionStyle == DimensionChangeAdaptStyle.Size) {
					ySize = (int) Math.ceil(yScale * wantedYSize);
				} else {
					yScale = ySize / wantedYSize;
				}
				invalidate();
			}
			repaint();
		}
	}

	@Override
	public void changedGraphColor(Graph<Key, Value> graph, Color color) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());

		double minKey = 0;
		double maxKey = 0;
		double minValue = 0;
		double maxValue = 0;
		for (Graph graph : graphs) {
			if (!graph.isDrawable()) {
				continue;
			}
			minKey = Math.min(minKey, graph.getMinKey().doubleValue());
			maxKey = Math.max(maxKey, graph.getMaxKey().doubleValue());
			minValue = Math.min(minValue, graph.getMinValue().doubleValue());
			maxValue = Math.max(maxValue, graph.getMaxValue().doubleValue());
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

		for (ColoredGraph<Key,Value> graph : graphs) {
			if (!graph.isDrawable()) {
				continue;
			}
			g.setColor(graph.getColor());

			final List<GraphValue<Key,Value>> values = graph.getValues();
			if (values.size() < 2) {
				return;
			}
			Path2D path = new Path2D.Float(Path2D.WIND_NON_ZERO, values.size());
			path.moveTo(values.get(0).getKey().floatValue(), values.get(0).getValue().floatValue());
			for (GraphValue<Key,Value> value : values) {
				path.lineTo(value.getKey().floatValue(), value.getValue().floatValue());
			}
			g.draw(path);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(xSize, ySize);
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
