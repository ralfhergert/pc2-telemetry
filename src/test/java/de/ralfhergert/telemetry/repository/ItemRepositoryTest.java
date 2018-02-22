package de.ralfhergert.telemetry.repository;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that the {@link ItemRepository} is working correctly.
 */
public class ItemRepositoryTest {

	@Test(expected = IllegalArgumentException.class)
	public void testTryingToAddNullListenerIsRejected() {
		new ItemRepository<Integer>().addListener(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTryingToRemoveNullListenerIsRejected() {
		new ItemRepository<Integer>().removeListener(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTryingToAddNullItemIsRejected() {
		new ItemRepository<Integer>().addItem(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTryingToRemoveNullItemIsRejected() {
		new ItemRepository<Integer>().removeItem(null);
	}

	@Test
	public void testListenersAreInformedWhenItemIsAdded() {
		final Integer item = 1984;
		final StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		final Repository<Integer> repository = new ItemRepository<Integer>().addListener(listener).addItem(item);
		Assert.assertEquals("number of added events listener received", 1, listener.getAddedEvents().size());
		{
			StoringRepositoryListener.ItemAddedEvent<Integer> event = listener.getAddedEvents().get(0);
			Assert.assertEquals("event should come from the correct repository", repository, event.getRepository());
			Assert.assertEquals("event should mention the correct item", item, event.getItem());
			Assert.assertEquals("event should have no item before", null, event.getItemBefore());
			Assert.assertEquals("event should have no item after", null, event.getItemAfter());
		}
		// clear the storing listener and add a second item.
		listener.clear();
		repository.addItem(2001);
		Assert.assertEquals("number of added events listener received", 1, listener.getAddedEvents().size());
		{
			StoringRepositoryListener.ItemAddedEvent<Integer> event = listener.getAddedEvents().get(0);
			Assert.assertEquals("event should come from the correct repository", repository, event.getRepository());
			Assert.assertEquals("event should mention the correct item", 2001, event.getItem().intValue());
			Assert.assertEquals("event should have correct item before", item, event.getItemBefore());
			Assert.assertEquals("event should have no item after", null, event.getItemAfter());
		}

	}

	@Test
	public void testListenersAreInformedWhenItemIsRemoved() {
		final Integer item = 1984;
		final StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		final Repository<Integer> repository = new ItemRepository<Integer>()
			.addListener(listener)
			.addItem(item)
			.removeItem(item);
		Assert.assertEquals("number of removed events listener received", 1, listener.getRemovedEvents().size());
		{
			StoringRepositoryListener.ItemRemovedEvent<Integer> event = listener.getRemovedEvents().get(0);
			Assert.assertEquals("event should come from the correct repository", repository, event.getRepository());
			Assert.assertEquals("event should mention the correct item", item, event.getItem());
		}
	}

	@Test
	public void testNoEventsAreFiredIfNonExistentItemIsTriedToBeRemoved() {
		final Integer item = 1984;
		final StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new ItemRepository<Integer>()
			.addListener(listener)
			.addItem(item)
			.removeItem(3);
		Assert.assertEquals("number of removed events listener received", 0, listener.getRemovedEvents().size());
	}

	@Test
	public void testAlreadyAttachedListenersCanNotBeAddedAgain() {
		final StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		new ItemRepository<Integer>()
			.addListener(listener)
			.addListener(listener) // try to add same listener twice
			.addItem(1984);
		// listener should still receive only one event.
		Assert.assertEquals("number of added events listener received", 1, listener.getAddedEvents().size());
	}

	@Test
	public void testListenerIsNoLongerInformedAfterBeingRemoved() {
		final StoringRepositoryListener<Integer> listener = new StoringRepositoryListener<>();
		final Repository<Integer> repository = new ItemRepository<Integer>()
			.addListener(listener)
			.addItem(1984);
		// confirm the listener is added correctly.
		Assert.assertEquals("number of added events listener received", 1, listener.getAddedEvents().size());
		listener.clear();
		repository.removeListener(listener).addItem(1749);
		// confirm the listener did receive an event
		Assert.assertEquals("number of added events listener received", 0, listener.getAddedEvents().size());
	}
}
