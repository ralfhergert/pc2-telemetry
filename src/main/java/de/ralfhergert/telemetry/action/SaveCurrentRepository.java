package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.Telemetry;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPackage;
import de.ralfhergert.telemetry.persistence.csv.CarPhysicsCsvWriter;
import de.ralfhergert.telemetry.repository.Repository;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * This action will prompt a save dialog. When approved the application's current
 * repository will be stored in the chosen file.
 */
public class SaveCurrentRepository extends AbstractAction {

	private Telemetry application;
	private Component parent;

	public SaveCurrentRepository(Telemetry application, Component parent) {
		super(ResourceBundle.getBundle("messages").getString("action.saveCurrentRepository.caption"));
		this.application = application;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Repository<CarPhysicsPackage> repository = application.getCurrentRepository();
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
		if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			try {
				JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(fileChooser.getSelectedFile()));
				jarStream.putNextEntry(new ZipEntry("carPhysics"));
				new CarPhysicsCsvWriter().write(repository.getItemStream(), jarStream);
				jarStream.close();
			} catch (IOException e) {
				// TODO prompt a warning.
			}
		}
	}
}
