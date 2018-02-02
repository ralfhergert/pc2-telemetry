package de.ralfhergert.telemetry;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.graph.NegativeOffsetAccessor;
import de.ralfhergert.telemetry.gui.GraphCanvas;
import de.ralfhergert.telemetry.pc2.UDPListener;
import de.ralfhergert.telemetry.pc2.UDPReceiver;
import de.ralfhergert.telemetry.pc2.datagram.v2.BasePacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.PacketParser;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Comparator;

/**
 * Entry point into the telemetry suite.
 */
public class Telemetry {

	private static final Logger logger = LoggerFactory.getLogger(Telemetry.class);
	private static final PacketParser parser = new PacketParser();

	private final ApplicationProperties properties;

	private IndexedRepository<CarPhysicsPacket> currentRepository;
	private GraphCanvas<CarPhysicsPacket, Long, Short> graphCanvas;

	public Telemetry() throws SocketException {
		properties = new ApplicationProperties(new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".PC2Telemetry", "defaults.ini"));

		currentRepository = new IndexedRepository<>((Comparator<CarPhysicsPacket>)
			(o1, o2) -> o1.getReceivedDate().compareTo(o2.getReceivedDate())
		);

		final NegativeOffsetAccessor<CarPhysicsPacket, Long> timeStampAccessor = (carPhysicsPacket, offset) -> {
			long timestamp = carPhysicsPacket.getReceivedDate().getTime();
			return (offset == null) ? timestamp : timestamp - offset;
		};

		final LineGraph<CarPhysicsPacket, Long, Short> graphUnfilteredThrottle = new LineGraph<>(currentRepository, timeStampAccessor, (p) -> p.unfilteredThrottle).setProperty("color", Color.GREEN);
		final LineGraph<CarPhysicsPacket, Long, Short> graphThrottle = new LineGraph<>(currentRepository, timeStampAccessor, (p) -> p.throttle).setProperty("color", new Color(0f, 1f, 0f, 0.4f));
		final LineGraph<CarPhysicsPacket, Long, Short> graphUnfilteredBreak = new LineGraph<>(currentRepository, timeStampAccessor, (p) -> p.unfilteredBrake).setProperty("color", Color.RED);
		final LineGraph<CarPhysicsPacket, Long, Short> graphBrake = new LineGraph<>(currentRepository, timeStampAccessor, (p) -> p.brake).setProperty("color", new Color(1f, 0f, 0f, 0.4f));

		graphCanvas = new GraphCanvas<CarPhysicsPacket,Long,Short>()
			.addGraph(graphThrottle)
			.addGraph(graphUnfilteredThrottle)
			.addGraph(graphBrake)
			.addGraph(graphUnfilteredBreak);

		DatagramSocket socket = new DatagramSocket(5606);
		new Thread(new UDPReceiver(socket, new UDPListener() {
			@Override
			public void received(DatagramPacket packet) {
				BasePacket basePacket = parser.parse(packet);
				if (basePacket == null) {
					logger.info("Received unknown message: {}", new BasePacket(packet.getData()));
				} else if (basePacket instanceof CarPhysicsPacket) {
					CarPhysicsPacket carPhysicsPacket = (CarPhysicsPacket) basePacket;
					currentRepository.addItem(carPhysicsPacket);
				}
			}
		})).start();
	}

	public IndexedRepository<CarPhysicsPacket> getCurrentRepository() {
		return currentRepository;
	}

	public GraphCanvas<CarPhysicsPacket, Long, Short> getGraphCanvas() {
		return graphCanvas;
	}

	/**
	 * This method performs last operation when shutting down the app.
	 */
	public void shutdown() {
		properties.storeProperties();
	}

	public static void main(String... args) throws IOException {
		final Telemetry app = new Telemetry();
		final TelemetryFrame frame = new TelemetryFrame(app);

		frame.getContentPane().add(new JScrollPane(app.getGraphCanvas()), BorderLayout.CENTER);
		frame.setSize(720,480);
		frame.setVisible(true);
	}
}
