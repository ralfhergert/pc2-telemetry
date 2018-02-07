package de.ralfhergert.telemetry.repository;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link LookupRepository} is working correctly.
 */
public class LookupRepositoryTest {

	@Test
	public void testAddItem() {
		LookupRepository<String,Integer> repository = new LookupRepository<>(String::valueOf);
		Assert.assertEquals("number of items in repository", 0, repository.getItemStream().count());
		repository.addItem(42);
		Assert.assertEquals("number of items in repository", 1, repository.getItemStream().count());
	}

	@Test
	public void testItemRetrieval() {
		LookupRepository<String,Integer> repository = new LookupRepository<>(String::valueOf);
		repository.addItem(42);
		Assert.assertEquals("number of items in repository", 1, repository.getItemStream().count());
		Assert.assertEquals("retrieved item should be", Integer.valueOf(42), repository.getItem("42"));
	}

	@Test
	public void testItemRemovalByKey() {
		LookupRepository<String,Integer> repository = new LookupRepository<>(String::valueOf);
		repository.addItem(42);
		Assert.assertEquals("number of items in repository", 1, repository.getItemStream().count());
		repository.removeItemByKey("42");
		Assert.assertEquals("number of items in repository", 0, repository.getItemStream().count());
	}

	@Test
	public void testItemRemovalByValue() {
		LookupRepository<String,Integer> repository = new LookupRepository<>(String::valueOf);
		repository.addItem(42);
		Assert.assertEquals("number of items in repository", 1, repository.getItemStream().count());
		repository.removeItem(42);
		Assert.assertEquals("number of items in repository", 0, repository.getItemStream().count());
	}
}
