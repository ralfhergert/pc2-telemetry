package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.gui.GraphCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This action will add the given {@link LineGraph} to the given {@link GraphCanvas}.
 */
public class AddLineGraphToGraphCanvasAction extends AbstractAction {

	private final LineGraph lineGraph;
	private final GraphCanvas graphCanvas;

	public AddLineGraphToGraphCanvasAction(LineGraph lineGraph, GraphCanvas graphCanvas) {
		this.lineGraph = lineGraph;
		this.graphCanvas = graphCanvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		graphCanvas.addGraph(lineGraph);
	}

	public AddLineGraphToGraphCanvasAction withCaption(String caption) {
		putValue(Action.NAME, caption);
		return this;
	}
}
