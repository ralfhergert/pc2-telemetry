package de.ralfhergert.telemetry.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoringRepositoryListener<Type> implements RepositoryListener<Type> {

	private final List<ItemAddedEvent<Type>> addedEvents = new ArrayList<>();
	private final List<ItemRemovedEvent<Type>> removedEvents = new ArrayList<>();

	@Override
	public void onItemAdded(Repository<Type> repository, Type item, Type itemBefore, Type itemAfter) {
		addedEvents.add(new ItemAddedEvent<>(repository, item, itemBefore, itemAfter));
	}

	@Override
	public void onItemRemoved(Repository<Type> repository, Type item) {
		removedEvents.add(new ItemRemovedEvent<>(repository, item));
	}

	public void clear() {
		addedEvents.clear();
		removedEvents.clear();
	}

	public List<ItemAddedEvent<Type>> getAddedEvents() {
		return Collections.unmodifiableList(addedEvents);
	}

	public List<ItemRemovedEvent<Type>> getRemovedEvents() {
		return Collections.unmodifiableList(removedEvents);
	}

	public static class ItemAddedEvent<Type> {

		private final Repository<Type> repository;
		private final Type item;
		private final Type itemBefore;
		private final Type itemAfter;

		public ItemAddedEvent(Repository<Type> repository, Type item, Type itemBefore, Type itemAfter) {
			this.repository = repository;
			this.item = item;
			this.itemBefore = itemBefore;
			this.itemAfter = itemAfter;
		}

		public Repository<Type> getRepository() {
			return repository;
		}

		public Type getItem() {
			return item;
		}

		public Type getItemBefore() {
			return itemBefore;
		}

		public Type getItemAfter() {
			return itemAfter;
		}
	}

	public static class ItemRemovedEvent<Type> {

		private final Repository<Type> repository;
		private final Type item;

		public ItemRemovedEvent(Repository<Type> repository, Type item) {
			this.repository = repository;
			this.item = item;
		}

		public Repository<Type> getRepository() {
			return repository;
		}

		public Type getItem() {
			return item;
		}
	}
}
