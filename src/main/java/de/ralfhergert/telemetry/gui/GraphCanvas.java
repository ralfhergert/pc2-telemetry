package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.graph.LineGraphListener;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * Canvas component rendering multiple {@link LineGraph}.
 */
public class GraphCanvas<Item, Key extends Number,Value extends Number> extends JComponent implements LineGraphListener<Item,Key,Value>, Scrollable {

	private double xScale = 0.2;
	private DimensionChangeAdaptStyle xAdaptionStyle = DimensionChangeAdaptStyle.Size;
	private double yScale = 1;
	private DimensionChangeAdaptStyle yAdaptionStyle = DimensionChangeAdaptStyle.Scale;

	private final List<LineGraph<Item, Key,Value>> graphs = new ArrayList<>();

	private Color zeroLineColor = new Color(1f, 1f, 1f, 0.3f);

	public GraphCanvas() {
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.isControlDown()) {
					xScale *= Math.pow(1.5, e.getWheelRotation());
					revalidate();
				}
			}
		});
	}

	public GraphCanvas<Item, Key, Value> addGraph(LineGraph<Item, Key, Value> graph) {
		if (graph == null) {
			throw new IllegalArgumentException("graph can not be null");
		}
		if (!graphs.contains(graph)) {
			graphs.add(graph);
			graph.addListener(this);
			graph.getPropertyValues().ensureProperty("color", Color.WHITE, (p,o,n) -> this.repaint());
		}
		return this;
	}

	public GraphCanvas<Item, Key,Value> removeGraph(LineGraph<Item, Key,Value> graph) {
		if (graph == null) {
			throw new IllegalArgumentException("graph can not be null");
		}
		if (graphs.remove(graph)) {
			graph.removeListener(this);
		}
		return this;
	}

	public List<LineGraph<Item, Key, Value>> getGraphs() {
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
		revalidate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());

		final Rectangle2D pathBounds = calcCombinedPathBounds();
		if (pathBounds.getWidth() == 0 || pathBounds.getHeight() == 0) {
			return; // nothing to draw
		}
		g.translate(0, getHeight());
		g.scale(getWidth() / pathBounds.getWidth(), -getHeight() / pathBounds.getHeight());
		g.translate(0, -pathBounds.getMinY());
		{ // render line at y = 0
			g.setColor(zeroLineColor);
			g.drawLine(0, 0, getWidth(), 0);
		}

		for (LineGraph<Item, Key,Value> graph : graphs) {
			Path2D path = graph.getPath();
			if (path == null) {
				continue;
			}
			g.setColor(graph.getProperty("color", Color.WHITE));
			g.draw(path);
		}
	}

	public Rectangle2D calcCombinedPathBounds() {
		Rectangle2D bounds = null;
		for (LineGraph graph : graphs) {
			Path2D path = graph.getPath();
			if (path == null) {
				continue;
			}
			if (bounds == null) {
				bounds = path.getBounds2D();
			} else {
				bounds = bounds.createUnion(path.getBounds2D());
			}
		}
		if (bounds == null) {
			return new Rectangle2D.Float(0, 0, 0, 0);
		} else {
			// ensure y=0 is contained within the bounds.
			return bounds.createUnion(new Rectangle2D.Double(bounds.getX(), 0, bounds.getWidth(), 0));
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Rectangle2D bounds = calcCombinedPathBounds();
		final double wantedXSize = bounds.getWidth() * xScale;
		final double wantedYSize = bounds.getHeight() * yScale;
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
