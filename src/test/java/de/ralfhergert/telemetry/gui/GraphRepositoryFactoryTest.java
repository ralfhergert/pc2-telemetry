package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.graph.NegativeOffsetAccessor;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import de.ralfhergert.telemetry.repository.Repository;
import org.junit.Assert;
import org.junit.Test;

/**
 * Ensures that {@link GraphRepositoryFactory} is working correctly.
 */
public class GraphRepositoryFactoryTest {

	@Test
	public void testRepositoryCreation() {
		IndexedRepository<CarPhysicsPacket> packetRepository = new IndexedRepository<>((p1, p2) -> p2.getPacketNumber() - p1.getPacketNumber());
		final NegativeOffsetAccessor<CarPhysicsPacket, Long> packetNumberAccessor = (carPhysicsPacket, offset) -> {
			long number = carPhysicsPacket.getPacketNumber();
			return (offset == null) ? number : number - offset;
		};
		Repository<LineGraph> lineGraphRepository = new GraphRepositoryFactory().createLineGraphs(packetRepository, packetNumberAccessor, null);
		Assert.assertNotNull("lineGraphRepository should not be null", lineGraphRepository);
		Assert.assertTrue("lineGraphRepository should not be empty", lineGraphRepository.getItemStream().count() > 0);
		Assert.assertTrue("every line graph has a name", lineGraphRepository.getItemStream().allMatch((graph) -> graph.getProperty("name", null) != null));
		Assert.assertTrue("every line graph has a color", lineGraphRepository.getItemStream().allMatch((graph) -> graph.getProperty("color", null) != null));
	}
}
