package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.Telemetry;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.persistence.csv.CarPhysicsCsvReader;
import de.ralfhergert.telemetry.repository.Repository;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * This action will prompt a save dialog. When approved the application's current
 * repository will be stored in the chosen file.
 */
public class LoadCurrentRepository extends AbstractAction {

	private Telemetry application;
	private Component parent;

	public LoadCurrentRepository(Telemetry application, Component parent) {
		super(ResourceBundle.getBundle("messages").getString("action.loadCurrentRepository.caption"));
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
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			try {
				readAsJarFile(repository, new FileInputStream(fileChooser.getSelectedFile()));
			} catch (IOException e) {
				// TODO prompt a warning.
			}
		}
	}

	public static void readAsJarFile(Repository<CarPhysicsPacket> repository, InputStream inStream) throws IOException {
		JarInputStream jarStream = new JarInputStream(inStream);
		JarEntry entry = jarStream.getNextJarEntry();
		if ("carPhysics".equals(entry.getName())) {
			new CarPhysicsCsvReader().read(repository, jarStream);
		}
	}
}
