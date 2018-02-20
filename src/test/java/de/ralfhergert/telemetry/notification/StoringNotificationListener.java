package de.ralfhergert.telemetry.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This {@link NotificationListener} stores all notification it receives.
 * It is mainly used for testing and debugging purposes.
 */
public class StoringNotificationListener implements NotificationListener {

	private final List<NotificationFromCache> notifications = new ArrayList<>();

	@Override
	public void receiveNotification(NotificationCache cache, Notification notification) {
		notifications.add(new NotificationFromCache(cache, notification));
	}

	public List<NotificationFromCache> getNotifications() {
		return Collections.unmodifiableList(notifications);
	}

	public StoringNotificationListener clear() {
		notifications.clear();
		return this;
	}

	public static class NotificationFromCache {

		private final NotificationCache cache;
		private final Notification notification;

		public NotificationFromCache(NotificationCache cache, Notification notification) {
			this.cache = cache;
			this.notification = notification;
		}

		public NotificationCache getCache() {
			return cache;
		}

		public Notification getNotification() {
			return notification;
		}

		@Override
		public String toString() {
			return "NotificationFromCache{" +
				"cache=" + cache +
				", notification=" + notification +
				'}';
		}
	}
}
