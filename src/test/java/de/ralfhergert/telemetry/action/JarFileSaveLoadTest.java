package de.ralfhergert.telemetry.action;

import de.ralfhergert.telemetry.ReflectiveComparator;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.PacketTypes;
import de.ralfhergert.telemetry.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * This test ensures that {@link LoadCurrentRepositoryAction} can read the JarFiles
 * produced by {@link LoadCurrentRepositoryAction}. This test wants to focus on the
 * JarFiles mainly. If something is wrong with the CSV format then have a look
 * into {@link de.ralfhergert.telemetry.persistence.csv.CsvExporterTest}.
 */
public class JarFileSaveLoadTest {

	@Test
	public void testLoadActionCanReadJarFileWrittenBySaveAction() throws IOException {
		final CarPhysicsPacket originalPacket = new CarPhysicsPacket();
		originalPacket.setPartialPacketNumber((short)1234);
		originalPacket.setReceivedDate(new Date());
		originalPacket.setPacketType(PacketTypes.CarPhysics);
		// create a repository.
		ItemRepository<CarPhysicsPacket> saveRepository = new ItemRepository<CarPhysicsPacket>().addItem(originalPacket);
		// we want to write the jarFile onto a ByteArrayOutputStream.
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// use the SaveAction to write a jarFile.
		SaveCurrentRepositoryAction.writeAsJarFile(saveRepository, outStream);
		// load from the written bytes.
		ItemRepository<CarPhysicsPacket> loadRepository = new ItemRepository<>();
		LoadCurrentRepositoryAction.readAsJarFile(loadRepository, new ByteArrayInputStream(outStream.toByteArray()));
		Assert.assertNotNull("saveRepository should not be null", saveRepository);
		Assert.assertNotNull("loadRepository should not be null", loadRepository);
		Assert.assertEquals("both repositories have the same amount of packets", saveRepository.getItemStream().count(), loadRepository.getItemStream().count());
		Assert.assertTrue("packets should be equal", ReflectiveComparator.equal(saveRepository.getItemStream().findFirst().get(), loadRepository.getItemStream().findFirst().get()));
	}
}
