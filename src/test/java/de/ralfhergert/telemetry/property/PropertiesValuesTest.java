package de.ralfhergert.telemetry.property;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test ensure that {@link PropertyValues} is working correctly.
 */
public class PropertiesValuesTest {

	@Test(expected = IllegalArgumentException.class)
	public void testAddingListenerForUnnamedProperty() {
		new PropertyValues().addListener(null, new StoringPropertyValueListener<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddingNullListenerForProperty() {
		new PropertyValues().addListener("foo", null);
	}

	@Test
	public void testAddingListenerForUnknownProperty() {
		final StoringPropertyValueListener<String> listener = new StoringPropertyValueListener<>();
		final PropertyValues values = new PropertyValues().addListener("foo", listener);
		Assert.assertEquals("value should be", null, (Object)values.getValue("foo", null));
		values.setValue("foo", "bar");
		Assert.assertEquals("value should be", "bar", values.getValue("foo", null));
		Assert.assertEquals("number of events received", 1, listener.getEvents().size());
	}

	@Test
	public void testAddingListenerToExistingProperty() {
		final StoringPropertyValueListener<String> listener = new StoringPropertyValueListener<>();
		final PropertyValues values = new PropertyValues()
			.setValue("foo", "one")
			.addListener("foo", listener);
		Assert.assertEquals("value should be", "one", values.getValue("foo", null));
		values.setValue("foo", "two");
		Assert.assertEquals("value should be", "two", values.getValue("foo", null));
		Assert.assertEquals("number of events received", 1, listener.getEvents().size());
	}

	@Test
	public void testListenerDoesNoLongerReceiveEventAfterBeingRemoved() {
		final StoringPropertyValueListener<String> listener = new StoringPropertyValueListener<>();
		final PropertyValues values = new PropertyValues()
			.addListener("foo", listener)
			.removeListener("foo", listener);
		// alter the value.
		values.setValue("foo", "bar");
		Assert.assertEquals("listener should have received no events", 0, listener.getEvents().size());
	}

	@Test
	public void testTryToRemoveListenerFromUnnamedProperty() {
		new PropertyValues().removeListener(null, new StoringPropertyValueListener<>());
	}

	@Test
	public void testTryToRemoveListenerFromUnknownProperty() {
		final StoringPropertyValueListener<String> listener = new StoringPropertyValueListener<>();
		final PropertyValues values = new PropertyValues().removeListener("foo", listener);
		// confirm the property does not exist.
		Assert.assertEquals("value should be", null, (Object)values.getValue("foo", null));
	}
}
