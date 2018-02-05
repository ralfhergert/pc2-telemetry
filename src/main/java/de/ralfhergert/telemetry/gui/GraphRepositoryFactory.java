package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.graph.NegativeOffsetAccessor;
import de.ralfhergert.telemetry.reflection.*;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import de.ralfhergert.telemetry.repository.ItemRepository;
import de.ralfhergert.telemetry.repository.Repository;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
	public Repository<LineGraph> createLineGraphs(IndexedRepository<CarPhysicsPacket> carPhysicRepository, NegativeOffsetAccessor<CarPhysicsPacket, ? extends Number> keyAccessor, CarPhysicsPacket sampleItem) {
		final Repository<LineGraph> graphRepository = new ItemRepository<>();

		final List<PropertyInfo<CarPhysicsPacket>> propertyInfoList = new ArrayList<>();
		// use reflection to iterate over all fields and compare them.
		for (Field field : CarPhysicsPacket.class.getFields()) {
			propertyInfoList.add(new PropertyInfo<>(field.getName(), field.getType(), new FieldAccessor<>(field)));
		}
		for (Method method : CarPhysicsPacket.class.getMethods()) {
			if (method.getName().startsWith("get")) {
				final String propertyName = convertFirstCharacterToLowerCase(method.getName().substring(3));
				propertyInfoList.add(new PropertyInfo<>(propertyName, method.getReturnType(), new MethodAccessor<>(method)));
			}
		}
		for (PropertyInfo<CarPhysicsPacket> propertyInfo : propertyInfoList) {
			if (propertyInfo.getPropertyType().isArray()) {
				if (!isNumber(propertyInfo.getPropertyType().getComponentType())) {
					continue; // skip this field if its not a number.
				}
				int length = 1;
				// use the sample to detect the array length.
				if (sampleItem != null) {
					// TODO implement array length detection.
				}
				for (int i = 0; i < length; i++) {
					graphRepository.addItem(
						new LineGraph<>(carPhysicRepository, keyAccessor, getArrayAccessor(propertyInfo.getPropertyType(), propertyInfo.getPropertyAccessor(), i))
							.setProperty("name", "carPhysicsPacket.property." + propertyInfo.getPropertyName() + "[" + i + "]")
							.setProperty("color", Color.GREEN)
					);
				}
			} else {
				if (!isNumber(propertyInfo.getPropertyType())) {
					continue; // skip this method.
				}
				graphRepository.addItem(
					new LineGraph<>(carPhysicRepository, keyAccessor, propertyInfo.getPropertyAccessor())
						.setProperty("name", "carPhysicsPacket.property." + propertyInfo.getPropertyName())
						.setProperty("color", Color.GREEN)
				);
			}
		}
		return graphRepository;
	}

	private <Item> Accessor<Item, ? extends Number> getArrayAccessor(Class type, Accessor<Item, ?> accessor, int index) {
		if (!type.isArray()) {
			throw new IllegalArgumentException("given type is not an array: " + type);
		}
		if (byte[].class.equals(type)) {
			return new ByteArrayAccessor<>(index, (Accessor<Item, byte[]>)accessor);
		} else if (short[].class.equals(type)) {
			return new ShortArrayAccessor<>(index, (Accessor<Item, short[]>)accessor);
		} else if (float[].class.equals(type)) {
			return new FloatArrayAccessor<>(index, (Accessor<Item, float[]>)accessor);
		} else {
			throw new IllegalArgumentException("no array accessor available for " + type);
		}
	}

	public static String convertFirstCharacterToLowerCase(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}

	public static boolean isNumber(Class clazz) {
		return Number.class.isAssignableFrom(clazz)
			|| short.class.equals(clazz)
			|| int.class.equals(clazz)
			|| long.class.equals(clazz)
			|| float.class.equals(clazz)
			|| double.class.equals(clazz);
	}

}
