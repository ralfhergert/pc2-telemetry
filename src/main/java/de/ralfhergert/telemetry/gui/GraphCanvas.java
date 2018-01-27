package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.Graph;
import de.ralfhergert.telemetry.graph.GraphValue;

import java.awt.geom.Path2D;
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

	private ColoredGraph<Key,Value> graph;

	private Color zeroLineColor = new Color(1f, 1f, 1f, 0.3f);

	public GraphCanvas<Key,Value> setGraph(ColoredGraph<Key,Value> graph) {
		if (graph == null) {
			throw new IllegalArgumentException("graph can not be null");
		}
		this.graph = graph;
		graph.addListener(this);
		return this;
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

		if (graph == null || !graph.isDrawable()) {
			return;
		}
		final double wantedXSize = graph.getMaxKey().doubleValue() - graph.getMinKey().doubleValue();
		final double wantedYSize = graph.getMaxValue().doubleValue() - graph.getMinValue().doubleValue();
		g.translate(0, getHeight());
		g.scale(getWidth() / wantedXSize, -getHeight() / wantedYSize);
		g.translate(0, -graph.getMinValue().doubleValue());

		{ // render line at y = 0
			g.setColor(zeroLineColor);
			g.drawLine(0, 0, getWidth(), 0);
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
