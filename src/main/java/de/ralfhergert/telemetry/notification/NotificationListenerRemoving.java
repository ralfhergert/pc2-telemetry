package de.ralfhergert.telemetry.notification;

public class NotificationListenerRemoving implements Notification {

	private final NotificationCache cache;
	private final NotificationListener listener;

	public NotificationListenerRemoving(NotificationCache cache, NotificationListener listener) {
		this.cache = cache;
		this.listener = listener;
	}

	@Override
	public String toString() {
		return "NotificationListenerRemoving{" +
			"cache=" + cache +
			", listener=" + listener +
			'}';
	}
}
