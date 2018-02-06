package de.ralfhergert.telemetry.repository;

/**
 * This class connect one repository with another.
 */
public class RepositoryConnector<Type> {

	public RepositoryConnector(Repository<Type> sourceRepo, Repository<Type> targetRepo) {
		sourceRepo.getItemStream().forEach((item) -> targetRepo.addItem(item));
		sourceRepo.addListener(new RepositoryListener<Type>() {
			@Override
			public void onItemAdded(Repository<Type> repository, Type item, Type itemBefore, Type itemAfter) {
				targetRepo.addItem(item);
			}

			@Override
			public void onItemRemoved(Repository<Type> repository, Type item) {
				targetRepo.removeItem(item);
			}
		});
	}
}
