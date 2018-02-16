package de.ralfhergert.telemetry.pc2;

import de.ralfhergert.telemetry.pc2.datagram.v2.BasePacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.PacketParser;
import de.ralfhergert.telemetry.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This class will create a new {@link UDPReceiver} upon the given socket.
 * Creation will fail if the socket is not connected.
 */
public class UDPCaptureThread {

	private static final Logger logger = LoggerFactory.getLogger(UDPCaptureThread.class);
	private static final PacketParser parser = new PacketParser();

	private DatagramSocket socket;
	private Repository<CarPhysicsPacket> carPhysicsPacketRepository;
	private UDPReceiver udpReceiver;

	public UDPCaptureThread(final DatagramSocket socket, Repository<CarPhysicsPacket> carPhysicsPacketRepository) {
		if (socket == null) {
			throw new IllegalArgumentException("socket can not be null");
		}
		if (carPhysicsPacketRepository == null) {
			throw new IllegalArgumentException("carPhysicsPacketRepository can not be null");
		}
		this.socket = socket;
		this.carPhysicsPacketRepository = carPhysicsPacketRepository;
	}

	public UDPCaptureThread start() {
		udpReceiver = new UDPReceiver(socket, new UDPListener() {
			@Override
			public void received(DatagramPacket packet) {
				BasePacket basePacket = parser.parse(packet);
				if (basePacket == null) {
					logger.info("Received unknown message: {}", new BasePacket(packet.getData()));
				} else if (basePacket instanceof CarPhysicsPacket) {
					CarPhysicsPacket carPhysicsPacket = (CarPhysicsPacket) basePacket;
					carPhysicsPacketRepository.addItem(carPhysicsPacket);
				}
			}
		});
		new Thread(udpReceiver).start();
		return this;
	}

	public boolean isRunning() {
		return udpReceiver != null && udpReceiver.isRunning();
	}

	public UDPCaptureThread stop() {
		if (isRunning()) {
			udpReceiver.stop();
		}
		return this;
	}

	public Repository<CarPhysicsPacket> getCarPhysicsPacketRepository() {
		return carPhysicsPacketRepository;
	}

	public UDPCaptureThread setCarPhysicsPacketRepository(Repository<CarPhysicsPacket> carPhysicsPacketRepository) {
		this.carPhysicsPacketRepository = carPhysicsPacketRepository;
		return this;
	}
}
