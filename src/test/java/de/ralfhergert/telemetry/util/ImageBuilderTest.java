package de.ralfhergert.telemetry.util;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * This test ensures that the {@link ImageBuilder} is working correctly.
 */
public class ImageBuilderTest {

	@Test
	public void testCreatedImageHasCorrectDimensions() {
		Image image = ImageBuilder.createSingleColoredImage(5, 7, null);
		Assert.assertNotNull("image should not be null", image);
		Assert.assertEquals("image has correct width", 5, image.getWidth(null));
		Assert.assertEquals("image has correct width", 7, image.getHeight(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatedImageWithZeroWidth() {
		ImageBuilder.createSingleColoredImage(0, 3, null);
		Assert.fail("width of 0 should be rejected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatedImageWithNegativeWidth() {
		ImageBuilder.createSingleColoredImage(-1, 3, null);
		Assert.fail("width of -1 should be rejected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatedImageWithZeroHeight() {
		ImageBuilder.createSingleColoredImage(3, 0, null);
		Assert.fail("height of 0 should be rejected");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatedImageWithNegativeHeight() {
		ImageBuilder.createSingleColoredImage(3, -1, null);
		Assert.fail("height of -1 should be rejected");
	}
}
