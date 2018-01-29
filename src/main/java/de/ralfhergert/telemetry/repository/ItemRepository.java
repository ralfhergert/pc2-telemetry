package de.ralfhergert.telemetry.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A repository is a store for a set of items.
 */
public class ItemRepository<Type> implements Repository<Type> {

	private final List<RepositoryListener<Type>> listeners = new ArrayList<>();
	private final List<Type> items = new ArrayList<>();

	public ItemRepository<Type> addListener(RepositoryListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener ca not be null");
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		return this;
	}

	public ItemRepository<Type> removeListener(RepositoryListener<Type> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener ca not be null");
		}
		listeners.remove(listener);
		return this;
	}

	public ItemRepository<Type> addItem(Type item) {
		if (item == null) {
			throw new IllegalArgumentException("item can not be null");
		}
		if (items.add(item)) {
			int index = items.indexOf(item);
			Type itemBefore = index == 0 ? null : items.get(index - 1);
			Type itemAfter = index == items.size() - 1 ? null : items.get(index + 1);
			listeners.forEach(listener -> listener.onItemAdded(this, item, itemBefore, itemAfter));
		}
		return this;
	}

	public ItemRepository<Type> removeItem(Type item) {
		if (item == null) {
			throw new IllegalArgumentException("item can not be null");
		}
		if (items.remove(item)) {
			listeners.forEach(listener -> listener.onItemRemoved(this, item));
		}
		return this;
	}

	@Override
	public Stream<Type> getItemStream() {
		return items.stream();
	}
}
