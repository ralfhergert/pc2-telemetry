package de.ralfhergert.telemetry.notification;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link NotificationListenerAdded} is working correctly.
 */
public class NotificationListenerAddedTest {

	@Test
	public void testToStringStartsWithClassName() {
		Assert.assertTrue("toString should start with classname",
			new NotificationListenerAdded(null, null).toString().startsWith(NotificationListenerAdded.class.getSimpleName()));
	}
}
