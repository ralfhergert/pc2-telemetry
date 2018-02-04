package de.ralfhergert.telemetry.reflection;

import java.lang.reflect.Method;

/**
 * This accessor uses the given {@link Method} to access an objects value.
 */
public class MethodAccessor<Item, Value> implements Accessor<Item, Value> {

	private final Method method;

	public MethodAccessor(Method method) {
		if (method == null) {
			throw new IllegalArgumentException("method can not be null");
		}
		if (!method.isAccessible()) {
			throw new IllegalArgumentException("method must be accessible");
		}
		this.method = method;
	}

	@Override
	public Value getValue(Item o) {
		try {
			return (Value)method.invoke(o);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}
}
