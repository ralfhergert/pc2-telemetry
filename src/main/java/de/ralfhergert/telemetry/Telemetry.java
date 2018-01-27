package de.ralfhergert.telemetry;

import de.ralfhergert.telemetry.graph.GraphValue;
import de.ralfhergert.telemetry.gui.ColoredGraph;
import de.ralfhergert.telemetry.gui.GraphCanvas;
import de.ralfhergert.telemetry.pc2.UDPListener;
import de.ralfhergert.telemetry.pc2.UDPReceiver;
import de.ralfhergert.telemetry.pc2.datagram.v2.BasePackage;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPackage;
import de.ralfhergert.telemetry.pc2.datagram.v2.PackageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Entry point into the telemetry suite.
 */
public class Telemetry {

	private static final Logger logger = LoggerFactory.getLogger(Telemetry.class);
	private static final PackageParser parser = new PackageParser();

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

	public static void main(String... args) throws IOException {
		final Telemetry app = new Telemetry();

		final JFrame frame = new JFrame("Racing Telemetry");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.shutdown();
			}
		});

		final ColoredGraph<Integer,Double> graph = new ColoredGraph<>(Integer::compareTo, Double::compareTo);
		final GraphCanvas<Integer,Double> canvas = new GraphCanvas<Integer,Double>().setGraph(graph);

		DatagramSocket socket = new DatagramSocket(5606);
		new Thread(new UDPReceiver(socket, new UDPListener() {
			@Override
			public void received(DatagramPacket packet) {
				BasePackage basePackage = parser.parse(packet);
				if (basePackage == null) {
					logger.info("Received unknown message: {}", new BasePackage(packet.getData()));
				} else if (basePackage instanceof CarPhysicsPackage) {
					CarPhysicsPackage carPhysicsPackage = (CarPhysicsPackage)basePackage;
					graph.addValue(new GraphValue<>(graph.getValues().size(), (double)carPhysicsPackage.tyreRPS[0]));
				}

			}
		})).start();

		frame.getContentPane().add(new JScrollPane(canvas), BorderLayout.CENTER);
		frame.setSize(720,480);
		frame.setVisible(true);
	}
}
