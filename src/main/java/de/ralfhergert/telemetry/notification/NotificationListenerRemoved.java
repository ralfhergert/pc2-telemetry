package de.ralfhergert.telemetry.notification;

public class NotificationListenerRemoved implements Notification {

	private final NotificationCache cache;
	private final NotificationListener listener;

	public NotificationListenerRemoved(NotificationCache cache, NotificationListener listener) {
		this.cache = cache;
		this.listener = listener;
	}

	@Override
	public String toString() {
		return "NotificationListenerRemoved{" +
			"cache=" + cache +
			", listener=" + listener +
			'}';
	}
}
