package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.gui.GraphCanvas;
import de.ralfhergert.telemetry.util.ImageBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This action will add the given {@link LineGraph} to the given {@link GraphCanvas}.
 */
public class AddLineGraphToGraphCanvasAction extends AbstractAction {

	private final LineGraph lineGraph;
	private final GraphCanvas graphCanvas;

	public AddLineGraphToGraphCanvasAction(LineGraph lineGraph, GraphCanvas graphCanvas) {
		super((String)lineGraph.getProperty("name", "unnamed"), new ImageIcon(ImageBuilder.createSingleColoredImage(10, 10, (Color)lineGraph.getProperty("color", Color.WHITE))));
		this.lineGraph = lineGraph;
		this.graphCanvas = graphCanvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		graphCanvas.addGraph(lineGraph);
		graphCanvas.revalidate();
	}

	public AddLineGraphToGraphCanvasAction withCaption(String caption) {
		putValue(Action.NAME, caption);
		return this;
	}
}
