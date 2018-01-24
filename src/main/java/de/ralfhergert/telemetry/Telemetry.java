package de.ralfhergert.telemetry;

import de.ralfhergert.telemetry.graph.Graph;
import de.ralfhergert.telemetry.graph.GraphValue;
import de.ralfhergert.telemetry.gui.GraphCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Random;

/**
 * Entry point into the telemetry suite.
 */
public class Telemetry {

	private final ApplicationProperties properties;

	public Telemetry() {
		properties = new ApplicationProperties(new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".PC2Telemetry", "defaults.ini"));
	}

	/**
	 * This method performs last operation when shutting down the app.
	 */
	public void shutdown() {
		properties.storeProperties();
	}

	public static void main(String... args) {
		final Telemetry app = new Telemetry();

		final JFrame frame = new JFrame("Racing Telemetry");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.shutdown();
			}
		});

		final Graph<Integer,Integer> graph = new Graph<>(Integer::compareTo, Integer::compareTo);
		final GraphCanvas<Integer,Integer> canvas = new GraphCanvas<Integer,Integer>().setGraph(graph);

		final Random random = new Random();

		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 800; i++) {
					graph.addValue(new GraphValue<>(i, random.nextInt(200)));
				}
			}
		}.start();

		frame.getContentPane().add(new JScrollPane(canvas), BorderLayout.CENTER);
		frame.setSize(720,480);
		frame.setVisible(true);
	}
}
