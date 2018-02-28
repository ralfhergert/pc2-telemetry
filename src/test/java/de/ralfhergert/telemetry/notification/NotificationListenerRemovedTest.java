package de.ralfhergert.telemetry.notification;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link NotificationListenerRemoving} is working correctly.
 */
public class NotificationListenerRemovedTest {

	@Test
	public void testToStringStartsWithClassName() {
		Assert.assertTrue("toString should start with classname",
			new NotificationListenerRemoved(null, null).toString().startsWith(NotificationListenerRemoved.class.getSimpleName()));
	}
}
