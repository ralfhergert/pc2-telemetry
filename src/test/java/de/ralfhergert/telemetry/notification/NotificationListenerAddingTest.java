package de.ralfhergert.telemetry.notification;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link NotificationListenerAdding} is working correctly.
 */
public class NotificationListenerAddingTest {

	@Test
	public void testToStringStartsWithClassName() {
		Assert.assertTrue("toString should start with classname",
			new NotificationListenerAdding(null, null).toString().startsWith(NotificationListenerAdding.class.getSimpleName()));
	}
}
