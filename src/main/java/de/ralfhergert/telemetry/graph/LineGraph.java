package de.ralfhergert.telemetry.graph;

import de.ralfhergert.telemetry.repository.IndexedRepository;
import de.ralfhergert.telemetry.repository.Repository;
import de.ralfhergert.telemetry.repository.RepositoryListener;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the values of a single graph.
 */
public class LineGraph<Item, Key extends Number, Value extends Number> implements RepositoryListener<Item> {

	private final Accessor<Item, Key> keyAccessor;
	private final Accessor<Item, Value> valueAccessor;

	private Path2D path = new Path2D.Double();

	protected final List<LineGraphListener<Item, Key,Value>> listeners = new ArrayList<>();
	private final List<GraphValue<Key,Value>> values = new ArrayList<>();

	private Key minKey = null;
	private Key maxKey = null;
	private Value minValue = null;
	private Value maxValue = null;

	public LineGraph(IndexedRepository<Item> repository, Accessor<Item, Key> keyAccessor, Accessor<Item, Value> valueAccessor) {
		if (repository == null) {
			throw new IllegalArgumentException("repository can not be null");
		}
		if (keyAccessor == null) {
			throw new IllegalArgumentException("keyAccessor can not be null");
		}
		if (valueAccessor == null) {
			throw new IllegalArgumentException("valueAccessor can not be null");
		}
		this.keyAccessor = keyAccessor;
		this.valueAccessor = valueAccessor;
		// register a listener on the given repository.
		repository.addListener(this);
	}

	@Override
	public void onItemAdded(Repository<Item> repository, Item item, Item itemBefore, Item itemAfter) {
		if (itemBefore == null && itemAfter == null) { // start a new path
			path = new Path2D.Double();
			path.moveTo(keyAccessor.getValue(item).doubleValue(), valueAccessor.getValue(item).doubleValue());
		} else if (itemAfter == null) {
			path.lineTo(keyAccessor.getValue(item).doubleValue(), valueAccessor.getValue(item).doubleValue());
		} else {
			redrawPath(repository);
		}
		listeners.forEach(listener -> listener.graphChanged(this, true));
	}

	@Override
	public void onItemRemoved(Repository<Item> repository, Item item) {
		redrawPath(repository);
	}

	protected void redrawPath(Repository<Item> repository) {
		path = new Path2D.Double();
		repository.getItemStream().findFirst().ifPresent(item ->
			path.moveTo(keyAccessor.getValue(item).doubleValue(), valueAccessor.getValue(item).doubleValue())
		);
		repository.getItemStream().forEach(item ->
			path.lineTo(keyAccessor.getValue(item).doubleValue(), valueAccessor.getValue(item).doubleValue())
		);
	}

	protected boolean updateBoundaries(Item item) {
		boolean keyDimensionChanged = false;
		final Key key = keyAccessor.getValue(item);
		if (key != null) {
			if (minKey == null || key.doubleValue() < minKey.doubleValue()) {
				minKey = key;
				keyDimensionChanged = true;
			}
			if (maxKey == null || key.doubleValue() > maxKey.doubleValue()) {
				maxKey = key;
				keyDimensionChanged = true;
			}
		}
		boolean valueDimensionChanged = false;
		final Value value = valueAccessor.getValue(item);
		if (value != null) {
			if (minValue == null || value.doubleValue() < minValue.doubleValue()) {
				minValue = value;
				valueDimensionChanged = true;
			}
			if (maxValue == null || value.doubleValue() > maxValue.doubleValue()) {
				maxValue = value;
				valueDimensionChanged = true;
			}
		}
		return keyDimensionChanged || valueDimensionChanged;
	}

	public LineGraph<Item, Key,Value> addListener(LineGraphListener<Item, Key,Value> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		return this;
	}

	public LineGraph<Item, Key,Value> removeListener(LineGraphListener<Item, Key,Value> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener can not be null");
		}
		listeners.remove(listener);
		return this;
	}

	public List<LineGraphListener<Item, Key, Value>> getListeners() {
		return new ArrayList<>(listeners); // create a copy to deny external manipulations.
	}
}
