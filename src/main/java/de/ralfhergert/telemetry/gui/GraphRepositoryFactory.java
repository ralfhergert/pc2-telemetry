package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.graph.NegativeOffsetAccessor;
import de.ralfhergert.telemetry.reflection.*;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import de.ralfhergert.telemetry.repository.ItemRepository;
import de.ralfhergert.telemetry.repository.Repository;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * This class will generate a {@link Repository} of {@link LineGraph}.
 */
public class GraphRepositoryFactory {

	/**
	 * This method create a set of LineGraphs for the given {@param carPhysicsRepository}.
	 * @param carPhysicRepository the repository to which all the LineGraph will be connected.
	 * @param keyAccessor determines which value is key for all generated LineGraphs.
	 * @param sampleItem mainly is used to detect the length of array values.
	 */
	public Repository<LineGraph> createLineGraphs(IndexedRepository<CarPhysicsPacket> carPhysicRepository, NegativeOffsetAccessor<CarPhysicsPacket, Number> keyAccessor, CarPhysicsPacket sampleItem) {
		final Repository<LineGraph> graphRepository = new ItemRepository<>();

		// use reflection to iterate over all fields and compare them.
		for (Field field : CarPhysicsPacket.class.getFields()) {
			if (!field.isAccessible() || !field.getType().isAssignableFrom(Number.class)) {
				continue; // skip this field if its neither accessible nor a number.
			}
			if (field.getType().isArray()) {
				if (!field.getType().getComponentType().isAssignableFrom(Number.class)) {
					continue; // skip this field if its not a number.
				}
				int length = 1;
				// use the sample to detect the array length.
				if (sampleItem != null) {
					// TODO implement array length detection.
				}
				for (int i = 0; i < length; i++) {
					graphRepository.addItem(
						new LineGraph<>(carPhysicRepository, keyAccessor, getArrayAccessor(field.getType().getComponentType(), new FieldAccessor<>(field), i)).setProperty("color", Color.GREEN)
					);
				}
			} else {
				if (!field.getType().isAssignableFrom(Number.class)) {
					continue; // skip this field if its not a number.
				}
				graphRepository.addItem(
					new LineGraph<>(carPhysicRepository, keyAccessor, new FieldAccessor<>(field)).setProperty("color", Color.GREEN)
				);
			}
		}
		for (Method method : CarPhysicsPacket.class.getMethods()) {
			if (method.getName().startsWith("get")) {
				if (method.getReturnType().isArray()) {
					if (!method.getReturnType().getComponentType().isAssignableFrom(Number.class)) {
						continue; // skip this field if its not a number.
					}
					int length = 1;
					// use the sample to detect the array length.
					if (sampleItem != null) {
						// TODO implement array length detection.
					}
					for (int i = 0; i < length; i++) {
						graphRepository.addItem(
							new LineGraph<>(carPhysicRepository, keyAccessor, getArrayAccessor(method.getReturnType().getComponentType(), new MethodAccessor<>(method), i)).setProperty("color", Color.GREEN)
						);
					}
				} else {
					if (!method.getReturnType().isAssignableFrom(Number.class)) {
						continue; // skip this method.
					}
					graphRepository.addItem(
						new LineGraph<>(carPhysicRepository, keyAccessor, new MethodAccessor<>(method)).setProperty("color", Color.GREEN)
					);
				}
			}
		}
		return graphRepository;
	}

	private <Item> Accessor<Item, ? extends Number> getArrayAccessor(Type type, Accessor<Item, ?> accessor, int index) {
		if (type.getClass().isAssignableFrom(byte[].class)) {
			return new ByteArrayAccessor<>(index, (Accessor<Item, byte[]>)accessor);
		} else if (type.getClass().isAssignableFrom(short[].class)) {
			return new ShortArrayAccessor<>(index, (Accessor<Item, short[]>)accessor);
		} else if (type.getClass().isAssignableFrom(float[].class)) {
			return new FloatArrayAccessor<>(index, (Accessor<Item, float[]>)accessor);
		} else {
			throw new IllegalArgumentException("no array accessor available for " + type);
		}
	}
}
