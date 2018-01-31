package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.Accessor;
import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.graph.LineGraphListener;
import de.ralfhergert.telemetry.graph.NegativeOffsetAccessor;
import de.ralfhergert.telemetry.repository.IndexedRepository;

import java.awt.*;

/**
 * This derived class defines a colored graph.
 */
public class ColoredLineGraph<Item, Key extends Number, Value extends Number> extends LineGraph<Item, Key,Value> {

	private Color color = Color.WHITE;

	public ColoredLineGraph(IndexedRepository<Item> repository, NegativeOffsetAccessor<Item, Key> keyAccessor, Accessor<Item, Value> valueAccessor) {
		super(repository, keyAccessor, valueAccessor);
	}

	public ColoredLineGraph<Item, Key,Value> addListener(ColoredLineGraphListener<Item, Key,Value> listener) {
		super.addListener(listener);
		return this;
	}

	public ColoredLineGraph<Item, Key,Value> setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("color can not be null");
		}
		if (!this.color.equals(color)) {
			this.color = color;
			for (LineGraphListener<Item, Key,Value> listener : listeners) {
				if (listener instanceof ColoredLineGraphListener) {
					((ColoredLineGraphListener)listener).changedGraphColor(this, color);
				}
			}
		}
		return this;
	}

	public Color getColor() {
		return color;
	}
}
