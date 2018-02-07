package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.graph.LineGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;

/**
 * This action will show a ColorChooser to change a {@link LineGraph} color.
 */
public class OpenWebLinkAction extends AbstractAction {

	private final URI webLink;

	public OpenWebLinkAction(URI webLink) {
		this.webLink = webLink;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Desktop.getDesktop().browse(webLink);
		} catch (IOException e1) {
			// TODO implement error prompt to inform user.
		}
	}

	public OpenWebLinkAction withCaption(String caption) {
		putValue(Action.NAME, caption);
		return this;
	}
}
