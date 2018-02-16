package de.ralfhergert.telemetry;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.gui.GraphRepositoryFactory;
import de.ralfhergert.telemetry.gui.MultiGraphCanvas;
import de.ralfhergert.telemetry.graph.NegativeOffsetAccessor;
import de.ralfhergert.telemetry.notification.Notification;
import de.ralfhergert.telemetry.notification.NotificationCache;
import de.ralfhergert.telemetry.pc2.UDPCaptureThread;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import de.ralfhergert.telemetry.repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Entry point into the telemetry suite.
 */
public class Telemetry {

	private final ApplicationProperties properties;

	private final NotificationCache notificationCache = new NotificationCache();

	private DatagramSocket socket = null;
	private IndexedRepository<CarPhysicsPacket> currentRepository;
	private Repository<LineGraph> graphRepository;

	private MultiGraphCanvas multiGraphCanvas;
	private UDPCaptureThread udpCaptureThread = null;

	public Telemetry() throws SocketException {
		properties = new ApplicationProperties(new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".PC2Telemetry", "defaults.ini"));

		currentRepository = new IndexedRepository<>((Comparator<CarPhysicsPacket>)
			(o1, o2) -> o1.getReceivedDate().compareTo(o2.getReceivedDate())
		);

		final NegativeOffsetAccessor<CarPhysicsPacket, Long> timeStampAccessor = (carPhysicsPacket, offset) -> {
			long timestamp = carPhysicsPacket.getReceivedDate().getTime();
			return (offset == null) ? timestamp : timestamp - offset;
		};

		graphRepository = new GraphRepositoryFactory().createLineGraphs(currentRepository, timeStampAccessor, new CarPhysicsPacket());
		multiGraphCanvas = new MultiGraphCanvas(graphRepository, Arrays.asList(
			Arrays.asList(
				"carPhysicsPacket.property.unfilteredThrottle",
				"carPhysicsPacket.property.unfilteredBrake",
				"carPhysicsPacket.property.unfilteredClutch",
				"carPhysicsPacket.property.throttle",
				"carPhysicsPacket.property.brake",
				"carPhysicsPacket.property.clutch",
				"carPhysicsPacket.property.handBrake"
			),
			Arrays.asList(
				"carPhysicsPacket.property.unfilteredSteering",
				"carPhysicsPacket.property.steering"
			),
			Arrays.asList(
				"carPhysicsPacket.property.speed"
			)
		));

		startCapturing();
	}

	public void startCapturing() {
		if (udpCaptureThread != null && udpCaptureThread.isRunning()) {
			return;
		}
		// try to create a socket and start listening
		try {
			udpCaptureThread = new UDPCaptureThread(createSocket(), currentRepository).start();
			notificationCache.sendNotification(new CaptureThreadNotification(true));
		} catch (SocketException se) {
			notificationCache.sendNotification(new CaptureThreadNotification(false));
		}
	}

	public void stopCapturing() {
		// stop and release the capture thread.
		if (udpCaptureThread != null) {
			if (udpCaptureThread.isRunning()) {
				udpCaptureThread.stop(); // this will just send a stop signal.
			}
			udpCaptureThread = null;
		}
		// close and release the DatagramSocket.
		if (socket != null) {
			socket.close(); /* closing the socket will create a SocketException in the
			 * UDPReceiver inside the UDPCaptureThread. */
			socket = null;
		}
		notificationCache.sendNotification(new CaptureThreadNotification(false));
	}

	public NotificationCache getNotificationCache() {
		return notificationCache;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	/**
	 * This method will either return the current socket, if it is still connected.
	 * Or else it try to create a new socket. This might fail due to resource conflicts.
	 */
	public DatagramSocket createSocket() throws SocketException {
		if (socket != null && socket.isConnected()) {
			return socket;
		}
		socket = new DatagramSocket(Integer.valueOf(properties.getProperty("projectCars2.udp.port", "5606")));
		return socket;
	}

	public IndexedRepository<CarPhysicsPacket> getCurrentRepository() {
		return currentRepository;
	}

	public MultiGraphCanvas getGraphCanvas() {
		return multiGraphCanvas;
	}

	public ApplicationProperties getProperties() {
		return properties;
	}

	/**
	 * This method performs last operation when shutting down the app.
	 */
	public void shutdown() {
		properties.storeProperties();
	}

	public static void main(String... args) throws IOException {
		tryToSetNimbusLookAndFeel();
		final Telemetry app = new Telemetry();
		final TelemetryFrame frame = new TelemetryFrame(app);

		frame.getContentPane().add(new JScrollPane(app.getGraphCanvas()), BorderLayout.CENTER);
		frame.setSize(720,480);
		frame.setVisible(true);
	}

	public static void tryToSetNimbusLookAndFeel() {
		try { // search for the Nimbus LnF and set it.
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					return;
				}
			}
		} catch (Exception e) {
			/* do nothing - stick to the default */
		}
	}

	public static class CaptureThreadNotification implements Notification {

		private boolean isCapturing;

		public CaptureThreadNotification(boolean isCapturing) {
			this.isCapturing = isCapturing;
		}

		public boolean isCapturing() {
			return isCapturing;
		}

		@Override
		public String toString() {
			return "CaptureThreadNotification{" +
				"isCapturing=" + isCapturing +
				'}';
		}
	}
}
