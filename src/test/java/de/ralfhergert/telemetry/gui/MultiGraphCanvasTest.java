package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.repository.ItemRepository;
import org.junit.Test;

/**
 * This test ensures that {@link MultiGraphCanvas} is working correctly.
 */
public class MultiGraphCanvasTest {

	@Test(expected = IllegalArgumentException.class)
	public void testMultiGraphCanvasRejectsNullRepository() {
		new MultiGraphCanvas(null);
	}

	@Test
	public void testCreateMultiGraphCanvasOnEmptyRepository() {
		new MultiGraphCanvas(new ItemRepository<>());
	}
}
