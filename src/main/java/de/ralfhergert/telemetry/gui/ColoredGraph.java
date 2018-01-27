package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.Graph;
import de.ralfhergert.telemetry.graph.GraphListener;

import java.awt.*;
import java.util.Comparator;

/**
 * This derived class defines a colored graph.
 */
public class ColoredGraph<Key extends Number, Value extends Number> extends Graph<Key,Value> {

	private Color color = Color.WHITE;

	public ColoredGraph(Comparator<Key> keyComparator, Comparator<Value> valueComparator) {
		super(keyComparator, valueComparator);
	}

	public ColoredGraph<Key,Value> addListener(ColoredGraphListener<Key,Value> listener) {
		super.addListener(listener);
		return this;
	}

	public ColoredGraph<Key,Value> setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("color can not be null");
		}
		if (!this.color.equals(color)) {
			this.color = color;
			for (GraphListener<Key,Value> listener : listeners) {
				if (listener instanceof ColoredGraphListener) {
					((ColoredGraphListener)listener).changedGraphColor(this, color);
				}
			}
		}
		return this;
	}

	public Color getColor() {
		return color;
	}
}
