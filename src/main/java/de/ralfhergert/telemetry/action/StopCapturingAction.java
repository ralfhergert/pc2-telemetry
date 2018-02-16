package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.Telemetry;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * This action will trigger the application to connect and listen for UDP packets.
 */
public class StopCapturingAction extends AbstractAction {

	private Telemetry application;

	public StopCapturingAction(Telemetry application) {
		super(ResourceBundle.getBundle("messages").getString("action.stopCapturing.caption"));
		this.application = application;
		// register a connection notification listener to control this action's enabled state.
		application.getNotificationCache().addListener((cache, notification) -> {
			if (notification instanceof Telemetry.CaptureThreadNotification) {
				setEnabled(((Telemetry.CaptureThreadNotification)notification).isCapturing());
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		application.stopCapturing();
	}
}
