package de.ralfhergert.telemetry.notification;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This test ensures that {@link NotificationCache} is working correctly.
 */
public class NotificationCacheTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsNotificationListenerIsRejected() {
		new NotificationCache().addListener(null);
	}

	@Test
	public void testAnAddedNotificationListenerReceivesTheAddedNotification() {
		final StoringNotificationListener storingListener = new StoringNotificationListener();
		final NotificationCache cache = new NotificationCache().addListener(storingListener);
		Assert.assertNotNull("cache should not be null", cache);
		Assert.assertFalse("listener received at least one notification", storingListener.getNotifications().isEmpty());
		final StoringNotificationListener.NotificationFromCache lastNotification = storingListener.getNotifications().get(storingListener.getNotifications().size() - 1);
		Assert.assertNotNull("last notification is not null", lastNotification);
		Assert.assertEquals("last notification came from cache", cache, lastNotification.getCache());
		Assert.assertEquals("last notification is instance of", NotificationListenerAdded.class, lastNotification.getNotification().getClass());
		Assert.assertEquals("send notification should state out this cache", cache, ((NotificationListenerAdded)lastNotification.getNotification()).getCache());
		Assert.assertEquals("send notification should state out this listener", storingListener, ((NotificationListenerAdded)lastNotification.getNotification()).getListener());
	}

	@Test
	public void testNotificationListenerReceivesItsOwnRemovingNotification() {
		final StoringNotificationListener storingListener = new StoringNotificationListener();
		final NotificationCache cache = new NotificationCache().addListener(storingListener).removeListener(storingListener);
		Assert.assertNotNull("cache should not be null", cache);
		Assert.assertFalse("listener received at least one notification", storingListener.getNotifications().isEmpty());
		final StoringNotificationListener.NotificationFromCache lastNotification = storingListener.getNotifications().get(storingListener.getNotifications().size() - 1);
		Assert.assertNotNull("last notification is not null", lastNotification);
		Assert.assertEquals("last notification came from cache", cache, lastNotification.getCache());
		Assert.assertEquals("last notification is instance of", NotificationListenerRemoving.class, lastNotification.getNotification().getClass());
		Assert.assertEquals("send notification should state out this cache", cache, ((NotificationListenerRemoving)lastNotification.getNotification()).getCache());
		Assert.assertEquals("send notification should state out this listener", storingListener, ((NotificationListenerRemoving)lastNotification.getNotification()).getListener());
	}

	/**
	 * The {@link NotificationCache} stores the last notification of each type.
	 * This test checks that feature.
	 */
	@Test
	public void testLastNotificationAfterAddingAListener() {
		final NotificationCache cache = new NotificationCache();
		Assert.assertEquals("cache is empty, no AddedNotification should exist", null, cache.getLastNotification(NotificationListenerAdded.class));
		cache.addListener(new StoringNotificationListener());
		Notification addedNotification = cache.getLastNotification(NotificationListenerAdded.class);
		Assert.assertNotNull("added notification should not be null", addedNotification);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAskingForNullAsLastNotificationTypeIsRejected() {
		new NotificationCache().getLastNotification(null);
	}

	@Test
	public void testTryingToRemoveNullAsListenerDoesNotCreateAnyEvents() {
		final StoringNotificationListener storingListener = new StoringNotificationListener();
		final NotificationCache cache = new NotificationCache().addListener(storingListener);
		// clear the storing listener.
		storingListener.clear();
		// try to remove null.
		cache.removeListener(null);
		Assert.assertEquals("no events should have been generated", 0, storingListener.getNotifications().size());
	}

	@Test
	public void testWhenAddedAListenerAllLastNotificationsAreBeingReplayed() {
		class FooNotification implements Notification {
			private final int id;

			public FooNotification(int id) {
				this.id = id;
			}

			public int getId() {
				return id;
			}
		}
		// create a cache and send the foo notification twice.
		final NotificationCache cache = new NotificationCache()
			.sendNotification(new FooNotification(1))
			.sendNotification(new FooNotification(2));
		final StoringNotificationListener storingListener = new StoringNotificationListener();
		cache.addListener(storingListener);
		// confirm that exactly one FooNotification was played back and that it was the second one.
		final List<StoringNotificationListener.NotificationFromCache> receivedFooNotifications =
			storingListener.getNotifications().stream()
				.filter(notification -> notification.getNotification().getClass() == FooNotification.class)
				.collect(Collectors.toList());
		Assert.assertEquals("number of FooNotification", 1, receivedFooNotifications.size());
		Assert.assertEquals("id of notification should be", 2, ((FooNotification)receivedFooNotifications.get(0).getNotification()).getId());
	}

	@Test
	public void testTryingToAddSameListenerTwiceIsIgnored() {
		final StoringNotificationListener storingListener = new StoringNotificationListener();
		final NotificationCache cache = new NotificationCache().addListener(storingListener);
		// clear the storingListener
		storingListener.clear();
		// try to add the listener a second time.
		cache.addListener(storingListener);
		Assert.assertEquals("no notification should have been fired", 0, storingListener.getNotifications().size());
	}
}
