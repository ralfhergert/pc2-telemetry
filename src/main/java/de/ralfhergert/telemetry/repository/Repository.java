package de.ralfhergert.telemetry.repository;

import java.util.stream.Stream;

/**
 * A repository is a store for a set of items.
 */
public interface Repository<Type> {

	Repository<Type> addListener(RepositoryListener<Type> listener);
	Repository<Type> removeListener(RepositoryListener<Type> listener);

	Repository<Type> addItem(Type item);
	Repository<Type> removeItem(Type item);

	Stream<Type> getItemStream();
}
