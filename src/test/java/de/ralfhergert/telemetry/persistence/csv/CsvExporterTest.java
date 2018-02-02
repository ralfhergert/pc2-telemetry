package de.ralfhergert.telemetry.persistence.csv;

import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.pc2.datagram.v2.PacketTypes;
import de.ralfhergert.telemetry.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.ExactComparisonCriteria;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.stream.Stream;

/**
 * This test ensures that {@link CarPhysicsCsvReader} can parse what {@link CarPhysicsCsvWriter} created.
 */
public class CsvExporterTest {

	@Test
	public void testExportOfDefaultCarPhysicsPacket() throws IOException, IllegalAccessException, InvocationTargetException {
		final CarPhysicsPacket originalPacket = new CarPhysicsPacket();
		originalPacket.setReceivedDate(new Date());
		originalPacket.setPacketType(PacketTypes.CarPhysics);
		// write the packet onto a stream
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		new CarPhysicsCsvWriter().write(Stream.of(originalPacket), stream);
		Assert.assertFalse("something was written", stream.toString("UTF-8").isEmpty());
		// read the packet from the stream.
		ItemRepository<CarPhysicsPacket> repository = new ItemRepository<>();
		new CarPhysicsCsvReader().read(repository, new ByteArrayInputStream(stream.toByteArray()));
		Assert.assertEquals("number of packets in repository", 1, repository.getItemStream().count());
		final CarPhysicsPacket parsedPacket = repository.getItemStream().findFirst().get();
		// use reflection to iterate over all fields and compare them.
		for (Field field : originalPacket.getClass().getFields()) {
			if (field.getType().isArray()) {
				new ExactComparisonCriteria().arrayEquals("field " + field.getName() + " should be equal", field.get(originalPacket), field.get(parsedPacket));
			} else {
				Assert.assertEquals("field " + field.getName() + " should be equal", field.get(originalPacket), field.get(parsedPacket));
			}
		}
		for (Method method : originalPacket.getClass().getMethods()) {
			if (method.getName().startsWith("get")) {
				if (method.getReturnType().isArray()) {
					new ExactComparisonCriteria().arrayEquals("method " + method.getName() + " return value should be equal",
						method.invoke(originalPacket), method.invoke(parsedPacket));
				} else {
					Assert.assertEquals("method " + method.getName() + " return value should be equal", method.invoke(originalPacket), method.invoke(parsedPacket));
				}
			}
		}
	}
}
