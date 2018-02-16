package de.ralfhergert.telemetry.notification;

public class NotificationListenerAdding implements Notification {

	private final NotificationCache cache;
	private final NotificationListener listener;

	public NotificationListenerAdding(NotificationCache cache, NotificationListener listener) {
		this.cache = cache;
		this.listener = listener;
	}

	@Override
	public String toString() {
		return "NotificationListenerAdding{" +
			"cache=" + cache +
			", listener=" + listener +
			'}';
	}
}
