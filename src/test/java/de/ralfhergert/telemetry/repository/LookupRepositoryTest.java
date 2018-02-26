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

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullAsListenerIsRejected() {
		new LookupRepository<>(String::valueOf).addListener(null);
	}

	@Test
	public void testAddingListener() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.addItem(3);
		Assert.assertEquals("number of add event received", 1, listener.getAddedEvents().size());
		Assert.assertEquals("object in add event", Integer.valueOf(3), listener.getAddedEvents().get(0).getItem());
	}

	@Test
	public void testAddingSameListenerTwice() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.addListener(listener) // try to add the same listener a second time
			.addItem(3);
		Assert.assertEquals("number of add event received", 1, listener.getAddedEvents().size());
		Assert.assertEquals("object in add event", Integer.valueOf(3), listener.getAddedEvents().get(0).getItem());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovingNullAsListenerIsRejected() {
		new LookupRepository<>(String::valueOf).removeListener(null);
	}

	@Test
	public void testRemovedListenerNoLongerReceivesEvents() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.removeListener(listener)
			.addItem(3);
		Assert.assertEquals("number of add event received", 0, listener.getAddedEvents().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullAsItemIsRejected() {
		new LookupRepository<String,Integer>(String::valueOf).addItem(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovingNullAsItemIsRejected() {
		new LookupRepository<String,Integer>(String::valueOf).removeItem(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovingItemWithNullAsKeyIsRejected() {
		new LookupRepository<String,Integer>(String::valueOf).removeItemByKey(null);
	}

	@Test
	public void testAddingSameItemASecondTimeIsIgnored() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.addItem(3)
			.addItem(3); // try to add the same item a second time
		Assert.assertEquals("number of add event received", 1, listener.getAddedEvents().size());
		Assert.assertEquals("object in add event", Integer.valueOf(3), listener.getAddedEvents().get(0).getItem());
	}

	@Test
	public void testRemovingItemFiresEvent() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.addItem(3)
			.removeItem(3);
		Assert.assertEquals("number of remove event received", 1, listener.getRemovedEvents().size());
		Assert.assertEquals("object in remove event", Integer.valueOf(3), listener.getRemovedEvents().get(0).getItem());
	}

	@Test
	public void testRemovingNonExistentItemNotFiresEvent() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.removeItem(3);
		Assert.assertEquals("number of remove event received", 0, listener.getRemovedEvents().size());
	}

	@Test
	public void testRemovingItemByKeyFiresEvent() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.addItem(3)
			.removeItemByKey("3");
		Assert.assertEquals("number of remove event received", 1, listener.getRemovedEvents().size());
		Assert.assertEquals("object in remove event", Integer.valueOf(3), listener.getRemovedEvents().get(0).getItem());
	}

	@Test
	public void testRemovingNonExistentItemByKeyNotFiresEvent() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new LookupRepository<String,Integer>(String::valueOf)
			.addListener(listener)
			.removeItemByKey("3");
		Assert.assertEquals("number of remove event received", 0, listener.getRemovedEvents().size());
	}
}
