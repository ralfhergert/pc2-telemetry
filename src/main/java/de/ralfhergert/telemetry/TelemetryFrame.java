package de.ralfhergert.telemetry;

import de.ralfhergert.telemetry.action.*;
import de.ralfhergert.telemetry.util.MessageFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * This is the main frame for this application.
 */
public class TelemetryFrame extends JFrame {

	public TelemetryFrame(final Telemetry application) throws HeadlessException {
		super(ResourceBundle.getBundle("messages").getString("application.title"));
		setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("icon.png")));

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		JMenuBar mainMenu = new JMenuBar();
		{ // create the file menu
			JMenu dataMenu = new JMenu(ResourceBundle.getBundle("messages").getString("mainMenu.data.caption"));
			dataMenu.add(new LoadCurrentRepositoryAction(application, this));
			dataMenu.add(new SaveCurrentRepositoryAction(application, this));
			dataMenu.addSeparator();
			dataMenu.add(new StartCapturingAction(application));
			dataMenu.add(new StopCapturingAction(application));
			mainMenu.add(dataMenu);
		}
		{ // create the help menu
			JMenu helpMenu = new JMenu(ResourceBundle.getBundle("messages").getString("mainMenu.help.caption"));
			helpMenu.add(new OpenWebLinkAction(URI.create("https://github.com/ralfhergert/pc2-telemetry/issues")).withCaption(ResourceBundle.getBundle("messages").getString("action.openGitHubProjectIssue.caption")));
			helpMenu.add(new OpenWebLinkAction(URI.create("https://github.com/ralfhergert/pc2-telemetry")).withCaption(ResourceBundle.getBundle("messages").getString("action.openGitHubProjectPage.caption")));
			mainMenu.add(helpMenu);
		}
		setJMenuBar(mainMenu);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				application.shutdown();
			}
		});

		// register notification listeners
		application.getNotificationCache().addListener((cache, notification) -> {
			if (notification instanceof Telemetry.CaptureThreadNotification) {
				final String titlePattern = ResourceBundle.getBundle("messages").getString("application.title.withConnectionState");
				final String state = ((Telemetry.CaptureThreadNotification)notification).isCapturing()
					? ResourceBundle.getBundle("messages").getString("generic.connected")
					: ResourceBundle.getBundle("messages").getString("generic.disconnected");
				setTitle(new MessageFormatter().format(titlePattern, Collections.singletonMap("state", state)));
			}
		});
	}
}
