package de.ralfhergert.telemetry.property;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensures that {@link PropertyValue} is working correctly.
 */
public class PropertyValueTest {

	@Test(expected = IllegalArgumentException.class)
	public void testPropertyValueRejectsAddingListener() {
		new PropertyValue<>(0, Integer::compareTo).addListener(null);
	}

	@Test
	public void testPropertyValueIgnoresRemovingNullListener() {
		new PropertyValue<>(0, Integer::compareTo).removeListener(null);
	}

	@Test
	public void testPropertyValueShouldAlwaysFireIfNoComparatorWasGiven() {
		final StoringPropertyValueListener<Integer> listener = new StoringPropertyValueListener<>();
		new PropertyValue<>(0)
			.addListener(listener)
			.setValue(0); // set the value again to 0 -an event should have been fired
		Assert.assertEquals("number of events fired", 1, listener.getEvents().size());
	}

	@Test
	public void testPropertyValueShouldUseComparatorIfGiven() {
		final StoringPropertyValueListener<Integer> listener = new StoringPropertyValueListener<>();
		final PropertyValue<Integer> propertyValue = new PropertyValue<>(0, Integer::compareTo)
			.addListener(listener)
			.setValue(0); // set the value again to 0 - no event should have been fired
		Assert.assertEquals("number of events fired", 0, listener.getEvents().size());
		propertyValue.setValue(1);
		Assert.assertEquals("number of events fired", 1, listener.getEvents().size());
	}
}
