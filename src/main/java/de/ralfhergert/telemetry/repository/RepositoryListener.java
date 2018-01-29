package de.ralfhergert.telemetry.repository;

/**
 * Interface for repository listeners.
 * A repository listener gets informed whenever items are added, removed or reordered.
 */
public interface RepositoryListener<Type> {

	/**
	 * Is called when an item had been added to the repository.
	 * @param repository to which the item had been added
	 * @param item the item which had been added
	 * @param itemBefore the item which is now before the new item.
	 *        Will be <code>null</code> if the item had been added to the very beginning.
	 * @param itemAfter the item which is now after the new item.
	 *        Will be <code>null</code> if the item had been added to the very end.
	 */
	void onItemAdded(Repository<Type> repository, Type item, Type itemBefore, Type itemAfter);

	/**
	 * Is called when an item had been removed for the repository.
	 */
	void onItemRemoved(Repository<Type> repository, Type item);
}
