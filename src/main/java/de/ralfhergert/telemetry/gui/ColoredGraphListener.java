package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.Graph;
import de.ralfhergert.telemetry.graph.GraphListener;
import de.ralfhergert.telemetry.graph.GraphValue;

import java.awt.*;

/**
 * Interface for receiving graph changes.
 */
public interface ColoredGraphListener<Key extends Number, Value extends Number> extends GraphListener<Key,Value> {

	void changedGraphColor(Graph<Key, Value> graph, Color color);
}
