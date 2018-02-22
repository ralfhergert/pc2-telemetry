package de.ralfhergert.telemetry.repository;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that the {@link RepositoryConnector} is working properly.
 */
public class RepositoryConnectorTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsSourceRepositoryIsRejected() {
		new RepositoryConnector<>(null, new ItemRepository<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsTargetRepositoryIsRejected() {
		new RepositoryConnector<>(new ItemRepository<>(), null);
	}

	@Test
	public void testOnConnectingItemsAlreadyInSourceRepositoryAreAddedToTargetRepository() {
		final Repository<Integer> sourceRepository = new ItemRepository<Integer>().addItem(7);
		final Repository<Integer> targetRepository = new ItemRepository<>();
		Assert.assertEquals("number of items in targetRepository", 0, targetRepository.getItemStream().count());
		new RepositoryConnector<>(sourceRepository, targetRepository);
		Assert.assertEquals("number of items in targetRepository", 1, targetRepository.getItemStream().count());
		Assert.assertEquals("item in targetRepository", 7, targetRepository.getItemStream().findFirst().get().intValue());
	}

	@Test
	public void testAfterConnectingRepositoriesItemsAreSyncedFromSourceToTargetRepository() {
		final Repository<Integer> sourceRepository = new ItemRepository<>();
		final Repository<Integer> targetRepository = new ItemRepository<>();
		new RepositoryConnector<>(sourceRepository, targetRepository);
		Assert.assertEquals("number of items in targetRepository", 0, targetRepository.getItemStream().count());
		sourceRepository.addItem(7);
		Assert.assertEquals("number of items in targetRepository", 1, targetRepository.getItemStream().count());
		Assert.assertEquals("item in targetRepository", 7, targetRepository.getItemStream().findFirst().get().intValue());
		sourceRepository.removeItem(7);
		Assert.assertEquals("number of items in targetRepository", 0, targetRepository.getItemStream().count());
	}
}
