package de.ralfhergert.telemetry.persistence.csv;

import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPackage;
import de.ralfhergert.telemetry.pc2.datagram.v2.PackageTypes;
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
	public void testExportOfDefaultCarPhysicsPackage() throws IOException {
		final CarPhysicsPackage packet = new CarPhysicsPackage();
		packet.setReceivedDate(new Date());
		packet.setPacketType(PackageTypes.CarPhysics);
		// write the packet onto a stream
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		new CarPhysicsCsvWriter().write(Stream.of(packet), stream);
		Assert.assertFalse("something was written", stream.toString("UTF-8").isEmpty());
		// read the packet from the stream.
		ItemRepository<CarPhysicsPackage> repository = new ItemRepository<>();
		new CarPhysicsCsvReader().read(repository, new ByteArrayInputStream(stream.toByteArray()));
		Assert.assertEquals("number of packets in repository", 1, repository.getItemStream().count());
		Assert.assertEquals("packet received date", packet.getReceivedDate(), repository.getItemStream().findFirst().get().getReceivedDate());
	}
}
