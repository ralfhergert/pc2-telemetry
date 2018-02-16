package de.ralfhergert.telemetry.notification;

public interface NotificationListener {

	void receiveNotification(NotificationCache cache, Notification notification);
}
