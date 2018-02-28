package de.ralfhergert.telemetry.notification;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link NotificationListenerRemoving} is working correctly.
 */
public class NotificationListenerRemovingTest {

	@Test
	public void testToStringStartsWithClassName() {
		Assert.assertTrue("toString should start with classname",
			new NotificationListenerRemoving(null, null).toString().startsWith(NotificationListenerRemoving.class.getSimpleName()));
	}
}
