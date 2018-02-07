package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.util.MapBuilder;
import de.ralfhergert.telemetry.util.MessageFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * This action will show a ColorChooser to change a {@link LineGraph} color.
 */
public class ChangeLineGraphColorAction extends AbstractAction {

	private final Component parentComponent;
	private final LineGraph lineGraph;

	public ChangeLineGraphColorAction(Component parentComponent, LineGraph lineGraph) {
		super(ResourceBundle.getBundle("messages").getString("action.changeLineGraphColor.caption"));
		this.parentComponent = parentComponent;
		this.lineGraph = lineGraph;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Color color = JColorChooser.showDialog(parentComponent,
			new MessageFormatter().format(ResourceBundle.getBundle("messages").getString("lineGraphColorChooser.title"), new MapBuilder<String,String>().put("name", (String)lineGraph.getProperty("name", "no name")).build()),
			(Color)lineGraph.getProperty("color", Color.LIGHT_GRAY));
		if (color != null) {
			lineGraph.setProperty("color", color);
		}
	}

	public ChangeLineGraphColorAction withCaption(String caption) {
		putValue(Action.NAME, caption);
		return this;
	}
}
