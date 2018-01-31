package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.*;

import java.awt.*;

/**
 * Interface for receiving graph changes.
 */
public interface ColoredLineGraphListener<Item, Key extends Number, Value extends Number> extends LineGraphListener<Item, Key,Value> {

	void changedGraphColor(LineGraph<Item, Key, Value> graph, Color color);
}
