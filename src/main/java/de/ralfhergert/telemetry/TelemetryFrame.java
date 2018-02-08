package de.ralfhergert.telemetry;

import de.ralfhergert.telemetry.action.LoadCurrentRepositoryAction;
import de.ralfhergert.telemetry.action.OpenWebLinkAction;
import de.ralfhergert.telemetry.action.SaveCurrentRepositoryAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
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
			JMenu fileMenu = new JMenu(ResourceBundle.getBundle("messages").getString("mainMenu.file.caption"));
			fileMenu.add(new LoadCurrentRepositoryAction(application, this));
			fileMenu.add(new SaveCurrentRepositoryAction(application, this));
			mainMenu.add(fileMenu);
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
	}
}
