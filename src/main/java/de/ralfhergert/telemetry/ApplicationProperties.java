package de.ralfhergert.telemetry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * This class handles access to the properties holding default values
 * or settings for this Application.
 */
public class ApplicationProperties {

	private final Properties properties = new Properties();
	private final File propertiesFile;

	public ApplicationProperties(File propertiesFile) {
		// store the file to be able storing the properties later.
		this.propertiesFile = propertiesFile;
		// try to read the properties file.
		if (propertiesFile.exists()) {
			try {
				properties.load(new FileInputStream(propertiesFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	public void storeProperties() {
		// try to store the properties in the given file.
		try {
			// ensure the propertiesFile exists.
			propertiesFile.getParentFile().mkdirs();
			properties.store(new FileWriter(propertiesFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
