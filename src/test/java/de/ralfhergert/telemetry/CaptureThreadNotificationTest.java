package de.ralfhergert.telemetry;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensure that {@link de.ralfhergert.telemetry.Telemetry.CaptureThreadNotification} is working correctly.
 */
public class CaptureThreadNotificationTest {

	@Test
	public void testCapturingStateIsTransported() {
		Assert.assertEquals("isCapturing should be", true, new Telemetry.CaptureThreadNotification(true).isCapturing());
	}

	@Test
	public void testNonCapturingStateIsTransported() {
		Assert.assertEquals("isCapturing should be", false, new Telemetry.CaptureThreadNotification(false).isCapturing());
	}

	@Test
	public void testToStringStartWithClassName() {
		Assert.assertTrue("toString should start with classname", new Telemetry.CaptureThreadNotification(true).toString().startsWith(Telemetry.CaptureThreadNotification.class.getSimpleName()));
	}
}
