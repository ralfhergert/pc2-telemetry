package de.ralfhergert.telemetry.repository;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that the {@link IndexedRepository} is working correctly.
 */
public class IndexedRepositoryTest {

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullAsListenerIsRejected() {
		new IndexedRepository<>(Integer::compareTo).addListener(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovingNullAsListenerIsRejected() {
		new IndexedRepository<>(Integer::compareTo).removeListener(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullAsItemIsRejected() {
		new IndexedRepository<>(Integer::compareTo).addItem(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovingNullAsItemIsRejected() {
		new IndexedRepository<>(Integer::compareTo).removeItem(null);
	}

	@Test
	public void testAddingAnItem() {
		Repository<Integer> repository = new IndexedRepository<>(Integer::compareTo)
			.addItem(53);
		Assert.assertEquals("number of items in repository", 1, repository.getItemStream().count());
	}

	@Test
	public void testRemovingAnItem() {
		Repository<Integer> repository = new IndexedRepository<>(Integer::compareTo)
			.addItem(53)
			.removeItem(53);
		Assert.assertEquals("number of items in repository", 0, repository.getItemStream().count());
	}

	@Test
	public void testWhenAddingItemAnEventIsFired() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addListener(listener)
			.addItem(7);
		Assert.assertEquals("number of add events sent", 1, listener.getAddedEvents().size());
		Assert.assertEquals("object in event sent", Integer.valueOf(7), listener.getAddedEvents().get(0).getItem());
		Assert.assertEquals("object in event sent", null, listener.getAddedEvents().get(0).getItemBefore());
		Assert.assertEquals("object in event sent", null, listener.getAddedEvents().get(0).getItemAfter());
	}

	@Test
	public void testAddingSameListenerASecondTimeIsIgnored() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addListener(listener)
			.addListener(listener)
			.addItem(7);
		Assert.assertEquals("number of add events sent", 1, listener.getAddedEvents().size());
	}

	@Test
	public void testListenerReceivesNoMoreEventAfterBeingRemoved() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addListener(listener)
			.removeListener(listener)
			.addItem(7);
		Assert.assertEquals("number of add events received", 0, listener.getAddedEvents().size());
	}

	@Test
	public void testEventIsFiredOnRemovingItem() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addListener(listener)
			.addItem(7)
			.removeItem(7);
		Assert.assertEquals("number of remove events received", 1, listener.getRemovedEvents().size());
		Assert.assertEquals("object in event received", Integer.valueOf(7), listener.getRemovedEvents().get(0).getItem());
	}

	@Test
	public void testNoEventIsFiredOnRemovingNonExistentItem() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addListener(listener)
			.addItem(7)
			.removeItem(84);
		Assert.assertEquals("number of remove events received", 0, listener.getRemovedEvents().size());
	}

	@Test
	public void testAddingSameItemASecondTimeDoesNoTriggerEvents() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addItem(7)
			.addListener(listener)
			.addItem(7);
		Assert.assertEquals("number of add events received", 0, listener.getAddedEvents().size());
	}

	@Test
	public void testAddingItemAfterExistingItem() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addItem(7)
			.addListener(listener)
			.addItem(8);
		Assert.assertEquals("number of add events received", 1, listener.getAddedEvents().size());
		Assert.assertEquals("item in event", Integer.valueOf(8), listener.getAddedEvents().get(0).getItem());
		Assert.assertEquals("item before added item", Integer.valueOf(7), listener.getAddedEvents().get(0).getItemBefore());
		Assert.assertEquals("item after added item", null, listener.getAddedEvents().get(0).getItemAfter());

	}

	@Test
	public void testAddingItemBeforeExistingItem() {
		StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new IndexedRepository<>(Integer::compareTo)
			.addItem(7)
			.addListener(listener)
			.addItem(3);
		Assert.assertEquals("number of add events received", 1, listener.getAddedEvents().size());
		Assert.assertEquals("item in event", Integer.valueOf(3), listener.getAddedEvents().get(0).getItem());
		Assert.assertEquals("item before added item", null, listener.getAddedEvents().get(0).getItemBefore());
		Assert.assertEquals("item after added item", Integer.valueOf(7), listener.getAddedEvents().get(0).getItemAfter());

	}
}
