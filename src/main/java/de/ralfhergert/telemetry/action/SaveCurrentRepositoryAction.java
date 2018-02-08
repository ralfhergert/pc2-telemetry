package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.Telemetry;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.persistence.csv.CarPhysicsCsvWriter;
import de.ralfhergert.telemetry.repository.Repository;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * This action will prompt a save dialog. When approved the application's current
 * repository will be stored in the chosen file.
 */
public class SaveCurrentRepositoryAction extends AbstractAction {

	private static final String LAST_USED_DIRECTORY_PROPERTY_NAME = "SaveCurrentRepositoryAction.lastUsedDirectory";

	private Telemetry application;
	private Component parent;

	public SaveCurrentRepositoryAction(Telemetry application, Component parent) {
		super(ResourceBundle.getBundle("messages").getString("action.saveCurrentRepository.caption"));
		this.application = application;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Repository<CarPhysicsPacket> repository = application.getCurrentRepository();
		if (repository == null) {
			return;
		}
		JFileChooser fileChooser = new JFileChooser();
		// try to initialize the file chooser on the last used directory.
		if (application.getProperties().getProperty(LAST_USED_DIRECTORY_PROPERTY_NAME) != null) {
			File lastUsedDirectory = new File(application.getProperties().getProperty(LAST_USED_DIRECTORY_PROPERTY_NAME));
			if (lastUsedDirectory.exists()) {
				fileChooser.setCurrentDirectory(lastUsedDirectory);
			}
		}
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".pc2td");
			}

			@Override
			public String getDescription() {
				return ResourceBundle.getBundle("messages").getString("fileName.pc2td.caption");
			}
		});
		fileChooser.setSelectedFile(new File("capture.pc2td"));
		if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			try {
				writeAsJarFile(repository, new FileOutputStream(fileChooser.getSelectedFile()));
				application.getProperties().setProperty(LAST_USED_DIRECTORY_PROPERTY_NAME, fileChooser.getCurrentDirectory().getAbsolutePath());
			} catch (IOException e) {
				// TODO prompt a warning.
			}
		}
	}

	public static void writeAsJarFile(Repository<CarPhysicsPacket> repository, OutputStream outStream) throws IOException {
		JarOutputStream jarStream = new JarOutputStream(outStream);
		jarStream.putNextEntry(new ZipEntry("carPhysics"));
		new CarPhysicsCsvWriter().write(repository.getItemStream(), jarStream);
		jarStream.close();
	}
}
