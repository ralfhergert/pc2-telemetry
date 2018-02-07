package de.ralfhergert.telemetry.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This message formatter allows to use messages with placeholders in it.
 * For instance: "Hello {name}!".
 * By default the placeholder delimiters are "{" and "}", but they can be
 * changed. A map use to consult it for the placeholder values.
 */
public class MessageFormatter {

	private final String delimiterStart;
	private final String delimiterEnd;

	public MessageFormatter() {
		this("{", "}");
	}

	public MessageFormatter(final String delimiterStart, final String delimiterEnd) {
		if (delimiterStart == null) {
			throw new IllegalArgumentException("delimiterStart cannot be null");
		}
		if (delimiterEnd == null) {
			throw new IllegalArgumentException("delimiterEnd cannot be null");
		}
		this.delimiterStart = delimiterStart;
		this.delimiterEnd = delimiterEnd;
	}

	/**
	 * This method will search the given message for placeholders. For each
	 * found placeholder the map is used to replace it with a value.
	 */
	public String format(final String message, Map<String,?> values) {
		if (values == null || values.isEmpty()) {
			return message;
		}
		// search for all placeholders in the given template and replace them. - This matcher allows only to take action for found placeholders.
		final Matcher matcher = Pattern.compile(Pattern.quote(delimiterStart) + "(" + String.join("|", values.keySet()) + ")" + Pattern.quote(delimiterEnd)).matcher(message);

		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String matchedPlaceholderKey = matcher.group(1);
			Object matchedPlaceholderValue = values.get(matchedPlaceholderKey);
			if (matchedPlaceholderValue != null) {
				matcher.appendReplacement(sb, Matcher.quoteReplacement(matchedPlaceholderValue.toString()));
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
