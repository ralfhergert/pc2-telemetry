package de.ralfhergert.telemetry.repository;

import de.ralfhergert.telemetry.reflection.Accessor;

import java.util.*;
import java.util.stream.Stream;

/**
 * An indexed repository creates an index over a delegate repository.
 */
public class LookupRepository<Key, Type> implements Repository<Type> {

	private final Accessor<Type, Key> accessor;

	private final List<RepositoryListener<Type>> listeners = new ArrayList<>();
	private final Map<Key, Type> items = new HashMap<>();

	public LookupRepository(Accessor<Type, Key> accessor) {
		this.accessor = accessor;
	}

	public LookupRepository<Key,Type> addListener(RepositoryListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		return this;
	}

	public LookupRepository<Key,Type> removeListener(RepositoryListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		listeners.remove(listener);
		return this;
	}

	public LookupRepository<Key,Type> addItem(Type item) {
		if (item == null) {
			throw new IllegalArgumentException("item can not be null");
		}
		if (items.containsValue(item)) {
			items.put(accessor.getValue(item), item);
			listeners.forEach(listener -> listener.onItemAdded(this, item, null, null));
		}
		return this;
	}

	public LookupRepository<Key,Type> removeItem(Type item) {
		if (item == null) {
			throw new IllegalArgumentException("item can not be null");
		}
		if (items.remove(accessor.getValue(item), item)) {
			listeners.forEach(listener -> listener.onItemRemoved(this, item));
		}
		return this;
	}

	public Stream<Type> getItemStream() {
		return items.values().stream();
	}

	public Type getItem(Key key) {
		return items.get(key);
	}
}
