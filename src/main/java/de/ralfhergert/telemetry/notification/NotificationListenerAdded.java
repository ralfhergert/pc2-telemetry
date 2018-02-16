package de.ralfhergert.telemetry.notification;

public class NotificationListenerAdded implements Notification {

	private final NotificationCache cache;
	private final NotificationListener listener;

	public NotificationListenerAdded(NotificationCache cache, NotificationListener listener) {
		this.cache = cache;
		this.listener = listener;
	}

	@Override
	public String toString() {
		return "NotificationListenerAdded{" +
			"cache=" + cache +
			", listener=" + listener +
			'}';
	}
}
