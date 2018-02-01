package de.ralfhergert.telemetry;

import de.ralfhergert.telemetry.action.SaveCurrentRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This is the main frame for this application.
 */
public class TelemetryFrame extends JFrame {

	public TelemetryFrame(final Telemetry application) throws HeadlessException {
		super("Racing Telemetry");

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		JMenuBar mainMenu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new SaveCurrentRepository(application, this));
		mainMenu.add(fileMenu);
		setJMenuBar(mainMenu);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				application.shutdown();
			}
		});
	}
}
