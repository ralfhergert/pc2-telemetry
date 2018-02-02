package de.ralfhergert.telemetry.persistence.csv;

import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.PacketTypes;
import de.ralfhergert.telemetry.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Stream;

/**
 * This test ensures that {@link CarPhysicsCsvReader} can parse what {@link CarPhysicsCsvWriter} created.
 */
public class CsvExporterTest {

	@Test
	public void testExportOfDefaultCarPhysicsPacket() throws IOException {
		final CarPhysicsPacket packet = new CarPhysicsPacket();
		packet.setReceivedDate(new Date());
		packet.setPacketType(PacketTypes.CarPhysics);
		// write the packet onto a stream
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		new CarPhysicsCsvWriter().write(Stream.of(packet), stream);
		Assert.assertFalse("something was written", stream.toString("UTF-8").isEmpty());
		// read the packet from the stream.
		ItemRepository<CarPhysicsPacket> repository = new ItemRepository<>();
		new CarPhysicsCsvReader().read(repository, new ByteArrayInputStream(stream.toByteArray()));
		Assert.assertEquals("number of packets in repository", 1, repository.getItemStream().count());
		Assert.assertEquals("packet received date", packet.getReceivedDate(), repository.getItemStream().findFirst().get().getReceivedDate());
	}
}
