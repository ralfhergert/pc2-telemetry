package de.ralfhergert.telemetry.repository;

import java.util.*;
import java.util.stream.Stream;

/**
 * An indexed repository creates an index over a delegate repository.
 */
public class IndexedRepository<Type> implements Repository<Type> {

	private final List<RepositoryListener<Type>> listeners = new ArrayList<>();
	private final TreeSet<Type> items;

	public IndexedRepository(Comparator<Type> comparator) {
		items = new TreeSet<>(comparator);
	}

	public IndexedRepository<Type> addListener(RepositoryListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener ca not be null");
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		return this;
	}

	public IndexedRepository<Type> removeListener(RepositoryListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener ca not be null");
		}
		listeners.remove(listener);
		return this;
	}

	public IndexedRepository<Type> addItem(Type item) {
		if (item == null) {
			throw new IllegalArgumentException("item can not be null");
		}
		if (items.add(item)) {
			listeners.forEach(listener -> listener.onItemAdded(this, item, items.lower(item), items.higher(item)));
		}
		return this;
	}

	public IndexedRepository<Type> removeItem(Type item) {
		if (item == null) {
			throw new IllegalArgumentException("item can not be null");
		}
		if (items.remove(item)) {
			listeners.forEach(listener -> listener.onItemRemoved(this, item));
		}
		return this;
	}

	public Stream<Type> getItemStream() {
		return items.stream();
	}
}
