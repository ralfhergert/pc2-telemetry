package de.ralfhergert.telemetry.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationCache {

	private List<NotificationListener> listeners = new ArrayList<>();
	private Map<Class<? extends Notification>,Notification> lastNotifications = new HashMap<>();

	public NotificationCache addListener(NotificationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		if (!listeners.contains(listener)) {
			sendNotification(new NotificationListenerAdding(this, listener));
			listeners.add(listener);
			// replay all last notification to the new listener.
			lastNotifications.values().forEach((notification -> listener.receiveNotification(this, notification)));
			sendNotification(new NotificationListenerAdded(this, listener));
		}
		return this;
	}

	public NotificationCache removeListener(NotificationListener listener) {
		if (listeners.contains(listener)) {
			sendNotification(new NotificationListenerRemoving(this, listener));
			listeners.remove(listener);
			sendNotification(new NotificationListenerRemoved(this, listener));
		}
		return this;
	}

	public NotificationCache sendNotification(Notification notification) {
		listeners.forEach((listener) -> listener.receiveNotification(this, notification));
		lastNotifications.put(notification.getClass(), notification);
		return this;
	}

	public Notification getLastNotification(final Class<? extends Notification> notificationClass) {
		if (notificationClass == null) {
			throw new IllegalArgumentException("notification class can not be null");
		}
		return lastNotifications.get(notificationClass);
	}
}
