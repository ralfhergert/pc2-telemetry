package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.gui.GraphCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This action will remove the given {@link LineGraph} from the given {@link GraphCanvas}.
 */
public class RemoveLineGraphFromGraphCanvasAction extends AbstractAction {

	private final LineGraph lineGraph;
	private final GraphCanvas graphCanvas;

	public RemoveLineGraphFromGraphCanvasAction(LineGraph lineGraph, GraphCanvas graphCanvas) {
		this.lineGraph = lineGraph;
		this.graphCanvas = graphCanvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		graphCanvas.removeGraph(lineGraph);
	}

	public RemoveLineGraphFromGraphCanvasAction withCaption(String caption) {
		putValue(Action.NAME, caption);
		return this;
	}
}
