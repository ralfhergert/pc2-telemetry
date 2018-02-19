package de.ralfhergert.telemetry.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * This test ensures that {@link MessageFormatter} is working correctly.
 */
public class MessageFormatterTest {

	@Test
	public void testHelloWorld() {
		final String result = new MessageFormatter().format("Hello, {name}!", Collections.singletonMap("name", "World"));
		Assert.assertNotNull("result should not be null", result);
		Assert.assertEquals("resulting string should be", "Hello, World!", result);
	}

	@Test
	public void testEncapsulatedHelloWorld() {
		final String result = new MessageFormatter().format("Hello, {{name}}!", Collections.singletonMap("{name}", "World"));
		Assert.assertNotNull("result should not be null", result);
		Assert.assertEquals("resulting string should be", "Hello, World!", result);
	}

	@Test
	public void testDoubledDelimiters() {
		final String result = new MessageFormatter("{{", "}}").format("Hello, {{name}}!", Collections.singletonMap("name", "World"));
		Assert.assertNotNull("result should not be null", result);
		Assert.assertEquals("resulting string should be", "Hello, World!", result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsStartDelimiterIsRejected() {
		new MessageFormatter(null, "}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullAsEndDelimiterIsRejected() {
		new MessageFormatter("{", null);
	}

	@Test()
	public void testNullMessageResultsInNullResponse() {
		final String result = new MessageFormatter().format(null, Collections.singletonMap("name", "ACME"));
		Assert.assertEquals("null message should result in null response", null, result);
	}

	@Test()
	public void testEmptyMessageResultsInEmptyResponse() {
		final String result = new MessageFormatter().format("", Collections.singletonMap("name", "ACME"));
		Assert.assertEquals("null message should result in null response", "", result);
	}

	@Test
	public void testNullPlaceholderValueIsNotInserted() {
		final String result = new MessageFormatter().format("Hello, {name}!", Collections.singletonMap("name", null));
		Assert.assertNotNull("result should not be null", result);
		Assert.assertEquals("resulting string should be", "Hello, {name}!", result);
	}

	@Test()
	public void testNullPlaceholderMapResultsGivenMessage() {
		final String result = new MessageFormatter().format("Foobar", null);
		Assert.assertEquals("null message should result in null response", "Foobar", result);
	}

	@Test()
	public void testEmptyPlaceholderMapResultsGivenMessage() {
		final String result = new MessageFormatter().format("Foobar", Collections.emptyMap());
		Assert.assertEquals("null message should result in null response", "Foobar", result);
	}
}
